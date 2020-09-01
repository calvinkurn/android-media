package com.tokopedia.developer_options.presentation.feedbackpage

import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.api.*
import com.tokopedia.developer_options.presentation.feedbackpage.dialog.LoadingDialog
import com.tokopedia.developer_options.presentation.preference.Preferences
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

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

    private var deviceInfo: String = ""
    private var androidVersion: String = ""
    private var appVersion: String = ""
    private var versionCode: String = ""
    private var userId: String = ""
    private var sessionToken: String = ""
    private var fcmToken: String = ""
    private var loginState: String = ""

    private var userSession: UserSessionInterface? = null
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_feedback_page, container, false)
        initView(mainView)
        initListener()
        return mainView
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


        screenshot = context?.contentResolver?.let {
            Screenshot(it, object : Screenshot.Listener {
                override fun onScreenShotTaken(screenshotData: ScreenshotData?) {
                    val uri = Uri.parse(screenshotData?.path)
                    imageView.setImageURI(uri)
                }
            })
        }!!

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
                    Toast.makeText(context,
                            "Email should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(affectedPageText) -> {
                    Toast.makeText(context,
                            "Issue should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(journeyText) -> {
                    Toast.makeText(context,
                            "Affected Page should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(actualResultText) -> {
                    Toast.makeText(context,
                            "Actual Result should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(expectedResultText) -> {
                    Toast.makeText(context,
                            "Expected Result should not be empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val issueType = bugType.selectedItem.toString()
                    submitFeedback(emailText, affectedPageText, journeyText, issueType, actualResultText, expectedResultText)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        screenshot.register()
    }

    override fun onPause() {
        super.onPause()
        screenshot.unregister()
    }

    private fun submitFeedback(email: String, page: String, desc: String, issueType: String, actualResult: String, expectedResult: String) {
        loadingDialog?.show()
        compositeSubscription.add(
                feedbackApi.getResponse(requestMapper(email, page, desc, issueType, actualResult, expectedResult))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<FeedbackResponse>() {
                            override fun onNext(t: FeedbackResponse) {
                                loadingDialog?.dismiss()
                                Toast.makeText(activity, t.key, Toast.LENGTH_SHORT).show()
                                myPreferences.setSubmitFlag(email, userSession?.userId.toString())
                                activity?.finish()
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
}