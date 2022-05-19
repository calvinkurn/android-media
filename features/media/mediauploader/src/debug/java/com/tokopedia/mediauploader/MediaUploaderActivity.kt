package com.tokopedia.mediauploader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.mediauploader.MediaUploaderStateManager.Companion.UploadState
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.di.DaggerMediaUploaderTestComponent
import com.tokopedia.mediauploader.di.MediaUploaderTestModule
import com.tokopedia.mediauploader.services.UploaderWorker
import com.tokopedia.mediauploader.services.UploaderWorker.Companion.RESULT_UPLOAD_ID
import com.tokopedia.mediauploader.services.UploaderWorker.Companion.RESULT_VIDEO_URL
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MediaUploaderActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var imgPreview: AppCompatImageView
    private lateinit var progressBar: ProgressBarUnify
    private lateinit var txtInfo: AppCompatTextView
    private lateinit var btnPickUp: UnifyButton
    private lateinit var btnRemove: UnifyButton
    private lateinit var btnUpload: UnifyButton
    private lateinit var btnAbort: UnifyButton
    private lateinit var edtUrl: AppCompatEditText

    @Inject lateinit var uploaderUseCase: UploaderUseCase

    private val viewModel by lazy {
        ViewModelProvider(this)
            .get(MediaUploaderStateManager::class.java)
    }

    private var mediaFilePath = ""
    private var isUploadImage = false
    private var isVideoTranscodeSupported = true
    private var isLargeUpload = false

    private var isAborted = false

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    val sourceId: String
        get() = if (isUploadImage) {
            "tuOYCg" // sourceId for image upload
        } else {
            "VsrJDL" // sourceId for video upload (simple and large)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.mediauploader.R.layout.activity_media_uploader)
        initInjector()

        initViewComponent()
        initObservable()
        initView()
    }

    private fun initView() {
        btnPickUp.setOnClickListener {
            showPickMediaPopUpMenu()
        }

        btnRemove.setOnClickListener {
            viewModel.setUploadingStatus(UploadState.Aborted)
        }

        btnUpload.setOnClickListener {
            progressBar.setValue(0, true)
            showPickUploadPopUpMenu()
        }

        abortButtonClicked()
    }

    private fun initObservable() {
        viewModel.uploading.observe(this, { status ->
            when (status) {
                is UploadState.Idle -> stateLargeUploadIdle()
                is UploadState.Uploading -> stateLargeUploading(status.withWorker)
                is UploadState.Stopped -> stateLargeUploadStopped()
                is UploadState.Aborted -> stateLargeUploadAborted()
                is UploadState.Finished -> stateLargeUploadFinished(status.result)
            }
        })

        WorkManager
            .getInstance(applicationContext)
            .getWorkInfosForUniqueWorkLiveData(UploaderWorker.UNIQUE_WORK_NAME)
            .observe(this, {
                it.firstOrNull()?.let { info ->
                    if (info.state == WorkInfo.State.SUCCEEDED) {
                        viewModel.setUploadingStatus(UploadState.Finished(UploadResult.Success(
                            videoUrl = info.outputData.getString(RESULT_VIDEO_URL)?: "",
                            uploadId = info.outputData.getString(RESULT_UPLOAD_ID)?: ""
                        )))

                        UploaderWorker.pruneWork(applicationContext)
                    }
                }
            })
    }

    private fun stateLargeUploadIdle() {
        appendInfo("\nready to upload\n")
        btnUpload.isEnabled = true
        btnPickUp.hide()
        btnRemove.show()
        edtUrl.hide()

        progressBar.setValue(0, true)
    }

    private fun stateLargeUploading(withWorker: Boolean) {
        if (withWorker) {
            appendInfo("\nthe uploader move to push notification.\n")
        } else {
            appendInfo("\nuploading...\n")
            btnAbort.show()
        }

        btnUpload.text = "Uploading..."

        // hide pick-up and show remove button
        btnPickUp.hide()
        btnRemove.show()

        // disabled upload button and change caption
        btnUpload.isEnabled = false
    }

    private fun stateLargeUploadStopped() {
        appendInfo("\nclick button to retry\n")
        btnUpload.text = "Retry"

        btnUpload.isEnabled = true
    }

    private fun stateLargeUploadAborted() {
        txtInfo.text = "Tidak ada info file."
        imgPreview.setImageDrawable(null)
        edtUrl.hide()

        btnUpload.text = "Upload"

        btnPickUp.show()
        btnRemove.hide()

        btnUpload.isEnabled = false
        btnAbort.hide()
    }

    private fun stateLargeUploadFinished(result: UploadResult) {
        appendInfo("\nuploaded\n")
        edtUrl.show()

        btnUpload.text = "Upload"
        btnUpload.isEnabled = false
        btnAbort.hide()

        if (result is UploadResult.Success) {
            if (isUploadImage) {
                appendInfo("${result.uploadId}\n")
                edtUrl.setText(result.uploadId)
            } else {
                appendInfo("${result.videoUrl}\n")
                edtUrl.setText(result.videoUrl)
            }
        }
    }

    private fun abortButtonClicked() {
        btnAbort.setOnClickListener {
            if (!isUploadImage && isLargeUpload) {
                launch {
                    uploaderUseCase.abortUpload(sourceId, mediaFilePath) {
                        withContext(Dispatchers.Main) {
                            viewModel.setUploadingStatus(UploadState.Aborted)
                            isAborted = true

                            UploaderWorker.cancelWork(applicationContext)
                            coroutineContext.cancelChildren()
                            btnAbort.hide()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "abort only support on large upload",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun mediaUploader(withWorker: Boolean) {
        if (mediaFilePath.isEmpty()) return

        btnUpload.isEnabled = false

        if (!withWorker) {
            val param = uploaderUseCase.createParams(
                sourceId = sourceId,
                filePath = File(mediaFilePath),
                withTranscode = isVideoTranscodeSupported
            )

            uploaderUseCase.trackProgress { progress ->
                progressBar.setValue(progress, true)
            }

            launch {
                val result = uploaderUseCase(param)

                withContext(Dispatchers.Main) {
                    btnAbort.hide()

                    when(result) {
                        is UploadResult.Success -> {
                            viewModel.setUploadingStatus(UploadState.Finished(result))
                        }
                        is UploadResult.Error -> {
                            if (!isAborted) {
                                viewModel.setUploadingStatus(UploadState.Stopped)
                                appendInfo("${result.message}\n")
                            } else {
                                isAborted = false
                            }
                        }
                    }
                }
            }
        } else {
            UploaderWorker.createChainedWorkRequests(
                context = applicationContext,
                sourceId = sourceId,
                filePath = mediaFilePath,
                isSupportTranscode = isVideoTranscodeSupported
            )
        }
    }

    private fun setPreviewInfo(path: String, isImageOrView: Boolean) {
        viewModel.setUploadingStatus(UploadState.Idle)

        isUploadImage = isImageOrView
        mediaFilePath = path
        showFileInfo(File(mediaFilePath))

        imgPreview.loadImageRounded(mediaFilePath, RADIUS_PREVIEW) {
            centerCrop()
        }
    }

    private fun appendInfo(text: String) {
        txtInfo.append(text)
    }

    @SuppressLint("SetTextI18n")
    private fun showFileInfo(file: File) {
        val sourceType = if (isUploadImage) "image" else "video"

        val sourceUpload = when {
            isUploadImage -> {
                "image-uploader"
            }
            file.length() >= 10.mbToBytes() -> {
                isLargeUpload = true
                "large-uploader"
            }
            else -> {
                isLargeUpload = false
                "simple-uploader"
            }
        }

        txtInfo.text = """
            -------------------------------
            File info:
            name    : ${file.name}
            path    : ${file.absolutePath}
            size    : ${file.length().formattedToMB()} MB
            type    : $sourceType
            source  : $sourceUpload
            -------------------------------
        """.trimIndent()
    }

    private fun showPickMediaPopUpMenu() {
        PopupMenu(applicationContext, btnPickUp).apply {
            menuInflater.inflate(R.menu.menu_pick_media, this.menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_image -> {
                        pickImageToUpload()
                    }
                    R.id.menu_video_transcode -> {
                        isVideoTranscodeSupported = true
                        pickVideoToUpload()
                    }
                    R.id.menu_video_notranscode -> {
                        isVideoTranscodeSupported = false
                        pickVideoToUpload()
                    }
                }
                true
            }
        }.show()
    }

    private fun showPickUploadPopUpMenu() {
        PopupMenu(applicationContext, btnUpload).apply {
            menuInflater.inflate(R.menu.menu_pick_upload, this.menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_normal -> {
                        viewModel.setUploadingStatus(UploadState.Uploading(false))
                        mediaUploader(false)
                    }
                    R.id.menu_worker -> {
                        viewModel.setUploadingStatus(UploadState.Uploading(true))
                        mediaUploader(true)
                    }
                }
                true
            }
        }.show()
    }

    private fun pickImageToUpload() {
        val builder = ImagePickerBuilder.getOriginalImageBuilder(this)
            .withSimpleMultipleSelection(maxPick = 1)
        val intent = RouteManager.getIntent(applicationContext, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
        isUploadImage = true
    }

    private fun pickVideoToUpload() {
        val intent = RouteManager.getIntent(applicationContext, ApplinkConstInternalGlobal.VIDEO_PICKER)
        startActivityForResult(intent, REQUEST_VIDEO_PICKER)
        isUploadImage = false
    }

    private fun initViewComponent() {
        imgPreview = findViewById(R.id.img_preview)
        progressBar = findViewById(R.id.progress_bar)
        txtInfo = findViewById(R.id.txt_info)
        btnPickUp = findViewById(R.id.btn_pick_up)
        btnRemove = findViewById(R.id.btn_remove)
        btnUpload = findViewById(R.id.btn_upload)
        btnAbort = findViewById(R.id.btn_abort)
        edtUrl = findViewById(R.id.edt_url)
    }

    private fun initInjector() {
        DaggerMediaUploaderTestComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .mediaUploaderTestModule(MediaUploaderTestModule(applicationContext))
            .build()
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            val imageList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
            setPreviewInfo(imageList.first(), true)
        } else if (requestCode == REQUEST_VIDEO_PICKER && resultCode == Activity.RESULT_OK) {
            val videoList = data?.getStringArrayListExtra(VIDEO_RESULT_CODE) ?: arrayListOf()
            setPreviewInfo(videoList.first(), false)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICKER = 123
        private const val REQUEST_VIDEO_PICKER = 456

        private const val VIDEO_RESULT_CODE = "video_result"
        private const val RADIUS_PREVIEW = 20f
    }

}