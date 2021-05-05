package com.tokopedia.feedback_form.feedbackpage.ui.feedbackpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage.FeedbackPagePresenter
import com.tokopedia.developer_options.presentation.feedbackpage.ui.tickercreated.TicketCreatedActivity
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.drawonpicture.presentation.activity.DrawOnPictureActivity
import com.tokopedia.feedback_form.drawonpicture.presentation.fragment.DrawOnPictureFragment.Companion.EXTRA_DRAW_IMAGE_URI
import com.tokopedia.feedback_form.drawonpicture.presentation.fragment.DrawOnPictureFragment.Companion.EXTRA_DRAW_IMAGE_URI_OLD
import com.tokopedia.feedback_form.feedbackpage.analytics.FeedbackPageAnalytics
import com.tokopedia.feedback_form.feedbackpage.di.FeedbackPageComponent
import com.tokopedia.feedback_form.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.FeedbackModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.feedback_form.feedbackpage.ui.adapter.ImageFeedbackAdapter
import com.tokopedia.feedback_form.feedbackpage.ui.adapter.PageItemAdapter
import com.tokopedia.feedback_form.feedbackpage.ui.dialog.LoadingDialog
import com.tokopedia.feedback_form.feedbackpage.ui.listener.ImageClickListener
import com.tokopedia.feedback_form.feedbackpage.ui.preference.Preferences
import com.tokopedia.feedback_form.feedbackpage.util.*
import com.tokopedia.imagepicker.common.*
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.android.synthetic.main.fragment_feedback_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.util.*
import javax.inject.Inject


class FeedbackPageFragment: BaseDaggerFragment(), FeedbackPageContract.View, ImageClickListener, PageItemAdapter.OnPageMenuSelected {

    @Inject
    lateinit var feedbackPagePresenter: FeedbackPagePresenter

    private var compositeSubscription: CompositeSubscription? = null
    private var myPreferences: Preferences? = null
    private val imageAdapter = ImageFeedbackAdapter(this)
    private var pagesAdapter = PageItemAdapter(this)
    private var bottomSheetPage: BottomSheetUnify? = null

    private var deviceInfo: String = ""
    private var androidVersion: String = ""
    private var appVersion: String = ""
    private var versionCode: String = ""
    private var userId: String = ""
    private var sessionToken: String = ""
    private var loginState: String = ""
    private var emailTokopedia: String = ""
    private var uriImage: Uri? = null
    private var lastAccessedPage: String? = ""
    private var categoryItem: Int = -1
    private var reportType: Int = 0
    private var labelsId: ArrayList<Int> = arrayListOf()
    private var isFromScreenshot: Boolean = false

    private var userSession: UserSessionInterface? = null
    private var loadingDialog: LoadingDialog? = null
    private var selectedImage: ArrayList<String> = arrayListOf()

    private val requiredPermissions: Array<String>
        get() = if (Build.VERSION.SDK_INT < VERSION_CODES.Q) {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedback_page, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!allPermissionsGranted()) {
            requestPermissions(requiredPermissions, 5111)
        }
        initViews()
        initFilter()
        initData()
        initListener()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FeedbackPageComponent::class.java).inject(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size == requiredPermissions.size) {
            initImageUri()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == RESULT_OK && data != null) {
                    selectedImage = ImagePickerResultExtractor.extract(data).imageUrlOrPathList as ArrayList<String>
                    feedbackPagePresenter.initImageData()

                    val imageListData = feedbackPagePresenter.getImageList(selectedImage)
                    if (selectedImage.isNotEmpty()) {
                        imageAdapter.setImageFeedbackData(imageListData)
                    }
                }
            }
            REQUEST_CODE_EDIT_IMAGE -> if (resultCode == RESULT_OK) {
                data?.let {
                    val oldPath = it.getStringExtra(EXTRA_DRAW_IMAGE_URI_OLD)
                    val newUri = it.getParcelableExtra<Uri>(EXTRA_DRAW_IMAGE_URI)
                    imageAdapter.setImageFeedbackData(feedbackPagePresenter.drawOnPictureResult(newUri, oldPath))
                    selectedImage = arrayListOf(newUri.toString())
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.show()
        }
    }

    override fun hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.hide()
        }
    }

    override fun setSubmitFlag() {
        myPreferences?.setSubmitFlag(emailTokopedia, userSession?.userId.toString())
    }

    override fun checkUriImage(feedbackId: Int, imageCount: Int) {
        var imageSize: Long = 0
        if (selectedImage.isNotEmpty()) {
            val totalImage = selectedImage.size
            var initCountImage = imageCount
            for (image in selectedImage) {
                initCountImage++
                val originalFile = File(image)
                val imageType = originalFile.absolutePath
                imageSize = originalFile.length()/1000

                if (!imageType.contains(".mp4") && imageSize > 250) {
                    resizeImage(image)?.let { imageFile ->
                        sendAttachmentImage(feedbackId, imageFile, totalImage, initCountImage)
                    }
                } else if (!imageType.contains(".mp4") && imageSize < 250) {
                    sendAttachmentImage(feedbackId, originalFile, totalImage, initCountImage)
                } else {
                    sendAttachmentVideo(feedbackId, originalFile, totalImage, initCountImage)
                }
            }
        } else {
            feedbackPagePresenter.commitData(feedbackId)
        }

    }

    override fun goToTicketCreatedActivity(issueUrl: String?) {
        activity?.finish()
        Intent(context, TicketCreatedActivity::class.java).apply {
            putExtra(EXTRA_IS_TICKET_LINK, issueUrl)
            startActivityForResult(this, 1212)
        }
    }

    override fun showError(throwable: Throwable) {
        Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun addImageClick() {
        context?.let {
            val builder = ImagePickerBuilder(
                    title = getString(R.string.image_picker_title),
                    imagePickerTab = arrayOf(ImagePickerTab.TYPE_GALLERY),
                    galleryType = GalleryType.ALL,
                    maxFileSizeInKB = DEFAULT_MAX_IMAGE_SIZE_IN_KB_PRO,
                    imageRatioType = ImageRatioType.ORIGINAL,
                    imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                            initialSelectedImagePathList = feedbackPagePresenter.getSelectedImageUrl()))
            val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onRemoveImageClick(item: BaseImageFeedbackUiModel) {
        imageAdapter.setImageFeedbackData(feedbackPagePresenter.removeImage(item))
        selectedImage = feedbackPagePresenter.getSelectedImageUrl()
    }

    override fun onImageClick(data: ImageFeedbackUiModel, position: Int) {
        val imageUriClicked = data.imageUrl
        startActivityForResult(DrawOnPictureActivity.getIntent(requireContext(), Uri.parse(imageUriClicked)),
                REQUEST_CODE_EDIT_IMAGE)
    }

    override fun setFeedbackData(model: FeedbackModel) {
        pagesAdapter.renderData(model.labels)
    }

    override fun setSubmitButton() {
        submitButton.isEnabled = true
    }

    override fun onSelect(selection: Int, pageName: String) {
        bottomSheetPage?.dismiss()
        page.setText(pageName)
        feedbackPagePresenter.setSelectedPage(selection)
        labelsId = arrayListOf(selection)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun initViews(){
        feedbackPagePresenter.attachView(this)

        compositeSubscription = CompositeSubscription()
        myPreferences = Preferences(context)
        loadingDialog = context?.let { LoadingDialog(it) }

        context?.let { ArrayAdapter.createFromResource(it,
                R.array.bug_type_array,
                R.layout.item_spinner_bug
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bugType.adapter = adapter
        } }
        userSession = UserSession(activity)
        feedbackPagePresenter.getFeedbackData()
        uriImage = arguments?.getParcelable(EXTRA_URI_IMAGE)
        lastAccessedPage = arguments?.getString(EXTRA_IS_CLASS_NAME, "")
        isFromScreenshot = arguments?.getBoolean(EXTRA_IS_FROM_SCREENSHOT)?: false

        if (isFromScreenshot) {
            FeedbackPageAnalytics.eventOpenFeedbackFromScreenshot()
        } else {
            FeedbackPageAnalytics.eventOpenFeedbackFromSettings()
        }
    }

    private fun initImageUri() {
        if (allPermissionsGranted() && uriImage != null) {
            val screenshotData = uriImage?.let { handleItem(it) }
            if (screenshotData != null) {
                imageAdapter.setImageFeedbackData(feedbackPagePresenter.screenshotImageResult(screenshotData))
                selectedImage = arrayListOf(screenshotData.path)
            }
        }
    }

    private fun initFilter() {
        setActiveFilter(filterBugs, filterFeedback)
        bugsLayout.visibility = View.VISIBLE
        feedbackLayout.visibility = View.GONE
        reportType = 1

        filterBugs.setOnClickListener {
            setActiveFilter(filterBugs, filterFeedback)
            bugsLayout.visibility = View.VISIBLE
            feedbackLayout.visibility = View.GONE
            reportType = 1
        }

        filterFeedback.setOnClickListener {
            setActiveFilter(filterFeedback, filterBugs)
            feedbackLayout.visibility = View.VISIBLE
            bugsLayout.visibility = View.GONE
            reportType = 2
        }
    }

    private fun initData() {
        rvImageFeedback.adapter = imageAdapter
        imageAdapter.setImageFeedbackData(feedbackPagePresenter.initImageData())
        val fields = VERSION_CODES::class.java.fields

        var codeName = "UNKNOWN"
        fields.filter { it.getInt(VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { codeName = it.name }

        when {
            codeName.startsWith("M") -> { codeName = getString(R.string.marshmallow) }
            codeName.startsWith("N") -> { codeName = getString(R.string.nougat) }
            codeName.startsWith("O") -> { codeName = getString(R.string.oreo) }
            codeName.startsWith("P") -> { codeName = getString(R.string.pie) }
            codeName.startsWith("Q") -> { codeName = getString(R.string.android10) }
        }

        deviceInfo = (StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString())
        androidVersion = codeName
        appVersion = GlobalConfig.VERSION_NAME
        versionCode = GlobalConfig.VERSION_CODE.toString()
        userId = userSession?.userId ?: ""
        sessionToken = userSession?.deviceId ?: ""
        loginState = userSession?.isLoggedIn.toString()
        initImageUri()
    }

    @SuppressLint("ResourceType")
    private fun initListener() {
        val separator = "@"
        val loginEmail = userSession?.email
        val topedEmail = loginEmail?.lastIndexOf(separator)
        val savedEmail = myPreferences?.getSubmitFlag(userSession?.userId.toString())
        if (loginEmail != null) {
            if (loginEmail.contains("@tokopedia.com") && savedEmail == null) {
                email.setText(topedEmail?.let { loginEmail.substring(0, it) })
            } else {
                val cacheEmail = savedEmail?.lastIndexOf(separator)
                email.setText(cacheEmail?.let { savedEmail.substring(0, it) })
            }
        }

        bugType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (parent != null) {
                    categoryItem = parent.getItemIdAtPosition(position).toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }
        }

        page.setOnClickListener {
            openBottomSheetPage()
        }

        submitButton.setOnClickListener {
            var emailText= email.text.toString()
            val affectedPageText = page.text.toString()
            val journeyText = journey.text.toString()
            val expectedResultText = expectedResult.text.toString()
            val detailFeedback = feedback.text.toString()
            var validate = true

            if (emailText.isEmpty()) {
                validate = false
                setWrapperError(et_email_wrapper, getString(R.string.warning_email))
            }

            if (reportType == 1) {
                if (affectedPageText.isEmpty()) {
                    validate = false
                    setWrapperError(et_affected_page_wrapper, getString(R.string.warning_page))
                }

                if(journeyText.isEmpty()) {
                    validate = false
                    setWrapperError(et_str_wrapper, getString(R.string.warning_str))
                }
            } else {
                if (detailFeedback.isEmpty()) {
                    validate = false
                    setWrapperError(et_feedback_wrapper, getString(R.string.warning_feedback))
                }
            }

            if(validate) {
                if (emailText.contains("@tokopedia.com")) emailText = emailText.substringBefore("@tokopedia.com", "")
                emailTokopedia = "$emailText@tokopedia.com"
                feedbackPagePresenter.sendFeedbackForm(requestMapper(emailTokopedia, journeyText, expectedResultText, detailFeedback))
                submitButton.isEnabled = false

                if (reportType == 1) {
                    FeedbackPageAnalytics.eventClickSubmitButtonBug()
                } else {
                    FeedbackPageAnalytics.eventClickSubmitButtonFeedback()
                }
            }
        }

    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.isErrorEnabled = false
        } else {
            wrapper.isErrorEnabled = true
            wrapper.hint = ""
            wrapper.error = s
        }
    }

    private fun handleItem(uri: Uri): ScreenshotData? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        var result: ScreenshotData? = null
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(uri, PROJECTION, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val screenshotData = generateScreenshotDataFromCursor(cursor)
                if (screenshotData != null) {
                    result = screenshotData
                }
            }
        } finally {
            cursor?.close()
        }
        return result
    }

    private fun generateScreenshotDataFromCursor(cursor: Cursor): ScreenshotData? {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
        val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        return ScreenshotData(id, fileName, path)
    }

    private fun requestMapper(email: String, journey: String, expectedResult: String, detailFeedback: String): FeedbackFormRequest {
        val affectedVersion = if (GlobalConfig.isSellerApp()) "SA-$appVersion" else "MA-$appVersion"
        return FeedbackFormRequest(
                platformID = 2,
                email = userSession?.email,
                appVersion = affectedVersion,
                bundleVersion = versionCode,
                device = deviceInfo,
                os = androidVersion,
                tokopediaUserID = userId,
                tokopediaEmail = email,
                sessionToken = sessionToken,
                fcmToken = "",
                loginState = loginState,
                lastAccessedPage = lastAccessedPage,
                category = categoryItem,
                journey = journey,
                expected = expectedResult,
                labelsId = labelsId,
                type = reportType,
                detail = detailFeedback
        )
    }

    private fun sendAttachmentImage(feedbackId: Int, file: File, totalImage: Int, countImage: Int) {
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileData = MultipartBody.Part.createFormData("file", file.name, requestFile)
        feedbackPagePresenter.sendAttachment(feedbackId, fileData, totalImage, countImage)
    }

    private fun sendAttachmentVideo(feedbackId: Int, file: File, totalImage: Int, countImage: Int) {
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("video/*"), file)
        val fileData = MultipartBody.Part.createFormData("file", file.name, requestFile)
        feedbackPagePresenter.sendAttachment(feedbackId, fileData, totalImage, countImage)
    }

    private fun resizeImage(data: String):File? {
        val b = BitmapFactory.decodeFile(data)
        val origWidth = b.width
        val origHeight = b.height
        val destHeight = 1440
        val destWidth = origWidth / (origHeight.toDouble() / destHeight)
        val b2 = Bitmap.createScaledBitmap(b, destWidth.toInt(), destHeight, false)
        return ImageProcessingUtil.writeImageToTkpdPath(b2, Bitmap.CompressFormat.JPEG, quality = 80)
    }

    private fun setActiveFilter(selected: ChipsUnify?, deselected: ChipsUnify?) {
        selected?.chipType = ChipsUnify.TYPE_SELECTED
        deselected?.chipType = ChipsUnify.TYPE_ALTERNATE
    }

    private fun openBottomSheetPage(){
        bottomSheetPage = BottomSheetUnify()
        val viewBottomSheetPage = View.inflate(context, R.layout.bottomsheet_pages_name, null).apply {
            val rvPages = findViewById<RecyclerView>(R.id.rv_pages)
            val searchInput = findViewById<SearchInputView>(R.id.search_input_page)
            rvPages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvPages.adapter = pagesAdapter

            searchInput?.setListener(object : SearchInputView.Listener {
                override fun onSearchSubmitted(text: String?) {
                    if (text != null) {
                        pagesAdapter.renderDataSearch(text)
                    }
                }

                override fun onSearchTextChanged(text: String?) {
                    if (text != null) {
                        pagesAdapter.renderDataSearch(text)
                    }
                }

            })
        }

        bottomSheetPage?.apply {
            setTitle(TITLE_PAGE_LIST)
            setCloseClickListener {
                pagesAdapter.renderDataSearch("")
                dismiss()
            }
            setChild(viewBottomSheetPage)
            setOnDismissListener {
                pagesAdapter.renderDataSearch("")
                dismiss()
            }
        }

        fragmentManager?.let {
            bottomSheetPage?.show(it, "show")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: Uri?, className: String?, isFromScreenshot: Boolean) : FeedbackPageFragment {
            return FeedbackPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_URI_IMAGE, uri)
                    putString(EXTRA_IS_CLASS_NAME, className)
                    putBoolean(EXTRA_IS_FROM_SCREENSHOT, isFromScreenshot)
                }
            }
        }

        private const val REQUEST_CODE_IMAGE = 111
        private const val REQUEST_CODE_EDIT_IMAGE = 101
        private const val DEFAULT_MAX_IMAGE_SIZE_IN_KB_PRO = 10240 // 10 * 1024KB (maks 10MB)


        private val PROJECTION = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        )
    }

}