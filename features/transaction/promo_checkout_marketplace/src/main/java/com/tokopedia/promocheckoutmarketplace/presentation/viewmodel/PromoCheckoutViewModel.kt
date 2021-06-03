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
import com.tokopedia.promocheckoutmarketplace.PromoCheckoutIdlingResource
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
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUsePromoRevamp
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PromoCheckoutViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                 private val graphqlRepository: GraphqlRepository,
                                                 private val uiModelMapper: PromoCheckoutUiModelMapper,
                                                 private val analytics: PromoCheckoutAnalytics,
                                                 private val gson: Gson,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper)
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

    // Promo Last Seen UI Model
    private val _promoLastSeenUiModel = MutableLiveData<PromoLastSeenUiModel>()
    val promoLastSeenUiModel: LiveData<PromoLastSeenUiModel>
        get() = _promoLastSeenUiModel

    // Live data to notify UI state after hit clear promo API
    private val _clearPromoResponse = MutableLiveData<ClearPromoResponseAction>()
    val clearPromoResponse: LiveData<ClearPromoResponseAction>
        get() = _clearPromoResponse

    // Live data to notify UI state after hit apply promo / validate use API
    private val _applyPromoResponseAction = MutableLiveData<ApplyPromoResponseAction>()
    val applyPromoResponseAction: LiveData<ApplyPromoResponseAction>
        get() = _applyPromoResponseAction

    // Live data to notify UI state after hit get promo list API
    private val _getPromoListResponseAction = MutableLiveData<GetPromoListResponseAction>()
    val getPromoListResponseAction: LiveData<GetPromoListResponseAction>
        get() = _getPromoListResponseAction

    // Live data to store and notify UI of promo last seen
    private val _getPromoLastSeenResponse = MutableLiveData<GetPromoLastSeenAction>()
    val getPromoLastSeenResponse: LiveData<GetPromoLastSeenAction>
        get() = _getPromoLastSeenResponse

    // Page source : CART, CHECKOUT, OCC
    fun getPageSource(): Int {
        return fragmentUiModel.value?.uiData?.pageSource ?: 0
    }

    // Used for mocking _fragmentUiModel value.
    // Should only be called from unit test.
    fun setFragmentUiModelValue(value: FragmentUiModel) {
        _fragmentUiModel.value = value
    }

    // Used for mocking _promoListUiModel value.
    // Should only be called from unit test.
    fun setPromoListValue(value: ArrayList<Visitable<*>>) {
        _promoListUiModel.value = value
    }

    // Used for mocking _promoRecommendationUiModel value.
    // Should only be called from unit test.
    fun setPromoRecommendationValue(value: PromoRecommendationUiModel) {
        _promoRecommendationUiModel.value = value
    }

    // Used for mocking _promoInputUiModel value.
    // Should only be called from unit test.
    fun setPromoInputUiModelValue(value: PromoInputUiModel) {
        _promoInputUiModel.value = value
    }


    //---------------------------------------//
    /* Network Call Section : Get Promo List */
    //---------------------------------------//

    fun getPromoList(mutation: String, promoRequest: PromoRequest, promoCode: String, chosenAddress: ChosenAddress? = null) {
        launchCatchError(block = {
            doGetPromoList(mutation, promoRequest, promoCode, chosenAddress)
        }) { throwable ->
            setFragmentStateLoadPromoListFailed(throwable)
        }
    }

    private suspend fun doGetPromoList(mutation: String, promoRequest: PromoRequest, tmpPromoCode: String, chosenAddress: ChosenAddress?) {
        // Set request data
        val getPromoRequestParam = setGetPromoRequestData(tmpPromoCode, promoRequest, chosenAddress)

        // Get response data
        PromoCheckoutIdlingResource.increment()
        val response = withContext(dispatcher) {
            val request = GraphqlRequest(mutation, CouponListRecommendationResponse::class.java, getPromoRequestParam)
            graphqlRepository.getReseponse(listOf(request))
                    .getSuccessData<CouponListRecommendationResponse>()
        }
        PromoCheckoutIdlingResource.decrement()

        // Handle response data
        handleGetPromoListResponse(response, tmpPromoCode)
    }

    private fun setGetPromoRequestData(tmpPromoCode: String, promoRequest: PromoRequest, chosenAddress: ChosenAddress?): Map<String, Any?> {
        val promoCode = tmpPromoCode.toUpperCase(Locale.getDefault())

        resetGetPromoRequestData(promoCode, promoRequest)

        // For refresh state, add current selected promo code to request param
        promoRequest.orders.forEach { order ->
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    // Goes here if coupon state is expanded
                    setGetPromoRequestDataFromSelectedPromoItem(visitable, order, promoRequest)
                } else if (visitable is PromoListHeaderUiModel && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Goes here if coupon state is collapsed
                    visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                        setGetPromoRequestDataFromSelectedPromoItem(promoListItemUiModel, order, promoRequest)
                    }
                }
            }
        }

        removeDuplicateAttemptedPromoRequestData(promoRequest, promoCode)

        return mapOf(
                "params" to CouponListRecommendationRequest(promoRequest = promoRequest),
                // Add current selected address from local cache
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to (chosenAddress ?: chosenAddressRequestHelper.getChosenAddress())
        )
    }

    private fun setGetPromoRequestDataFromSelectedPromoItem(it: PromoListItemUiModel, order: Order, promoRequest: PromoRequest) {
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

    private fun removeDuplicateAttemptedPromoRequestData(promoRequest: PromoRequest, promoCode: String) {
        // Remove code if same with attempted
        if (promoRequest.codes.contains(promoCode)) {
            promoRequest.codes.remove(promoCode)
        }
        promoRequest.orders.forEach {
            if (it.codes.contains(promoCode)) {
                it.codes.remove(promoCode)
            }
        }
    }

    private fun resetGetPromoRequestData(promoCode: String, promoRequest: PromoRequest) {
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
    }

    private fun handleGetPromoListResponse(response: CouponListRecommendationResponse, tmpPromoCode: String) {
        if (response.couponListRecommendation.status == "OK") {
            if (response.couponListRecommendation.data.couponSections.isNotEmpty()) {
                handlePromoListNotEmpty(response, tmpPromoCode)
            } else {
                handlePromoListEmpty(response, tmpPromoCode)
            }
        } else {
            throw PromoErrorException()
        }
    }

    private fun handlePromoListNotEmpty(response: CouponListRecommendationResponse, tmpPromoCode: String) {
        setFragmentStateStopLoading()

        initGetPromoListResponseAction()
        setGetPromoListResponseActionClearData(tmpPromoCode)
        initPromoRecommendation(response)
        initPromoInput()
        initPromoList(response)

        sendAnalyticsPromoPageLoaded()

        setPromoInputErrorIfAny(response)

        val hasPreSelectedPromo = checkHasPreSelectedPromo()
        val preSelectedPromoCodes = getPreSelectedPromoList()

        setFragmentStateLoadPromoListSuccess(preSelectedPromoCodes, hasPreSelectedPromo)

        calculateAndRenderTotalBenefit()
        updateRecommendationState()
    }

    private fun handlePromoListEmpty(response: CouponListRecommendationResponse, tmpPromoCode: String) {
        initGetPromoListResponseAction()

        if (response.couponListRecommendation.data.emptyState.title.isEmpty() &&
                response.couponListRecommendation.data.emptyState.description.isEmpty() &&
                response.couponListRecommendation.data.emptyState.imageUrl.isEmpty()) {
            handleEmptyStateDataNotAvailable(tmpPromoCode, response)
        } else {
            setFragmentStateStopLoading()
            handleEmptyStateDataAvailable(tmpPromoCode, response)
        }

        setPromoInputErrorIfAny(response)
    }

    private fun handleEmptyStateDataNotAvailable(tmpPromoCode: String, response: CouponListRecommendationResponse) {
        if (tmpPromoCode.isNotBlank()) {
            getPromoListResponseAction.value?.let {
                it.state = GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR
                it.exception = PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString { ". " })
                _getPromoListResponseAction.value = it
            }
        } else {
            throw PromoErrorException()
        }
    }

    private fun handleEmptyStateDataAvailable(tmpPromoCode: String, response: CouponListRecommendationResponse) {
        if (tmpPromoCode.isNotBlank()) {
            promoInputUiModel.value?.let {
                it.uiData.exception = PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString(". "))
                it.uiState.isError = true
                it.uiState.isButtonSelectEnabled = true
                it.uiState.isLoading = false

                _promoInputUiModel.value = it
            }
        } else {
            handleEmptyStateData(response)
        }
    }

    private fun handleEmptyStateData(response: CouponListRecommendationResponse) {
        val emptyState = uiModelMapper.mapEmptyState(response.couponListRecommendation)
        emptyState.uiData.emptyStateStatus = response.couponListRecommendation.data.resultStatus.code
        when (response.couponListRecommendation.data.resultStatus.code) {
            STATUS_COUPON_LIST_EMPTY -> {
                emptyState.uiState.isShowButton = false
                initPromoInput()
                analytics.eventViewAvailablePromoListNoPromo(getPageSource())
            }
            STATUS_PHONE_NOT_VERIFIED -> {
                emptyState.uiData.buttonText = LABEL_BUTTON_PHONE_VERIFICATION
                emptyState.uiState.isShowButton = true
                analytics.eventViewPhoneVerificationMessage(getPageSource())
            }
            STATUS_USER_BLACKLISTED -> {
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

    private fun initGetPromoListResponseAction() {
        if (getPromoListResponseAction.value == null) {
            _getPromoListResponseAction.value = GetPromoListResponseAction()
        }
    }

    private fun setGetPromoListResponseActionClearData(tmpPromoCode: String) {
        if (tmpPromoCode.isNotBlank()) {
            getPromoListResponseAction.value?.let {
                it.state = GetPromoListResponseAction.ACTION_CLEAR_DATA
                _getPromoListResponseAction.value = it
            }
        }
    }

    private fun getPreSelectedPromoList(): ArrayList<String> {
        val preSelectedPromoCodes = ArrayList<String>()
        promoListUiModel.value?.forEach { visitable ->
            if (visitable is PromoListItemUiModel && visitable.uiState.isSelected) {
                preSelectedPromoCodes.add(visitable.uiData.promoCode)
            } else if (visitable is PromoListHeaderUiModel && visitable.uiState.isEnabled && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    if (promoListItemUiModel.uiState.isSelected) {
                        preSelectedPromoCodes.add(promoListItemUiModel.uiData.promoCode)
                    }
                }
            }
        }

        return preSelectedPromoCodes
    }

    private fun checkHasPreSelectedPromo(): Boolean {
        var tmpHasPreSelectedPromo = false
        promoListUiModel.value?.forEach visitableLoop@{ visitable ->
            if (visitable is PromoListItemUiModel && visitable.uiState.isSelected) {
                tmpHasPreSelectedPromo = true
                return@visitableLoop
            } else if (visitable is PromoListHeaderUiModel && visitable.uiState.isEnabled && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                visitable.uiData.tmpPromoItemList.forEach promoItemLoop@{ promoListItemUiModel ->
                    if (promoListItemUiModel.uiState.isSelected) {
                        tmpHasPreSelectedPromo = true
                        return@promoItemLoop
                    }
                }
            }
        }
        return tmpHasPreSelectedPromo
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

    private fun initPromoList(response: CouponListRecommendationResponse) {
        // Get all pre selected promo
        val preSelectedPromoList = getAllPreSelectedPromo(response)

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
                couponSubSection.coupons.forEachIndexed { index, couponItem ->
                    val promoItem = uiModelMapper.mapPromoListItemUiModel(
                            couponItem, promoHeader.uiData.identifierId,
                            couponSubSection.isEnabled, preSelectedPromoList,
                            index
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

    private fun getAllPreSelectedPromo(response: CouponListRecommendationResponse): ArrayList<String> {
        val preSelectedPromoList = ArrayList<String>()

        response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
            if (couponSectionItem.isEnabled) {
                couponSectionItem.subSections.forEach { subSection ->
                    subSection.coupons.forEach { coupon ->
                        if (coupon.isSelected) {
                            preSelectedPromoList.add(coupon.code)
                        }
                    }
                }
            }
        }

        return preSelectedPromoList
    }

    fun initPromoInput() {
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

    private fun updateRecommendationState() {
        val recommendationPromoCodeList = promoRecommendationUiModel.value?.uiData?.promoCodes
                ?: emptyList()
        if (recommendationPromoCodeList.isNotEmpty()) {
            var selectedRecommendationCount = 0
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    if (visitable.uiState.isSelected && recommendationPromoCodeList.contains(visitable.uiData.promoCode)) {
                        selectedRecommendationCount++
                    }
                } else if (visitable is PromoListHeaderUiModel && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                    visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                        if (promoListItemUiModel.uiState.isSelected && recommendationPromoCodeList.contains(promoListItemUiModel.uiData.promoCode)) {
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

    private fun setFragmentStateLoadPromoListSuccess(preSelectedPromoCodes: ArrayList<String>, hasPreSelectedPromo: Boolean) {
        fragmentUiModel.value?.let {
            it.uiData.preAppliedPromoCode = preSelectedPromoCodes
            it.uiState.hasPreAppliedPromo = hasPreSelectedPromo
            it.uiState.hasAnyPromoSelected = hasPreSelectedPromo
            it.uiState.hasFailedToLoad = false
            it.uiData.exception = null
            _fragmentUiModel.value = it
        }
    }

    private fun setFragmentStateLoadPromoListFailed(throwable: Throwable) {
        fragmentUiModel.value?.let {
            it.uiState.isLoading = false
            it.uiState.hasFailedToLoad = true
            it.uiData.exception = throwable
            _fragmentUiModel.value = it
        }
    }

    private fun setFragmentStateStopLoading() {
        fragmentUiModel.value?.let {
            it.uiState.isLoading = false
            _fragmentUiModel.value = it
        }
    }


    //------------------------------------//
    /* Network Call Section : Apply Promo */
    //------------------------------------//

    fun applyPromo(mutation: String, requestParam: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>, chosenAddress: ChosenAddress? = null) {
        launchCatchError(block = {
            doApplyPromo(mutation, requestParam, bboPromoCodes, chosenAddress)
        }) { throwable ->
            // Notify fragment apply promo to stop loading
            setApplyPromoStateFailed(throwable)
        }
    }

    private suspend fun doApplyPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>, chosenAddress: ChosenAddress?) {
        // Set request data
        setApplyPromoRequestData(validateUsePromoRequest)

        // Get all current selected promo (highlighted as green)
        val selectedPromoList = getSelectedPromoList()

        // Remove invalid promo code
        // Invalid promo code is promo code from outside promo page (cart/checkout) which previously selected,
        // but become invalid or not selected on promo page, except promo BBO
        removeInvalidPromoCode(validateUsePromoRequest, selectedPromoList, bboPromoCodes)

        validateUsePromoRequest.skipApply = 0

        // Set param
        val applyPromoRequestParam = mapOf(
                "params" to mapOf(
                        "promo" to validateUsePromoRequest
                ),
                // Add current selected address from local cache
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to (chosenAddress ?: chosenAddressRequestHelper.getChosenAddress())
        )

        // Get response data
        PromoCheckoutIdlingResource.increment()
        val response = withContext(dispatcher) {
            val request = GraphqlRequest(mutation, ValidateUseResponse::class.java, applyPromoRequestParam)
            graphqlRepository.getReseponse(listOf(request))
                    .getSuccessData<ValidateUseResponse>()
        }
        PromoCheckoutIdlingResource.decrement()

        // Handle response data
        handleApplyPromoResponse(response, selectedPromoList, validateUsePromoRequest)
    }

    private fun removeInvalidPromoCode(validateUsePromoRequest: ValidateUsePromoRequest,
                                       selectedPromoList: ArrayList<String>,
                                       bboPromoCodes: ArrayList<String>) {
        val invalidPromoCodes = ArrayList<String>()
        validateUsePromoRequest.codes.forEach { promoGlobalCode ->
            promoGlobalCode?.let {
                if (!selectedPromoList.contains(it)) {
                    invalidPromoCodes.add(it)
                }
            }
        }
        validateUsePromoRequest.orders.forEach { order ->
            order?.codes?.forEach { promoCode ->
                if (!selectedPromoList.contains(promoCode) && !bboPromoCodes.contains(promoCode)) {
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
    }

    private fun setApplyPromoRequestData(validateUsePromoRequest: ValidateUsePromoRequest) {
        validateUsePromoRequest.orders.forEach { order ->
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    // Goes here if coupon state is expanded
                    setApplyPromoRequestDataFromSelectedPromo(visitable, order, validateUsePromoRequest)
                } else if (visitable is PromoListHeaderUiModel && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                    // Goes here if coupon state is collapsed
                    visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                        setApplyPromoRequestDataFromSelectedPromo(promoListItemUiModel, order, validateUsePromoRequest)
                    }
                }
            }
        }
    }

    private fun setApplyPromoRequestDataFromSelectedPromo(promoListItemUiModel: PromoListItemUiModel,
                                                          order: OrdersItem?,
                                                          validateUsePromoRequest: ValidateUsePromoRequest) {
        if (promoListItemUiModel.uiState.isSelected && !promoListItemUiModel.uiState.isDisabled &&
                promoListItemUiModel.uiData.currentClashingPromo.isNullOrEmpty()) {
            // If coupon is selected, not disabled, and not clashing, add to request param
            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
            if (promoListItemUiModel.uiData.uniqueId == order?.uniqueId &&
                    !order.codes.contains(promoListItemUiModel.uiData.promoCode)) {
                order.codes.add(promoListItemUiModel.uiData.promoCode)
            } else if (promoListItemUiModel.uiData.shopId == 0 &&
                    !validateUsePromoRequest.codes.contains(promoListItemUiModel.uiData.promoCode)) {
                validateUsePromoRequest.codes.add(promoListItemUiModel.uiData.promoCode)
            }
        } else {
            // If coupon is unselected, disabled, or clashing, remove from request param
            // If unique_id = 0, means it's a coupon global, else it's a coupon merchant
            if (promoListItemUiModel.uiData.uniqueId == order?.uniqueId &&
                    order.codes.contains(promoListItemUiModel.uiData.promoCode)) {
                order.codes.remove(promoListItemUiModel.uiData.promoCode)
            } else if (promoListItemUiModel.uiData.shopId == 0 &&
                    validateUsePromoRequest.codes.contains(promoListItemUiModel.uiData.promoCode)) {
                validateUsePromoRequest.codes.remove(promoListItemUiModel.uiData.promoCode)
            }
        }
    }

    private fun getSelectedPromoList(): ArrayList<String> {
        val selectedPromoList = ArrayList<String>()
        promoListUiModel.value?.forEach { visitable ->
            if (visitable is PromoListItemUiModel && visitable.uiState.isSelected) {
                selectedPromoList.add(visitable.uiData.promoCode)
            } else if (visitable is PromoListHeaderUiModel && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    if (promoListItemUiModel.uiState.isSelected) {
                        selectedPromoList.add(promoListItemUiModel.uiData.promoCode)
                    }
                }
            }
        }

        return selectedPromoList
    }

    private fun handleApplyPromoResponse(response: ValidateUseResponse,
                                         selectedPromoList: ArrayList<String>,
                                         validateUsePromoRequest: ValidateUsePromoRequest) {
        if (response.validateUsePromoRevamp.status == "OK") {
            // Initialize response action state
            initApplyPromoResponseAction()

            // Response is OK, then need to check whether it's apply promo manual or apply checked promo items
            val responseValidateUse = response.validateUsePromoRevamp
            if (responseValidateUse.promo.clashingInfoDetail.isClashedPromos) {
                // Promo is clashing. Need to reload promo page
                setApplyPromoStateClashing()
            } else {
                if (responseValidateUse.promo.globalSuccess) {
                    handleApplyPromoSuccess(selectedPromoList, responseValidateUse, validateUsePromoRequest)
                } else {
                    handleApplyPromoFailed(responseValidateUse)
                }
            }
        } else {
            // Response is not OK, need to show error message
            throw PromoErrorException(response.validateUsePromoRevamp.message.joinToString(". "))
        }
    }

    private fun handleApplyPromoSuccess(selectedPromoList: ArrayList<String>,
                                        response: ValidateUsePromoRevamp,
                                        request: ValidateUsePromoRequest) {
        val responseValidatePromo = response.promo

        // Check promo global is success
        var isGlobalSuccess = false
        if (responseValidatePromo.message.state != "red") {
            isGlobalSuccess = true
        }

        // Check all promo merchant is success
        // Update : This logic might be unnecessary,
        // since if global success is true, all voucher order status shoulbe success
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
                if (selectedPromoList.contains(it)) selectedRecommendationCount++
            }
            val promoRecommendationCount = promoRecommendationUiModel.value?.uiData?.promoCodes?.size
                    ?: 0
            val status = if (selectedPromoList.size == promoRecommendationCount && selectedRecommendationCount == promoRecommendationCount) 1 else 0
            analytics.eventClickPakaiPromoSuccess(getPageSource(), status.toString(), selectedPromoList)
            // If all promo merchant are success, then navigate to cart
            setApplyPromoStateSuccess(request, response)
        }
    }

    private fun handleApplyPromoFailed(responseValidateUse: ValidateUsePromoRevamp) {
        val responseValidatePromo = responseValidateUse.promo
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
                    if (!it.success && it.code.isNotBlank() && !redStateMap.containsKey(it.code)) {
                        redStateMap[it.code] = it.message.text
                    }
                }
            }
        }

        if (redStateMap.isNotEmpty()) {
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    setPromoItemDisabled(redStateMap, visitable)
                } else if (visitable is PromoListHeaderUiModel && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                    visitable.uiData.tmpPromoItemList.forEach { promoListItemuiModel ->
                        setPromoItemDisabled(redStateMap, promoListItemuiModel)
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

    private fun setPromoItemDisabled(redStateMap: HashMap<String, String>, it: PromoListItemUiModel) {
        if (redStateMap.containsKey(it.uiData.promoCode)) {
            it.uiState.isSelected = false
            it.uiData.errorMessage = redStateMap[it.uiData.promoCode] ?: ""
            it.uiState.isDisabled = true
            _tmpUiModel.value = Update(it)
        }
    }

    private fun initApplyPromoResponseAction() {
        if (applyPromoResponseAction.value == null) {
            _applyPromoResponseAction.value = ApplyPromoResponseAction()
        }
    }

    private fun setApplyPromoStateClashing() {
        applyPromoResponseAction.value?.let {
            it.state = ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO
            it.exception = PromoErrorException()
            _applyPromoResponseAction.value = it
        }
    }

    private fun setApplyPromoStateSuccess(request: ValidateUsePromoRequest, response: ValidateUsePromoRevamp) {
        applyPromoResponseAction.value?.let {
            it.state = ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE
            it.data = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response)
            it.lastValidateUseRequest = request
            _applyPromoResponseAction.value = it
        }
    }

    private fun setApplyPromoStateFailed(throwable: Throwable) {
        // Initialize response action state if needed
        initApplyPromoResponseAction()
        applyPromoResponseAction.value?.let {
            it.state = ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR
            it.exception = throwable
            _applyPromoResponseAction.value = it
        }
    }


    //------------------------------------//
    /* Network Call Section : Clear Promo */
    //------------------------------------//

    fun clearPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        launchCatchError(block = {
            doClearPromo(mutation, validateUsePromoRequest, bboPromoCodes)
        }) { throwable ->
            setClearPromoStateFailed(throwable)
        }
    }

    private suspend fun doClearPromo(mutation: String, validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {

        val toBeRemovedPromoCodes = getToBeClearedPromoCodes(validateUsePromoRequest, bboPromoCodes)

        var tmpMutation = mutation
        val promoCodesJson = gson.toJson(toBeRemovedPromoCodes)
        tmpMutation = tmpMutation.replace("#promoCode", promoCodesJson)
        tmpMutation = tmpMutation.replace("#isOCC", (validateUsePromoRequest.cartType == "occ").toString())

        // Get response
        PromoCheckoutIdlingResource.increment()
        val response = withContext(dispatcher) {
            val request = GraphqlRequest(tmpMutation, ClearPromoResponse::class.java)
            graphqlRepository.getReseponse(listOf(request))
                    .getSuccessData<ClearPromoResponse>()
        }
        PromoCheckoutIdlingResource.decrement()

        handleClearPromoResponse(response, validateUsePromoRequest, toBeRemovedPromoCodes)
    }

    private fun getToBeClearedPromoCodes(validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>): ArrayList<String> {
        val toBeRemovedPromoCodes = ArrayList<String>()

        // Add unselected promo
        addUnSelectedPromoCodes(toBeRemovedPromoCodes)

        // Add invalid promo
        // Invalid promo code is promo code from outside promo page (cart/checkout) which previously selected,
        // but become invalid or not selected on promo page, except promo BBO
        addInvalidPromo(validateUsePromoRequest, bboPromoCodes, toBeRemovedPromoCodes)

        return toBeRemovedPromoCodes
    }

    private fun addInvalidPromo(validateUsePromoRequest: ValidateUsePromoRequest,
                                bboPromoCodes: ArrayList<String>,
                                toBeRemovedPromoCodes: ArrayList<String>) {
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
    }

    private fun addUnSelectedPromoCodes(toBeRemovedPromoCodes: ArrayList<String>) {
        promoListUiModel.value?.forEach { visitable ->
            if (visitable is PromoListItemUiModel && visitable.uiState.isParentEnabled && !toBeRemovedPromoCodes.contains(visitable.uiData.promoCode)) {
                toBeRemovedPromoCodes.add(visitable.uiData.promoCode)
            } else if (visitable is PromoListHeaderUiModel && visitable.uiState.isEnabled && visitable.uiData.tmpPromoItemList.isNotEmpty()) {
                visitable.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    if (promoListItemUiModel.uiState.isParentEnabled && !toBeRemovedPromoCodes.contains(promoListItemUiModel.uiData.promoCode)) {
                        toBeRemovedPromoCodes.add(promoListItemUiModel.uiData.promoCode)
                    }
                }
            }
        }
    }

    private fun handleClearPromoResponse(response: ClearPromoResponse, validateUsePromoRequest: ValidateUsePromoRequest, toBeRemovedPromoCodes: ArrayList<String>) {
        // Initialize response action state
        initClearPromoResponseAction()

        if (response.successData.success) {
            // Remove promo code on validate use params after clear promo success
            toBeRemovedPromoCodes.forEach { promo ->
                if (validateUsePromoRequest.codes.contains(promo)) {
                    validateUsePromoRequest.codes.remove(promo)
                }

                validateUsePromoRequest.orders.forEach {
                    if (it?.codes?.contains(promo) == true) {
                        it.codes.remove(promo)
                    }
                }
            }
            setClearPromoStateSuccess(response, validateUsePromoRequest)
        } else {
            throw PromoErrorException()
        }
    }

    private fun setClearPromoStateSuccess(response: ClearPromoResponse, tmpValidateUsePromoRequest: ValidateUsePromoRequest) {
        clearPromoResponse.value?.let {
            it.state = ClearPromoResponseAction.ACTION_STATE_SUCCESS
            it.data = uiModelMapper.mapClearPromoResponse(response)
            it.lastValidateUseRequest = tmpValidateUsePromoRequest
            _clearPromoResponse.value = it
        }
    }

    private fun initClearPromoResponseAction() {
        if (clearPromoResponse.value == null) {
            _clearPromoResponse.value = ClearPromoResponseAction()
        }
    }

    private fun setClearPromoStateFailed(throwable: Throwable) {
        clearPromoResponse.value?.let {
            it.state = ClearPromoResponseAction.ACTION_STATE_ERROR
            it.exception = throwable
            _clearPromoResponse.value = it
        }
    }


    //--------------------------------------------//
    /* Network Call Section : Get Last Seen Promo */
    //--------------------------------------------//

    fun getPromoLastSeen(query: String) {
        launchCatchError(block = {
            doGetPromoLastSeen(query)
        }) {
            getPromoLastSeenResponse.value?.let {
                it.state = GetPromoLastSeenAction.ACTION_RELEASE_LOCK_FLAG
                _getPromoLastSeenResponse.value = it
            }
        }
    }

    private suspend fun doGetPromoLastSeen(query: String) {
        // Get response
        PromoCheckoutIdlingResource.increment()
        val response = withContext(dispatcher) {
            val request = GraphqlRequest(query, GetPromoSuggestionResponse::class.java)
            graphqlRepository.getReseponse(listOf(request))
                    .getSuccessData<GetPromoSuggestionResponse>()
        }
        PromoCheckoutIdlingResource.decrement()

        handleGetPromoLastSeenResponse(response)
    }

    private fun handleGetPromoLastSeenResponse(response: GetPromoSuggestionResponse) {
        // Initialize response action state
        if (getPromoLastSeenResponse.value == null) {
            _getPromoLastSeenResponse.value = GetPromoLastSeenAction()
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
    }


    //---------------------//
    /* UI Callback Section */
    //---------------------//

    fun initFragmentUiModel(pageSource: Int) {
        val fragmentUiModel = uiModelMapper.mapFragmentUiModel(pageSource)
        _fragmentUiModel.value = fragmentUiModel
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

    fun resetPromo() {
        analytics.eventClickResetPromo(getPageSource())
        resetSelectedPromo()
    }

    private fun resetSelectedPromo() {
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

    fun applyRecommendedPromo() {
        resetSelectedPromo()
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

    fun setPromoInputFromLastApply(promoCode: String) {
        analytics.eventClickPromoLastSeenItem(getPageSource(), promoCode)
        promoInputUiModel.value?.let {
            it.uiState.isValidLastSeenPromo = true
            it.uiData.validLastSeenPromoCode = promoCode
            it.uiData.promoCode = promoCode

            _tmpUiModel.value = Update(it)
        }
    }

    private fun resetRecommendedPromo() {
        val promoRecommendation = promoRecommendationUiModel.value
        promoRecommendation?.let {
            it.uiState.isButtonSelectEnabled = true
            _promoRecommendationUiModel.value = it
        }
    }

    private fun uncheckSibling(promoItem: PromoListItemUiModel) {
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


    //-------------------//
    /* Analytics Section */
    //-------------------//

    private fun generatePromoEnhancedEcommerceMapData(): Map<String, String> {
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

    fun sendAnalyticsPromoPageLoaded() {
        val enabledPromotions = mutableListOf<Map<String, String>>()
        val disabledPromotions = mutableListOf<Map<String, String>>()
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel) {
                if (it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                    if (it.uiState.isDisabled) {
                        // Update : This logic is not valid for case load promo page.
                        // Promo item might be disabled only if get red state after apply promo / validate use.
                        disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                    } else {
                        enabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                    }
                } else {
                    disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                }
            } else if (it is PromoListHeaderUiModel && it.uiData.tmpPromoItemList.isNotEmpty()) {
                it.uiData.tmpPromoItemList.forEach {
                    if (it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                        if (it.uiState.isDisabled) {
                            // Update : This logic is not valid for case load promo page.
                            // Promo item might be disabled only if get red state after apply promo / validate use.
                            disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                        } else {
                            enabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                        }
                    } else {
                        disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                    }
                }
            } else if (it is PromoEligibilityHeaderUiModel && !it.uiState.isEnabled && it.uiData.tmpPromo.isNotEmpty()) {
                it.uiData.tmpPromo.forEach {
                    // Update : This logic is not valid.
                    // Goes here for promo disabled section so all promo item must be disabled.
                    if (it is PromoListItemUiModel && it.uiState.isParentEnabled && it.uiData.currentClashingPromo.isNullOrEmpty()) {
                        if (it.uiState.isDisabled) {
                            disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                        } else {
                            enabledPromotions.add(generatePromoEnhancedEcommerceMapData())
                        }
                    } else {
                        disabledPromotions.add(generatePromoEnhancedEcommerceMapData())
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

}