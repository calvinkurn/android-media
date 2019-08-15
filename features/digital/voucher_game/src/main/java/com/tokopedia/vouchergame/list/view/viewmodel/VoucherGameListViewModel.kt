package com.tokopedia.vouchergame.list.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListViewModel @Inject constructor(private val voucherGameUseCase: VoucherGameListUseCase,
                                                   val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val voucherGameList = MutableLiveData<Result<VoucherGameListData>>()

    fun getVoucherGameList(rawQuery: String, mapParam: Map<String, Any>) {
        launch {
            voucherGameList.value = voucherGameUseCase.getVoucherGameList(rawQuery, mapParam)
        }
    }

    fun searchVoucherGame(searchQuery: String, rawQuery: String, mapParam: Map<String, Any>) {
        launch {
            voucherGameList.value = voucherGameUseCase.searchVoucherGame(searchQuery, rawQuery, mapParam)
        }
    }

    fun createParams(menuID: Int, platformID: Int): Map<String,Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
//        params.put(PARAM_MENU_ID, menuID)
//        params.put(PARAM_PLATFORM_ID, platformID)
        params.put(PARAM_MENU_ID, 10)
        params.put(PARAM_PLATFORM_ID, 7)
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_PLATFORM_ID = "platformID"
    }

}