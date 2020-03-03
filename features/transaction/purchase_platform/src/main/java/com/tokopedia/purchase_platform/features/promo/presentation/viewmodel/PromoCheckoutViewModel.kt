package com.tokopedia.purchase_platform.features.promo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PromoCheckoutViewModel @Inject constructor(val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    // Fragment UI Model
    private val _fragmentUiModel = MutableLiveData<FragmentUiModel>()
    val fragmentUiModel: LiveData<FragmentUiModel>
        get() = _fragmentUiModel

    // Promo Empty State UI Model
    private val _promoEmptyStateUiModel = MutableLiveData<PromoEmptyStateUiModel>()
    val promoEmptyStateUiModel: LiveData<PromoEmptyStateUiModel>
        get() = _promoEmptyStateUiModel

    // Promo Recommendation UI Model
    private val _promoRecommendationUiModel = MutableLiveData<PromoRecommendationUiModel>()
    val promoRecommendationUiModel: LiveData<PromoRecommendationUiModel>
        get() = _promoRecommendationUiModel

    // Promo Input UI Model
    private val _promoInputUiModel = MutableLiveData<PromoInputUiModel>()
    val promoInputUiModel: LiveData<PromoInputUiModel>
        get() = _promoInputUiModel

    // Promo Section UI Model (Eligible / Ineligible based on API response)
    private val _promoListUiModel = MutableLiveData<MutableList<Visitable<*>>>()
    val promoListUiModel: LiveData<MutableList<Visitable<*>>>
        get() = _promoListUiModel

    // Temporary data
    private val _tmpPromoListUiModel = MutableLiveData<Action<Visitable<*>>>()
    val tmpPromoListUiModel: LiveData<Action<Visitable<*>>>
        get() = _tmpPromoListUiModel

    fun loadData() {
        // Todo : Hit API. Currently use mock local data

        // Init fragment ui model
        val fragmentUiModel = FragmentUiModel(
                uiData = FragmentUiModel.UiData().apply {
                    promoInputViewHeight = 0
                },
                uiState = FragmentUiModel.UiState().apply {
                    hasPresellectedPromo = false
                    hasAnyPromoSelected = false
                    hasFailedToLoad = false
                }
        )
        _fragmentUiModel.value = fragmentUiModel

        // Init Data
        _promoRecommendationUiModel.value = mockPromoRecommendation()
        _promoInputUiModel.value = mockPromoInput()

        val promoListUiModel = ArrayList<Visitable<*>>().apply {
            add(mockEligibleHeader())
            addAll(mockEligiblePromoGlobalSection())
            addAll(mockEligiblePromoGoldMerchantSection())
            addAll(mockEligiblePromoOfficialStoreSection())

            add(mockIneligibleHeader())
            addAll(mockIneligiblePromoGlobalSection())
            addAll(mockIneligiblePromoGoldMerchantSection())
            addAll(mockIneligiblePromoOfficialStoreSection())
        }

        if (!promoListUiModel.isNullOrEmpty()) {
            _promoListUiModel.value = promoListUiModel
        }

//        _promoEmptyStateUiModel.value = mockEmptyState()
    }

    fun updateHeightPromoInputView(height: Int) {
        fragmentUiModel.value?.let {
            it.uiData.promoInputViewHeight = height
            _fragmentUiModel.value = it
        }
    }

    fun isPromoScopeHasAnySelectedItem(parentIdentifierId: Int): Boolean {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected && it.uiData.parentIdentifierId == parentIdentifierId) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }

        return hasAnyPromoSellected
    }

    fun updateResetButtonState() {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }

        setFragmentStateHasPromoSelected(hasAnyPromoSellected)
    }

    fun setFragmentStateHasPromoSelected(hasAnyPromoSelected: Boolean) {
        fragmentUiModel.value?.let {
            it.uiState.hasAnyPromoSelected = hasAnyPromoSelected
            _fragmentUiModel.value = it
        }
    }
}