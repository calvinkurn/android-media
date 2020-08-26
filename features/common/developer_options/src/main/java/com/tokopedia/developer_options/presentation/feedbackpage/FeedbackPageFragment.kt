package com.tokopedia.developer_options.presentation.feedbackpage

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedbackPageFragment: Fragment() {

    private lateinit var bugType : Spinner
    private lateinit var email: EditText
    private lateinit var issueDesc: EditText
    private lateinit var affectedPage: EditText
    private lateinit var submitButton: View
    private lateinit var feedbackApi: FeedbackApi
    private lateinit var compositeSubscription: CompositeSubscription
    private lateinit var myPreferences: Preferences

    private var deviceInfo: String = ""
    private var androidVersion: String = ""
    private var appVersion: String = ""

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
        issueDesc = mainView.findViewById(R.id.issue_description)
        affectedPage = mainView.findViewById(R.id.affected_page)
        submitButton = mainView.findViewById(R.id.submit_button)

        feedbackApi = ApiClient.getAPIService()
        compositeSubscription = CompositeSubscription()
        myPreferences = Preferences(context)
        loadingDialog = context?.let { LoadingDialog(it) }

        context?.let { ArrayAdapter.createFromResource(it,
                R.array.bug_type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bugType.adapter = adapter
        } }

        userSession = UserSession(activity)
        initVersionData()

    }

    private fun initVersionData() {
        val fields = VERSION_CODES::class.java.fields
        var codeName = "UNKNOWN"
        fields.filter { it.getInt(VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { codeName = it.name }

        when {
            codeName.startsWith("M") -> { codeName = "MARSHMALLOW" }
            codeName.startsWith("N") -> { codeName = "NOUGAT" }
            codeName.startsWith("O") -> { codeName = "OREO" }
            codeName.startsWith("N") -> { codeName = "PIE" }
            codeName.startsWith("Q") -> { codeName = "ANDROID 10" }
        }

        deviceInfo = (StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString())
        androidVersion = codeName
        appVersion = context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName ?: ""
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
            val emailText: String = email.text.toString()
            val issueText: String = issueDesc.text.toString()
            val affectedPageText: String = affectedPage.text.toString()
            when {
                TextUtils.isEmpty(emailText) -> {
                    Toast.makeText(context,
                            "Email should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(issueText) -> {
                    Toast.makeText(context,
                            "Issue should not be empty", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(affectedPageText) -> {
                    Toast.makeText(context,
                            "Affected Page should not be empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val issueType = bugType.selectedItem.toString()
                    Log.d("ANDROID_VERSION", androidVersion)
                    submitFeedback(emailText, affectedPageText, issueText, issueType)
                }
            }
        }

    }

    private fun submitFeedback(email: String, page: String, desc: String, issueType: String) {
        loadingDialog?.show()
        compositeSubscription.add(
                feedbackApi.getResponse(requestMapper(email, page, desc, issueType))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<FeedbackResponse>() {
                            override fun onNext(t: FeedbackResponse) {
                                loadingDialog?.dismiss()
                                Toast.makeText(activity, t.key.toString(), Toast.LENGTH_SHORT).show()
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

    private fun requestMapper(email: String, page: String, desc: String, issueType: String): FeedbackRequest {
        val affectedVersion = if (GlobalConfig.isSellerApp()) "SA-$appVersion" else "MA-$appVersion"
        Log.d("AFFECTED_VERSION", affectedVersion)

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
                                                "App Version : $appVersion \n" +
                                                "Affected Page : $page \n\n" +
                                                "Description of Issue : \n" +
                                                desc
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