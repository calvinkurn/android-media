package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

interface InvitationConfirmationParam {
    fun setShopManageId(manageID: String)
    fun setShopName(shopName: String)
    fun setAdminType(adminType: String)

    fun getShopManageId(): String
    fun getShopName(): String
    fun getAdminType(): String
}