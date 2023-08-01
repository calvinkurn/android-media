package com.tokopedia.buy_more_get_more.sort.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.sort.mapper.ShopProductSortMapper
import com.tokopedia.buy_more_get_more.sort.model.ShopProductSortModel
import com.tokopedia.buy_more_get_more.sort.model.ShopStickySortFilter
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.domain.usecase.shopsort.GqlGetShopSortUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopProductSortViewModel @Inject constructor(
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper,
    private val dispatcherProvider: CoroutineDispatchers
): BaseViewModel(dispatcherProvider.main) {

    val shopSortListData = MutableLiveData<Result<ShopStickySortFilter>>()

    fun getShopSortListData() {
        launchCatchError(block = {
            val sortResponse = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getSortListData()
                },
                onError = {
                    shopSortListData.postValue(Fail(it))
                    null
                }
            )
            sortResponse.await()?.let { sort ->
                shopSortListData.postValue(
                    Success(
                        ShopStickySortFilter(sortList = sort)
                    )
                )
            }
        }) {
            shopSortListData.postValue(Fail(it))
        }
    }

    private suspend fun getSortListData(): List<ShopProductSortModel> {
        val listSort = gqlGetShopSortUseCase.executeOnBackground()
        return shopProductSortMapper.convertSort(listSort).toMutableList()
    }

}
