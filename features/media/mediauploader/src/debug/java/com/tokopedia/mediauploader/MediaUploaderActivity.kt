package com.tokopedia.mediauploader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.di.DaggerMediaUploaderTestComponent
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.tokopedia.mediauploader.MediaUploaderViewModel.Companion.UploadState

class MediaUploaderActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var imgPreview: AppCompatImageView
    private lateinit var progressBar: ProgressBarUnify
    private lateinit var txtInfo: AppCompatTextView
    private lateinit var btnPickUp: UnifyButton
    private lateinit var btnRemove: UnifyButton
    private lateinit var btnUpload: UnifyButton
    private lateinit var btnAbort: UnifyButton

    @Inject lateinit var uploaderUseCase: UploaderUseCase

    private val viewModel by lazy {
        ViewModelProvider(this)
            .get(MediaUploaderViewModel::class.java)
    }

    private var mediaFilePath = ""
    private var isUploadImage = false

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_uploader)
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
            viewModel.setUploadingStatus(UploadState.Uploading)
            mediaUploader()
        }
    }

    private fun initObservable() {
        viewModel.uploading.observe(this, { status ->
            when (status) {
                is UploadState.Idle -> stateLargeUploadIdle()
                is UploadState.Uploading -> stateLargeUploading()
                is UploadState.Stopped -> stateLargeUploadStopped()
                is UploadState.Aborted -> stateLargeUploadAborted()
                is UploadState.Finished -> stateLargeUploadFinished()
            }
        })
    }

    private fun stateLargeUploadIdle() {
        appendInfo("\nready to upload\n")
        btnUpload.isEnabled = true
        btnPickUp.hide()
        btnRemove.show()

        progressBar.setValue(0, true)
    }

    private fun stateLargeUploading() {
        appendInfo("\nuploading...\n")
        btnUpload.text = "Uploading..."

        // hide pick-up and show remove button
        btnPickUp.hide()
        btnRemove.show()

        // disabled upload button and change caption
        btnUpload.isEnabled = false
        btnAbort.show()
    }

    private fun stateLargeUploadStopped() {
        appendInfo("\nclick button to retry\n")
        btnUpload.text = "Retry"

        btnUpload.isEnabled = true
    }

    private fun stateLargeUploadAborted() {
        txtInfo.text = "Tidak ada info file."
        imgPreview.setImageDrawable(null)

        btnUpload.text = "Upload"

        btnPickUp.show()
        btnRemove.hide()

        btnUpload.isEnabled = false
        btnAbort.hide()
    }

    private fun stateLargeUploadFinished() {
        appendInfo("\nuploaded\n")
        btnUpload.text = "Upload"
        btnUpload.isEnabled = false
        btnAbort.hide()
    }

    private fun abortButtonClicked() {
        if (!isUploadImage) {
            progressBar.setValue(0, true)
            btnUpload.isEnabled = false
            btnAbort.show()

            btnAbort.setOnClickListener {
                launch {
                    uploaderUseCase.abortUpload {
                        coroutineContext.cancelChildren()
                        btnAbort.hide()
                    }
                }
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun mediaUploader() {
        if (mediaFilePath.isEmpty()) return

        abortButtonClicked()

        val param = uploaderUseCase.createParams(
            sourceId = if (isUploadImage) {
                "tuOYCg" // sourceId for image upload
            } else {
                "VsrJDL" // sourceId for video upload (simple and large)
            },
            filePath = File(mediaFilePath)
        )

        uploaderUseCase.trackProgress {
            progressBar.setValue(it, true)
        }

        launch {
            val result = uploaderUseCase(param)

            withContext(Dispatchers.Main) {
                btnAbort.hide()

                when(result) {
                    is UploadResult.Success -> {
                        viewModel.setUploadingStatus(UploadState.Finished)
                        if (isUploadImage) {
                            appendInfo("${result.uploadId}\n")
                        } else {
                            appendInfo("${result.videoUrl}\n")
                        }
                    }
                    is UploadResult.Error -> {
                        viewModel.setUploadingStatus(UploadState.Stopped)
                        appendInfo("${result.message}\n")
                    }
                }
            }
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
        txtInfo.text = txtInfo.text.toString() + text
    }

    @SuppressLint("SetTextI18n")
    private fun showFileInfo(file: File) {
        val sourceType = if (isUploadImage) "image" else "video"

        val sourceUpload = when {
            isUploadImage -> "image-uploader"
            file.length() >= THRESHOLD_LARGE_FILE_SIZE -> "large-uploader"
            else -> "simple-uploader"
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
                if (item.itemId == R.id.menu_image) {
                    pickImageToUpload()
                } else if (item.itemId == R.id.menu_video) {
                    pickVideoToUpload()
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
    }

    private fun pickVideoToUpload() {
        val intent = RouteManager.getIntent(applicationContext, ApplinkConstInternalGlobal.VIDEO_PICKER)
        startActivityForResult(intent, REQUEST_VIDEO_PICKER)
    }

    private fun initViewComponent() {
        imgPreview = findViewById(R.id.img_preview)
        progressBar = findViewById(R.id.progress_bar)
        txtInfo = findViewById(R.id.txt_info)
        btnPickUp = findViewById(R.id.btn_pick_up)
        btnRemove = findViewById(R.id.btn_remove)
        btnUpload = findViewById(R.id.btn_upload)
        btnAbort = findViewById(R.id.btn_abort)
    }

    private fun initInjector() {
        DaggerMediaUploaderTestComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .mediaUploaderModule(MediaUploaderModule())
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
        private const val THRESHOLD_LARGE_FILE_SIZE = 20971520
        private const val REQUEST_IMAGE_PICKER = 123
        private const val REQUEST_VIDEO_PICKER = 456

        private const val VIDEO_RESULT_CODE = "video_result"
        private const val RADIUS_PREVIEW = 20f
    }

}