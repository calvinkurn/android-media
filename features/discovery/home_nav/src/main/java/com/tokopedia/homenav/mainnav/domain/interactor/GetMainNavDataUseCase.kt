package com.tokopedia.homenav.mainnav.domain.interactor

import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMainNavDataUseCase @Inject constructor(
        private val mainNavMapper: MainNavMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase

): UseCase<MainNavigationDataModel>() {

    override suspend fun executeOnBackground(): MainNavigationDataModel {
        return mainNavMapper.mapMainNavData(getUserInfoUseCase.executeOnBackground())
    }
}