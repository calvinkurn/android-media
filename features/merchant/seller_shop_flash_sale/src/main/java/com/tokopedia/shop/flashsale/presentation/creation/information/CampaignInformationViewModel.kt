package com.tokopedia.shop.flashsale.presentation.creation.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.flashsale.common.constant.Constant.CAMPAIGN_NOT_CREATED_ID
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR
import com.tokopedia.shop.flashsale.common.extension.hourOnly
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CampaignInformationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val dateManager: DateManager,
    private val tracker: ShopFlashSaleTracker
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MIN_HEX_COLOR_LENGTH = 6
        private const val MIN_CAMPAIGN_NAME_LENGTH = 5
        private const val ONE_HOUR = 1
    }

    private val _currentMonthRemainingQuota = MutableLiveData<Result<Int>>()
    val currentMonthRemainingQuota: LiveData<Result<Int>>
        get() = _currentMonthRemainingQuota

    private val _areInputValid = SingleLiveEvent<ValidationResult>()
    val areInputValid: LiveData<ValidationResult>
        get() = _areInputValid

    private val _campaignCreation = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignCreation: LiveData<Result<CampaignCreationResult>>
        get() = _campaignCreation

    private val _campaignUpdate = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignUpdate: LiveData<Result<CampaignCreationResult>>
        get() = _campaignUpdate

    private val _campaignDetail= MutableLiveData<Result<CampaignUiModel>>()
    val campaignDetail: LiveData<Result<CampaignUiModel>>
        get() = _campaignDetail

    private val _saveDraft= MutableLiveData<Result<CampaignCreationResult>>()
    val saveDraft: LiveData<Result<CampaignCreationResult>>
        get() = _saveDraft

    private val _campaignQuota = MutableLiveData<Result<Int>>()
    val campaignQuota: LiveData<Result<Int>>
        get() = _campaignQuota

    private var selectedColor = defaultGradientColor
    private var selectedStartDate = Date()
    private var selectedEndDate = Date()
    private var showTeaser = true
    private var paymentType = PaymentType.INSTANT
    private var remainingQuota = Int.ZERO
    private var selection : Selection? = null
    private var campaignId: Long = CAMPAIGN_NOT_CREATED_ID
    private var relatedCampaigns: List<RelatedCampaign> = emptyList()
    private var isCampaignRuleSubmit = false

    private val forbiddenWords = listOf(
        "kejar diskon",
        "flash sale",
        "flashsale",
        "toped",
        "tokopedia",
        "tkpd",
        "shopee",
        "bukalapak",
        "lazada"
    )

    sealed class CampaignNameValidationResult {
        object CampaignNameIsEmpty : CampaignNameValidationResult()
        object CampaignNameBelowMinCharacter : CampaignNameValidationResult()
        object CampaignNameHasForbiddenWords : CampaignNameValidationResult()
        object Valid : CampaignNameValidationResult()
    }

    sealed class ValidationResult {
        object LapsedStartDate : ValidationResult()
        object LapsedTeaserStartDate : ValidationResult()
        object NoRemainingQuota : ValidationResult()
        object InvalidHexColor : ValidationResult()
        object Valid : ValidationResult()
    }


    data class Selection(
        val campaignName: String,
        val startDate: Date,
        val endDate: Date,
        val showTeaser: Boolean,
        val teaserDate: Date,
        val firstColor: String,
        val secondColor: String,
        val paymentType: PaymentType,
        val remainingQuota: Int
    )

    fun validateCampaignName(campaignName: String) : CampaignNameValidationResult {
        if (campaignName.isEmpty()) {
            return CampaignNameValidationResult.CampaignNameIsEmpty
        }

        if (campaignName.length < MIN_CAMPAIGN_NAME_LENGTH) {
            return CampaignNameValidationResult.CampaignNameBelowMinCharacter

        }

        if (campaignName.lowercase() in forbiddenWords) {
            return  CampaignNameValidationResult.CampaignNameHasForbiddenWords

        }

        return CampaignNameValidationResult.Valid
    }

    fun validateInput(mode: PageMode, selection: Selection, now: Date) {
        tracker.sendClickButtonProceedOnCampaignInfoPageEvent()

        if (mode == PageMode.CREATE && selection.remainingQuota == Int.ZERO) {
            _areInputValid.value = ValidationResult.NoRemainingQuota
            return
        }


        if (now.after(selection.startDate)) {
            _areInputValid.value = ValidationResult.LapsedStartDate
            return
        }

        if (selection.showTeaser && now.after(selection.teaserDate)) {
            _areInputValid.value = ValidationResult.LapsedTeaserStartDate
            return
        }

        if (selection.firstColor.length < MIN_HEX_COLOR_LENGTH || selection.secondColor.length < MIN_HEX_COLOR_LENGTH) {
            _areInputValid.value = ValidationResult.InvalidHexColor
            return
        }

        _areInputValid.value = ValidationResult.Valid
    }


    fun getCurrentMonthRemainingQuota() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignAttribute = getSellerCampaignAttributeUseCase.execute(
                    month = dateManager.getCurrentMonth(),
                    year = dateManager.getCurrentYear()
                )

                _currentMonthRemainingQuota.postValue(Success(campaignAttribute.remainingCampaignQuota))
            },
            onError = { error ->
                _currentMonthRemainingQuota.postValue(Fail(error))
            }
        )

    }

    fun submit(selection: Selection) {
        if (campaignId == CAMPAIGN_NOT_CREATED_ID) {
            createCampaign(selection)
        } else {
            updateCampaign(selection, campaignId)
        }
    }

    private fun createCampaign(selection: Selection) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param =
                    DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Create,
                        selection.campaignName,
                        selection.startDate,
                        selection.endDate,
                        selection.teaserDate,
                        showTeaser = selection.showTeaser,
                        firstColor = selection.firstColor,
                        secondColor = selection.secondColor,
                        paymentType = selection.paymentType
                    )
                val result = doSellerCampaignCreationUseCase.execute(param)
                _campaignCreation.postValue(Success(result))

                if (result.isSuccess) {
                    campaignId = result.campaignId
                }
            },
            onError = { error ->
                _campaignCreation.postValue(Fail(error))
            }
        )

    }

    private fun updateCampaign(selection: Selection, campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Update(campaignId),
                        selection.campaignName,
                        selection.startDate,
                        selection.endDate,
                        selection.teaserDate,
                        showTeaser = selection.showTeaser,
                        firstColor = selection.firstColor,
                        secondColor = selection.secondColor,
                        paymentType = selection.paymentType,
                        campaignRelation = relatedCampaigns.map { it.id },
                        isCampaignRuleSubmit = isCampaignRuleSubmit,
                )
                val result = doSellerCampaignCreationUseCase.execute(param)
                _campaignUpdate.postValue(Success(result))
            },
            onError = { error ->
                _campaignUpdate.postValue(Fail(error))
            }
        )

    }

    fun saveDraft(mode: PageMode, campaignId: Long, selection: Selection) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action = if (mode == PageMode.CREATE) {
                    CampaignAction.Create
                } else {
                    CampaignAction.Update(campaignId)
                }

                val param = DoSellerCampaignCreationUseCase.Param(
                    action,
                    selection.campaignName,
                    selection.startDate,
                    selection.endDate,
                    selection.teaserDate,
                    showTeaser = selection.showTeaser,
                    firstColor = selection.firstColor,
                    secondColor = selection.secondColor,
                    paymentType = selection.paymentType,
                    campaignRelation = relatedCampaigns.map { it.id },
                    isCampaignRuleSubmit = isCampaignRuleSubmit,
                )
                val result = doSellerCampaignCreationUseCase.execute(param)
                _saveDraft.postValue(Success(result))
            },
            onError = { error ->
                _saveDraft.postValue(Fail(error))
            }
        )

    }


    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignDetailUseCase.execute(campaignId)
                relatedCampaigns = result.relatedCampaigns
                isCampaignRuleSubmit = result.isCampaignRuleSubmit
                _campaignDetail.postValue(Success(result))
            },
            onError = { error ->
                _campaignDetail.postValue(Fail(error))
            }
        )

    }

    fun getCampaignQuota(month : Int, year: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignAttribute = getSellerCampaignAttributeUseCase.execute(
                    month = month,
                    year = year
                )

                _campaignQuota.postValue(Success(campaignAttribute.remainingCampaignQuota))
            },
            onError = { error ->
                _campaignQuota.postValue(Fail(error))
            }
        )

    }

    fun setCampaignId(campaignId: Long) {
        this.campaignId = campaignId
    }

    fun setPaymentType(paymentType : PaymentType) {
        this.paymentType = paymentType
    }

    fun getPaymentType(): PaymentType {
        return paymentType
    }

    fun setSelectedColor(gradient: Gradient) {
        selectedColor = gradient
    }

    fun getColor(): Gradient {
        return selectedColor
    }

    fun setSelectedStartDate(selectedStartDate: Date) {
        this.selectedStartDate = selectedStartDate
    }

    fun getSelectedStartDate(): Date {
        return selectedStartDate
    }

    fun setSelectedEndDate(selectedEndDate: Date) {
        this.selectedEndDate = selectedEndDate
    }

    fun getSelectedEndDate(): Date {
        return selectedEndDate
    }

    fun setRemainingQuota(remainingQuota : Int) {
        this.remainingQuota = remainingQuota
    }

    fun getRemainingQuota(): Int {
        return remainingQuota
    }

    fun setShowTeaser(showTeaser: Boolean) {
        this.showTeaser = showTeaser
    }

    fun storeAsDefaultSelection(selection : Selection) {
        this.selection = selection
    }

    fun getDefaultSelection(): Selection? {
        return this.selection
    }

    fun markColorAsSelected(selectedGradient: Gradient, gradients: List<Gradient>): List<Gradient> {
        return gradients.map { gradient ->
            if (gradient.first == selectedGradient.first && gradient.second == selectedGradient.second) {
                gradient.copy(isSelected = true)
            } else {
                gradient.copy(isSelected = false)
            }
        }
    }

    fun deselectAllColor(gradients: List<Gradient>): List<Gradient> {
        return gradients.map { gradient -> gradient.copy(isSelected = false) }
    }

    fun normalizeEndDate(endDate: Date, startDate: Date): Date {
        return if (endDate.before(startDate)) {
            startDate
        } else {
            endDate
        }
    }

    fun getTeaserQuantityEditorMaxValue(startDate: Date, now: Date): Int {
        val startDateWithoutMinute = startDate.hourOnly()
        val nowWithoutMinute = now.hourOnly()
        val differenceInMillis = startDateWithoutMinute.time - nowWithoutMinute.time
        val hourDifference = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
        val adjustedHourDifference = (hourDifference - ONE_HOUR).toInt()
        return when {
            adjustedHourDifference > CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR -> CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
            adjustedHourDifference <= CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR -> CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR
            else -> adjustedHourDifference
        }
    }

    fun isUsingHexColor(firstColor : String, secondColor : String) : Boolean {
        return firstColor == secondColor
    }

    fun findUpcomingTimeDifferenceInHour(startDate : Date, upcomingDate : Date) : Int {
        val differenceInMillis = startDate.time - upcomingDate.time
        val differenceInHour = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
        return differenceInHour.toInt()
    }

    fun isDataChanged(previousData: Selection, currentData: Selection): Boolean {
        return previousData != currentData
    }
}