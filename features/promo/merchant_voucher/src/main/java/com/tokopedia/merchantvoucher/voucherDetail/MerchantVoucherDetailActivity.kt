package com.tokopedia.merchantvoucher.voucherDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import java.sql.RowId

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherDetailActivity : BaseSimpleActivity() {

    var voucherId: Int = 0
    var merchantVoucherViewModel: MerchantVoucherViewModel? = null
    var voucherShopId: String? = null

    override fun getNewFragment(): Fragment = MerchantVoucherDetailFragment.createInstance(voucherId, merchantVoucherViewModel,
            voucherShopId)

    companion object {
        const val VOUCHER_ID = "voucher_id"     // to get voucher detail
        const val VOUCHER = "voucher"           // voucher model
        const val VOUCHER_SHOP_ID = "voucher_shop_id"           // shop_id

        @JvmStatic
        fun createIntent(context: Context, voucherId: Int, merchantVoucherViewModel: MerchantVoucherViewModel? = null,
                         voucherShopId: String? = null): Intent {
            return Intent(context, MerchantVoucherDetailActivity::class.java).apply {
                putExtra(MerchantVoucherDetailActivity.VOUCHER_ID, voucherId)
                putExtra(MerchantVoucherDetailActivity.VOUCHER, merchantVoucherViewModel)
                putExtra(MerchantVoucherDetailActivity.VOUCHER_SHOP_ID, voucherShopId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = intent.getIntExtra(MerchantVoucherDetailActivity.VOUCHER_ID, 0)
        merchantVoucherViewModel = intent.getParcelableExtra(MerchantVoucherDetailActivity.VOUCHER)
        voucherShopId = intent.getStringExtra(MerchantVoucherDetailActivity.VOUCHER_SHOP_ID)
        super.onCreate(savedInstanceState)
    }
}
