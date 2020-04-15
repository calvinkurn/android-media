package com.tokopedia.vouchercreation.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.view.fragment.MerchantVoucherTargetFragment

class CreateMerchantVoucherStepsActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = MerchantVoucherTargetFragment.createInstance()

}