package com.tokopedia.purchase_platform.features.promo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.purchase_platform.features.promo.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromoCheckoutViewModel @Inject constructor(val dispatcher: CoroutineDispatcher,
                                                 val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
                                                 val uiModelMapper: PromoCheckoutUiModelMapper)
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

    // Temporary single data. This live data is used for modify or delete single item
    private val _tmpUiModel = MutableLiveData<Action<Visitable<*>>>()
    val tmpUiModel: LiveData<Action<Visitable<*>>>
        get() = _tmpUiModel

    // Temporary multiple data. This live data is used for insert list of items to adapter.
    // The data is a map, with visitable as key and list of visitable as value.
    // The key is supposed to be the header of the list
    private val _tmpListUiModel = MutableLiveData<Action<Map<Visitable<*>, List<Visitable<*>>>>>()
    val tmpListUiModel: LiveData<Action<Map<Visitable<*>, List<Visitable<*>>>>>
        get() = _tmpListUiModel

    fun loadData() {
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

//        mockData()

        launch { getCouponRecommendation() }
    }

    suspend fun getCouponRecommendation() {
        launchCatchError(block = {
            // Get response
            val response = withContext(dispatcher) {
                getCouponListRecommendationUseCase.params = HashMap()
                getCouponListRecommendationUseCase.executeOnBackground()
            }

            // Initialize promo recommendation
            val promoRecommendation = response.couponListRecommendation.data.promoRecommendation
            if (promoRecommendation.codes.isNotEmpty()) {
                _promoRecommendationUiModel.value = uiModelMapper.mapPromoRecommendationUiModel(response.couponListRecommendation)
            }

            // Initialize promo input model
            val promoInputUiModel = uiModelMapper.mapPromoInputUiModel()
            _promoInputUiModel.value = promoInputUiModel

            // Get all sellected promo
            val selectedPromoList = ArrayList<String>()
            response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
                if (couponSectionItem.isEnabled) {
                    couponSectionItem.subSections.forEach {
                        it.coupons.forEach {
                            if (it.isSelected) {
                                selectedPromoList.add(it.code)
                            }
                        }
                    }
                }
            }

            // Initialize coupon section
            val couponList = ArrayList<Visitable<*>>()
            response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
                // Initialize eligibility header
                val eligibilityHeader = uiModelMapper.mapPromoEligibilityHeaderUiModel(couponSectionItem)
                couponList.add(eligibilityHeader)

                // Initialize promo list header
                val headerIdentifierId = 0
                couponSectionItem.subSections.forEach { couponSubSection ->
                    val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(couponSubSection, headerIdentifierId)
                    couponList.add(promoHeader)

                    // Initialize promo list item
                    couponSubSection.coupons.forEach { couponItem ->
                        val promoItem = uiModelMapper.mapPromoListItemUiModel(couponItem, promoHeader.uiData.identifierId, selectedPromoList)
                        couponList.add(promoItem)
                    }
                }
            }
            _promoListUiModel.value = couponList

        }) {
            // Todo : Show error state
        }
    }

    fun calculateClash(selectedItem: PromoListItemUiModel) {
        if (selectedItem.uiState.isSellected) {
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    if (it.uiData.clashingInfo.isNotEmpty()) {
                        if (it.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
                            if (!it.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                                it.uiData.currentClashingPromo.add(selectedItem.uiData.promoCode)
                                val errorMessageBuilder = StringBuilder(it.uiData.errorMessage)
                                errorMessageBuilder.append(it.uiData.clashingInfo[selectedItem.uiData.promoCode])
                                it.uiData.errorMessage = errorMessageBuilder.toString()
                                it.uiState.isEnabled = false
                            }
                        }
                        _tmpUiModel.value = Update(it)
                    }
                }
            }
        } else {
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    if (it.uiData.clashingInfo.isNotEmpty()) {
                        if (it.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
                            if (it.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                                it.uiData.currentClashingPromo.remove(selectedItem.uiData.promoCode)
                                if (it.uiData.currentClashingPromo.isNotEmpty()) {
                                    val errorMessageBuilder = StringBuilder()
                                    it.uiData.currentClashingPromo.forEach { string ->
                                        if (it.uiData.clashingInfo.containsKey(string)) {
                                            if (errorMessageBuilder.isNotBlank()) {
                                                errorMessageBuilder.append("\n")
                                            }
                                            errorMessageBuilder.append(it.uiData.clashingInfo[string])
                                        }
                                    }
                                    it.uiData.errorMessage = errorMessageBuilder.toString()
                                    it.uiState.isEnabled = false
                                } else {
                                    it.uiData.errorMessage = ""
                                    it.uiState.isEnabled = true
                                }
                            }
                        }
                        _tmpUiModel.value = Update(it)
                    }
                }
            }
        }
    }

    private fun mockData() {
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
                // Get the header data and set inverse collapsed value
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isCollapsed = !headerData.uiState.isCollapsed

                // Get promo item which will be collapsed and store into single list
                val modifiedData = ArrayList<PromoListItemUiModel>()
                val startIndex = it + 1
                val endIndex = promoListUiModel.value?.size ?: 0
                for (index in startIndex until endIndex) {
                    if (promoListUiModel.value?.get(index) !is PromoListItemUiModel) break
                    val oldPromoItem = promoListUiModel.value?.get(index) as PromoListItemUiModel
                    modifiedData.add(oldPromoItem)
                }

                // Update header
                _tmpUiModel.value = Update(headerData)

                // Store collapsed promo item to promo header as temporary value
                headerData.uiData.tmpPromoItemList = modifiedData

                // Remove collapsed promo item from view
                modifiedData.forEach {
                    _tmpUiModel.value = Delete(it)
                }

                // Remove collapsed item
                promoListUiModel.value?.removeAll(modifiedData)
            }
        } else {
            promoListUiModel.value?.indexOf(element)?.let {
                // Get the header data and set inverse collapsed value
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isCollapsed = !headerData.uiState.isCollapsed

                // Get expanded promo item from temporary data on header, then put on the map
                val mapList = HashMap<Visitable<*>, List<Visitable<*>>>()
                // Set map key = header, map value = expanded promo list
                mapList[headerData] = headerData.uiData.tmpPromoItemList
                // Update expanded view
                _tmpListUiModel.value = Insert(mapList)

                // Store expanded promo item into live data
                var startIndex = it + 1
                headerData.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    promoListUiModel.value?.add(startIndex++, promoListItemUiModel)
                }

                // Update header
                _tmpUiModel.value = Update(headerData)

                headerData.uiData.tmpPromoItemList = emptyList()
                _tmpUiModel.value = Update(headerData)
            }

        }
    }
}