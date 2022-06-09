package com.tokopedia.shop.flashsale.presentation.creation.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR
import com.tokopedia.shop.flashsale.common.extension.advanceMinuteBy
import com.tokopedia.shop.flashsale.common.extension.hourOnly
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CampaignInformationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MIN_CAMPAIGN_NAME_LENGTH = 5
        private const val THIRTY_MINUTE = 30
        private const val ONE_HOUR = 1
    }

    private val _areInputValid = SingleLiveEvent<ValidationResult>()
    val areInputValid: LiveData<ValidationResult>
        get() = _areInputValid

    private val _campaignCreation = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignCreation: LiveData<Result<CampaignCreationResult>>
        get() = _campaignCreation

    private val _campaignUpdate = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignUpdate: LiveData<Result<CampaignCreationResult>>
        get() = _campaignUpdate

    private val _campaignName = MutableLiveData<CampaignNameValidationResult>()
    val campaignName: LiveData<CampaignNameValidationResult>
        get() = _campaignName

    private var selection: Selection? = null
    private var selectedColor: Gradient? = null
    private var selectedStartDate = Date()
    private var selectedEndDate = Date()
    private var showTeaser = false

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
        object ExceedMaxOverlappedCampaign : ValidationResult()
        object LapsedTeaserStartDate : ValidationResult()
        object Valid : ValidationResult()
    }


    data class Selection(
        val campaignName: String,
        val startDate: Date,
        val endDate: Date,
        val showTeaser: Boolean,
        val teaserDate: Date?,
        val firstColor: String,
        val secondColor: String
    )

    fun onCampaignNameChange(campaignName: String) {
        if (campaignName.isEmpty()) {
            _campaignName.value = CampaignNameValidationResult.CampaignNameIsEmpty
            return
        }

        if (campaignName.length < MIN_CAMPAIGN_NAME_LENGTH) {
            _campaignName.value = CampaignNameValidationResult.CampaignNameBelowMinCharacter
            return
        }

        if (campaignName in forbiddenWords) {
            _campaignName.value = CampaignNameValidationResult.CampaignNameHasForbiddenWords
            return
        }

        _campaignName.value = CampaignNameValidationResult.Valid
    }

    fun onNextButtonPressed(selection: Selection, now: Date) {
        this.selection = selection

        val teaserDate = selection.teaserDate ?: selection.startDate

        if (selection.showTeaser && teaserDate.after(now)) {
            _areInputValid.value = ValidationResult.LapsedTeaserStartDate
            return
        }

        if (selection.startDate.after(now)) {
            _areInputValid.value = ValidationResult.LapsedStartDate
            return
        }

        _areInputValid.value = ValidationResult.Valid
    }

    fun createCampaign(selection: Selection) {
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
                        firstColor = selection.firstColor,
                        secondColor = selection.secondColor
                    )
                val result = doSellerCampaignCreationUseCase.execute(param)
                _campaignCreation.postValue(Success(result))
            },
            onError = { error ->
                _campaignCreation.postValue(Fail(error))
            }
        )

    }

    fun updateCampaign(selection: Selection, campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param =
                    DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Update(campaignId),
                        selection.campaignName,
                        selection.startDate,
                        selection.endDate,
                        selection.teaserDate,
                        firstColor = selection.firstColor,
                        secondColor = selection.secondColor
                    )
                val result = doSellerCampaignCreationUseCase.execute(param)
                _campaignUpdate.postValue(Success(result))
            },
            onError = { error ->
                _campaignUpdate.postValue(Fail(error))
            }
        )

    }

    fun getSelection(): Selection? {
        return selection
    }

    fun setSelectedColor(gradient: Gradient) {
        selectedColor = gradient
    }

    fun getColor(): Gradient? {
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

    fun setShowTeaser(showTeaser: Boolean) {
        this.showTeaser = showTeaser
    }

    fun isUsingTeaser(): Boolean {
        return showTeaser
    }

    fun markAsSelected(selectedGradient: Gradient, gradients: List<Gradient>): List<Gradient> {
        return gradients.map { gradient ->
            if (gradient == selectedGradient) {
                gradient.copy(isSelected = true)
            } else {
                gradient.copy(isSelected = false)
            }
        }
    }

    fun deselectAll(gradients: List<Gradient>): List<Gradient> {
        return gradients.map { gradient -> gradient.copy(isSelected = false) }
    }

    fun normalizeEndDate(endDate: Date, startDate: Date): Date {
        return if (endDate.before(startDate)) {
            startDate.advanceMinuteBy(THIRTY_MINUTE)
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
}