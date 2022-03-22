package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeCloseChannelRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeKeywordSearchRepository
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import javax.inject.Inject

class HomeSearchUseCase @Inject constructor(
        val homeKeywordSearchRepository: HomeKeywordSearchRepository
) {
    suspend fun onGetSearchHint(isFirstInstall: Boolean, deviceId: String, userId: String): SearchPlaceholder {
        return try {
            homeKeywordSearchRepository.params = homeKeywordSearchRepository.createParams(
                    isFirstInstall, deviceId, userId)
            return homeKeywordSearchRepository.executeOnBackground().searchData
        } catch (e: Exception) {
            SearchPlaceholder()
        }
    }
}