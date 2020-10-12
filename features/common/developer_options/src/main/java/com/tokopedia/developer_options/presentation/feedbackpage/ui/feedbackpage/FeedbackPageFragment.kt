package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
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
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.drawonpicture.presentation.fragment.DrawOnPictureFragment.Companion.EXTRA_DRAW_IMAGE_URI
import com.tokopedia.developer_options.presentation.feedbackpage.adapter.ImageFeedbackAdapter
import com.tokopedia.developer_options.presentation.feedbackpage.di.FeedbackPageComponent
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.CategoriesModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.developer_options.presentation.feedbackpage.listener.ImageClickListener
import com.tokopedia.developer_options.presentation.feedbackpage.ui.dialog.LoadingDialog
import com.tokopedia.developer_options.presentation.feedbackpage.ui.tickercreated.TicketCreatedActivity
import com.tokopedia.developer_options.presentation.feedbackpage.utils.EXTRA_URI_IMAGE
import com.tokopedia.developer_options.presentation.preference.Preferences
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feedback_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.subscriptions.CompositeSubscription
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject


class FeedbackPageFragment: BaseDaggerFragment(), FeedbackPageContract.View, ImageClickListener {

    @Inject
    lateinit var feedbackPagePresenter: FeedbackPagePresenter

    private var compositeSubscription: CompositeSubscription? = null
    private var myPreferences: Preferences? = null
    private val imageAdapter: ImageFeedbackAdapter by lazy {
        ImageFeedbackAdapter(this)
    }

    private var deviceInfo: String = ""
    private var androidVersion: String = ""
    private var appVersion: String = ""
    private var versionCode: String = ""
    private var userId: String = ""
    private var sessionToken: String = ""
    private var fcmToken: String = ""
    private var loginState: String = ""
    private var emailTokopedia: String = ""
    private var uriImage: Uri? = null
    private var resizedUriImage: Uri? = null
    private var categoryItem: Int = -1

    private var userSession: UserSessionInterface? = null
    private var loadingDialog: LoadingDialog? = null
    private var selectedImage: ArrayList<String> = arrayListOf()

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedback_page, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!allPermissionsGranted()) {
            requestPermissions(requiredPermissions, 5111)
        }
        initViews()
        initListener()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FeedbackPageComponent::class.java).inject(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //do more validation here
        if(grantResults.size == requiredPermissions.size) {
            initImageUri()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedImage = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    feedbackPagePresenter.initImageData()

                    val imageListData = feedbackPagePresenter.getImageList(selectedImage)
                    if (selectedImage.isNotEmpty()) {
                        imageAdapter.setImageFeedbackData(imageListData)
                    }
                }
            }
            REQUEST_CODE_EDIT_IMAGE -> if (resultCode == RESULT_OK) {
                data?.let {
                    val newUri = it.getParcelableExtra<Uri>(EXTRA_DRAW_IMAGE_URI)
                    uriImage = newUri
                   /*set imageuri here*/

//                    imageView.setImageURI(uriImage)
                }
            }
           else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initViews(){
        feedbackPagePresenter.attachView(this)

        compositeSubscription = CompositeSubscription()
        myPreferences = Preferences(context)
        loadingDialog = context?.let { LoadingDialog(it) }

        uriImage = arguments?.getParcelable(EXTRA_URI_IMAGE)

        /*go To intent activity*/
        /*imageAdapter.setOnClickListener {
            startActivityForResult(DrawOnPictureActivity.getIntent(requireContext(), uriImage),
                    REQUEST_CODE_EDIT_IMAGE)
        }*/

        context?.let { ArrayAdapter.createFromResource(it,
                R.array.bug_type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bugType.adapter = adapter
        } }
        userSession = UserSession(activity)
        initData()
    }

    private fun initImageUri() {
        if (allPermissionsGranted() && uriImage != null) {
            val screenshotData = uriImage?.let { handleItem(it) }
            if (screenshotData != null) {
//                feedbackPagePresenter.getImageList(listOf(screenshotData.path))
                imageAdapter.setImageFeedbackData(feedbackPagePresenter.screenshotImageResult(screenshotData))
                selectedImage = arrayListOf(screenshotData.path)
            }
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

//        feedbackPagePresenter.getCategories()

        deviceInfo = (StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString())
        androidVersion = codeName
        appVersion = GlobalConfig.VERSION_NAME
        versionCode = GlobalConfig.VERSION_CODE.toString()
        userId = userSession?.userId ?: ""
        sessionToken = userSession?.deviceId ?: ""
        loginState = userSession?.isLoggedIn.toString()
        initImageUri()
    }

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

        submitButton.setOnClickListener {
            val emailText= email.text.toString()
            val affectedPageText = page.text.toString()
            val journeyText = journey.text.toString()
            val actualResultText = actualResult.text.toString()
            val expectedResultText = expectedResult.text.toString()
            var validate = true

            if (emailText.isEmpty()) {
                validate = false
                setWrapperError(et_email_wrapper, getString(R.string.warning_email) )
            }

            if (affectedPageText.isEmpty()) {
                validate = false
                setWrapperError(et_affected_page_wrapper, getString(R.string.warning_page) )
            }

            if(journeyText.isEmpty()) {
                validate = false
                setWrapperError(et_str_wrapper, getString(R.string.warning_str) )
            }

            if(actualResultText.isEmpty()) {
                validate = false
                setWrapperError(et_actual_result_wrapper, getString(R.string.warning_actual) )
            }

            if(expectedResultText.isEmpty()) {
                validate = false
                setWrapperError(et_expected_result_wrapper, getString(R.string.warning_expected) )
            }

            if(validate) {
                val issueType = bugType.selectedItem.toString()
                emailTokopedia = "$emailText@tokopedia.com"
                feedbackPagePresenter.sendFeedbackForm(requestMapper(emailTokopedia, affectedPageText, journeyText, issueType, actualResultText, expectedResultText))
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

    private fun isFileScreenshot(fileName: String): Boolean {
        return fileName.toLowerCase().startsWith(FILE_NAME_PREFIX)
    }

    private fun isPathScreenshot(path: String): Boolean {
        return path.toLowerCase().contains(PATH_SCREENSHOT)
    }

    private fun requestMapper(email: String, page: String, journey: String, issueType: String, actualResult: String, expectedResult: String): FeedbackFormRequest {
        val affectedVersion = if (GlobalConfig.isSellerApp()) "SA-$appVersion" else "MA-$appVersion"
        return FeedbackFormRequest(
                platformID = 2,
                //this should be user email
                email = email,
                appVersion =  affectedVersion,
                bundleVersion = versionCode,
                device = deviceInfo,
                os = androidVersion,
                tokopediaUserID = userId,
                tokopediaEmail = email,
                sessionToken = sessionToken,
                fcmToken = "",
                loginState = "login",
                lastAccessedPage = page,
                category = categoryItem,
                journey = journey,
                actual = actualResult,
                expected = expectedResult
        )
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

    override fun checkUriImage(feedbackId: Int) {
        val screenshotData = uriImage?.let { handleItem(it) }
        val originalFile = File(screenshotData?.path)
        val imageSize = originalFile.length()/1000

        if (uriImage != null && imageSize > 250) {
            screenshotData?.let { resizeImage(it) }
            val resizedData = resizedUriImage?.let { handleItem(it) }
            val resizedFile = File(resizedData?.path)
            sendAttachment(feedbackId, resizedFile)
        } else if (imageSize < 250) {
            sendAttachment(feedbackId, originalFile)
        }
        else {
            feedbackPagePresenter.commitData(feedbackId)
        }
    }

    private fun sendAttachment(feedbackId: Int, file: File) {
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileData = MultipartBody.Part.createFormData("file", file.name, requestFile)
        feedbackPagePresenter.sendAttachment(feedbackId, fileData)
    }

    private fun resizeImage(data: ScreenshotData) {
        val b = BitmapFactory.decodeFile(data.path)
        val origWidth = b.width
        val origHeight = b.height
        val destHeight = 2480
        val destWidth = origWidth / (origHeight.toDouble() / destHeight)
        val b2 = Bitmap.createScaledBitmap(b, destWidth.toInt(), destHeight, false)
        resizedUriImage = context?.let { getImageUri(it, b2) }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun goToTicketCreatedActivity() {
        activity?.finish()
        Intent(context, TicketCreatedActivity::class.java).apply {
            startActivityForResult(this, 1212)
        }
    }

    override fun showError(throwable: Throwable) {
        Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun categoriesMapper(data: CategoriesModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: Uri?) : FeedbackPageFragment {
            return FeedbackPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_URI_IMAGE, uri)
                }
            }
        }

        private const val REQUEST_CODE_IMAGE = 111
        private const val REQUEST_CODE_EDIT_IMAGE = 101

        private val FILE_NAME_PREFIX = "screenshot"
        private val PATH_SCREENSHOT = "screenshots/"
        private val PROJECTION = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        )
    }

    override fun addImageClick() {
        context?.let {
            val builder = ImagePickerBuilder(getString(R.string.image_picker_title),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY),
                    GalleryType.ALL, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                    null,
                    ImagePickerMultipleSelectionBuilder(
                    selectedImage, null, -1, 5
            ))
            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }
}