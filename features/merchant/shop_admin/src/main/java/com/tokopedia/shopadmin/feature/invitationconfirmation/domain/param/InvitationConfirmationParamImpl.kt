package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import javax.inject.Inject

class InvitationConfirmationParamImpl @Inject constructor(): InvitationConfirmationParam {

    private var shopManageID: String? = null

    override fun setShopManageId(manageID: String) {
        this.shopManageID = manageID
    }

    override fun getShopManageId(): String {
        return shopManageID?.ifEmpty { "" }.orEmpty()
    }
}