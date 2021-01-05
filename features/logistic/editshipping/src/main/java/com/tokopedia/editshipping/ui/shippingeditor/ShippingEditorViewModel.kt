package com.tokopedia.editshipping.ui.shippingeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocWhitelist
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingEditorViewModel @Inject constructor(
        private val repo: ShopLocationRepository) : ViewModel() {

    private val _shopWhitelist = MutableLiveData<ShippingEditorState<ShopLocWhitelist>>()
    val shopWhitelist: LiveData<ShippingEditorState<ShopLocWhitelist>>
        get() = _shopWhitelist

    fun getWhitelistData(shopId: Int) {
        _shopWhitelist.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetWhitelistData) {
            val getWhitelistShop = repo.getShopLocationWhitelist(shopId)
            _shopWhitelist.value = ShippingEditorState.Success(getWhitelistShop.shopLocWhitelist)
        }
    }

    private val onErrorGetWhitelistData = CoroutineExceptionHandler { _, e ->
        _shopWhitelist.value = ShippingEditorState.Fail(e, "")
    }

}