package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantCancellationQuestionnaireFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment

class PowerMerchantCancellationQuestionnaireActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PowerMerchantCancellationQuestionnaireFragment.createInstance()
    }

}
