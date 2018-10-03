package com.tokopedia.merchantvoucher.voucherDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.graphql.data.GraphqlClient

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherDetailActivity : BaseSimpleActivity() {

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
