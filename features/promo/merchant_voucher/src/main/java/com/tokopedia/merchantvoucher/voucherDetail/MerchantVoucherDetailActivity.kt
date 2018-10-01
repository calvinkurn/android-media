package com.tokopedia.merchantvoucher.voucherDetail

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherComponent
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherDetailActivity : BaseSimpleActivity(), HasComponent<MerchantVoucherComponent> {
    override fun getComponent() = DaggerMerchantVoucherComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getNewFragment(): Fragment = MerchantVoucherDetailFragment.createInstance()

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, MerchantVoucherListActivity::class.java)
    }
}
