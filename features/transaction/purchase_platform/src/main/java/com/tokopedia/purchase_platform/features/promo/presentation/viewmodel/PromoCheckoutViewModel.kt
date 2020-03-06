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
                var headerIdentifierId = 0
                couponSectionItem.subSections.forEach { couponSubSection ->
                    val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(couponSubSection, headerIdentifierId)
                    couponList.add(promoHeader)
                    headerIdentifierId++

                    // Initialize promo list item
                    couponSubSection.coupons.forEach { couponItem ->
                        val promoItem = uiModelMapper.mapPromoListItemUiModel(
                                couponItem, promoHeader.uiData.identifierId, couponSubSection.isEnabled, selectedPromoList
                        )
                        couponList.add(promoItem)
                    }
                }
            }
            _promoListUiModel.value = couponList

            // Init fragment ui model
            val fragmentUiModel = FragmentUiModel(
                    uiData = FragmentUiModel.UiData().apply {

                    },
                    uiState = FragmentUiModel.UiState().apply {
                        hasPresellectedPromo = promoListUiModel.value?.isNotEmpty() == true
                        hasAnyPromoSelected = promoListUiModel.value?.isNotEmpty() == true
                        hasFailedToLoad = false
                    }
            )
            _fragmentUiModel.value = fragmentUiModel
            calculateAndRenderTotalBenefit()
        }) {
            // Init fragment ui model
            val fragmentUiModel = FragmentUiModel(
                    uiData = FragmentUiModel.UiData().apply {

                    },
                    uiState = FragmentUiModel.UiState().apply {
                        hasPresellectedPromo = false
                        hasAnyPromoSelected = false
                        hasFailedToLoad = true
                    }
            )
            _fragmentUiModel.value = fragmentUiModel
        }
    }

    private fun calculateClash(selectedItem: PromoListItemUiModel) {
        if (selectedItem.uiState.isSellected) {
            // Calculate clash on selection event
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    // Calculate clash on expanded promo item
                    if (it.uiData.clashingInfo.isNotEmpty()) {
                        setClashOnSelectionEvent(it, selectedItem)
                        _tmpUiModel.value = Update(it)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Calculate clash on collapsed promo item
                    it.uiData.tmpPromoItemList.forEach {
                        setClashOnSelectionEvent(it, selectedItem)
                    }
                    _tmpUiModel.value = Update(it)
                }
            }
        } else {
            // Calculate clash on un selection event
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    // Calculate clash on expanded promo item
                    if (it.uiData.clashingInfo.isNotEmpty()) {
                        setClashOnUnSelectionEvent(it, selectedItem)
                        _tmpUiModel.value = Update(it)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Calculate clash on collapsed promo item
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiData.clashingInfo.isNotEmpty()) {
                            setClashOnUnSelectionEvent(it, selectedItem)
                        }
                    }
                    _tmpUiModel.value = Update(it)
                }
            }
        }
    }

    private fun setClashOnUnSelectionEvent(it: PromoListItemUiModel, selectedItem: PromoListItemUiModel) {
        if (it.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
            if (it.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                it.uiData.currentClashingPromo.remove(selectedItem.uiData.promoCode)
                if (it.uiData.currentClashingPromo.isNotEmpty()) {
                    val errorMessageBuilder = StringBuilder()
                    it.uiData.currentClashingPromo.forEach { string ->
                        if (it.uiData.clashingInfo.containsKey(string)) {
                            errorMessageBuilder.append(it.uiData.clashingInfo[string])
                        }
                    }
                    it.uiData.errorMessage = errorMessageBuilder.toString()
                } else {
                    it.uiData.errorMessage = ""
                }
            }
        }
    }

    private fun setClashOnSelectionEvent(it: PromoListItemUiModel, selectedItem: PromoListItemUiModel) {
        if (it.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
            if (!it.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                it.uiData.currentClashingPromo.add(selectedItem.uiData.promoCode)
                val errorMessageBuilder = StringBuilder(it.uiData.errorMessage)
                errorMessageBuilder.append(it.uiData.clashingInfo[selectedItem.uiData.promoCode])
                it.uiData.errorMessage = errorMessageBuilder.toString()
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

    fun resetPromo() {
        val promoList = ArrayList<Visitable<*>>()
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel) {
                // Reset promo on expanded item
                it.uiState.isSellected = false
                it.uiData.currentClashingPromo.clear()
                promoList.add(it)
            } else if (it is PromoListHeaderUiModel) {
                // Reset promo on collapsed item
                it.uiState.hasSelectedPromoItem = false
                it.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    promoListItemUiModel.uiState.isSellected = false
                    promoListItemUiModel.uiData.currentClashingPromo.clear()
                }
                promoList.add(it)
            }
        }

        // Update view
        promoList.forEach {
            _tmpUiModel.value = Update(it)
        }

        setFragmentStateHasPromoSelected(false)
    }

    fun setFragmentStateHasPromoSelected(hasAnyPromoSelected: Boolean) {
        // Set fragment state
        val fragmentUiModel = fragmentUiModel.value
        fragmentUiModel?.let {
            it.uiState.hasAnyPromoSelected = hasAnyPromoSelected
            it.uiData.usedPromoCount = 0
            it.uiData.totalBenefit = 0

            _fragmentUiModel.value = it
        }
    }

    fun updatePromoListAfterClickPromoHeader(element: PromoListHeaderUiModel) {
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

    fun updatePromoListAfterClickPromoItem(element: PromoListItemUiModel) {
        // Set to selected / un selected
        promoListUiModel.value?.indexOf(element)?.let {
            // Get the promo item data and set inverted collapsed value
            val promoItem = promoListUiModel.value?.get(it) as PromoListItemUiModel
            promoItem.uiState.isSellected = !promoItem.uiState.isSellected

            // Update view
            _tmpUiModel.value = Update(promoItem)

            // Perform clash calculation
            calculateClash(promoItem)

            // Update header sub total
            var header: PromoListHeaderUiModel? = null
            promoListUiModel.value?.forEach {
                if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.identifierId == promoItem.uiData.parentIdentifierId) {
                    header = it
                    return@forEach
                }
            }

            header?.let {
                val hasSelectPromo = isPromoScopeHasAnySelectedItem(it.uiData.identifierId)
                it.uiState.hasSelectedPromoItem = hasSelectPromo

                val headerIndex = promoListUiModel.value?.indexOf(it) ?: 0
                _tmpUiModel.value = Update(it)

                if (headerIndex != 0) {
                    // Un check other item on current header if previously selected
                    val promoListSize = promoListUiModel.value?.size ?: 0
                    for (index in headerIndex + 1 until promoListSize) {
                        if (promoListUiModel.value?.get(index) !is PromoListItemUiModel) {
                            break
                        } else {
                            val tmpPromoItem = promoListUiModel.value?.get(index) as PromoListItemUiModel
                            if (tmpPromoItem.uiData.promoCode != element.uiData.promoCode && tmpPromoItem.uiState.isSellected) {
                                tmpPromoItem.uiState.isSellected = false
                                _tmpUiModel.value = Update(tmpPromoItem)
                                calculateClash(tmpPromoItem)
                                break
                            }
                        }
                    }

                    updateResetButtonState()
                }
            }

            calculateAndRenderTotalBenefit()
        }
    }

    private fun calculateAndRenderTotalBenefit() {
        var totalBenefit = 0
        var usedPromoCount = 0
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty() && it.uiState.isSellected) {
                totalBenefit += it.uiData.benefitAmount
                usedPromoCount++
            }
        }

        val fragmentUiModel = fragmentUiModel.value
        fragmentUiModel?.let {
            it.uiData.totalBenefit = totalBenefit
            it.uiData.usedPromoCount = usedPromoCount

            _fragmentUiModel.value = it
        }
    }

    fun applyPromoSuggestion() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            it.uiState.isButtonSelectEnabled = false
            it.uiState.hasAppliedRecommendation = true

            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    if (promoRecommendation.uiData.promoCodes.contains(it.uiData.promoCode)) {
                        it.uiState.isSellected = true
                        _tmpUiModel.value = Update(it)
                    }
                }
            }

            _promoRecommendationUiModel.value = it
            calculateAndRenderTotalBenefit()
        }
    }
}