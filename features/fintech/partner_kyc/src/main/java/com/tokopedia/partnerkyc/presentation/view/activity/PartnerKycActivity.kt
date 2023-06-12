package com.tokopedia.partnerkyc.presentation.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.partnerkyc.presentation.view.fragment.PartnerKycFragment

class PartnerKycActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val url = intent.data?.getQueryParameter("url")
            ?: "https://sandbox01.indodana.com/borrower/credit-limit/apply/sign-in?campaignid=%7Btokopedia%7C%7Ccustomized%7D&formVersion=V4&network=tokopedia&phoneNumber=%7BphoneNumber%7D&uiVersion=V2&utm_campaign=%7Btokopedia%7C%7Ccustomized%7D&utm_medium=webform&utm_source=tokopedia"
        return PartnerKycFragment.create(url)
    }
}
