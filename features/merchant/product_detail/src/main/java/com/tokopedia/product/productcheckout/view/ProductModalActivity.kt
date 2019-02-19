package com.tokopedia.product.productcheckout.view

import android.support.v4.app.Fragment
import com.tokopedia.expresscheckout.router.ExpressCheckoutInternalRouter
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActivity
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantFragment
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam

/**
 * Created by hendry on 19/02/19.
 */
class ProductModalActivity : CheckoutVariantActivity() {
    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance(
                intent.extras[ExpressCheckoutInternalRouter.EXTRA_ATC_REQUEST] as AtcRequestParam)
    }

}
