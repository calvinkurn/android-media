package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.*
import javax.inject.Inject

class CatalogListItemViewModel @Inject constructor(private val repository: CatalogListingRepository) : CatalogPurchaseRedeemptionViewModel(repository), CatalogListItemContract.Presenter {

    var pointRange = 0

    val latestStatusLiveData = MutableLiveData<List<CatalogStatusItem>>()

    override fun fetchLatestStatus(catalogsIds: List<Int>) {
        launchCatchError(block = {
            val data = repository.fetchLatestStatus(catalogsIds)
            if (data.catalogStatus != null) { //For detail page we only interested in one item
                latestStatusLiveData.value = data.catalogStatus.catalogs
            }
        }) {}
    }



}