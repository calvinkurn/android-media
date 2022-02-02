package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promocheckoutmarketplace.PromoCheckoutIdlingResource
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.data.response.ErrorPage
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_COUPON_LIST_EMPTY
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_USER_BLACKLISTED
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promocheckoutmarketplace.domain.usecase.GetPromoSuggestionUseCase
import com.tokopedia.promocheckoutmarketplace.presentation.PromoCheckoutLogger
import com.tokopedia.promocheckoutmarketplace.presentation.PromoErrorException
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel.UiData.Companion.LABEL_BUTTON_PHONE_VERIFICATION
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel.UiData.Companion.LABEL_BUTTON_TRY_AGAIN
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_OCC_MULTI
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PromoCheckoutViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                 private val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
                                                 private val validateUseUseCase: ValidateUsePromoRevampUseCase,
                                                 private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                 private val getPromoSuggestionUseCase: GetPromoSuggestionUseCase,
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

    // Promo Error State UI Model
    private val _promoErrorStateUiModel = MutableLiveData<PromoErrorStateUiModel>()
    val promoErrorStateUiModel: LiveData<PromoErrorStateUiModel>
        get() = _promoErrorStateUiModel

    // Promo Recommendation UI Model
    private val _promoRecommendationUiModel = MutableLiveData<PromoRecommendationUiModel>()
    val promoRecommendationUiModel: LiveData<PromoRecommendationUiModel>
        get() = _promoRecommendationUiModel

    // Promo Recommendation UI Model
    private val _promoTabUiModel = MutableLiveData<PromoTabUiModel>()
    val promoTabUiModel: LiveData<PromoTabUiModel>
        get() = _promoTabUiModel

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

    // Promo Suggestion UI Model
    private val _promoSuggestionUiModel = MutableLiveData<PromoSuggestionUiModel>()
    val promoSuggestionUiModel: LiveData<PromoSuggestionUiModel>
        get() = _promoSuggestionUiModel

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

    // Live data to store and notify UI of promo suggestion
    private val _getPromoSuggestionResponse = MutableLiveData<GetPromoSuggestionAction>()
    val getPromoSuggestionResponse: LiveData<GetPromoSuggestionAction>
        get() = _getPromoSuggestionResponse

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

    override fun onCleared() {
        super.onCleared()
        getCouponListRecommendationUseCase.cancelJobs()
        validateUseUseCase.cancelJobs()
        clearCacheAutoApplyStackUseCase.cancelJobs()
        getPromoSuggestionUseCase.cancelJobs()
    }

    //---------------------------------------//
    /* Network Call Section : Get Promo List */
    //---------------------------------------//

    fun getPromoList(promoRequest: PromoRequest, attemptedPromoCode: String, chosenAddress: ChosenAddress? = null) {
        // Set request data
        prepareGetPromoRequestData(attemptedPromoCode, promoRequest, chosenAddress)

        // Get response data
        PromoCheckoutIdlingResource.increment()
        getCouponListRecommendationUseCase.setParams(promoRequest, chosenAddress)
        getCouponListRecommendationUseCase.execute(
                onSuccess = {
                    PromoCheckoutIdlingResource.decrement()
                    onSuccessGetPromoList(it, attemptedPromoCode)
                },
                onError = {
                    PromoCheckoutIdlingResource.decrement()
                    setFragmentStateLoadPromoListFailed(it)
                    sendAnalyticsOnErrorAttemptPromo(attemptedPromoCode, it.message ?: "")
                }
        )
    }

    private fun prepareGetPromoRequestData(tmpPromoCode: String, promoRequest: PromoRequest, chosenAddress: ChosenAddress?) {
        val promoCode = tmpPromoCode.toUpperCase(Locale.getDefault())

        resetGetPromoRequestData(promoCode, promoRequest)

        // For refresh state, add current selected promo code to request param
        promoRequest.orders.forEach { order ->
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    // Goes here if coupon state is expanded
                    setPromoRequestDataFromSelectedPromoItem(visitable, order, promoRequest)
                }
            }
        }

        removeDuplicateAttemptedPromoRequestData(promoRequest, promoCode)
    }

    private fun setPromoRequestDataFromSelectedPromoItem(it: PromoListItemUiModel, order: Order, promoRequest: PromoRequest) {
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

    private fun onSuccessGetPromoList(response: CouponListRecommendationResponse, attemptedPromoCode: String) {
        if (response.couponListRecommendation.status == "OK") {
            if (response.couponListRecommendation.data.errorPage.isShowErrorPage) {
                handleShowErrorPage(response.couponListRecommendation.data.errorPage)
            } else {
                if (response.couponListRecommendation.data.couponSections.isNotEmpty()) {
                    handlePromoListNotEmpty(response, attemptedPromoCode)
                } else {
                    handlePromoListEmpty(response, attemptedPromoCode)
                }
            }
        } else {
            PromoCheckoutLogger.logOnErrorLoadPromoCheckoutPage(PromoErrorException("response status error"))
            val exception = PromoErrorException()
            setFragmentStateLoadPromoListFailed(exception)
            sendAnalyticsOnErrorAttemptPromo(attemptedPromoCode, exception.message ?: "")
        }
    }

    private fun handlePromoListNotEmpty(response: CouponListRecommendationResponse, tmpPromoCode: String) {
        setFragmentStateStopLoading()

        initGetPromoListResponseAction()
        setGetPromoListResponseActionClearData(tmpPromoCode)
        initPromoList(response)

        sendAnalyticsPromoPageLoaded()

        setPromoInputErrorIfAny(response)

        val hasPreSelectedPromo = checkHasPreSelectedPromo()
        val preSelectedPromoCodes = getPreSelectedPromoList()

        setFragmentStateLoadPromoListSuccess(preSelectedPromoCodes, hasPreSelectedPromo)

        calculateAndRenderTotalBenefit()
        updateRecommendationState()
    }

    private fun handlePromoListEmpty(response: CouponListRecommendationResponse, attemptedPromoCode: String) {
        initGetPromoListResponseAction()

        if (response.couponListRecommendation.data.emptyState.title.isEmpty() &&
                response.couponListRecommendation.data.emptyState.description.isEmpty() &&
                response.couponListRecommendation.data.emptyState.imageUrl.isEmpty()) {
            handleEmptyStateDataNotAvailable(attemptedPromoCode, response)
        } else {
            setFragmentStateStopLoading()
            handleEmptyStateDataAvailable(attemptedPromoCode, response)
        }

        setPromoInputErrorIfAny(response)
    }

    private fun handleEmptyStateDataNotAvailable(attemptedPromoCode: String, response: CouponListRecommendationResponse) {
        if (attemptedPromoCode.isNotBlank()) {
            getPromoListResponseAction.value?.let {
                it.state = GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR
                it.exception = PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString { ". " })
                _getPromoListResponseAction.value = it
            }
            PromoCheckoutLogger.logOnErrorLoadPromoCheckoutPage(PromoErrorException(response.couponListRecommendation.data.resultStatus.message.joinToString { ". " }))
        } else {
            PromoCheckoutLogger.logOnErrorLoadPromoCheckoutPage(PromoErrorException("response status ok but data is empty"))
            val exception = PromoErrorException()
            setFragmentStateLoadPromoListFailed(exception)
            sendAnalyticsOnErrorAttemptPromo(attemptedPromoCode, exception.message ?: "")
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

    private fun handleShowErrorPage(errorPage: ErrorPage) {
        setFragmentStateStopLoading()
        val errorState = uiModelMapper.mapErrorState(errorPage)
        _promoErrorStateUiModel.value = errorState
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

    private fun sendAnalyticsOnErrorAttemptPromo(promoCode: String, errorMessage: String) {
        if (promoCode.isNotBlank()) {
            analytics.eventViewErrorAfterClickTerapkanPromo(getPageSource(), errorMessage, 0, promoCode)
        }
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
            }
        }
        return tmpHasPreSelectedPromo
    }

    private fun setPromoInputErrorIfAny(response: CouponListRecommendationResponse) {
        val attemptedPromoCodeError = response.couponListRecommendation.data.attemptedPromoCodeError
        if (attemptedPromoCodeError.code.isNotBlank() && attemptedPromoCodeError.message.isNotBlank()) {
            sendAnalyticsOnErrorAttemptPromo(attemptedPromoCodeError.code, attemptedPromoCodeError.message)
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
        initPromoInput()

        // Get all pre selected promo
        val preSelectedPromoList = getAllPreSelectedPromo(response)

        // Initialize coupon section
        val couponList = ArrayList<Visitable<*>>()
        var headerIdentifierId = 0
        response.couponListRecommendation.data.couponSections.forEach { couponSectionItem ->
            // Initialize eligibility header
            val eligibilityHeader = uiModelMapper.mapPromoEligibilityHeaderUiModel(couponSectionItem)
            couponList.add(eligibilityHeader)

            if (eligibilityHeader.uiState.isEnabled) {
                // Initialize promo recommendation
                val promoRecommendation = response.couponListRecommendation.data.promoRecommendation
                if (promoRecommendation.codes.isNotEmpty()) {
                    val promoRecommendationUiModel = uiModelMapper.mapPromoRecommendationUiModel(response.couponListRecommendation)
                    _promoRecommendationUiModel.value = promoRecommendationUiModel
                    couponList.add(promoRecommendationUiModel)
                }

                // Initialize promo tab
                val promoTabUiModel = uiModelMapper.mapPromoTabsUiModel(response.couponListRecommendation.data.sectionTabs)
                _promoTabUiModel.value = promoTabUiModel
                couponList.add(promoTabUiModel)
            }

            // Initialize promo list header
            val tmpIneligiblePromoList = ArrayList<Visitable<*>>()
            couponSectionItem.subSections.forEach { couponSubSection ->
                val promoHeader = uiModelMapper.mapPromoListHeaderUiModel(
                        couponSubSection, couponSectionItem, headerIdentifierId, couponSectionItem.isEnabled
                )

                if (eligibilityHeader.uiState.isEnabled) {
                    couponList.add(promoHeader)
                } else {
                    tmpIneligiblePromoList.add(promoHeader)
                }
                headerIdentifierId++

                // Initialize promo list item
                couponSubSection.coupons.forEachIndexed { index, couponItem ->
                    if (couponItem.isGroupHeader) {
                        val promoItem = uiModelMapper.mapPromoListItemUiModel(
                                couponItem, couponSubSection, couponSectionItem,
                                promoHeader.uiData.identifierId, preSelectedPromoList, index
                        )
                        if (eligibilityHeader.uiState.isEnabled) {
                            couponList.add(promoItem)
                        } else {
                            tmpIneligiblePromoList.add(promoItem)
                        }
                    }
                }
            }

            if (tmpIneligiblePromoList.isNotEmpty()) {
                couponList.addAll(tmpIneligiblePromoList)
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

    private fun initPromoInput() {
        // Initialize promo input model
        val promoInputUiModel = uiModelMapper.mapPromoInputUiModel()
        _promoInputUiModel.value = promoInputUiModel
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

    fun applyPromo(validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        // Set request data
        setApplyPromoRequestData(validateUsePromoRequest)

        // Get all current selected promo (highlighted as green)
        val selectedPromoList = getSelectedPromoList()

        // Remove invalid promo code
        // Invalid promo code is promo code from outside promo page (cart/checkout) which previously selected,
        // but become invalid or not selected on promo page, except promo BBO
        removeInvalidPromoCode(validateUsePromoRequest, selectedPromoList, bboPromoCodes)

        validateUsePromoRequest.skipApply = 0

        // Get response data
        PromoCheckoutIdlingResource.increment()
        validateUseUseCase.setParam(validateUsePromoRequest)
        validateUseUseCase.execute(
                onSuccess = {
                    PromoCheckoutIdlingResource.decrement()
                    onSuccessValidateUse(it, selectedPromoList, validateUsePromoRequest)
                },
                onError = {
                    PromoCheckoutIdlingResource.decrement()
                    onErrorValidateUse(it, selectedPromoList)
                }
        )
    }

    private fun onErrorValidateUse(throwable: Throwable, selectedPromoList: ArrayList<String>) {
        PromoCheckoutLogger.logOnErrorApplyPromo(throwable)
        // Notify fragment apply promo to stop loading
        setApplyPromoStateFailed(throwable, selectedPromoList)
        sendAnalyticsOnErrorApplyPromo(throwable, selectedPromoList)
    }

    private fun sendAnalyticsOnErrorApplyPromo(throwable: Throwable, promoCodeList: ArrayList<String>) {
        var errorMessage = throwable.message
        if (errorMessage.isNullOrBlank()) {
            errorMessage = fragmentUiModel.value?.uiData?.defaultErrorMessage ?: ""
        }
        promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && promoCodeList.contains(it.uiData.promoCode)) {
                analytics.eventViewErrorAfterClickPakaiPromo(getPageSource(), it.uiData.promoId, errorMessage)
            }
        }
    }

    private fun onSuccessValidateUse(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
                                     selectedPromoList: ArrayList<String>,
                                     validateUsePromoRequest: ValidateUsePromoRequest) {
        if (validateUsePromoRevampUiModel.status == "OK") {
            // Initialize response action state
            initApplyPromoResponseAction()

            // Response is OK, then need to check whether it's apply promo manual or apply checked promo items
            if (validateUsePromoRevampUiModel.promoUiModel.clashingInfoDetailUiModel.isClashedPromos) {
                // Promo is clashing. Need to reload promo page
                setApplyPromoStateClashing()
            } else {
                if (validateUsePromoRevampUiModel.promoUiModel.globalSuccess) {
                    handleApplyPromoSuccess(selectedPromoList, validateUsePromoRevampUiModel, validateUsePromoRequest)
                } else {
                    handleApplyPromoFailed(selectedPromoList, validateUsePromoRevampUiModel)
                }
            }
        } else {
            // Response is not OK, need to show error message
            val exception = PromoErrorException(validateUsePromoRevampUiModel.message.joinToString(". "))
            PromoCheckoutLogger.logOnErrorApplyPromo(exception)
            // Notify fragment apply promo to stop loading
            setApplyPromoStateFailed(exception, selectedPromoList)
            sendAnalyticsOnErrorApplyPromo(exception, selectedPromoList)
        }
    }

    private fun removeInvalidPromoCode(validateUsePromoRequest: ValidateUsePromoRequest,
                                       selectedPromoList: ArrayList<String>,
                                       bboPromoCodes: ArrayList<String>) {
        val invalidPromoCodes = ArrayList<String>()
        validateUsePromoRequest.codes.forEach { promoGlobalCode ->
            if (!selectedPromoList.contains(promoGlobalCode)) {
                invalidPromoCodes.add(promoGlobalCode)
            }
        }
        validateUsePromoRequest.orders.forEach { order ->
            order.codes.forEach { promoCode ->
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
                if (order.codes.contains(invalidPromoCode)) {
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
            }
        }

        return selectedPromoList
    }

    private fun handleApplyPromoSuccess(selectedPromoList: ArrayList<String>,
                                        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
                                        request: ValidateUsePromoRequest) {
        val responseValidatePromo = validateUsePromoRevampUiModel.promoUiModel

        // Check promo global is success
        var isGlobalSuccess = false
        if (responseValidatePromo.messageUiModel.state != "red") {
            isGlobalSuccess = true
        }

        // Check all promo merchant is success
        // Update : This logic might be unnecessary,
        // since if global success is true, all voucher order status shoulbe success
        var successCount = 0
        responseValidatePromo.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.success) {
                successCount++
            } else {
                // If one of promo merchant is error, then show error message
                val exception = PromoErrorException(voucherOrder.messageUiModel.text)
                PromoCheckoutLogger.logOnErrorApplyPromo(exception)
                // Notify fragment apply promo to stop loading
                setApplyPromoStateFailed(exception, selectedPromoList)
                sendAnalyticsOnErrorApplyPromo(exception, selectedPromoList)
            }
        }

        if (isGlobalSuccess || successCount == responseValidatePromo.voucherOrderUiModels.size) {
            var selectedRecommendationCount = 0
            promoRecommendationUiModel.value?.uiData?.promoCodes?.forEach {
                if (selectedPromoList.contains(it)) selectedRecommendationCount++
            }
            val promoRecommendationCount = promoRecommendationUiModel.value?.uiData?.promoCodes?.size
                    ?: 0
            val status = if (selectedPromoList.size == promoRecommendationCount && selectedRecommendationCount == promoRecommendationCount) 1 else 0
            analytics.eventClickPakaiPromoSuccess(getPageSource(), status.toString(), selectedPromoList)
            // If all promo merchant are success, then navigate to cart
            setApplyPromoStateSuccess(request, validateUsePromoRevampUiModel)
        }
    }

    private fun handleApplyPromoFailed(selectedPromoList: ArrayList<String>, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val responseValidatePromo = validateUsePromoRevampUiModel.promoUiModel
        val redStateMap = HashMap<String, String>()
        if (!responseValidatePromo.success) {
            // Error promo global
            if (responseValidatePromo.codes.isNotEmpty()) {
                responseValidatePromo.codes.forEach {
                    if (!redStateMap.containsKey(it)) {
                        redStateMap[it] = responseValidatePromo.messageUiModel.text
                    }
                }
            }
        } else {
            // Error promo merchant
            if (responseValidatePromo.voucherOrderUiModels.isNotEmpty()) {
                responseValidatePromo.voucherOrderUiModels.forEach {
                    if (!it.success && it.code.isNotBlank() && !redStateMap.containsKey(it.code)) {
                        redStateMap[it.code] = it.messageUiModel.text
                    }
                }
            }
        }

        if (redStateMap.isNotEmpty()) {
            promoListUiModel.value?.forEach { visitable ->
                if (visitable is PromoListItemUiModel) {
                    setPromoItemDisabled(redStateMap, visitable)
                }
            }
            calculateAndRenderTotalBenefit()
            val exception = PromoErrorException(responseValidatePromo.additionalInfoUiModel.errorDetailUiModel.message)
            PromoCheckoutLogger.logOnErrorApplyPromo(exception)
            // Notify fragment apply promo to stop loading
            setApplyPromoStateFailed(exception, selectedPromoList)
            sendAnalyticsOnErrorApplyPromo(exception, selectedPromoList)
        } else {
            // Voucher global is empty and voucher orders are empty but the response is OK
            // This section is added as fallback mechanism
            PromoCheckoutLogger.logOnErrorApplyPromo(PromoErrorException("response is ok but got empty applied promo"))
            val exception = PromoErrorException()
            // Notify fragment apply promo to stop loading
            setApplyPromoStateFailed(exception, selectedPromoList)
            sendAnalyticsOnErrorApplyPromo(exception, selectedPromoList)
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

    private fun setApplyPromoStateSuccess(request: ValidateUsePromoRequest, response: ValidateUsePromoRevampUiModel) {
        applyPromoResponseAction.value?.let {
            it.state = ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE
            it.data = response
            it.lastValidateUseRequest = request
            _applyPromoResponseAction.value = it
        }
    }

    private fun setApplyPromoStateFailed(throwable: Throwable, promoCodeList: List<String>) {
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

    fun clearPromo(validateUsePromoRequest: ValidateUsePromoRequest, bboPromoCodes: ArrayList<String>) {
        val toBeRemovedPromoCodes = getToBeClearedPromoCodes(validateUsePromoRequest, bboPromoCodes)

        PromoCheckoutIdlingResource.increment()
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, toBeRemovedPromoCodes, validateUsePromoRequest.cartType == PARAM_OCC_MULTI)
        clearCacheAutoApplyStackUseCase.execute(
                onSuccess = {
                    PromoCheckoutIdlingResource.decrement()
                    onSuccessClearPromo(it, toBeRemovedPromoCodes, validateUsePromoRequest)
                },
                onError = {
                    PromoCheckoutIdlingResource.decrement()
                    setClearPromoStateFailed(it)
                }
        )
    }

    private fun onSuccessClearPromo(clearPromoUiModel: ClearPromoUiModel, toBeRemovedPromoCodes: ArrayList<String>, validateUsePromoRequest: ValidateUsePromoRequest) {
        // Initialize response action state
        initClearPromoResponseAction()

        if (clearPromoUiModel.successDataModel.success) {
            // Remove promo code on validate use params after clear promo success
            toBeRemovedPromoCodes.forEach { promo ->
                if (validateUsePromoRequest.codes.contains(promo)) {
                    validateUsePromoRequest.codes.remove(promo)
                }

                validateUsePromoRequest.orders.forEach {
                    if (it.codes.contains(promo)) {
                        it.codes.remove(promo)
                    }
                }
            }
            setClearPromoStateSuccess(clearPromoUiModel, validateUsePromoRequest)
        } else {
            val exception = PromoErrorException()
            setClearPromoStateFailed(exception)
        }
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
            if (!bboPromoCodes.contains(promoGlobalCode) && !toBeRemovedPromoCodes.contains(promoGlobalCode)) {
                toBeRemovedPromoCodes.add(promoGlobalCode)
            }
        }

        validateUsePromoRequest.orders.forEach { order ->
            order.codes.forEach {
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
            }
        }
    }

    private fun setClearPromoStateSuccess(clearPromoUiModel: ClearPromoUiModel, tmpValidateUsePromoRequest: ValidateUsePromoRequest) {
        clearPromoResponse.value?.let {
            it.state = ClearPromoResponseAction.ACTION_STATE_SUCCESS
            it.data = clearPromoUiModel
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

    fun getPromoSuggestion() {
        // Get response
        PromoCheckoutIdlingResource.increment()
        getPromoSuggestionUseCase.execute(
                onSuccess = {
                    PromoCheckoutIdlingResource.decrement()
                    onSuccessGetPromoSuggestion(it)
                },
                onError = {
                    PromoCheckoutIdlingResource.decrement()
                    onErrorGetPromoSuggestion()
                }
        )
    }

    private fun onSuccessGetPromoSuggestion(response: GetPromoSuggestionResponse) {
        // Initialize response action state
        if (getPromoSuggestionResponse.value == null) {
            _getPromoSuggestionResponse.value = GetPromoSuggestionAction()
        }

        if (response.promoSuggestion.promoHistory.isNotEmpty()) {
            // Remove promo code on validate use params after clear promo success
            getPromoSuggestionResponse.value?.let {
                it.state = GetPromoSuggestionAction.ACTION_SHOW
                it.data = uiModelMapper.mapPromoSuggestionResponse(response)
                _getPromoSuggestionResponse.value = it
            }
        } else {
            onErrorGetPromoSuggestion()
        }
    }

    private fun onErrorGetPromoSuggestion() {
        getPromoSuggestionResponse.value?.let {
            it.state = GetPromoSuggestionAction.ACTION_RELEASE_LOCK_FLAG
            _getPromoSuggestionResponse.value = it
        }
    }

    //---------------------//
    /* UI Callback Section */
    //---------------------//

    fun initFragmentUiModel(pageSource: Int, defaultErrorMessage: String) {
        val fragmentUiModel = uiModelMapper.mapFragmentUiModel(pageSource, defaultErrorMessage)
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
                it.uiState.isSelected = false
                if (it.uiData.currentClashingPromo.isNotEmpty()) {
                    it.uiData.currentClashingPromo.clear()
                    it.uiData.errorMessage = ""
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

    fun updatePromoListAfterClickPromoItem(element: PromoListItemUiModel) {
        // Set to selected / un selected
        val itemIndex = promoListUiModel.value?.indexOf(element)
        if (itemIndex == -1) return
        itemIndex?.let {
            // Get the promo item data and set inverted selected value
            val promoItem = promoListUiModel.value?.get(it) as PromoListItemUiModel
            promoItem.uiState.isSelected = !promoItem.uiState.isSelected
            promoItem.uiState.isUpdateSelectionStateAction = true

            if (!promoItem.uiState.isSelected && promoItem.uiState.isRecommended) {
                resetRecommendedPromo()
            }

            // Update view
            _tmpUiModel.value = Update(promoItem)

            // Send tracker
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

            // Update header sub total and sibling check uncheck state
            updateHeaderAndSiblingState(promoItem, element)

            // Perform clash calculation
            calculateClash(promoItem)

            // Calculate total benefit
            calculateAndRenderTotalBenefit()
        }
    }

    private fun updateHeaderAndSiblingState(promoItem: PromoListItemUiModel, element: PromoListItemUiModel) {
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
                            calculateClash(tmpPromoItem)
                            break
                        }
                    }
                }

                updateResetButtonState()
            }
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
                        analytics.eventClickPilihOnRecommendation(getPageSource(), it.uiData.promoCode, it.uiState.isCausingOtherPromoClash)
                    }
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

    fun updatePromoInputStateBeforeApplyPromo(promoCode: String, isFromSuggestion: Boolean) {
        analytics.eventClickTerapkanPromo(getPageSource(), promoCode)
        analytics.eventClickTerapkanAfterTypingPromoCode(getPageSource(), promoCode, isFromSuggestion)
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
                }
            }
        }

        return false
    }

    fun setPromoInputFromLastApply(promoCode: String) {
        analytics.eventClickPromoLastSeenItem(getPageSource(), promoCode)
        promoInputUiModel.value?.let {
            it.uiState.isValidSuggestionPromo = true
            it.uiData.validSuggestionPromoCode = promoCode
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
                    if (it.uiData.clashingInfos.isNotEmpty()) {
                        val tmpClashResult = checkAndSetClashOnSelectionEvent(it, selectedItem)
                        if (!clashResult) clashResult = tmpClashResult
                        _tmpUiModel.value = Update(it)
                    }
                }
            }
        } else {
            // Calculate clash on un selection event
            promoListUiModel.value?.forEach {
                if (it is PromoListItemUiModel && it.uiData.promoCode != selectedItem.uiData.promoCode) {
                    if (it.uiData.clashingInfos.isNotEmpty()) {
                        checkAndSetClashOnUnSelectionEvent(it, selectedItem)
                        _tmpUiModel.value = Update(it)
                    }
                }
            }
        }

        if (clashResult) {
            selectedItem.uiState.isCausingOtherPromoClash = true
        }
    }

    private fun checkAndSetClashOnUnSelectionEvent(promoListItemUiModel: PromoListItemUiModel, selectedItem: PromoListItemUiModel) {
        val clashingInfo = promoListItemUiModel.uiData.clashingInfos.firstOrNull { clashingInfo -> clashingInfo.code == selectedItem.uiData.promoCode }
        if (clashingInfo != null) {
            if (promoListItemUiModel.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                promoListItemUiModel.uiData.currentClashingPromo.remove(selectedItem.uiData.promoCode)
                if (promoListItemUiModel.uiData.currentClashingPromo.isNotEmpty()) {
                    val errorMessageBuilder = StringBuilder()
                    promoListItemUiModel.uiData.currentClashingPromo.forEach { promoCode ->
                        val tmpClashingInfo = promoListItemUiModel.uiData.clashingInfos.firstOrNull { tmpClashingInfo -> tmpClashingInfo.code == promoCode }
                        if (tmpClashingInfo != null) {
                            errorMessageBuilder.append(tmpClashingInfo.message)
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
        val clashingInfo = promoListItemUiModel.uiData.clashingInfos.firstOrNull { clashingInfo -> clashingInfo.code == selectedItem.uiData.promoCode }
        if (clashingInfo != null) {
            if (!promoListItemUiModel.uiData.currentClashingPromo.contains(selectedItem.uiData.promoCode)) {
                promoListItemUiModel.uiData.currentClashingPromo.add(selectedItem.uiData.promoCode)
                val errorMessageBuilder = StringBuilder(promoListItemUiModel.uiData.errorMessage)
                if (promoListItemUiModel.uiData.errorMessage.isNotBlank()) {
                    errorMessageBuilder.clear()
                }
                errorMessageBuilder.append(clashingInfo.message)
                promoListItemUiModel.uiData.errorMessage = errorMessageBuilder.toString()
                clashResult = true
            }
        }
        return clashResult
    }

    fun changeSelectedTab(promoTabUiModel: PromoTabUiModel) {
        _promoTabUiModel.value = promoTabUiModel
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