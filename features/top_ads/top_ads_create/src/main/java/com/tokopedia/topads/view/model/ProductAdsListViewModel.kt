package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ETALASE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.ROWS
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.SORT_BY
import com.tokopedia.topads.common.data.internal.ParamObject.START
import com.tokopedia.topads.common.data.internal.ParamObject.STATUS
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class ProductAdsListViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getEtalaseListUseCase: GetEtalaseListUseCase,
        private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : BaseViewModel(dispatcher) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit), onError: ((Throwable) -> Unit)) {
        launch {
            getEtalaseListUseCase.setParams(userSession.shopId ?: "0")
            getEtalaseListUseCase.execute(
                    {
                        onSuccess(it.shopShowcasesByShopID.result)
                    },
                    {
                        onError(it)
                    })
        }
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        launch {
            topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, userSession.shopId.toIntOrZero())
            topAdsGetListProductUseCase.execute(
                    {
                        if (it.topadsGetListProduct.data.isEmpty()) {
                            onEmpty()
                        } else {
                            if (etalaseId.isEmpty()) {
                                totalCount = it.topadsGetListProduct.data.size
                            }
                            onSuccess(it.topadsGetListProduct.data, it.topadsGetListProduct.eof)
                        }
                    },
                    {
                        onError(it)
                    })
        }
    }

    fun addSemuaProduk(): ResponseEtalase.Data.ShopShowcasesByShopID.Result {
        return ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
    }
}