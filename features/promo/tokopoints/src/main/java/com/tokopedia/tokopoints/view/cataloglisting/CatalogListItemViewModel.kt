package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.contract.CatalogListItemContract
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import com.tokopedia.tokopoints.view.util.ValidationError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rx.Subscriber
import java.util.*
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