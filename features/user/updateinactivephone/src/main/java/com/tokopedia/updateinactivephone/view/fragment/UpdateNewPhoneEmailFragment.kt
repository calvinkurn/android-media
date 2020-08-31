package com.tokopedia.updateinactivephone.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.USER_ID
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneAnalytics
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants
import com.tokopedia.updateinactivephone.di.component.DaggerUpdateInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import javax.inject.Inject

class UpdateNewPhoneEmailFragment : BaseDaggerFragment() {

    private var userId: String? = null
    private var oldPhoneNumber: String? = null
    private var newPhoneEditText: EditText? = null
    private var newEmailEditText: EditText? = null
    private var submissionButton: Button? = null
    private var emailHintTextView: TextView? = null
    private var phoneHintTextView: TextView? = null

    private var phoneErrorTextView: TextView? = null
    private var emailErrorTextView: TextView? = null

    private var updateNewPhoneEmailInteractor: UpdateNewPhoneEmailInteractor? = null

    @Inject
    lateinit var analytics: UpdateInactivePhoneAnalytics

    override fun getScreenName(): String {
        return UpdateInactivePhoneEventConstants.INPUT_NEW_PHONE_SCREEN
    }

    override fun initInjector() {
        DaggerUpdateInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .updateInactivePhoneModule(UpdateInactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    override fun onStart() {
        super.onStart()
        analytics.screen(screenName)
        analytics.eventViewKirimPengajuan()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        try {
            updateNewPhoneEmailInteractor = context as UpdateNewPhoneEmailInteractor
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_update_new_phone_email_fragment, parent, false)
        prepareView(view)
        return view
    }

    private fun prepareView(view: View) {
        if (arguments != null) {
            val bundle = arguments
            userId = bundle?.getString(USER_ID)
            oldPhoneNumber = bundle?.getString(OLD_PHONE)
        }

        emailErrorTextView = view.findViewById(R.id.email_error)
        phoneErrorTextView = view.findViewById(R.id.phone_error)

        newEmailEditText = view.findViewById(R.id.new_email_edit_text)
        newPhoneEditText = view.findViewById(R.id.new_phone_edit_text)
        submissionButton = view.findViewById(R.id.submission_button)

        emailHintTextView = view.findViewById(R.id.email_hint_text_view)
        phoneHintTextView = view.findViewById(R.id.phone_hint_text_view)

        newEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                setSubmissionButtonState()
                emailErrorTextView?.visibility = View.GONE
                emailHintTextView?.visibility = View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        newPhoneEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                setSubmissionButtonState()
                phoneErrorTextView?.visibility = View.GONE
                phoneHintTextView?.visibility = View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        submissionButton?.setOnClickListener {
            analytics.eventClickKirimPengajuan()
            analytics.eventClickButtonSubmission()
            updateNewPhoneEmailInteractor?.onSubmissionButtonClicked(
                    newEmailEditText?.text.toString(),
                    newPhoneEditText?.text.toString(),
                    userId)
        }

    }

    private fun setSubmissionButtonState() {
        if (!TextUtils.isEmpty(newEmailEditText?.text) && !TextUtils.isEmpty(newPhoneEditText?.text)) {
            submissionButton?.isEnabled = true
            submissionButton?.isClickable = true
            submissionButton?.setTextColor(Color.WHITE)
        } else {
            submissionButton?.isEnabled = false
            submissionButton?.isClickable = false
            submissionButton?.setTextColor(resources.getColor(R.color.black_26))
        }
    }

    fun showErrorPhone(phoneErrorId: Int) {
        val message = getString(phoneErrorId)
        analytics.eventFailedClickButtonSubmission(message)
        phoneErrorTextView?.text = message
        phoneErrorTextView?.visibility = View.VISIBLE
        phoneHintTextView?.visibility = View.GONE
    }

    fun showErrorEmail(error_invalid_email: Int) {
        val message = getString(error_invalid_email)
        analytics.eventFailedClickButtonSubmission(message)
        emailErrorTextView?.text = message
        emailErrorTextView?.visibility = View.VISIBLE
        emailHintTextView?.visibility = View.GONE
    }

    interface UpdateNewPhoneEmailInteractor {
        fun onSubmissionButtonClicked(email: String, phone: String, userId: String?)
    }

    companion object {

        fun getInstance(bundle: Bundle): UpdateNewPhoneEmailFragment {
            val updateNewPhoneEmailFragment = UpdateNewPhoneEmailFragment()
            updateNewPhoneEmailFragment.arguments = bundle
            return updateNewPhoneEmailFragment
        }
    }
}
