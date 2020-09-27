package com.tokopedia.developer_options.presentation.feedbackpage.ui

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.api.request.FeedbackFormRequest
import com.tokopedia.developer_options.presentation.feedbackpage.di.FeedbackPageComponent
import com.tokopedia.developer_options.presentation.feedbackpage.dialog.LoadingDialog
import com.tokopedia.developer_options.presentation.feedbackpage.utils.EXTRA_URI_IMAGE
import com.tokopedia.developer_options.presentation.preference.Preferences
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feedback_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject


class FeedbackPageFragment: BaseDaggerFragment(), FeedbackPageContract.View {

    @Inject
    lateinit var feedbackPagePresenter: FeedbackPagePresenter

    private lateinit var bugType : Spinner
    private lateinit var email: EditText
    private lateinit var page: EditText
    private lateinit var journey: EditText
    private lateinit var actualResult: EditText
    private lateinit var expectedResult: EditText
    private lateinit var imageView: ImageView
    private lateinit var submitButton: View
    private lateinit var compositeSubscription: CompositeSubscription
    private lateinit var myPreferences: Preferences
    private lateinit var tvImage: Typography

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
    private var categoryItem: Int = -1

    private var userSession: UserSessionInterface? = null
    private var loadingDialog: LoadingDialog? = null

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_feedback_page, container, false)
        if(!allPermissionsGranted()) {
            requestPermissions(requiredPermissions, 5111)
        }
        initView(mainView)
        initListener()
        return mainView
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

    private fun initView(mainView: View){
        email =  mainView.findViewById(R.id.email)
        bugType = mainView.findViewById(R.id.spinner_bug_type)
        page = mainView.findViewById(R.id.page)
        journey = mainView.findViewById(R.id.step_to_reproduce)
        actualResult = mainView.findViewById(R.id.actual_result)
        expectedResult = mainView.findViewById(R.id.expected_result)
        imageView = mainView.findViewById(R.id.image_feedback)
        submitButton = mainView.findViewById(R.id.submit_button)
        tvImage = mainView.findViewById(R.id.image_feedback_tv)
        feedbackPagePresenter.attachView(this)

        compositeSubscription = CompositeSubscription()
        myPreferences = Preferences(context)
        loadingDialog = context?.let { LoadingDialog(it) }

        uriImage = arguments?.getParcelable(EXTRA_URI_IMAGE)

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
            imageView.visibility = View.VISIBLE
            tvImage.visibility = View.VISIBLE
            imageView.setImageURI(Uri.parse(screenshotData?.path))
        } else {
            imageView.visibility = View.GONE
            tvImage.visibility = View.GONE
        }
    }

    private fun initData() {
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

    private fun initListener() {
        val separator = "@"
        val loginEmail = userSession?.email
        val topedEmail = loginEmail?.lastIndexOf(separator)
        val savedEmail = myPreferences.getSubmitFlag(userSession?.userId.toString())
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
/*
        email.addTextChangedListener(setWrapperWatcher(et_email_wrapper))
        page.addTextChangedListener(setWrapperWatcher(et_affected_page_wrapper))
        journey.addTextChangedListener(setWrapperWatcher(et_str_wrapper))
        actualResult.addTextChangedListener(setWrapperWatcher(et_actual_result_wrapper))
        expectedResult.addTextChangedListener(setWrapperWatcher(et_expected_result_wrapper))*/

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

    private fun setWrapperWatcher(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }
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
        return if (isPathScreenshot(path) && isFileScreenshot(fileName)) {
            ScreenshotData(id, fileName, path)
        } else {
            null
        }
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
        myPreferences.setSubmitFlag(emailTokopedia, userSession?.userId.toString())
    }

    override fun checkUriImage(feedbackId: Int?) {
        if (uriImage != null) feedbackPagePresenter.sendAttachment(feedbackId, checkUriImage())
        else feedbackPagePresenter.commitData(feedbackId)
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

    private fun checkUriImage(): MultipartBody.Part {
        val screenshotData = uriImage?.let { handleItem(it) }
        val file = File(screenshotData?.path)

        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileData = MultipartBody.Part.createFormData("file", file.name, requestFile)

        return fileData
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

        private val FILE_NAME_PREFIX = "screenshot"
        private val PATH_SCREENSHOT = "screenshots/"
        private val PROJECTION = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        )
    }
}