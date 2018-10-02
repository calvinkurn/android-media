package com.tokopedia.merchantvoucher.voucherDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherComponent
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherDetailActivity : BaseSimpleActivity(), HasComponent<MerchantVoucherComponent> {
    override fun getComponent() = DaggerMerchantVoucherComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    var voucherId: Int = 0

    override fun getNewFragment(): Fragment = MerchantVoucherDetailFragment.createInstance(voucherId)

    companion object {
        const val VOUCHER_ID = "voucher_id"     // to get voucher detail


        @JvmStatic
        fun createIntent(context: Context, voucherId: Int): Intent {
            return Intent(context, MerchantVoucherDetailActivity::class.java).apply {
                putExtra(MerchantVoucherDetailActivity.VOUCHER_ID, voucherId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = intent.getIntExtra(MerchantVoucherDetailActivity.VOUCHER_ID, 0)
        GraphqlClient.init(this)
        super.onCreate(savedInstanceState)
    }
}
