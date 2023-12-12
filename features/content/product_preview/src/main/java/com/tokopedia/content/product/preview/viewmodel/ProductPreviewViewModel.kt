package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewViewModel @AssistedInject constructor(
    @Assisted private val param: EntrySource,
    private val repo: ProductPreviewRepository,
    private val userSessionInterface: UserSessionInterface,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(param: EntrySource): ProductPreviewViewModel
    }

    //TODO: add uiState
    //TODO: add uiEvent

    private val _review = MutableStateFlow(emptyList<ReviewUiModel>())
    val review : Flow<List<ReviewUiModel>>
        get() = _review //TODO: add state

    private val _miniInfo = MutableStateFlow(BottomNavUiModel.Empty)
    val miniInfo: Flow<BottomNavUiModel>
        get() = _miniInfo

    fun addAction(action: ProductPreviewAction) {
        when(action) {
            ProductPreviewAction.FetchReview -> getReview()
            ProductPreviewAction.FetchMiniInfo -> getMiniInfo()
            is ProductPreviewAction.ProductAction -> addToCart(action.model)
            is ProductPreviewAction.SubmitReport -> submitReport(action.model)
            else -> {}
        }
    }

    private fun getReview() {
        viewModelScope.launchCatchError(block = {
            _review.value = repo.getReview(param.productId, 1) //TODO: add pagination
        }) {}
    }

    private fun getMiniInfo() {
        viewModelScope.launchCatchError(block = {
            _miniInfo.value = repo.getProductMiniInfo(param.productId)
        }) {}
    }

    private fun addToCart(model: BottomNavUiModel) {
        //TODO: required login
        viewModelScope.launchCatchError(block = {
            //TODO: emit Event
            repo.addToCart(param.productId, model.title, model.shop.id, model.price.finalPrice.toDoubleOrZero())
        }) {}
    }

    private fun submitReport(model: ReportUiModel) {
        viewModelScope.launchCatchError(block = {
            //TODO: emit Event
            repo.submitReport(model)
        }) {}
    }
}
