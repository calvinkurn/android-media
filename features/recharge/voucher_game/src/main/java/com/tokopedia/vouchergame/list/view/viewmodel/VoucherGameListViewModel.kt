package com.tokopedia.vouchergame.list.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListViewModel @Inject constructor(private val voucherGameUseCase: VoucherGameListUseCase,
                                                   private val graphqlRepository: GraphqlRepository,
                                                   private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    val voucherGameList = MutableLiveData<Result<VoucherGameListData>>()
    val voucherGameMenuDetail = MutableLiveData<Result<TopupBillsMenuDetail>>()

    fun getVoucherGameOperators(rawQuery: String, mapParam: Map<String, Any>, searchQuery: String, isForceRefresh: Boolean = false) {
        launch {
            voucherGameList.value = voucherGameUseCase.getVoucherGameOperators(rawQuery, mapParam, searchQuery, isForceRefresh)
        }
    }

    fun getVoucherGameMenuDetail(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            voucherGameMenuDetail.value = Success(data.catalogMenuDetailData)
        }) {
            voucherGameMenuDetail.value = Fail(it)
        }
    }

    fun createParams(menuID: Int): Map<String, Any> {
        return voucherGameUseCase.createParams(menuID)
    }

    fun createMenuDetailParams(menuID: Int): Map<String,Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
    }

}