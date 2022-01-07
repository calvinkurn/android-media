package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.modiface.mfemakeupkit.effects.MFEMakeupLayer
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_ar.di.PRODUCT_ID_PROVIDED
import com.tokopedia.product_ar.di.SHOP_ID_PROVIDED
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.model.ProductArUiModel
import com.tokopedia.product_ar.model.state.AnimatedTextIconClickMode
import com.tokopedia.product_ar.model.state.AnimatedTextIconState
import com.tokopedia.product_ar.model.state.ModifaceViewMode
import com.tokopedia.product_ar.model.state.ModifaceViewState
import com.tokopedia.product_ar.usecase.GetProductArUseCase
import com.tokopedia.product_ar.util.ProductArMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named


class ProductArViewModel @Inject constructor(dispatchers: CoroutineDispatchers,
                                             @Named(PRODUCT_ID_PROVIDED) private val initialProductId: String,
                                             @Named(SHOP_ID_PROVIDED) private val shopId: String,
                                             private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
                                             private val getProductArUseCase: GetProductArUseCase)
    : BaseViewModel(dispatchers.io) {

    init {
        getArData()
    }

    private var productArUiModel: ProductArUiModel = ProductArUiModel()

    fun getProductArUiModel(): ProductArUiModel = productArUiModel

    private val _animatedTextIconState = MutableStateFlow(AnimatedTextIconState(
            view2ClickMode = AnimatedTextIconClickMode.CHOOSE_FROM_GALLERY)
    )
    val animatedTextIconState: StateFlow<AnimatedTextIconState> = _animatedTextIconState

    private val _modifaceViewState = MutableStateFlow(ModifaceViewState(
            mode = ModifaceViewMode.LIVE)
    )
    val modifaceViewState: StateFlow<ModifaceViewState> = _modifaceViewState

    private val _modifaceLoadingState = MutableStateFlow(true)
    val modifaceLoadingState: StateFlow<Boolean> = _modifaceLoadingState

    private val _bottomLoadingState=  MutableStateFlow(true)
    val bottomLoadingState: StateFlow<Boolean> = _bottomLoadingState

    private val _selectedProductArData = MutableLiveData<Result<ProductAr>>()
    val selectedProductArData: LiveData<Result<ProductAr>>
        get() = _selectedProductArData

    private val _productArList = MutableLiveData<Result<List<ModifaceUiModel>>>()
    val productArList: LiveData<Result<List<ModifaceUiModel>>>
        get() = _productArList

    private val _mfeMakeUpLook = MutableLiveData<Result<MFEMakeupLook>>()
    val mfeMakeUpLook: LiveData<Result<MFEMakeupLook>>
        get() = _mfeMakeUpLook

    var imageDrawable: Bitmap? = null

    fun setLoadingState(isLoading: Boolean) {
        if (_modifaceLoadingState.value == isLoading) return

        _modifaceLoadingState.update {
            isLoading
        }
    }

    private fun getArData() {
        viewModelScope.launchCatchError(block = {
            val result = getProductArUseCase.executeOnBackground(
                    GetProductArUseCase.createParams(initialProductId, shopId, chosenAddressRequestHelper)
            )
            productArUiModel = result

            val listUiModel = ProductArMapper.mapToModifaceUiModel(initialProductId, result)

            val selectedProductMfeData = ProductArMapper.getMfMakeUpLookByProductId(
                    listUiModel,
                    initialProductId)

            _mfeMakeUpLook.postValue(Success(selectedProductMfeData))
            _productArList.postValue(Success(listUiModel))
            _selectedProductArData.postValue(Success(result.options[initialProductId]
                    ?: ProductAr()))
            _bottomLoadingState.update {
                false
            }

        }, onError = {
            _selectedProductArData.postValue(Fail(it))
        })
    }

    fun onVariantClicked(productId: String,
                         uiModel: ProductArUiModel,
                         currentList: List<ModifaceUiModel>,
                         mfeMakeUpProduct: MFEMakeupProduct) {
        val updatedData = ProductArMapper.updateListAfterChangeSelection(productId, currentList)
        MFEMakeupLook().apply {
            lipLayers.add(MFEMakeupLayer(mfeMakeUpProduct))
        }.let {
            _mfeMakeUpLook.postValue(Success(it))
        }
        _productArList.postValue(Success(updatedData))
        _selectedProductArData.postValue(Success(uiModel.options[productId] ?: ProductAr()))
    }

    fun changeMode(modifaceViewMode: ModifaceViewMode,
                   pathImageDrawable: String = "") {
        if (modifaceViewMode == ModifaceViewMode.LIVE) {
            setLoadingState(true)
            _modifaceViewState.update {
                it.copy(mode = ModifaceViewMode.LIVE,
                        imageDrawablePath = "")
            }

            _animatedTextIconState.update {
                it.copy(view1ClickMode = null,
                        view2ClickMode = AnimatedTextIconClickMode.CHOOSE_FROM_GALLERY)
            }
        } else {
            _modifaceViewState.update {
                it.copy(mode = ModifaceViewMode.IMAGE,
                        imageDrawablePath = pathImageDrawable)
            }

            _animatedTextIconState.update {
                it.copy(view1ClickMode = AnimatedTextIconClickMode.USE_CAMERA,
                        view2ClickMode = AnimatedTextIconClickMode.CHANGE_PHOTO)
            }
        }
    }
}