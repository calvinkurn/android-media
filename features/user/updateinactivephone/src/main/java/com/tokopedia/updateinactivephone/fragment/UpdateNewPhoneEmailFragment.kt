package com.tokopedia.updateinactivephone.fragment

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

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.core.analytics.ScreenTracking
import com.tokopedia.core.app.MainApplication
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking

class UpdateNewPhoneEmailFragment : TkpdBaseV4Fragment() {

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

    override fun getScreenName(): String {
        return UpdateInactivePhoneEventConstants.Screen.INPUT_NEW_PHONE_SCREEN
    }

    override fun onStart() {
        super.onStart()
        ScreenTracking.screen(MainApplication.getAppContext(), screenName)
        activity?.let { UpdateInactivePhoneEventTracking.eventViewKirimPengajuan(it) }
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

        submissionButton?.setOnClickListener { view1 ->
            activity?.let { UpdateInactivePhoneEventTracking.eventClickKirimPengajuan(it) }
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
        phoneErrorTextView?.text = getString(phoneErrorId)
        phoneErrorTextView?.visibility = View.VISIBLE
        phoneHintTextView?.visibility = View.GONE
    }

    fun showErrorEmail(error_invalid_email: Int) {
        emailErrorTextView?.text = getString(error_invalid_email)
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
