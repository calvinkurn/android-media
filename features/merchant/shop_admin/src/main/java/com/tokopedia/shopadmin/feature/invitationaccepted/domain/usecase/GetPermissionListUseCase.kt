package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper.PermissionListMapper
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPermissionListUseCase @Inject constructor(
    private val getAdminManagementInfoListUseCase: Lazy<GetAdminManagementInfoListUseCase>,
    private val getAdminInfoUseCase: Lazy<GetAdminInfoUseCase>,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val permissionListMapper: PermissionListMapper
) {

    suspend fun execute(shopId: String): List<AdminPermissionUiModel> {
        val allPermissionList = withContext(coroutineDispatchers.io) {
            getAdminManagementInfoListUseCase.get().execute(shopId)
        }
        val adminPermissionList = withContext(coroutineDispatchers.io) {
            getAdminInfoUseCase.get().execute(shopId.toLongOrZero())
        }
        return permissionListMapper.mapAdminPermissionListUiModel(allPermissionList, adminPermissionList)
    }
}