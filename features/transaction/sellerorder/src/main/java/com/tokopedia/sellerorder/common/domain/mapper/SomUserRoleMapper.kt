package com.tokopedia.sellerorder.common.domain.mapper

import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleDataModel
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import javax.inject.Inject

class SomUserRoleMapper @Inject constructor() {

    companion object {
        private const val MANAGE_SHOP = "MANAGE_SHOP"
        private const val MANAGE_TX = "MANAGE_TX"
        private const val MANAGE_INBOX = "MANAGE_INBOX"
        private const val MANAGE_SHOPSTATS = "MANAGE_SHOPSTATS"
        private const val MANAGE_TA = "MANAGE_TA"
        private const val MANAGE_KS = "MANAGE_KS"
    }

    fun mapDomainToUiModel(data: SomGetUserRoleDataModel?): SomGetUserRoleUiModel = SomGetUserRoleUiModel(roles = mapRoles(data?.roles))

    private fun mapRoles(roles: List<String>?): List<Roles> {
        return roles?.map {
            when (it) {
                MANAGE_SHOP -> Roles.MANAGE_SHOP
                MANAGE_TX -> Roles.MANAGE_TX
                MANAGE_INBOX -> Roles.MANAGE_INBOX
                MANAGE_SHOPSTATS -> Roles.MANAGE_SHOPSTATS
                MANAGE_TA -> Roles.MANAGE_TA
                MANAGE_KS -> Roles.MANAGE_KS
                else -> throw IllegalArgumentException("Unknown user role $it!")
            }
        }.orEmpty()
    }
}