package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.domain.OrderList
import com.tokopedia.thankyou_native.presentation.adapter.model.PurchaseItemAdapterModel
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopItemAdapterModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderDetailListMapperViewModel @Inject constructor(dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val adapterListMutableLiveData = MutableLiveData<ArrayList<Visitable<*>>>()

    fun mapOrderListTOAdapterVisitable(orderList: ArrayList<OrderList>) {
        launchCatchError(
                block = {
                    val data = getAdapterVisitable(orderList)
                    adapterListMutableLiveData.value = data
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    private suspend fun getAdapterVisitable(orderListArrayList: ArrayList<OrderList>): ArrayList<Visitable<*>> = withContext(Dispatchers.IO) {
        val visitableArrayList = ArrayList<Visitable<*>>()
        orderListArrayList.forEach {
            val shopName = it.storeName
            visitableArrayList.add(ShopItemAdapterModel(shopName))
            it.purchaseItemList.forEach { purchasedItem ->
                visitableArrayList.add(PurchaseItemAdapterModel(purchasedItem.thumbnailProduct,
                        purchasedItem.productName,
                        "${purchasedItem.quantity}(${purchasedItem.weight})"))
            }
        }
        return@withContext visitableArrayList
    }

}