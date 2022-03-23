package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import javax.inject.Inject

class InvitationConfirmationParamImpl @Inject constructor(): InvitationConfirmationParam {

    private var shopId: String? = null
    private var shopName: String? = null
    private var otpToken: String? = null
    private var shopManageID: String? = null

    override fun setShopId(shopId: String) {
        this.shopId = shopId
    }

    override fun setShopName(shopName: String) {
        this.shopName = shopName
    }

    override fun setOtpToken(otpToken: String) {
        this.otpToken = otpToken
    }

    override fun setShopManageId(manageID: String) {
        this.shopManageID = manageID
    }

    override fun getShopName(): String {
        return shopName?.ifEmpty { "" }.orEmpty()
    }

    override fun getShopId(): String {
        return shopId?.ifEmpty { "" }.orEmpty()
    }

    override fun getOtpToken(): String {
        return otpToken?.ifEmpty { "" }.orEmpty()
    }

    override fun getShopManageId(): String {
        return shopManageID?.ifEmpty { "" }.orEmpty()
    }
}