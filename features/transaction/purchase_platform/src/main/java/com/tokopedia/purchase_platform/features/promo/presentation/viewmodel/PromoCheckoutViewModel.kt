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
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.ClearPromoResponse
import com.tokopedia.purchase_platform.features.promo.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_COUPON_LIST_EMPTY
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_USER_BLACKLISTED
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.ValidateUseResponse
import com.tokopedia.purchase_platform.features.promo.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.purchase_platform.features.promo.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromoCheckoutViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                 private val graphqlRepository: GraphqlRepository,
                                                 private val uiModelMapper: PromoCheckoutUiModelMapper,
                                                 private val analytics: PromoCheckoutAnalytics)
    : BaseViewModel(dispatcher) {

    // Fragment UI Model. Store UI model and state on fragment level
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

    // Live data to notify UI state after hit clear promo API
    private val _clearPromoResponse = MutableLiveData<ClearPromoResponseAction>()
    val clearPromoResponse: LiveData<ClearPromoResponseAction>
        get() = _clearPromoResponse

    // Live data to notify UI state after hit apply promo / validate use API
    private val _applyPromoResponse = MutableLiveData<ApplyPromoResponseAction>()
    val applyPromoResponse: LiveData<ApplyPromoResponseAction>
        get() = _applyPromoResponse

    // Live data to notify UI state after hit get coupon recommendation API
    private val _getCouponRecommendationResponse = MutableLiveData<GetCouponRecommendationAction>()
    val getCouponRecommendationResponse: LiveData<GetCouponRecommendationAction>
        get() = _getCouponRecommendationResponse

    private fun getPageSource(): Int {
        return fragmentUiModel.value?.uiData?.pageSource ?: 0
    }

    fun initFragmentUiModel(pageSource: Int) {
        val fragmentUiModel = uiModelMapper.mapFragmentUiModel(pageSource)
        _fragmentUiModel.value = fragmentUiModel
    }

    fun loadData(mutation: String, promoRequest: PromoRequest, promoCode: String) {
        launch { getCouponRecommendation(mutation, promoRequest, promoCode) }
    }

    private suspend fun getCouponRecommendation(mutation: String, promoRequest: PromoRequest, promoCode: String) {
        launchCatchError(block = {
            // Set param manual input
            if (promoCode.isNotBlank()) {
                promoRequest.attemptedCodes.clear()
                promoRequest.attemptedCodes.add(promoCode)
            }

            promoRequest.orders.forEach { order ->
                order.codes.clear()
                promoListUiModel.value?.forEach {
                    if (it is PromoListItemUiModel && it.uiState.isSelected) {
                        if (it.uiData.uniqueId == order.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                            order.codes.add(it.uiData.promoCode)
                        } else if (it.uiData.shopId == 0 && !promoRequest.codes.contains(promoCode)) {
                            promoRequest.codes.add(promoCode)
                        }
                    } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                        it.uiData.tmpPromoItemList.forEach {
                            if (it.uiState.isSelected) {
                                if (it.uiData.uniqueId == order.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                                    order.codes.add(it.uiData.promoCode)
                                } else if (it.uiData.shopId == 0 && !promoRequest.codes.contains(promoCode)) {
                                    promoRequest.codes.add(promoCode)
                                }
                            }
                        }
                    }
                }
            }

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
                    fragmentUiModel.value?.let {
                        it.uiState.isLoading = false
                        _fragmentUiModel.value = it
                    }

                    if (getCouponRecommendationResponse.value == null) {
                        _getCouponRecommendationResponse.value = GetCouponRecommendationAction()
                    }

                    if (promoCode.isNotBlank()) {
                        getCouponRecommendationResponse.value?.let {
                            it.state = GetCouponRecommendationAction.ACTION_CLEAR_DATA
                            _getCouponRecommendationResponse.value = it
                        }
                    }

                    initPromoRecommendation(response)
                    initPromoInput()
                    initPromoList(response)

                    val attemptedPromoCodeError = response.couponListRecommendation.data.attemptedPromoCodeError
                    if (attemptedPromoCodeError.code.isNotBlank() && attemptedPromoCodeError.message.isNotBlank()) {
                        promoInputUiModel.value?.let {
                            it.uiData.exception = MessageErrorException(attemptedPromoCodeError.message)
                            it.uiData.promoCode = attemptedPromoCodeError.code
                            it.uiState.isError = true
                            it.uiState.isButtonSelectEnabled = true
                            it.uiState.isLoading = false

                            _promoInputUiModel.value = it
                        }
                    }

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

                    val preAppliedPromoCodes = ArrayList<String>()
                    promoListUiModel.value?.forEach {
                        if (it is PromoListItemUiModel && it.uiState.isSelected) {
                            preAppliedPromoCodes.add(it.uiData.promoCode)
                        } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                            it.uiData.tmpPromoItemList.forEach {
                                if (it.uiState.isSelected) {
                                    preAppliedPromoCodes.add(it.uiData.promoCode)
                                }
                            }
                        }
                    }

                    fragmentUiModel.value?.let {
                        it.uiData.preAppliedPromoCode = preAppliedPromoCodes
                        it.uiState.hasPreAppliedPromo = tmpHasPreSelectedPromo
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
                        throw MessageErrorException()
                    } else {
                        fragmentUiModel.value?.let {
                            it.uiState.isLoading = false
                            _fragmentUiModel.value = it
                        }

                        val emptyState = uiModelMapper.mapEmptyState(response.couponListRecommendation)
                        emptyState.uiData.emptyStateStatus = response.couponListRecommendation.data.resultStatus.code
                        when {
                            response.couponListRecommendation.data.resultStatus.code == STATUS_COUPON_LIST_EMPTY -> {
                                emptyState.uiState.isShowButton = false
                                initPromoInput()
                                analytics.eventViewAvailablePromoListNoPromo(getPageSource())
                            }
                            response.couponListRecommendation.data.resultStatus.code == STATUS_PHONE_NOT_VERIFIED -> {
                                emptyState.uiData.buttonText = "Verifikasi Nomor HP"
                                emptyState.uiState.isShowButton = true
                                analytics.eventViewPhoneVerificationMessage(getPageSource())
                            }
                            response.couponListRecommendation.data.resultStatus.code == STATUS_USER_BLACKLISTED -> {
                                emptyState.uiState.isShowButton = false
                                analytics.eventViewBlacklistErrorAfterApplyPromo(getPageSource())
                            }
                        }
                        _promoEmptyStateUiModel.value = emptyState
                    }
                }
            } else {
                throw MessageErrorException()
            }

        }) { throwable ->
            fragmentUiModel.value?.let {
                it.uiState.isLoading = false
                it.uiState.hasFailedToLoad = true
                it.uiData.exception = throwable
                _fragmentUiModel.value = it
            }
        }
    }

    fun applyPromo(mutation: String, requestParam: ValidateUsePromoRequest) {
        launch { doApplyPromo(mutation, requestParam) }
    }

    private suspend fun doApplyPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest) {
        launchCatchError(block = {
            // Initialize response action state
            if (applyPromoResponse.value == null) {
                _applyPromoResponse.value = ApplyPromoResponseAction()
            }

            // Set param
            val varPromo = mapOf(
                    "promo" to validateUsePromoRequest
            )
            val varParams = mapOf(
                    "params" to varPromo
            )

            // Get all sellected promo for analytical purpose
            val promoList = ArrayList<String>()
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiState.isSelected) {
                    promoList.add(it.uiData.promoCode)
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiState.isSelected) {
                            promoList.add(it.uiData.promoCode)
                        }
                    }
                }
            }

            // Get response
            val response = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(mutation, ValidateUseResponse::class.java, varParams)
                graphqlRepository.getReseponse(listOf(request))
                        .getSuccessData<ValidateUseResponse>()
            }

            if (response.validateUsePromoRevamp.status == "OK") {
                // Response is OK, then need to check whether it's apply promo manual or apply checked promo items
                val responseValidatePromo = response.validateUsePromoRevamp.promo
                if (responseValidatePromo.clashingInfoDetail.isClashedPromos) {
                    // Promo is clashing. Need to reload promo page
                    applyPromoResponse.value?.let {
                        it.state = ApplyPromoResponseAction.ACTION_RELOAD_PROMO
                        _applyPromoResponse.value = it
                    }
                } else {
                    if (responseValidatePromo.voucherOrders.isNotEmpty()) {
                        // Check all promo merchant is success
                        var successCount = 0
                        responseValidatePromo.voucherOrders.forEach { voucherOrder ->
                            if (voucherOrder.success) {
                                successCount++
                            } else {
                                // If one of promo merchant is error, then show error message
                                throw MessageErrorException(voucherOrder.message.text)
                            }
                        }
                        if (successCount == responseValidatePromo.voucherOrders.size) {
                            var selectedRecommendationCount = 0
                            promoRecommendationUiModel.value?.uiData?.promoCodes?.forEach {
                                if (promoList.contains(it)) selectedRecommendationCount++
                            }
                            val promoRecommendationCount = promoRecommendationUiModel.value?.uiData?.promoCodes?.size
                                    ?: 0
                            val status = if (promoList.size == promoRecommendationCount && selectedRecommendationCount == promoRecommendationCount) 1 else 0
                            analytics.eventClickPakaiPromoSuccess(getPageSource(), status.toString(), promoList)
                            // If all promo merchant are success, then navigate to cart
                            applyPromoResponse.value?.let {
                                it.state = ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CART
                                _applyPromoResponse.value = it
                            }
                        }
                    } else {
                        analytics.eventClickPilihPromoFailedTerjadiKesalahanServer(getPageSource())
                        // Voucher orders is empty but the response is OK
                        // This section is added as fallback mechanism
                        throw MessageErrorException()
                    }
                }
            } else {
                analytics.eventClickPilihPromoFailedTerjadiKesalahanServer(getPageSource())
                // Response is not OK, need to show error message
                throw MessageErrorException(response.validateUsePromoRevamp.message.joinToString(". "))
            }
        }) { throwable ->
            // Notify fragment apply promo to stop loading
            applyPromoResponse.value?.let {
                it.state = ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR
                it.exception = throwable
                _applyPromoResponse.value = it
            }
        }
    }

    fun clearPromo(mutation: String) {
        launch { doClearPromo(mutation) }
    }

    private suspend fun doClearPromo(mutation: String) {
        launchCatchError(block = {
            // Initialize response action state
            if (clearPromoResponse.value == null) {
                _clearPromoResponse.value = ClearPromoResponseAction()
            }

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
                clearPromoResponse.value?.let {
                    it.state = ClearPromoResponseAction.ACTION_STATE_SUCCESS
                    _clearPromoResponse.value = it
                }
            } else {
                throw MessageErrorException()
            }
        }) { throwable ->
            clearPromoResponse.value?.let {
                it.state = ClearPromoResponseAction.ACTION_STATE_ERROR
                it.exception = throwable
                _clearPromoResponse.value = it
            }
        }
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
        var hasAnyEnabledPromo = false
        response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
            // Initialize eligibility header
            val eligibilityHeader = uiModelMapper.mapPromoEligibilityHeaderUiModel(couponSectionItem)
            couponList.add(eligibilityHeader)

            // Initialize promo list header
            val tmpPromoHeaderList = ArrayList<Visitable<*>>()
            couponSectionItem.subSections.forEach { couponSubSection ->
                val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(couponSubSection, headerIdentifierId, couponSectionItem.isEnabled)
                if (eligibilityHeader.uiState.isEnabled) {
                    hasAnyEnabledPromo = true
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
        if (!hasAnyEnabledPromo) analytics.eventViewAvailablePromoListIneligibleProduct(getPageSource())
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
        // Return clash result for analytics purpose
        var clashResult = false
        if (selectedItem.uiState.isSelected) {
            // Calculate clash on selection event
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    // Calculate clash on expanded promo item
                    if (it.uiData.clashingInfo.isNotEmpty()) {
                        val tmpClashResult = checkAndSetClashOnSelectionEvent(it, selectedItem)
                        if (!clashResult) clashResult = tmpClashResult
                        _tmpUiModel.value = Update(it)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Calculate clash on collapsed promo item
                    it.uiData.tmpPromoItemList.forEach {
                        val tmpClashResult = checkAndSetClashOnSelectionEvent(it, selectedItem)
                        if (!clashResult) clashResult = tmpClashResult
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
                        checkAndSetClashOnUnSelectionEvent(it, selectedItem)
                        _tmpUiModel.value = Update(it)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Calculate clash on collapsed promo item
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiData.clashingInfo.isNotEmpty()) {
                            checkAndSetClashOnUnSelectionEvent(it, selectedItem)
                        }
                    }
                    _tmpUiModel.value = Update(it)
                }
            }
        }

        if (clashResult) {
            selectedItem.uiState.isCausingOtherPromoClash = true
        }
    }

    private fun checkAndSetClashOnUnSelectionEvent(promoListItemUiModel: PromoListItemUiModel, selectedItem: PromoListItemUiModel) {
        if (promoListItemUiModel.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
            if (promoListItemUiModel.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                promoListItemUiModel.uiData.currentClashingPromo.remove(selectedItem.uiData.promoCode)
                if (promoListItemUiModel.uiData.currentClashingPromo.isNotEmpty()) {
                    val errorMessageBuilder = StringBuilder()
                    promoListItemUiModel.uiData.currentClashingPromo.forEach { string ->
                        if (promoListItemUiModel.uiData.clashingInfo.containsKey(string)) {
                            errorMessageBuilder.append(promoListItemUiModel.uiData.clashingInfo[string])
                        }
                    }
                    promoListItemUiModel.uiData.errorMessage = errorMessageBuilder.toString()
                } else {
                    promoListItemUiModel.uiData.errorMessage = ""
                }
            }
        }
    }

    private fun checkAndSetClashOnSelectionEvent(promoListItemUiModel: PromoListItemUiModel, selectedItem: PromoListItemUiModel): Boolean {
        var clashResult = false
        if (promoListItemUiModel.uiData.clashingInfo.containsKey(selectedItem.uiData.promoCode)) {
            if (!promoListItemUiModel.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                promoListItemUiModel.uiData.currentClashingPromo.add(selectedItem.uiData.promoCode)
                val errorMessageBuilder = StringBuilder(promoListItemUiModel.uiData.errorMessage)
                if (promoListItemUiModel.uiData.errorMessage.isNotBlank()) {
                    errorMessageBuilder.append("\n")
                }
                errorMessageBuilder.append(promoListItemUiModel.uiData.clashingInfo[selectedItem.uiData.promoCode])
                promoListItemUiModel.uiData.errorMessage = errorMessageBuilder.toString()
                clashResult = true
            }
        }
        return clashResult
    }

    private fun isPromoScopeHasAnySelectedPromoItem(parentIdentifierId: Int): Boolean {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSelected && it.uiData.parentIdentifierId == parentIdentifierId) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }

        return hasAnyPromoSellected
    }

    fun isHasAnySelectedPromoItem(): Boolean {
        var hasAnyPromoSellected = false
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSelected) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }

        return hasAnyPromoSellected
    }

    private fun updateResetButtonState() {
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
        analytics.eventClickResetPromo(getPageSource())
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

    private fun setFragmentStateHasPromoSelected(hasAnyPromoSelected: Boolean) {
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
            // Get the promo item data and set inverted selected value
            val promoItem = promoListUiModel.value?.get(it) as PromoListItemUiModel
            promoItem.uiState.isSelected = !promoItem.uiState.isSelected

            // Update view
            _tmpUiModel.value = Update(promoItem)

            // Perform clash calculation
            calculateClash(promoItem)
            if (promoItem.uiState.isSelected) {
                analytics.eventClickSelectKupon(getPageSource(), promoItem.uiData.promoCode, promoItem.uiState.isCausingOtherPromoClash)
                if (promoItem.uiState.isAttempted) {
                    analytics.eventClickSelectPromo(getPageSource(), promoItem.uiData.promoCode)
                }
            } else {
                analytics.eventClickDeselectKupon(getPageSource(), promoItem.uiData.promoCode, promoItem.uiState.isCausingOtherPromoClash)
                if (promoItem.uiState.isAttempted) {
                    analytics.eventClickDeselectPromo(getPageSource(), promoItem.uiData.promoCode)
                }
            }

            // Update header sub total
            var header: PromoListHeaderUiModel? = null
            promoListUiModel.value?.forEach {
                if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.identifierId == promoItem.uiData.parentIdentifierId) {
                    header = it
                    return@forEach
                }
            }

            header?.let {
                val hasSelectPromo = isPromoScopeHasAnySelectedPromoItem(it.uiData.identifierId)
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
                                // Calculate clash after uncheck
                                // Clash result is ignoned
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
            analytics.eventClickPilihPromoRecommendation(getPageSource(), it.uiData.promoCodes)

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

                    analytics.eventClickExpandIneligiblePromoList(getPageSource())
                }
            }
        }
    }

    fun updatePromoInputStateBeforeApplyPromo(promoCode: String) {
        analytics.eventClickTerapkanPromo(getPageSource(), promoCode)
        promoInputUiModel.value?.let {
            it.uiState.isLoading = true
            it.uiState.isButtonSelectEnabled = true
            it.uiData.promoCode = promoCode

            _tmpUiModel.value = Update(it)
        }
    }

    fun hasDifferentPreAppliedState(): Boolean {
        // Check if :
        // CASE 1 : has any promo item unchecked, but exist as pre applied promo item
        // CASE 2 : has any promo item checked but have not been applied, or
        val preAppliedPromoCodes = fragmentUiModel.value?.uiData?.preAppliedPromoCode ?: emptyList()
        if (preAppliedPromoCodes.isEmpty()) {
            return false
        } else {
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    // CASE 1
                    if (preAppliedPromoCodes.contains(it.uiData.promoCode) && !it.uiState.isSelected) {
                        return true
                    }
                    // CASE 2
                    if (!preAppliedPromoCodes.contains(it.uiData.promoCode) && it.uiState.isSelected) {
                        return true
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    it.uiData.tmpPromoItemList.forEach {
                        // CASE 1
                        if (preAppliedPromoCodes.contains(it.uiData.promoCode) && !it.uiState.isSelected) {
                            return true
                        }
                        // CASE 2
                        if (!preAppliedPromoCodes.contains(it.uiData.promoCode) && it.uiState.isSelected) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun sendAnalyticsClickLihatDetailKupon(promoCode: String) {
        analytics.eventClickLihatDetailKupon(getPageSource(), promoCode)
    }

    fun sendAnalyticsClickRemovePromoCode() {
        analytics.eventClickRemovePromoCode(getPageSource())
    }

    fun sendAnalyticsViewPopupSavePromo() {
        analytics.eventViewPopupSavePromo(getPageSource())
    }

    fun sendAnalyticsClickKeluarHalaman() {
        analytics.eventClickKeluarHalaman(getPageSource())
    }

    fun sendAnalyticsClickSimpanPromoBaru() {
        analytics.eventClickSimpanPromoBaru(getPageSource())
    }

    fun sendAnalyticsClickButtonVerifikasiNomorHp() {
        analytics.eventClickButtonVerifikasiNomorHp(getPageSource())
    }

    fun sendAnalyticsViewErrorPopup() {
        analytics.eventViewErrorPopup(getPageSource())
    }

    fun sendAnalyticsClickCobaLagi() {
        analytics.eventClickCobaLagi(getPageSource())
    }

    fun sendAnalyticsClickPakaiPromoFailed(errorMessage: String) {
        analytics.eventClickPakaiPromoFailed(getPageSource(), errorMessage)
    }
}