package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

interface InvitationConfirmationParam {
    fun setShopId(shopId: String)
    fun setShopName(shopName: String)
    fun setShopManageId(manageID: String)

    fun getShopName(): String
    fun getShopId(): String
    fun getShopManageId(): String
}