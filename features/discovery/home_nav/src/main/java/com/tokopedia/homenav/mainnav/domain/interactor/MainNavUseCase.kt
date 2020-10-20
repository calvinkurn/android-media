package com.tokopedia.homenav.mainnav.domain.interactor

import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.data.repository.MainNavRepo
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainNavUseCase @Inject constructor(
        private val mainNavRepo: MainNavRepo,
        private val mainNavMapper: MainNavMapper
) {

    fun getMainNavData(): Flow<MainNavigationDataModel> = flow {
        mainNavRepo.getMainNavData().collect{data ->

            emit(mainNavMapper.mapData(data))
        }
    }

}