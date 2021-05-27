package com.tokopedia.minicart.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUiModel
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import javax.inject.Inject

class MiniCartListViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                                private val miniCartListViewHolderMapper: MiniCartListViewHolderMapper) : BaseViewModel(executorDispatchers.main) {

    private val _miniCartUiModel = MutableLiveData<MiniCartUiModel>()
    val miniCartUiModel: LiveData<MiniCartUiModel>
        get() = _miniCartUiModel

    fun getCartList(shopIds: List<String>) {
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(onSuccess = {
            _miniCartUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
        }, onError = {
            _miniCartUiModel.value = MiniCartUiModel().apply {
                title = "Belanjaanmu di TokoNOW!"
            }
        })

    }

    fun calculateProduct() {
        val miniCartProductList = mutableListOf<MiniCartProductUiModel>()
        miniCartUiModel.value?.visitables?.forEach { visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                miniCartProductList.add(visitable)
            }
        }

        // Set temporary parent id for non variant product
        var tmpParentId = 0
        miniCartProductList.forEach { visitable ->
            if (visitable.parentId == "0") {
                visitable.parentId = (++tmpParentId).toString()
            }
        }

        // Map of parent id - qty
        val productParentQtyMap = mutableMapOf<String, Int>()
        miniCartProductList.forEach { visitable ->
            if (productParentQtyMap.containsKey(visitable.parentId)) {
                val newQty = productParentQtyMap[visitable.parentId] ?: 0 + visitable.productQty
                productParentQtyMap[visitable.parentId] = newQty
            } else {
                productParentQtyMap[visitable.parentId] = visitable.productQty
            }
        }

        // Set wholesale price
        miniCartProductList.forEach { visitable ->
            visitable.wholesalePriceGroup.forEach wholesaleLoop@{ wholesalePrice ->
                val qty = productParentQtyMap[visitable.parentId] ?: 0
                if (qty >= wholesalePrice.qtyMin) {
                    visitable.productWholeSalePrice = wholesalePrice.prdPrc
                    return@wholesaleLoop
                }
            }
        }

        // Calculate total price
        var totalPrice = 0L
        var totalQty = 0
        miniCartProductList.forEach { visitable ->
            val price = if (visitable.productWholeSalePrice > 0) visitable.productWholeSalePrice else visitable.productPrice
            totalPrice += visitable.productQty * price
            totalQty += visitable.productQty
            miniCartUiModel.value?.miniCartWidgetData?.totalProductPrice = totalPrice
            miniCartUiModel.value?.miniCartWidgetData?.totalProductCount = totalQty
        }

        _miniCartUiModel.value = miniCartUiModel.value
    }
}