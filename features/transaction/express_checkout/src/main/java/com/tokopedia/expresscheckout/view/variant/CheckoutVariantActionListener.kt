package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantOptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantProductViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantTypeVariantViewModel
import com.tokopedia.transactiondata.entity.response.variantdata.Option

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

    fun onBindVariantGetProductViewModel(): CheckoutVariantProductViewModel?

    fun onBindVariantUpdateProductViewModel()

    fun onChangeVariant(variantId: Int, checkoutVariantOptionVariantViewModel: CheckoutVariantOptionVariantViewModel)

}