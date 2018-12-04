package com.tokopedia.expresscheckout.view.variant

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface CheckoutVariantActionListener {

    fun onNeedToNotifySingleItem(position: Int)

    fun onNeedToNotifyAllItem()

    fun onClickEditProfile()

    fun onClickEditDuration()

    fun onClickEditCourier()

    fun onClickInsuranceInfo(insuranceInfo: String)

}