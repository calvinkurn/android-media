package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment

class VerificationMethodFragmentStub : VerificationMethodFragment() {

    override fun onGoToInactivePhoneNumber() {
        analytics.trackClickInactivePhoneNumber(otpData.otpType.toString())
        analytics.trackClickInactivePhoneLink()
        activity?.finish()
    }

    companion object {
        fun createInstance(
                bundle: Bundle
        ): VerificationMethodFragmentStub {
            return VerificationMethodFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}