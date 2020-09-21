package com.tokopedia.developer_options.presentation.feedbackpage

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
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.api.*
import com.tokopedia.developer_options.presentation.feedbackpage.dialog.LoadingDialog
import com.tokopedia.developer_options.presentation.preference.Preferences
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feedback_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File


class FeedbackPageFragment: Fragment() {

    private lateinit var bugType : Spinner
    private lateinit var email: EditText
    private lateinit var affectedPage: EditText
    private lateinit var journey: EditText
    private lateinit var actualResult: EditText
    private lateinit var expectedResult: EditText
    private lateinit var imageView: ImageView
    private lateinit var submitButton: View
    private lateinit var feedbackApi: FeedbackApi
    private lateinit var compositeSubscription: CompositeSubscription
    private lateinit var myPreferences: Preferences
    private lateinit var screenshot: Screenshot

    private val PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    )

    private val FILE_NAME_PREFIX = "screenshot"
    private val PATH_SCREENSHOT = "screenshots/"

    private var deviceInfo: String = ""
    private var androidVersion: String = ""
    private var appVersion: String = ""
    private var versionCode: String = ""
    private var userId: String = ""
    private var sessionToken: String = ""
    private var fcmToken: String = ""
    private var loginState: String = ""
    private var uriImage: Uri? = null

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("grant_result_size", grantResults.size.toString())
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
        affectedPage = mainView.findViewById(R.id.affected_page)
        journey = mainView.findViewById(R.id.step_to_reproduce)
        actualResult = mainView.findViewById(R.id.actual_result)
        expectedResult = mainView.findViewById(R.id.expected_result)
        imageView = mainView.findViewById(R.id.image_feedback)
        submitButton = mainView.findViewById(R.id.submit_button)

        feedbackApi = ApiClient.getAPIService()
        compositeSubscription = CompositeSubscription()
        myPreferences = Preferences(context)
        loadingDialog = context?.let { LoadingDialog(it) }

        uriImage = arguments?.getParcelable("EXTRA_URI_IMAGE")
        Log.d("IMAGE_URI", uriImage.toString())
        initImageUri()

        /*screenshot = context?.contentResolver?.let {
            Screenshot(it, object : Screenshot.Listener {
                override fun onScreenShotTaken(screenshotData: ScreenshotData?) {
                    uriImage = Uri.parse(screenshotData?.path)
                    imageView.setImageURI(uriImage)
                }
            })
        }!!*/

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
            imageView.setImageURI(Uri.parse(screenshotData?.path))
        } else {
            imageView.visibility = View.GONE
        }
    }

    private fun initData() {
        val fields = VERSION_CODES::class.java.fields

        var codeName = "UNKNOWN"
        fields.filter { it.getInt(VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { codeName = it.name }

        when {
            codeName.startsWith("M") -> { codeName = "MARSHMALLOW" }
            codeName.startsWith("N") -> { codeName = "NOUGAT" }
            codeName.startsWith("O") -> { codeName = "OREO" }
            codeName.startsWith("P") -> { codeName = "PIE" }
            codeName.startsWith("Q") -> { codeName = "ANDROID 10" }
        }

        deviceInfo = (StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString())
        androidVersion = codeName
        appVersion = GlobalConfig.VERSION_NAME
        versionCode = GlobalConfig.VERSION_CODE.toString()
        userId = userSession?.userId ?: ""
        sessionToken = userSession?.deviceId ?: ""
        loginState = userSession?.isLoggedIn.toString()
    }

    private fun initListener() {
        val savedEmail = myPreferences.getSubmitFlag(userSession?.userId.toString())
        if (savedEmail != null) {
            email.setText(savedEmail)
        } else {
            email.setText(userSession?.email)
        }


        bugType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                //no-op
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }
        }

        submitButton.setOnClickListener {
            val emailText= email.text.toString()
            val affectedPageText = affectedPage.text.toString()
            val journeyText = journey.text.toString()
            val actualResultText = actualResult.text.toString()
            val expectedResultText = expectedResult.text.toString()

            when {
                TextUtils.isEmpty(emailText) -> {
                    setWrapperError(et_email_wrapper, "Email should not be empty" )
                }
                TextUtils.isEmpty(affectedPageText) -> {
                    setWrapperError(et_affected_page_wrapper, "Issue should not be empty" )
                }
                TextUtils.isEmpty(journeyText) -> {
                    setWrapperError(et_str_wrapper, "Affected Page should not be empty" )
                }
                TextUtils.isEmpty(actualResultText) -> {
                    setWrapperError(et_actual_result_wrapper, "Actual Result should not be empty" )
                }
                TextUtils.isEmpty(expectedResultText) -> {
                    setWrapperError(et_expected_result_wrapper, "Expected Result not be empty" )
                }
                else -> {
                    val issueType = bugType.selectedItem.toString()
                    submitFeedback(emailText, affectedPageText, journeyText, issueType, actualResultText, expectedResultText)
                }
            }
        }

    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.setHint("")
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

    private fun submitFeedback(email: String, page: String, desc: String, issueType: String, actualResult: String, expectedResult: String) {
        loadingDialog?.show()
        compositeSubscription.add(
                feedbackApi.getResponse(requestMapper(email, page, desc, issueType, actualResult, expectedResult))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<FeedbackResponse>() {
                            override fun onNext(t: FeedbackResponse) {
                                myPreferences.setSubmitFlag(email, userSession?.userId.toString())
                                if (uriImage != null) sendUriImage(t.key)
                                else goToTicketCreatedActivity(t.key)
                            }

                            override fun onCompleted() {
                                //no-op
                            }

                            override fun onError(e: Throwable?) {
                                loadingDialog?.dismiss()
                                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                            }

                        })
        )
    }

    private fun sendUriImage(issueKey: String) {
        val file = File(uriImage?.path)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileData = MultipartBody.Part.createFormData("file", file.name, requestFile)

        feedbackApi.getImageResponse("issue/$issueKey/attachments/", fileData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<ImageResponse>>(){
                    override fun onNext(t: List<ImageResponse>?) {
                        loadingDialog?.dismiss()
                        Toast.makeText(activity, issueKey, Toast.LENGTH_SHORT).show()
                        goToTicketCreatedActivity(issueKey)
//                        activity?.finish()
                    }

                    override fun onCompleted() {
                       //no-op
                    }

                    override fun onError(e: Throwable?) {
                        loadingDialog?.dismiss()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
    }


    private fun requestMapper(email: String, page: String, desc: String, issueType: String, actualResult: String, expectedResult: String): FeedbackRequest {
        val affectedVersion = if (GlobalConfig.isSellerApp()) "SA-$appVersion" else "MA-$appVersion"
        return FeedbackRequest(Fields(
                summary = "[INTERNAL-FEEDBACK] {$email} {$page}",
                project = Project(
                        id = "10027",
                        key = "AN",
                        name = "[MB] Android"
                ),
                issuetype = Issuetype(
                        id = "10004",
                        name = "Bug"
                ),
                description = Description(
                        version = 1,
                        type = "doc",
                        content = listOf(Content(
                                type = "paragraph",
                                content = listOf(DeepContent(
                                        type = "text",
                                        text = "Reporter : $email \n" +
                                                "Device Info : $deviceInfo | $androidVersion\n" +
                                                "App Version : $appVersion $versionCode \n" +
                                                "UserId : $userId \n" +
                                                "Session Token : $sessionToken \n" +
                                                "Login State : $loginState \n" +
                                                "Affected Page : $page \n\n" +
                                                "Step to Reproduce : \n" +
                                                "$desc \n\n" +
                                                "Actual Result : \n" +
                                                "$actualResult \n\n" +
                                                "Expected Result : \n" +
                                                expectedResult
                                ))
                        ))
                ),
                customfield_10077 = Customfield_10077(
                        value = "[MB] Android",
                        id = "10031"
                ),
                customfield_10548 = listOf(Customfield_10548(
                        value = "Main App",
                        id = "11200"
                )),
                customfield_10253 = Customfield_10253(
                        /*value = "Release Candidate",
                        id = "11209"*/
                        //Temporary
                        value = "In Development",
                        id = "11208"
                ),
                customfield_10181 = Customfield_10181(
                        value = "Android - Minion Jorge",
                        id = "11144"
                ),
                labels = listOf("Internal-Feedback"),
                customfield_10550 = listOf(issueType),
                versions = listOf(Version(
                        name = affectedVersion
                ))
        ))
    }

    private fun goToTicketCreatedActivity(ticketLink: String) {
        Intent(context, TicketCreatedActivity::class.java).apply {
            putExtra("EXTRA_IS_TICKET_LINK", ticketLink)
            startActivityForResult(this, 1212)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: Uri?) : FeedbackPageFragment {
            return FeedbackPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("EXTRA_URI_IMAGE", uri)
                }
            }
        }
    }
}