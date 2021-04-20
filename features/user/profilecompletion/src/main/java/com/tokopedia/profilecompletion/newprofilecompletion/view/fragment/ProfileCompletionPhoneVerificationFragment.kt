package com.tokopedia.profilecompletion.newprofilecompletion.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.newprofilecompletion.ProfileCompletionNewConstants
import com.tokopedia.profilecompletion.newprofilecompletion.data.ProfileCompletionDataView

/**
 * Created by nisie on 2/22/17.
 */
class ProfileCompletionPhoneVerificationFragment : AddPhoneFragment() {

    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var verifyButton: TextView? = null
    private var data: ProfileCompletionDataView? = null
    private var txtSkip: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        data = profileCompletionFragment?.data
        verifyButton = profileCompletionFragment?.view?.findViewById(R.id.txt_proceed)
        verifyButton?.text = resources.getString(R.string.continue_form)
        verifyButton?.visibility = View.GONE
        txtSkip = profileCompletionFragment?.view?.findViewById(R.id.txt_skip)
        txtSkip?.visibility = View.GONE
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun onSuccessAddPhone(result: AddPhoneResult) {
        super.dismissLoading()
        super.storeLocalSession(result.phoneNumber)
        profileCompletionFragment?.onSuccessEditProfile(ProfileCompletionNewConstants.EDIT_VERIF)
        Toast.makeText(activity, MethodChecker.fromHtml(getString(R.string.success_verify_phone_number)), Toast.LENGTH_LONG).show()
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