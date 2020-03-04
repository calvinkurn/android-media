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

    // Temporary single data
    private val _tmpUiModel = MutableLiveData<Action<Visitable<*>>>()
    val tmpUiModel: LiveData<Action<Visitable<*>>>
        get() = _tmpUiModel

    // Temporary multiple data with header as index
    private val _tmpListUiModel = MutableLiveData<Action<Map<Visitable<*>, List<Visitable<*>>>>>()
    val tmpListUiModel: LiveData<Action<Map<Visitable<*>, List<Visitable<*>>>>>
        get() = _tmpListUiModel

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

    fun updatePromoSectionList(element: PromoListHeaderUiModel) {
        if (!element.uiState.isCollapsed) {
            promoListUiModel.value?.indexOf(element)?.let {
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isCollapsed = !headerData.uiState.isCollapsed

                val modifiedData = ArrayList<PromoListItemUiModel>()
                val startIndex = it + 1
                val endIndex = promoListUiModel.value?.size ?: 0
                for (index in startIndex until endIndex) {
                    if (promoListUiModel.value?.get(index) !is PromoListItemUiModel) break
                    val oldPromoItem = promoListUiModel.value?.get(index) as PromoListItemUiModel
                    modifiedData.add(oldPromoItem)
                }
                _tmpUiModel.value = Update(headerData)
                headerData.uiData.tmpPromoItemList = modifiedData
                modifiedData.forEach {
                    _tmpUiModel.value = Delete(it)
                }
                promoListUiModel.value?.removeAll(modifiedData)
            }
        } else {
            promoListUiModel.value?.indexOf(element)?.let {
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isCollapsed = !headerData.uiState.isCollapsed

                val mapList = HashMap<Visitable<*>, List<Visitable<*>>>()
                mapList[headerData] = headerData.uiData.tmpPromoItemList
                _tmpListUiModel.value = Insert(mapList)

                var startIndex = it + 1
                headerData.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    promoListUiModel.value?.add(startIndex++, promoListItemUiModel)
                }

                _tmpUiModel.value = Update(headerData)

                headerData.uiData.tmpPromoItemList = emptyList()
                _tmpUiModel.value = Update(headerData)
            }

        }
    }
}