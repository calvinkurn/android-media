package com.tokopedia.profilecompletion.newprofilecompletion.view.fragment

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.newprofilecompletion.ProfileCompletionNewConstants
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform
import com.tokopedia.profilecompletion.newprofilecompletion.data.ProfileCompletionDataView

/**
 * Created by nisie on 2/22/17.
 */
class ProfileCompletionPhoneVerificationFragment : PhoneVerificationFragment() {

    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var verifyButton: TextView? = null
    private var data: ProfileCompletionDataView? = null
    private var txtSkip: TextView? = null

    override fun findView(view: View) {
        super.findView(view)
        initView()
    }

    override fun setViewListener() {
        super.setViewListener()
        skipButton.visibility = View.GONE
        txtSkip?.setOnClickListener { profileCompletionFragment?.skipView(TAG) }
    }

    private fun initView() {
        data = profileCompletionFragment?.data
        verifyButton = profileCompletionFragment?.view?.findViewById(R.id.txt_proceed)
        verifyButton?.text = resources.getString(R.string.continue_form)
        verifyButton?.visibility = View.GONE
        txtSkip = profileCompletionFragment?.view?.findViewById(R.id.txt_skip)
        txtSkip?.visibility = View.GONE
        if (data?.phone != null) {
            phoneNumberEditText.text = transform(data?.phone?: "")
        } else {
            SnackbarManager.make(activity,
                    getString(R.string.please_fill_phone_number),
                    Snackbar.LENGTH_LONG)
                    .show()
        }
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun onSuccessVerifyPhoneNumber() {
        profileCompletionFragment?.userSession?.setIsMSISDNVerified(true)
        profileCompletionFragment?.onSuccessEditProfile(ProfileCompletionNewConstants.EDIT_VERIF)
        Toast.makeText(activity, MethodChecker.fromHtml(getString(com.tokopedia.phoneverification.R.string.success_verify_phone_number)), Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "verif"

        fun createInstance(view: ProfileCompletionFragment?): ProfileCompletionPhoneVerificationFragment {
            val fragment = ProfileCompletionPhoneVerificationFragment()
            fragment.profileCompletionFragment = view
            return fragment
        }
    }
}