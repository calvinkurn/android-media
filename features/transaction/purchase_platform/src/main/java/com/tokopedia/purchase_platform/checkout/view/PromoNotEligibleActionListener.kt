package com.tokopedia.purchase_platform.checkout.view

/**
 * Created by Irfan Khoirul on 2019-06-24.
 */

interface PromoNotEligibleActionListener {

    fun onShow();

    fun onButtonContinueClicked(checkoutType: Int);

}