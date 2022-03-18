package com.tokopedia.shopadmin.invitationaccepted.presentation.model

data class InvitationAcceptedUiModel(
    val adminInfoUiModel: AdminInfoUiModel = AdminInfoUiModel(),
    val adminPermissionUiModel: List<AdminPermissionUiModel> = listOf()
)