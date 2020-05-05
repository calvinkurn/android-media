package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view

import androidx.fragment.app.Fragment
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment

interface PreferenceEditParent {

    fun setPreferenceIndex(preferenceIndex: String)
    fun getPreferenceIndex(): String

    fun setProfileId(profileId: Int)
    fun getProfileId(): Int

    fun setAddressId(addressId: Int)
    fun getAddressId(): Int

    fun setShippingId(shippingId: Int)
    fun getShippingId(): Int

    fun setGatewayCode(gatewayCode: String)
    fun getGatewayCode(): String

    fun setPaymentQuery(paymentQuery: String)
    fun getPaymentQuery(): String

    fun setShippingParam(shippingParam: ShippingParam)
    fun getShippingParam(): ShippingParam?

    fun getListShopShipment(): ArrayList<ShopShipment>?

    fun getShouldShowDeleteButton(): Boolean

    fun setHeaderTitle(title: String)
    fun setHeaderSubtitle(subtitle: String)

    fun addFragment(fragment: Fragment)
    fun goBack()

    fun setStepperValue(value: Int, isSmooth: Boolean = true)
    fun showStepper()
    fun hideStepper()

    fun showAddButton()
    fun hideAddButton()
    fun setAddButtonOnClickListener(onClick: () -> Unit)

    fun showDeleteButton()
    fun hideDeleteButton()
    fun setDeleteButtonOnClickListener(onClick: () -> Unit)

    fun getFromFlow(): Int
}