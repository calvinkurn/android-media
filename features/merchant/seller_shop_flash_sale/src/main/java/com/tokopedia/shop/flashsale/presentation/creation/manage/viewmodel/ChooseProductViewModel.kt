package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignValidatedProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ReserveProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignValidatedProductListUseCase: GetSellerCampaignValidatedProductListUseCase
) : BaseViewModel(dispatchers.main) {

    private var _reserveProductList = MutableLiveData<List<ReserveProductModel>>()
    val reserveProductList: LiveData<List<ReserveProductModel>>
        get() = _reserveProductList

    private var _errors = MutableLiveData<Throwable>()
    val errors: LiveData<Throwable>
        get() = _errors

    private var searchKeyword: String = ""

    fun isSearching(): Boolean {
        return searchKeyword.isNotEmpty()
    }

    fun getReserveProductList(page: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignValidatedProductListUseCase.execute(searchKeyword, page)
                _reserveProductList.postValue(ReserveProductMapper.mapFromProductList(result))
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun setSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }
}