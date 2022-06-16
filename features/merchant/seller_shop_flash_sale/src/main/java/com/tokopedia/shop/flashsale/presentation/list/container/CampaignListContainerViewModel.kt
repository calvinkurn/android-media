package com.tokopedia.shop.flashsale.presentation.list.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class CampaignListContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase
) : BaseViewModel(dispatchers.main) {

    private var autoFocusTabPosition = 0
    private var storedTabsMetadata : List<TabMeta> = emptyList()

    private val _tabsMeta = MutableLiveData<Result<List<TabMeta>>>()
    val tabsMeta: LiveData<Result<List<TabMeta>>>
        get() = _tabsMeta

    fun getTabsMeta() {
        launchCatchError(
            dispatchers.io,
            block = {
                val tabs = getSellerCampaignListMetaUseCase.execute()
                _tabsMeta.postValue(Success(tabs))
            },
            onError = { error ->
                _tabsMeta.postValue(Fail(error))
            }
        )

    }

    fun storeTabsMetadata(tabsMetadata : List<TabMeta>) {
        this.storedTabsMetadata = tabsMetadata
    }

    fun getStoredTabsMetadata(): List<TabMeta> {
        return storedTabsMetadata
    }

    fun setAutoFocusTabPosition(selectedTabPosition: Int) {
        this.autoFocusTabPosition = selectedTabPosition
    }

    fun getAutoFocusTabPosition(): Int {
        return autoFocusTabPosition
    }
}