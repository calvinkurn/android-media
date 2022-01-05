package com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.fragment

import android.content.Intent
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dg_transaction.testing.digital_checkout.presentation.activity.DigitalCartActivityStub
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment

class EmoneyPdpFragmentStub: EmoneyPdpFragment() {

    override fun navigateToCart(categoryId: String) {
        context?.let {
            val intent = Intent(activity, DigitalCartActivityStub::class.java)
            emoneyPdpViewModel.digitalCheckoutPassData.categoryId = categoryId
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, emoneyPdpViewModel.digitalCheckoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }
}