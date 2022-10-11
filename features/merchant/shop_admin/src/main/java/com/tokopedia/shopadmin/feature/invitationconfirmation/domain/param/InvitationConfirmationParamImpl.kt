package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import javax.inject.Inject

class InvitationConfirmationParamImpl @Inject constructor(): InvitationConfirmationParam {

    private var shopManageID: String? = null
    private var shopName: String? = null
    private var adminType: String? = null

    override fun setShopManageId(manageID: String) {
        this.shopManageID = manageID
    }

    override fun setShopName(shopName: String) {
        this.shopName = shopName
    }

    override fun setAdminType(adminType: String) {
        this.adminType = adminType
    }

    override fun getShopManageId(): String {
        return shopManageID.orEmpty()
    }

    override fun getShopName(): String {
        return shopName.orEmpty()
    }

    override fun getAdminType(): String {
        return adminType.orEmpty()
    }
}