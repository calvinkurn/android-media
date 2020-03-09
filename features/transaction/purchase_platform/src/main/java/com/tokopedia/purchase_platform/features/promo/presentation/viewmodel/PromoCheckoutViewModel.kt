package com.tokopedia.purchase_platform.features.promo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.promo.data.request.CouponListRecommendationRequest
import com.tokopedia.purchase_platform.features.promo.data.request.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.ClearPromoResponse
import com.tokopedia.purchase_platform.features.promo.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_COUPON_LIST_EMPTY
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_USER_BLACKLISTED
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromoCheckoutViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                 private val graphqlRepository: GraphqlRepository,
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

    // Live data to store clear promo result
    private val _clearPromoResponse = MutableLiveData<Result<ClearPromoResponse>>()
    val clearPromoResponse: LiveData<Result<ClearPromoResponse>>
        get() = _clearPromoResponse

    // Live data to store apply promo result
    private val _applyPromoResponse = MutableLiveData<Result<Any>>()
    val applyPromoResponse: LiveData<Result<Any>>
        get() = _applyPromoResponse

    fun loadData(mutation: String, promoRequest: PromoRequest) {
        launch { getCouponRecommendation(mutation, promoRequest) }
    }

    private suspend fun getCouponRecommendation(mutation: String, promoRequest: PromoRequest) {
        launchCatchError(block = {
            // Set param
            val promo = HashMap<String, Any>()
            promo["params"] = CouponListRecommendationRequest(promoRequest = promoRequest)

            // Get response
            val response = withContext(Dispatchers.IO) {
//                Gson().fromJson(MOCK_RESPONSE, CouponListRecommendationResponse::class.java)
                val request = GraphqlRequest(mutation, CouponListRecommendationResponse::class.java, promo)
                graphqlRepository.getReseponse(listOf(request))
                        .getSuccessData<CouponListRecommendationResponse>()
            }

            if (response.couponListRecommendation.status == "OK") {
                if (response.couponListRecommendation.data.couponSections.isNotEmpty()) {
                    initFragmentUiModel(false)
                    initPromoRecommendation(response)
                    initPromoInput()
                    initPromoList(response)

                    var tmpHasPreSelectedPromo = false
                    promoListUiModel.value?.forEach {
                        if (it is PromoListItemUiModel && it.uiState.isSelected) {
                            tmpHasPreSelectedPromo = true
                            return@forEach
                        } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                            it.uiData.tmpPromoItemList.forEach {
                                if (it.uiState.isSelected) {
                                    tmpHasPreSelectedPromo = true
                                    return@forEach
                                }
                            }
                        }
                    }

                    fragmentUiModel.value?.let {
                        it.uiState.hasPreselectedPromo = tmpHasPreSelectedPromo
                        it.uiState.hasAnyPromoSelected = tmpHasPreSelectedPromo
                        val rewardPointInfo = response.couponListRecommendation.data.rewardPointsInfo
                        if (rewardPointInfo.gainRewardPointsTnc.tncDetails.isNotEmpty()) {
                            it.uiData.tokopointsTncLabel = rewardPointInfo.message
                            it.uiData.tokopointsTncTitle = rewardPointInfo.gainRewardPointsTnc.title
                            it.uiData.tokopointsTncDetails = uiModelMapper.mapTokoPointsTncDetails(rewardPointInfo.gainRewardPointsTnc.tncDetails)
                        }
                        _fragmentUiModel.value = it
                    }

                    calculateAndRenderTotalBenefit()
                } else {
                    if (response.couponListRecommendation.data.emptyState.title.isEmpty() &&
                            response.couponListRecommendation.data.emptyState.description.isEmpty() &&
                            response.couponListRecommendation.data.emptyState.imageUrl.isEmpty()) {
                        initFragmentUiModel(true)
                        fragmentUiModel.value?.uiData?.exception = RuntimeException()
                    } else {
                        initFragmentUiModel(false)
                        val emptyState = uiModelMapper.mapEmptyState(response.couponListRecommendation)
                        emptyState.uiData.emptyStateStatus = response.couponListRecommendation.data.resultStatus.code
                        if (response.couponListRecommendation.data.resultStatus.code == STATUS_COUPON_LIST_EMPTY) {
                            emptyState.uiState.isShowButton = false
                            initPromoInput()
                        } else if (response.couponListRecommendation.data.resultStatus.code == STATUS_PHONE_NOT_VERIFIED) {
                            emptyState.uiData.buttonText = "Verifikasi Nomor HP"
                            emptyState.uiState.isShowButton = true
                        } else if (response.couponListRecommendation.data.resultStatus.code == STATUS_USER_BLACKLISTED) {
                            emptyState.uiState.isShowButton = false
                        }

                        _promoEmptyStateUiModel.value = emptyState
                    }
                }
            } else {
                initFragmentUiModel(true)
            }

        }) {
            initFragmentUiModel(true)
            fragmentUiModel.value?.uiData?.exception = it
        }
    }

    fun applyPromo(mutation: String, promoCode: String) {
        launch { doApplyPromo(mutation, promoCode) }
    }

    fun applyPromo(mutation: String) {
        launch { doApplyPromo(mutation, "") }
    }

    private suspend fun doApplyPromo(mutation: String, promoCode: String) {
        launchCatchError(block = {
            throw MessageErrorException("Gagal apply promo for the moment")
        }) {
            if (promoCode.isNotBlank()) {
                // Notify promo input to stop loading
                promoInputUiModel.value?.let { uiModel ->
                    uiModel.uiState.isLoading = false
                    uiModel.uiState.isError = true
                    uiModel.uiData.exception = it
                    _tmpUiModel.value = Update(uiModel)
                }
            } else {
                // Notify fragment apply promo to stop loading
                _applyPromoResponse.value = Fail(it)
            }
        }
    }

    fun clearPromo(mutation: String) {
        launch { doClearPromo(mutation) }
    }

    private suspend fun doClearPromo(mutation: String) {
        launchCatchError(block = {
            // Set param
            val promoCodes = ArrayList<String>()
            var tmpMutation = mutation
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiState.isParentEnabled && it.uiState.isAlreadyApplied) {
                    promoCodes.add(it.uiData.promoCode)
                } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiState.isParentEnabled && it.uiState.isAlreadyApplied) {
                            promoCodes.add(it.uiData.promoCode)
                        }
                    }
                }
            }
            val promoCodesJson = Gson().toJson(promoCodes)
            tmpMutation = tmpMutation.replace("#promoCode", promoCodesJson)

            // Get response
            val response = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(tmpMutation, ClearPromoResponse::class.java)
                graphqlRepository.getReseponse(listOf(request))
                        .getSuccessData<ClearPromoResponse>()
            }

            if (response.successData.success) {
                _clearPromoResponse.value = Success(response)
            } else {
                _clearPromoResponse.value = Fail(RuntimeException())
            }
        }) {
            _clearPromoResponse.value = Fail(it)
        }
    }

    private fun initFragmentUiModel(failedToLoad: Boolean) {
        val fragmentUiModel = uiModelMapper.mapFragmentUiModel(failedToLoad)
        _fragmentUiModel.value = fragmentUiModel
    }

    private fun initPromoList(response: CouponListRecommendationResponse) {
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
        var headerIdentifierId = 0
        response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
            // Initialize eligibility header
            val eligibilityHeader = uiModelMapper.mapPromoEligibilityHeaderUiModel(couponSectionItem)
            couponList.add(eligibilityHeader)

            // Initialize promo list header
            val tmpPromoHeaderList = ArrayList<Visitable<*>>()
            couponSectionItem.subSections.forEach { couponSubSection ->
                val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(couponSubSection, headerIdentifierId, couponSectionItem.isEnabled)
                if (eligibilityHeader.uiState.isEnabled) {
                    if (!eligibilityHeader.uiState.isExpanded) {
                        couponList.add(promoHeader)
                    }
                }
                headerIdentifierId++

                // Initialize promo list item
                val tmpCouponList = ArrayList<PromoListItemUiModel>()
                couponSubSection.coupons.forEach { couponItem ->
                    val promoItem = uiModelMapper.mapPromoListItemUiModel(
                            couponItem, promoHeader.uiData.identifierId, couponSubSection.isEnabled, selectedPromoList
                    )
                    if (promoHeader.uiState.isExpanded) {
                        tmpCouponList.add(promoItem)
                    } else {
                        couponList.add(promoItem)
                    }
                }
                if (tmpCouponList.isNotEmpty()) {
                    promoHeader.uiData.tmpPromoItemList = tmpCouponList
                }

                if (!eligibilityHeader.uiState.isEnabled) {
                    tmpPromoHeaderList.add(promoHeader)
                } else {
                    if (promoHeader.uiState.isExpanded) {
                        tmpPromoHeaderList.add(promoHeader)
                    }
                }
            }

            if (tmpPromoHeaderList.isNotEmpty()) {
                eligibilityHeader.uiData.tmpPromo = tmpPromoHeaderList
            }
        }
        _promoListUiModel.value = couponList
    }

    private fun initPromoInput() {
        // Initialize promo input model
        val promoInputUiModel = uiModelMapper.mapPromoInputUiModel()
        _promoInputUiModel.value = promoInputUiModel
    }

    private fun initPromoRecommendation(response: CouponListRecommendationResponse) {
        // Initialize promo recommendation
        val promoRecommendation = response.couponListRecommendation.data.promoRecommendation
        if (promoRecommendation.codes.isNotEmpty()) {
            _promoRecommendationUiModel.value = uiModelMapper.mapPromoRecommendationUiModel(response.couponListRecommendation)
        }
    }

    private fun calculateClash(selectedItem: PromoListItemUiModel) {
        if (selectedItem.uiState.isSelected) {
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
                if (it.uiData.errorMessage.isNotBlank()) {
                    errorMessageBuilder.append("\n")
                }
                errorMessageBuilder.append(it.uiData.clashingInfo[selectedItem.uiData.promoCode])
                it.uiData.errorMessage = errorMessageBuilder.toString()
            }
        }
    }

    fun isPromoScopeHasAnySelectedItem(parentIdentifierId: Int): Boolean {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSelected && it.uiData.parentIdentifierId == parentIdentifierId) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }

        return hasAnyPromoSellected
    }

    fun updateResetButtonState() {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSelected) {
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
                it.uiState.isSelected = false
                it.uiData.currentClashingPromo.clear()
                it.uiData.errorMessage = ""
                promoList.add(it)
            } else if (it is PromoListHeaderUiModel) {
                // Reset promo on collapsed item
                it.uiState.hasSelectedPromoItem = false
                it.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    promoListItemUiModel.uiData.errorMessage = ""
                    promoListItemUiModel.uiState.isSelected = false
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
        resetPromoSuggestion()
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
        if (!element.uiState.isExpanded) {
            promoListUiModel.value?.indexOf(element)?.let {
                // Get the header data and set inverse collapsed value
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isExpanded = !headerData.uiState.isExpanded

                // Get promo item which will be collapsed and store into single list
                val modifiedData = ArrayList<PromoListItemUiModel>()
                val startIndex = it + 1
                val endIndex = promoListUiModel.value?.size ?: 0
                for (index in startIndex until endIndex) {
                    if (promoListUiModel.value?.get(index) !is PromoListItemUiModel) break
                    val oldPromoItem = promoListUiModel.value?.get(index) as PromoListItemUiModel
                    modifiedData.add(oldPromoItem)
                }

                // Store collapsed promo item to promo header as temporary value
                headerData.uiData.tmpPromoItemList = modifiedData

                // Remove collapsed promo item from view
                modifiedData.forEach {
                    _tmpUiModel.value = Delete(it)
                }

                // Remove collapsed item
                promoListUiModel.value?.removeAll(modifiedData)

                // Update header
                _tmpUiModel.value = Update(headerData)
            }
        } else {
            promoListUiModel.value?.indexOf(element)?.let {
                // Get the header data and set inverse collapsed value
                val headerData = promoListUiModel.value?.get(it) as PromoListHeaderUiModel
                headerData.uiState.isExpanded = !headerData.uiState.isExpanded

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
            promoItem.uiState.isSelected = !promoItem.uiState.isSelected

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
                            if (tmpPromoItem.uiData.promoCode != element.uiData.promoCode && tmpPromoItem.uiState.isSelected) {
                                tmpPromoItem.uiState.isSelected = false
                                _tmpUiModel.value = Update(tmpPromoItem)
                                // calculate clash after uncheck
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
            if (it is PromoListItemUiModel && it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty() && it.uiState.isSelected) {
                totalBenefit += it.uiData.benefitAmount
                usedPromoCount++
            } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                it.uiData.tmpPromoItemList.forEach {
                    if (it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty() && it.uiState.isSelected) {
                        totalBenefit += it.uiData.benefitAmount
                        usedPromoCount++
                    }
                }
            }
        }

        val fragmentUiModel = fragmentUiModel.value
        fragmentUiModel?.let {
            if (usedPromoCount != 0) {
                it.uiData.totalBenefit = totalBenefit
                it.uiData.usedPromoCount = usedPromoCount
                it.uiState.hasAnyPromoSelected = true
            } else {
                it.uiState.hasAnyPromoSelected = false
            }

            _fragmentUiModel.value = it
        }
    }

    fun applyPromoSuggestion() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            it.uiState.isButtonSelectEnabled = false

            val expandedParentIdentifierList = mutableSetOf<Int>()
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    if (promoRecommendation.uiData.promoCodes.contains(it.uiData.promoCode)) {
                        it.uiState.isSelected = true
                        calculateClash(it)
                        expandedParentIdentifierList.add(it.uiData.parentIdentifierId)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    var hasSelectedPromoItem = false
                    it.uiData.tmpPromoItemList.forEach {
                        if (promoRecommendation.uiData.promoCodes.contains(it.uiData.promoCode)) {
                            it.uiState.isSelected = true
                            calculateClash(it)
                            hasSelectedPromoItem = true
                        }
                    }
                    it.uiState.hasSelectedPromoItem = hasSelectedPromoItem
                }
            }

            promoListUiModel.value?.forEach {
                if (it is PromoListHeaderUiModel && expandedParentIdentifierList.contains(it.uiData.identifierId)) {
                    it.uiState.hasSelectedPromoItem = true
                    _tmpUiModel.value = Update(it)
                }
            }

            _promoRecommendationUiModel.value = it
            calculateAndRenderTotalBenefit()
        }
    }

    fun resetPromoSuggestion() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            it.uiState.isButtonSelectEnabled = true
            _promoRecommendationUiModel.value = it
        }
    }

    fun updateIneligiblePromoList(element: PromoEligibilityHeaderUiModel) {
        val modifiedData = ArrayList<Visitable<*>>()

        val dataIndex = promoListUiModel.value?.indexOf(element) ?: 0

        if (dataIndex != 0) {
            val data = promoListUiModel.value?.get(dataIndex) as PromoEligibilityHeaderUiModel
            data.let {
                if (!it.uiState.isExpanded) {
                    val startIndex = dataIndex + 1
                    val promoListSize = promoListUiModel.value?.size ?: 0
                    for (index in startIndex until promoListSize) {
                        promoListUiModel.value?.get(index)?.let {
                            modifiedData.add(it)
                        }
                    }

                    it.uiState.isExpanded = !it.uiState.isExpanded
                    it.uiData.tmpPromo = modifiedData

                    _tmpUiModel.value = Update(it)
                    modifiedData.forEach {
                        _tmpUiModel.value = Delete(it)
                    }
                } else {
                    it.uiState.isExpanded = !it.uiState.isExpanded

                    _tmpUiModel.value = Update(it)
                    val mapListPromoHeader = HashMap<Visitable<*>, List<Visitable<*>>>()
                    mapListPromoHeader[it] = it.uiData.tmpPromo

                    _tmpListUiModel.value = Insert(mapListPromoHeader)
                    it.uiData.tmpPromo.forEach {
                        if (it is PromoListHeaderUiModel && it.uiState.isExpanded && it.uiData.tmpPromoItemList.isNotEmpty()) {
                            promoListUiModel.value?.add(it)

                            val mapListPromoItem = HashMap<Visitable<*>, List<Visitable<*>>>()
                            mapListPromoItem[it] = it.uiData.tmpPromoItemList
                            _tmpListUiModel.value = Insert(mapListPromoItem)

                            it.uiData.tmpPromoItemList.forEach {
                                promoListUiModel.value?.add(it)
                            }

                            it.uiState.isExpanded = false
                            it.uiData.tmpPromoItemList = emptyList()
                            _tmpUiModel.value = Insert(it)
                        }
                    }
                    it.uiData.tmpPromo = emptyList()
                    _tmpUiModel.value = Update(it)
                }
            }
        }
    }

    fun updatePromoInputState(promoCode: String) {
        promoInputUiModel.value?.let {
            it.uiState.isLoading = true
            it.uiState.isButtonSelectEnabled = true
            it.uiData.promoCode = promoCode

            _tmpUiModel.value = Update(it)
        }
    }
}