package com.tokopedia.homenav.mainnav.domain.interactor

import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetMainNavDataUseCase @Inject constructor(
        private val mainNavMapper: MainNavMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getCategoryGroupUseCase: GetCategoryGroupUseCase
): UseCase<MainNavigationDataModel>() {

    override suspend fun executeOnBackground(): MainNavigationDataModel {
        return withContext(coroutineContext){
            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }
            val getCategoryCall = async {
                getCategoryGroupUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.executeOnBackground()
            }

            val userInfoData = getUserInfoCall.await()
            val categoryData = (getCategoryCall.await().takeIf { it is Success } as? Success<List<DynamicHomeIconEntity.Category>>)?.data

            mainNavMapper.mapMainNavData(
                userInfoData,
                categoryData
            )
        }
    }
}