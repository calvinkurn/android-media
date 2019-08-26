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
                                                   dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val voucherGameList = MutableLiveData<Result<VoucherGameListData>>()

    fun getVoucherGameList(rawQuery: String, mapParam: Map<String, Any>, searchQuery: String, isForceRefresh: Boolean = false) {
        launch {
            voucherGameList.value = voucherGameUseCase.getVoucherGameList(rawQuery, mapParam, searchQuery, isForceRefresh)
        }
    }

    fun createParams(menuID: Int): Map<String, Any> {
        return voucherGameUseCase.createParams(menuID)
    }

}