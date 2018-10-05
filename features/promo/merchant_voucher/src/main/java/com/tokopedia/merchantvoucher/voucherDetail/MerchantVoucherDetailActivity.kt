package com.tokopedia.merchantvoucher.voucherDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherDetailActivity : BaseSimpleActivity() {

    var voucherId: Int = 0
    var merchantVoucherViewModel: MerchantVoucherViewModel? = null

    override fun getNewFragment(): Fragment = MerchantVoucherDetailFragment.createInstance(voucherId, merchantVoucherViewModel)

    companion object {
        const val VOUCHER_ID = "voucher_id"     // to get voucher detail
        const val VOUCHER = "voucher"           // voucher model


        @JvmStatic
        fun createIntent(context: Context, voucherId: Int, merchantVoucherViewModel: MerchantVoucherViewModel? = null): Intent {
            return Intent(context, MerchantVoucherDetailActivity::class.java).apply {
                putExtra(MerchantVoucherDetailActivity.VOUCHER_ID, voucherId)
                putExtra(MerchantVoucherDetailActivity.VOUCHER, merchantVoucherViewModel)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = intent.getIntExtra(MerchantVoucherDetailActivity.VOUCHER_ID, 0)
        merchantVoucherViewModel = intent.getParcelableExtra(MerchantVoucherDetailActivity.VOUCHER)
        GraphqlClient.init(this)
        super.onCreate(savedInstanceState)
    }
}
