package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import javax.inject.Inject

class InvitationConfirmationParamImpl @Inject constructor(): InvitationConfirmationParam {

    private var shopId: String? = null
    private var shopManageID: String? = null

    override fun setShopId(shopId: String) {
        this.shopId = shopId
    }

    override fun setShopManageId(manageID: String) {
        this.shopManageID = manageID
    }

    override fun getShopId(): String {
        return shopId?.ifEmpty { "" }.orEmpty()
    }

    override fun getShopManageId(): String {
        return shopManageID?.ifEmpty { "" }.orEmpty()
    }
}