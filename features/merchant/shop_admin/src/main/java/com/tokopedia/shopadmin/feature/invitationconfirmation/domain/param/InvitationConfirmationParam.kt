package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

interface InvitationConfirmationParam {
    fun setShopId(shopId: String)
    fun setShopManageId(manageID: String)

    fun getShopId(): String
    fun getShopManageId(): String
}