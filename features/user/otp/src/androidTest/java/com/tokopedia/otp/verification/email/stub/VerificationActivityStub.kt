package com.tokopedia.otp.verification.email.stub

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity

class VerificationActivityStub: VerificationActivity() {

    override fun getNewFragment(): Fragment {
        return VerificationFragmentStub()
    }

    override fun goToVerificationMethodPage() {
        val bundle = Bundle()
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        bundle.putParcelable(OtpConstant.OTP_MODE_EXTRA, ModeListData(
            modeCode=6,
            modeText="email",
            otpListText="<b>E-mail ke</b> <br>k****************@t********.com",
            afterOtpListText="Kode verifikasi telah dikirim melalui e-mail ke k****************@t********.com.",
            afterOtpListTextHtml="Kode verifikasi telah dikirim melalui e-mail ke k****************@t********.com.",
            otpListImgUrl="https://ecs7.tokopedia.net/otp/cotp/ICON_EMAIL_NEW.png",
            usingPopUp=false,
            popUpHeader="",
            popUpBody="",
            countdown=true,
            otpDigit=4
        ))

        val fragment = VerificationFragmentStub.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }
}