package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.di.GqlGetShopCloseDetailInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant.PRODUCT_SELECTION_MAX
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignValidatedProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ReserveProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignValidatedProductListUseCase: GetSellerCampaignValidatedProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase,
    @GqlGetShopCloseDetailInfoQualifier private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase
) : BaseViewModel(dispatchers.main) {

    private var _reserveProductList = MutableLiveData<List<ReserveProductModel>>()
    val reserveProductList: LiveData<List<ReserveProductModel>>
        get() = _reserveProductList

    private var _errors = MutableLiveData<Throwable>()
    val errors: LiveData<Throwable>
        get() = _errors

    private var _selectedItems = MutableLiveData<List<SelectedProductModel>>(listOf())
    val selectedItems: LiveData<List<SelectedProductModel>>
        get() = _selectedItems

    private var _isAddProductSuccess = MutableLiveData<Boolean>()
    val isAddProductSuccess: LiveData<Boolean>
        get() = _isAddProductSuccess

    private var _shopStatus = MutableLiveData<ReserveProductMapper.ShopStatusEnum>()
    val shopStatus: LiveData<ReserveProductMapper.ShopStatusEnum>
        get() = _shopStatus

    val isSelectionValid = Transformations.map(selectedItems) {
        validateSelection(it)
    }

    private var searchKeyword: String = ""

    private fun validateSelection(selectedItem: List<SelectedProductModel>): Boolean {
        return selectedItem.size.isMoreThanZero() && selectedItem.size <= PRODUCT_SELECTION_MAX
    }

    fun isSearching(): Boolean {
        return searchKeyword.isNotEmpty()
    }

    fun getReserveProductList(campaignId: String, page: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignValidatedProductListUseCase.execute(
                    campaignId, searchKeyword, page)
                _reserveProductList.postValue(ReserveProductMapper.mapFromProductList(result))
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun getShopInfo() {
        launchCatchError(
            dispatchers.io,
            block = {
                val shopId = userSessionInterface.shopId.toIntOrZero()
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(shopId))
                val result = gqlGetShopInfoUseCase.executeOnBackground()
                _shopStatus.postValue(ReserveProductMapper.mapToShopStatusEnum(
                    result.statusInfo.shopStatus))
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun addProduct(campaignId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId,
                    ReserveProductMapper.mapToProductDataList(selectedItems.value)
                )
                _isAddProductSuccess.postValue(result)
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun setSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    fun setSelectedItems(selectedItems: List<SelectedProductModel>) {
        _selectedItems.value = selectedItems
    }
}