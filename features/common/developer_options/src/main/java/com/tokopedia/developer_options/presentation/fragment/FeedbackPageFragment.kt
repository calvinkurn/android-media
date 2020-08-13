package com.tokopedia.developer_options.presentation.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.tokopedia.developer_options.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class FeedbackPageFragment: Fragment() {

    private lateinit var bugType : Spinner
    private lateinit var email: EditText
    private lateinit var issueDesc: EditText
    private lateinit var affectedPage: EditText
    private lateinit var submitButton: View

    private var userSession: UserSessionInterface? = null

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

        context?.let { ArrayAdapter.createFromResource(it,
                R.array.bug_type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bugType.adapter = adapter
        } }

        userSession = UserSession(activity)
    }

    private fun initListener() {
        email.setText(userSession?.email)

        bugType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                /*On Item selected*/
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
                    /*arahin ke submit button*/
                }
            }
        }

    }



}