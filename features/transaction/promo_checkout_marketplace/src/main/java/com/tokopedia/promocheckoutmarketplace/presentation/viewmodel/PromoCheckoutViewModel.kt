package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promocheckoutmarketplace.data.request.CouponListRecommendationRequest
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_COUPON_LIST_EMPTY
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_USER_BLACKLISTED
import com.tokopedia.promocheckoutmarketplace.presentation.PromoErrorException
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel.UiData.Companion.LABEL_BUTTON_PHONE_VERIFICATION
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel.UiData.Companion.LABEL_BUTTON_TRY_AGAIN
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PromoCheckoutViewModel @Inject constructor(val dispatcher: CoroutineDispatcher,
                                                 private val graphqlRepository: GraphqlRepository,
                                                 private val uiModelMapper: PromoCheckoutUiModelMapper,
                                                 private val analytics: PromoCheckoutAnalytics,
                                                 private val userSession: UserSessionInterface)
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
    val _promoListUiModel = MutableLiveData<MutableList<Visitable<*>>>()
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

    // Promo Last Seen UI Model
    private val _promoLastSeenUiModel = MutableLiveData<PromoLastSeenUiModel>()
    val promoLastSeenUiModel: LiveData<PromoLastSeenUiModel>
        get() = _promoLastSeenUiModel

    // Live data to notify UI state after hit clear promo API
    private val _clearPromoResponse = MutableLiveData<ClearPromoResponseAction>()
    val clearPromoResponse: LiveData<ClearPromoResponseAction>
        get() = _clearPromoResponse

    // Live data to notify UI state after hit apply promo / validate use API
    private val _applyPromoResponse = MutableLiveData<ApplyPromoResponseAction>()
    val applyPromoResponse: LiveData<ApplyPromoResponseAction>
        get() = _applyPromoResponse

    // Live data to notify UI state after hit get coupon recommendation API
    val _getCouponRecommendationResponse = MutableLiveData<GetCouponRecommendationAction>()
    val getCouponRecommendationResponse: LiveData<GetCouponRecommendationAction>
        get() = _getCouponRecommendationResponse

    private val _getPromoLastSeenResponse = MutableLiveData<GetPromoLastSeenAction>()
    val getPromoLastSeenResponse: LiveData<GetPromoLastSeenAction>
        get() = _getPromoLastSeenResponse

    // Page source : CART, CHECKOUT, OCC
    private fun getPageSource(): Int {
        return fragmentUiModel.value?.uiData?.pageSource ?: 0
    }

    fun initFragmentUiModel(pageSource: Int) {
        val fragmentUiModel = uiModelMapper.mapFragmentUiModel(pageSource)
        _fragmentUiModel.value = fragmentUiModel
    }

    fun loadData(mutation: String, promoRequest: PromoRequest, promoCode: String) {
        launchCatchError(block = {
            getCouponRecommendation(mutation, promoRequest, promoCode)
        }) { throwable ->
            fragmentUiModel.value?.let {
                it.uiState.isLoading = false
                it.uiState.hasFailedToLoad = true
                it.uiData.exception = throwable
                _fragmentUiModel.value = it
            }
        }
    }

    private suspend fun getCouponRecommendation(mutation: String, promoRequest: PromoRequest, tmpPromoCode: String) {
        val promoCode = tmpPromoCode.toUpperCase(Locale.getDefault())

        // Clear all pre selected promo on load data
        promoRequest.attemptedCodes.clear()
        promoRequest.codes.clear()
        promoRequest.orders.forEach {
            it.codes.clear()
        }

        // Set param manual input
        if (promoCode.isNotBlank()) {
            promoRequest.attemptedCodes.add(promoCode)
            promoRequest.skipApply = 0
        } else {
            promoRequest.skipApply = 1
        }

        // For refresh state, add current selected promo code to request param
        promoRequest.orders.forEach { order ->
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    // Goes here if coupon state is expanded
                    if (it.uiState.isSelected) {
                        // If coupon is selected, add to request param.
                        // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                        if (it.uiData.uniqueId == order.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                            order.codes.add(it.uiData.promoCode)
                        } else if (it.uiData.shopId == 0 && !promoRequest.codes.contains(it.uiData.promoCode)) {
                            promoRequest.codes.add(it.uiData.promoCode)
                        }
                    } else {
                        // If coupon is unselected and exist on current promo request, remove from request param
                        // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                        if (it.uiData.uniqueId == order.uniqueId && order.codes.contains(it.uiData.promoCode)) {
                            order.codes.remove(it.uiData.promoCode)
                        } else if (it.uiData.shopId == 0 && promoRequest.codes.contains(it.uiData.promoCode)) {
                            promoRequest.codes.remove(it.uiData.promoCode)
                        }
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Goes here if coupon state is collapsed
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiState.isSelected) {
                            // If coupon is selected, add to request param
                            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                            if (it.uiData.uniqueId == order.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                                order.codes.add(it.uiData.promoCode)
                            } else if (it.uiData.shopId == 0 && !promoRequest.codes.contains(it.uiData.promoCode)) {
                                promoRequest.codes.add(it.uiData.promoCode)
                            }
                        } else {
                            // If coupon is unselected and exist on current promo request, remove from request param
                            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                            if (it.uiData.uniqueId == order.uniqueId && order.codes.contains(it.uiData.promoCode)) {
                                order.codes.remove(it.uiData.promoCode)
                            } else if (it.uiData.shopId == 0 && promoRequest.codes.contains(it.uiData.promoCode)) {
                                promoRequest.codes.remove(it.uiData.promoCode)
                            }
                        }
                    }
                }
            }
        }

        // Remove code if same with attempted
        if (promoRequest.codes.contains(promoCode)) {
            promoRequest.codes.remove(promoCode)
        }
        promoRequest.orders.forEach {
            if (it.codes.contains(promoCode)) {
                it.codes.remove(promoCode)
            }
        }

        val promo = HashMap<String, Any>()
        promo["params"] = CouponListRecommendationRequest(promoRequest = promoRequest)

        // Get response
        val response = withContext(dispatcher) {
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
                sendAnalyticsPromoPageLoaded()

                setPromoInputErrorIfAny(response)

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
                    _fragmentUiModel.value = it
                }

                calculateAndRenderTotalBenefit()
                updateRecommendationState()
            } else {
                if (getCouponRecommendationResponse.value == null) {
                    _getCouponRecommendationResponse.value = GetCouponRecommendationAction()
                }

                if (response.couponListRecommendation.data.emptyState.title.isEmpty() &&
                        response.couponListRecommendation.data.emptyState.description.isEmpty() &&
                        response.couponListRecommendation.data.emptyState.imageUrl.isEmpty()) {
                    if (promoCode.isNotBlank()) {
                        getCouponRecommendationResponse.value?.let {
                            it.state = GetCouponRecommendationAction.ACTION_SHOW_TOAST_ERROR
                            it.exception = PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString { ". " })
                            _getCouponRecommendationResponse.value = it
                        }
                    } else {
                        throw PromoErrorException()
                    }
                } else {
                    fragmentUiModel.value?.let {
                        it.uiState.isLoading = false
                        _fragmentUiModel.value = it
                    }

                    if (promoCode.isNotBlank()) {
                        promoInputUiModel.value?.let {
                            it.uiData.exception = PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString(". "))
                            it.uiState.isError = true
                            it.uiState.isButtonSelectEnabled = true
                            it.uiState.isLoading = false

                            _promoInputUiModel.value = it
                        }
                    } else {
                        val emptyState = uiModelMapper.mapEmptyState(response.couponListRecommendation)
                        emptyState.uiData.emptyStateStatus = response.couponListRecommendation.data.resultStatus.code
                        when {
                            response.couponListRecommendation.data.resultStatus.code == STATUS_COUPON_LIST_EMPTY -> {
                                emptyState.uiState.isShowButton = false
                                initPromoInput()
                                analytics.eventViewAvailablePromoListNoPromo(getPageSource())
                            }
                            response.couponListRecommendation.data.resultStatus.code == STATUS_PHONE_NOT_VERIFIED -> {
                                emptyState.uiData.buttonText = LABEL_BUTTON_PHONE_VERIFICATION
                                emptyState.uiState.isShowButton = true
                                analytics.eventViewPhoneVerificationMessage(getPageSource())
                            }
                            response.couponListRecommendation.data.resultStatus.code == STATUS_USER_BLACKLISTED -> {
                                emptyState.uiState.isShowButton = false
                                analytics.eventViewBlacklistErrorAfterApplyPromo(getPageSource())
                            }
                            else -> {
                                emptyState.uiState.isShowButton = true
                                emptyState.uiData.buttonText = LABEL_BUTTON_TRY_AGAIN
                            }
                        }
                        _promoEmptyStateUiModel.value = emptyState
                    }
                }
                setPromoInputErrorIfAny(response)
            }
        } else {
            throw PromoErrorException()
        }
    }

    private fun setPromoInputErrorIfAny(response: CouponListRecommendationResponse) {
        val attemptedPromoCodeError = response.couponListRecommendation.data.attemptedPromoCodeError
        if (attemptedPromoCodeError.code.isNotBlank() && attemptedPromoCodeError.message.isNotBlank()) {
            promoInputUiModel.value?.let {
                it.uiData.exception = PromoErrorException(attemptedPromoCodeError.message)
                it.uiData.promoCode = attemptedPromoCodeError.code
                it.uiState.isError = true
                it.uiState.isButtonSelectEnabled = true
                it.uiState.isLoading = false

                _promoInputUiModel.value = it
            }
        }
    }

    fun applyPromo(mutation: String, requestParam: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        launch { doApplyPromo(mutation, requestParam, bboPromoCodes) }
    }

    private suspend fun doApplyPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        launchCatchError(block = {
            // Initialize response action state
            if (applyPromoResponse.value == null) {
                _applyPromoResponse.value = ApplyPromoResponseAction()
            }

            validateUsePromoRequest.orders.forEach { order ->
                promoListUiModel.value?.forEach {
                    if (it is PromoListItemUiModel) {
                        // Goes here if coupon state is expanded
                        if (it.uiState.isSelected && !it.uiState.isDisabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                            // If coupon is selected, not disabled, and not clashing, add to request param.
                            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                            if (it.uiData.uniqueId == order?.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                                order.codes.add(it.uiData.promoCode)
                            } else if (it.uiData.shopId == 0 && !validateUsePromoRequest.codes.contains(it.uiData.promoCode)) {
                                validateUsePromoRequest.codes.add(it.uiData.promoCode)
                            }
                        } else {
                            // If coupon is unselected, disabled, or clashing, remove from request param
                            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                            if (it.uiData.uniqueId == order?.uniqueId && order.codes.contains(it.uiData.promoCode)) {
                                order.codes.remove(it.uiData.promoCode)
                            } else if (it.uiData.shopId == 0 && validateUsePromoRequest.codes.contains(it.uiData.promoCode)) {
                                validateUsePromoRequest.codes.remove(it.uiData.promoCode)
                            }
                        }
                    } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                        // Goes here if coupon state is collapsed
                        it.uiData.tmpPromoItemList.forEach {
                            if (it.uiState.isSelected && !it.uiState.isDisabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                                // If coupon is selected, not disabled, and not clashing, add to request param.
                                // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                                if (it.uiData.uniqueId == order?.uniqueId && !order.codes.contains(it.uiData.promoCode)) {
                                    order.codes.add(it.uiData.promoCode)
                                } else if (it.uiData.shopId == 0 && !validateUsePromoRequest.codes.contains(it.uiData.promoCode)) {
                                    validateUsePromoRequest.codes.add(it.uiData.promoCode)
                                }
                            } else {
                                // If coupon is unselected, disabled, or clashing, remove from request param
                                // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
                                if (it.uiData.uniqueId == order?.uniqueId && order.codes.contains(it.uiData.promoCode)) {
                                    order.codes.remove(it.uiData.promoCode)
                                } else if (it.uiData.shopId == 0 && validateUsePromoRequest.codes.contains(it.uiData.promoCode)) {
                                    validateUsePromoRequest.codes.remove(it.uiData.promoCode)
                                }
                            }
                        }
                    }
                }
            }

            // Get all selected promo
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

            // Remove invalid promo code
            val invalidPromoCodes = ArrayList<String>()
            validateUsePromoRequest.codes.forEach { promoGlobalCode ->
                promoGlobalCode?.let {
                    if (!promoList.contains(it)) {
                        invalidPromoCodes.add(it)
                    }
                }
            }
            validateUsePromoRequest.orders.forEach { order ->
                order?.codes?.forEach { promoCode ->
                    if (!promoList.contains(promoCode) && !bboPromoCodes.contains(promoCode)) {
                        invalidPromoCodes.add(promoCode)
                    }
                }
            }
            invalidPromoCodes.forEach { invalidPromoCode ->
                if (validateUsePromoRequest.codes.contains(invalidPromoCode)) {
                    validateUsePromoRequest.codes.remove(invalidPromoCode)
                }
                validateUsePromoRequest.orders.forEach { order ->
                    if (order?.codes?.contains(invalidPromoCode) == true) {
                        order.codes.remove(invalidPromoCode)
                    }
                }
            }

            validateUsePromoRequest.skipApply = 0

            // Set param
            val varPromo = mapOf(
                    "promo" to validateUsePromoRequest
            )
            val varParams = mapOf(
                    "params" to varPromo
            )

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
                        it.state = ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO
                        it.exception = PromoErrorException()
                        _applyPromoResponse.value = it
                    }
                } else {
                    if (responseValidatePromo.globalSuccess) {
                        // Check promo global is success
                        var isGlobalSuccess = false
                        if (responseValidatePromo.message.state != "red") {
                            isGlobalSuccess = true
                        }

                        // Check all promo merchant is success
                        var successCount = 0
                        responseValidatePromo.voucherOrders.forEach { voucherOrder ->
                            if (voucherOrder.success) {
                                successCount++
                            } else {
                                // If one of promo merchant is error, then show error message
                                throw PromoErrorException(voucherOrder.message.text)
                            }
                        }
                        if (isGlobalSuccess || successCount == responseValidatePromo.voucherOrders.size) {
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
                                it.data = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp)
                                it.lastValidateUseRequest = validateUsePromoRequest
                                _applyPromoResponse.value = it
                            }
                        }
                    } else {
                        val redStateMap = HashMap<String, String>()
                        if (!responseValidatePromo.success) {
                            // Error promo global
                            if (responseValidatePromo.codes.isNotEmpty()) {
                                responseValidatePromo.codes.forEach {
                                    if (!redStateMap.containsKey(it)) {
                                        redStateMap[it] = responseValidatePromo.message.text
                                    }
                                }
                            }
                        } else {
                            // Error promo merchant
                            if (responseValidatePromo.voucherOrders.isNotEmpty()) {
                                responseValidatePromo.voucherOrders.forEach {
                                    if (!it.success) {
                                        if (it.code.isNotBlank()) {
                                            if (!redStateMap.containsKey(it.code)) {
                                                redStateMap[it.code] = it.message.text
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (redStateMap.isNotEmpty()) {
                            promoListUiModel.value?.forEach {
                                if (it is PromoListItemUiModel) {
                                    if (redStateMap.containsKey(it.uiData.promoCode)) {
                                        it.uiState.isSelected = false
                                        it.uiData.errorMessage = redStateMap[it.uiData.promoCode]
                                                ?: ""
                                        it.uiState.isDisabled = true
                                        _tmpUiModel.value = Update(it)
                                    }
                                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                                    it.uiData.tmpPromoItemList.forEach {
                                        if (redStateMap.containsKey(it.uiData.promoCode)) {
                                            it.uiState.isSelected = false
                                            it.uiData.errorMessage = redStateMap[it.uiData.promoCode]
                                                    ?: ""
                                            it.uiState.isDisabled = true
                                            _tmpUiModel.value = Update(it)
                                        }
                                    }
                                }
                            }
                            calculateAndRenderTotalBenefit()
                            throw PromoErrorException(responseValidatePromo.additionalInfo.errorDetail.message)
                        } else {
                            // Voucher global is empty and voucher orders are empty but the response is OK
                            // This section is added as fallback mechanism
                            throw PromoErrorException()
                        }
                    }
                }
            } else {
                // Response is not OK, need to show error message
                throw PromoErrorException(response.validateUsePromoRevamp.message.joinToString(". "))
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

    fun clearPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        launch { doClearPromo(mutation, validateUsePromoRequest, bboPromoCodes) }
    }

    private suspend fun doClearPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        launchCatchError(block = {
            // Initialize response action state
            if (clearPromoResponse.value == null) {
                _clearPromoResponse.value = ClearPromoResponseAction()
            }

            // Add unselected promo
            val toBeRemovedPromoCodes = ArrayList<String>()
            var tmpMutation = mutation
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiState.isParentEnabled) {
                    toBeRemovedPromoCodes.add(it.uiData.promoCode)
                } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiState.isParentEnabled) {
                            toBeRemovedPromoCodes.add(it.uiData.promoCode)
                        }
                    }
                }
            }

            // Add invalid promo
            validateUsePromoRequest.codes.forEach { promoGlobalCode ->
                promoGlobalCode?.let {
                    if (!bboPromoCodes.contains(it) && !toBeRemovedPromoCodes.contains(it)) {
                        toBeRemovedPromoCodes.add(it)
                    }
                }
            }
            validateUsePromoRequest.orders.forEach { order ->
                order?.codes?.forEach {
                    if (!bboPromoCodes.contains(it) && !toBeRemovedPromoCodes.contains(it)) {
                        toBeRemovedPromoCodes.add(it)
                    }
                }
            }

            val promoCodesJson = Gson().toJson(toBeRemovedPromoCodes)
            tmpMutation = tmpMutation.replace("#promoCode", promoCodesJson)

            tmpMutation = tmpMutation.replace("#isOCC", validateUsePromoRequest.cartType.equals("occ").toString())
            // Get response
            val response = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(tmpMutation, ClearPromoResponse::class.java)
                graphqlRepository.getReseponse(listOf(request))
                        .getSuccessData<ClearPromoResponse>()
            }

            if (response.successData.success) {
                // Remove promo code on validate use params after clear promo success
                val tmpValidateUsePromoRequest = validateUsePromoRequest
                toBeRemovedPromoCodes.forEach { promo ->
                    if (tmpValidateUsePromoRequest.codes.contains(promo)) {
                        tmpValidateUsePromoRequest.codes.remove(promo)
                    }

                    tmpValidateUsePromoRequest.orders.forEach {
                        if (it?.codes?.contains(promo) == true) {
                            it.codes.remove(promo)
                        }
                    }
                }
                clearPromoResponse.value?.let {
                    it.state = ClearPromoResponseAction.ACTION_STATE_SUCCESS
                    it.data = uiModelMapper.mapClearPromoResponse(response)
                    it.lastValidateUseRequest = tmpValidateUsePromoRequest
                    _clearPromoResponse.value = it
                }
            } else {
                throw PromoErrorException()
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
        // Get all selected promo
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
            val tmpIneligiblePromoList = ArrayList<Visitable<*>>()
            val tmpPromoHeaderList = ArrayList<Visitable<*>>()
            couponSectionItem.subSections.forEach { couponSubSection ->
                val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(couponSubSection, headerIdentifierId, couponSectionItem.isEnabled)

                if (eligibilityHeader.uiState.isEnabled) {
                    if (!eligibilityHeader.uiState.isCollapsed) {
                        couponList.add(promoHeader)
                    }
                } else {
                    tmpIneligiblePromoList.add(promoHeader)
                }
                headerIdentifierId++

                // Initialize promo list item
                val tmpCouponList = ArrayList<PromoListItemUiModel>()
                couponSubSection.coupons.forEach { couponItem ->
                    val promoItem = uiModelMapper.mapPromoListItemUiModel(
                            couponItem, promoHeader.uiData.identifierId, couponSubSection.isEnabled, selectedPromoList
                    )
                    if (eligibilityHeader.uiState.isEnabled) {
                        if (promoHeader.uiState.isCollapsed) {
                            tmpCouponList.add(promoItem)
                        } else {
                            couponList.add(promoItem)
                        }
                    } else {
                        tmpIneligiblePromoList.add(promoItem)
                    }
                }

                if (eligibilityHeader.uiState.isEnabled) {
                    if (tmpCouponList.isNotEmpty()) {
                        promoHeader.uiData.tmpPromoItemList = tmpCouponList
                    }

                    if (promoHeader.uiState.isCollapsed) {
                        tmpPromoHeaderList.add(promoHeader)
                    }
                }
            }

            if (tmpPromoHeaderList.isNotEmpty()) {
                eligibilityHeader.uiData.tmpPromo = tmpPromoHeaderList
            }

            if (tmpIneligiblePromoList.isNotEmpty()) {
                if (eligibilityHeader.uiState.isCollapsed) {
                    eligibilityHeader.uiData.tmpPromo = tmpIneligiblePromoList
                } else {
                    couponList.addAll(tmpIneligiblePromoList)
                }
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
                        if (it.uiData.promoCode != selectedItem.uiData.promoCode && it.uiData.clashingInfo.isNotEmpty()) {
                            val tmpClashResult = checkAndSetClashOnSelectionEvent(it, selectedItem)
                            if (!clashResult) clashResult = tmpClashResult
                        }
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
                            return@forEach
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
                    errorMessageBuilder.clear()
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
        resetRecommendedPromo()
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
        if (!element.uiState.isCollapsed) {
            val headerIndex = promoListUiModel.value?.indexOf(element)
            if (headerIndex == -1) return
            headerIndex?.let {
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
            val headerIndex = promoListUiModel.value?.indexOf(element)
            if (headerIndex == -1) return
            headerIndex?.let {
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

                headerData.uiData.tmpPromoItemList = emptyList()
                _tmpUiModel.value = Update(headerData)
            }

        }
    }

    fun updatePromoListAfterClickPromoItem(element: PromoListItemUiModel) {
        // Set to selected / un selected
        val itemIndex = promoListUiModel.value?.indexOf(element)
        if (itemIndex == -1) return
        itemIndex?.let {
            // Get the promo item data and set inverted selected value
            val promoItem = promoListUiModel.value?.get(it) as PromoListItemUiModel
            promoItem.uiState.isSelected = !promoItem.uiState.isSelected

            if (!promoItem.uiState.isSelected && promoItem.uiState.isRecommended) {
                resetRecommendedPromo()
            }

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

                if (headerIndex != -1) {
                    // Un check other item on current header if previously selected
                    val promoListSize = promoListUiModel.value?.size ?: 0
                    for (index in headerIndex + 1 until promoListSize) {
                        if (promoListUiModel.value?.get(index) !is PromoListItemUiModel) {
                            break
                        } else {
                            val tmpPromoItem = promoListUiModel.value?.get(index) as PromoListItemUiModel
                            if (tmpPromoItem.uiData.promoCode != element.uiData.promoCode && tmpPromoItem.uiState.isSelected) {
                                tmpPromoItem.uiState.isSelected = false
                                if (tmpPromoItem.uiState.isRecommended) {
                                    resetRecommendedPromo()
                                }
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

    fun applyRecommendedPromo() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            analytics.eventClickPilihPromoRecommendation(getPageSource(), it.uiData.promoCodes)

            it.uiState.isButtonSelectEnabled = false

            val expandedParentIdentifierList = mutableSetOf<Int>()
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    if (promoRecommendation.uiData.promoCodes.contains(it.uiData.promoCode)) {
                        uncheckSibling(it)
                        it.uiState.isSelected = true
                        it.uiState.isRecommended = true
                        _tmpUiModel.value = Update(it)
                        calculateClash(it)
                        expandedParentIdentifierList.add(it.uiData.parentIdentifierId)
                    }
                } else if (it is PromoListHeaderUiModel && it.uiState.isEnabled && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    var hasSelectedPromoItem = false
                    it.uiData.tmpPromoItemList.forEach {
                        if (promoRecommendation.uiData.promoCodes.contains(it.uiData.promoCode)) {
                            uncheckSibling(it)
                            it.uiState.isSelected = true
                            it.uiState.isRecommended = true
                            _tmpUiModel.value = Update(it)
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

    fun uncheckSibling(promoItem: PromoListItemUiModel) {
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && it.uiData.parentIdentifierId == promoItem.uiData.parentIdentifierId && it.uiState.isSelected) {
                it.uiState.isSelected = false
                _tmpUiModel.value = Update(it)
            } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                it.uiData.tmpPromoItemList.forEach {
                    if (it.uiData.parentIdentifierId == promoItem.uiData.parentIdentifierId && it.uiState.isSelected) {
                        it.uiState.isSelected = false
                        _tmpUiModel.value = Update(it)
                    }
                }
            }
        }
    }

    fun resetRecommendedPromo() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            it.uiState.isButtonSelectEnabled = true
            _promoRecommendationUiModel.value = it
        }
    }

    fun updateRecommendationState() {
        val recommendationPromoCodeList = promoRecommendationUiModel.value?.uiData?.promoCodes
                ?: emptyList()
        if (recommendationPromoCodeList.isNotEmpty()) {
            var selectedRecommendationCount = 0
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel) {
                    if (it.uiState.isSelected && recommendationPromoCodeList.contains(it.uiData.promoCode)) {
                        selectedRecommendationCount++
                    }
                } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                    it.uiData.tmpPromoItemList.forEach {
                        if (it.uiState.isSelected && recommendationPromoCodeList.contains(it.uiData.promoCode)) {
                            selectedRecommendationCount++
                        }
                    }
                }
            }

            if (recommendationPromoCodeList.size == selectedRecommendationCount) {
                promoRecommendationUiModel.value?.let {
                    it.uiState.isButtonSelectEnabled = false
                    _promoRecommendationUiModel.value = it
                }
            }
        }
    }

    fun updateIneligiblePromoList(element: PromoEligibilityHeaderUiModel) {
        val modifiedData = ArrayList<Visitable<*>>()

        val dataIndex = promoListUiModel.value?.indexOf(element) ?: 0

        if (dataIndex != -1) {
            val data = promoListUiModel.value?.get(dataIndex) as PromoEligibilityHeaderUiModel
            data.let {
                if (!it.uiState.isCollapsed) {
                    // Collapse ineligible section
                    val startIndex = dataIndex + 1
                    val promoListSize = promoListUiModel.value?.size ?: 0
                    for (index in startIndex until promoListSize) {
                        promoListUiModel.value?.get(index)?.let {
                            modifiedData.add(it)
                        }
                    }

                    it.uiState.isCollapsed = !it.uiState.isCollapsed
                    it.uiData.tmpPromo = modifiedData

                    _tmpUiModel.value = Update(it)
                    modifiedData.forEach {
                        _tmpUiModel.value = Delete(it)
                    }
                    promoListUiModel.value?.removeAll(modifiedData)
                } else {
                    // Expand ineligible section
                    it.uiState.isCollapsed = !it.uiState.isCollapsed

                    _tmpUiModel.value = Update(it)
                    val mapListPromoHeader = HashMap<Visitable<*>, List<Visitable<*>>>()
                    mapListPromoHeader[it] = it.uiData.tmpPromo

                    _tmpListUiModel.value = Insert(mapListPromoHeader)
                    it.uiData.tmpPromo.forEach {
                        promoListUiModel.value?.add(it)
                    }
                    it.uiData.tmpPromo = emptyList()
                    _tmpUiModel.value = Update(it)

                    analytics.eventClickExpandIneligiblePromoList(getPageSource())
                }
            }
        }
    }

    fun updatePromoInputStateBeforeApplyPromo(promoCode: String, isFromLastSeen: Boolean) {
        analytics.eventClickTerapkanPromo(getPageSource(), promoCode)
        analytics.eventClickTerapkanAfterTypingPromoCode(getPageSource(), promoCode, isFromLastSeen)
        promoInputUiModel.value?.let {
            it.uiState.isLoading = true
            it.uiState.isButtonSelectEnabled = true
            it.uiData.promoCode = promoCode

            _tmpUiModel.value = Update(it)
        }
    }

    fun resetPromoInput() {
        promoInputUiModel.value?.let {
            it.uiState.isLoading = false
            it.uiState.isButtonSelectEnabled = false
            it.uiData.promoCode = ""

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

    fun getPromotionMap(): Map<String, String> {
        return mapOf(
                "id" to "",
                "name" to "",
                "creative" to "",
                "creative_url" to "",
                "position" to "",
                "category" to "",
                "promo_id" to "",
                "promo_code" to ""
        )
    }

    fun loadPromoLastSeen(query: String) {
        launch { getPromoLastSeen(query) }
    }

    private suspend fun getPromoLastSeen(query: String) {
        launchCatchError(block = {
            // Initialize response action state
            if (getPromoLastSeenResponse.value == null) {
                _getPromoLastSeenResponse.value = GetPromoLastSeenAction()
            }

            // Get response
            val response = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(query, GetPromoSuggestionResponse::class.java)
                graphqlRepository.getReseponse(listOf(request))
                        .getSuccessData<GetPromoSuggestionResponse>()
            }

            if (response.promoSuggestion.promoHistory.isNotEmpty()) {
                // Remove promo code on validate use params after clear promo success
                getPromoLastSeenResponse.value?.let {
                    it.state = GetPromoLastSeenAction.ACTION_SHOW
                    it.data = uiModelMapper.mapPromoLastSeenResponse(response)
                    _getPromoLastSeenResponse.value = it
                }
            } else {
                throw PromoErrorException()
            }
        }) { throwable ->
            getPromoLastSeenResponse.value?.let {
                it.state = GetPromoLastSeenAction.ACTION_RELEASE_LOCK_FLAG
                _getPromoLastSeenResponse.value = it
            }
        }
    }

    fun setPromoInputFromLastApply(promoCode: String) {
        analytics.eventClickPromoLastSeenItem(getPageSource(), promoCode)
        promoInputUiModel.value?.let {
            it.uiState.isValidLastSeenPromo = true
            it.uiData.validLastSeenPromoCode = promoCode
            it.uiData.promoCode = promoCode

            _tmpUiModel.value = Update(it)
        }
    }

    fun sendAnalyticsPromoPageLoaded() {
        val enabledPromotions = mutableListOf<Map<String, String>>()
        val disabledPromotions = mutableListOf<Map<String, String>>()
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel) {
                if (it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                    if (it.uiState.isDisabled) {
                        // Update : This logic is not valid for case load promo page.
                        // Promo item might be disabled only if get red state after apply promo / validate use.
                        disabledPromotions.add(getPromotionMap())
                    } else {
                        enabledPromotions.add(getPromotionMap())
                    }
                } else {
                    disabledPromotions.add(getPromotionMap())
                }
            } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                it.uiData.tmpPromoItemList.forEach {
                    if (it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                        if (it.uiState.isDisabled) {
                            // Update : This logic is not valid for case load promo page.
                            // Promo item might be disabled only if get red state after apply promo / validate use.
                            disabledPromotions.add(getPromotionMap())
                        } else {
                            enabledPromotions.add(getPromotionMap())
                        }
                    } else {
                        disabledPromotions.add(getPromotionMap())
                    }
                }
            } else if (it is PromoEligibilityHeaderUiModel && !it.uiState.isEnabled && it.uiData.tmpPromo.isNotEmpty()) {
                it.uiData.tmpPromo.forEach {
                    // Update : This logic is not valid.
                    // Goes here for promo disabled section so all promo item must be disabled.
                    if (it is PromoListItemUiModel && it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                        if (it.uiState.isDisabled) {
                            disabledPromotions.add(getPromotionMap())
                        } else {
                            enabledPromotions.add(getPromotionMap())
                        }
                    } else {
                        disabledPromotions.add(getPromotionMap())
                    }
                }
            }
        }

        if (enabledPromotions.size > 0) {
            val promoViewMap = HashMap<String, Any>()
            promoViewMap["promotions"] = enabledPromotions

            val eCommerceMap = HashMap<String, Any>()
            eCommerceMap["promoView"] = promoViewMap

            analytics.eventViewAvailablePromoListEligiblePromo(getPageSource(), eCommerceMap)
        } else if (disabledPromotions.size > 0) {
            val promoViewMap = HashMap<String, Any>()
            promoViewMap["promotions"] = disabledPromotions

            val eCommerceMap = HashMap<String, Any>()
            eCommerceMap["promoView"] = promoViewMap

            analytics.eventViewAvailablePromoListIneligibleProduct(getPageSource(), eCommerceMap)
        }
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

    fun sendAnalyticsClickBeliTanpaPromo() {
        analytics.eventClickBeliTanpaPromo(getPageSource())
    }

    fun sendAnalyticsDismissLastSeen() {
        analytics.eventDismissLastSeen(getPageSource())
    }

    fun sendAnalyticsClickPromoInputField() {
        analytics.eventClickInputField(getPageSource(), userSession.userId)
    }

    fun sendAnalyticsViewLastSeenPromo() {
        analytics.eventShowLastSeenPopUp(getPageSource(), userSession.userId)
    }
}