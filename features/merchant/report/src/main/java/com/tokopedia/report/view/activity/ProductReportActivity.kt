package com.tokopedia.report.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.fragment.ProductReportFragment

class ProductReportActivity: BaseSimpleActivity(), HasComponent<MerchantReportComponent> {

    override fun getNewFragment(): Fragment = ProductReportFragment()

    override fun getComponent(): MerchantReportComponent = DaggerMerchantReportComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object {
         fun getCallingIntent(context: Context): Intent = Intent(context, ProductReportActivity::class.java)
    }
}