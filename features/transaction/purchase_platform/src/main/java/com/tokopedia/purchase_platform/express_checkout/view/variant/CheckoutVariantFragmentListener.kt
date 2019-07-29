package com.tokopedia.purchase_platform.express_checkout.view.variant

/**
 * Created by Irfan Khoirul on 04/12/18.
 */

interface CheckoutVariantFragmentListener {

    fun finishWithResult(messages: String)

    fun navigateAtcToOcs()

    fun navigateAtcToNcf()
}