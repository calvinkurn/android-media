package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import javax.inject.Inject

class InvitationConfirmationParamImpl @Inject constructor(): InvitationConfirmationParam {

    private var shopManageID: String? = null
    private var shopName: String? = null

    override fun setShopManageId(manageID: String) {
        this.shopManageID = manageID
    }

    override fun setShopName(shopName: String) {
        this.shopName = shopName
    }

    override fun getShopManageId(): String {
        return shopManageID?.ifEmpty { "" }.orEmpty()
    }

    override fun getShopName(): String {
        return shopName?.ifEmpty { "" }.orEmpty()
    }
}