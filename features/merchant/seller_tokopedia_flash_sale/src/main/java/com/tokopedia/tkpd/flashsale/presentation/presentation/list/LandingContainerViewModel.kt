package com.tokopedia.tkpd.flashsale.presentation.presentation.list

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.Result
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.presentation.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LandingContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase
) : BaseViewModel(dispatchers.main){

    private val _tabsMetadata = MutableStateFlow<Result<List<TabMetadata>>>(Result.Loading)
    val tabsMetadata = _tabsMetadata.asStateFlow()

    fun getTabsMetaData() {
        _tabsMetadata.value = Result.Loading

        launchCatchError(
            dispatchers.io,
            block = {
               val response = getFlashSaleListForSellerMetaUseCase.execute()

                _tabsMetadata.value = Result.Success(response)
            },
            onError = { error ->
                _tabsMetadata.value = Result.Failure(error)
            }
        )

    }
}