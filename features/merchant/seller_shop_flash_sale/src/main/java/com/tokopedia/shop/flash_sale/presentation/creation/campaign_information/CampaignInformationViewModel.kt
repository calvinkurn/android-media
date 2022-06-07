package com.tokopedia.shop.flash_sale.presentation.creation.campaign_information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.CampaignAction
import com.tokopedia.shop.flash_sale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flash_sale.domain.entity.Gradient
import com.tokopedia.shop.flash_sale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flash_sale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.*
import javax.inject.Inject

class CampaignInformationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MIN_CAMPAIGN_NAME_LENGTH = 5
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

    private var selectedColor: Color? = null

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

    sealed class Color {
        data class GradientColor(val gradient: Gradient) : Color()
        data class HexColor(val hexColor: String) : Color()
    }

    sealed class ValidationResult {
        object CampaignNameIsEmpty : ValidationResult()
        object CampaignNameBelowMinCharacter : ValidationResult()
        object CampaignNameHasForbiddenWords : ValidationResult()
        object LapsedStartDate : ValidationResult()
        object ExceedMaxOverlappedCampaign : ValidationResult()
        object LapsedTeaserStartDate : ValidationResult()
        object Valid : ValidationResult()
    }

    fun setSelectedColor(selectedColor: Color) {
        this.selectedColor = selectedColor
    }


    data class Selection(
        val campaignName: String,
        val startDate: Date,
        val endDate: Date,
        val showTeaser: Boolean,
        val teaserDate: Date,
        val firstColor : String,
        val secondColor : String
    )

    fun validateInput(selection: Selection) {
        val now = Date()

        if (selection.campaignName.isEmpty()) {
            _areInputValid.value = ValidationResult.CampaignNameIsEmpty
            return
        }

        if (selection.campaignName.length < MIN_CAMPAIGN_NAME_LENGTH) {
            _areInputValid.value = ValidationResult.CampaignNameBelowMinCharacter
            return
        }

        if (selection.campaignName in forbiddenWords) {
            _areInputValid.value = ValidationResult.CampaignNameHasForbiddenWords
            return
        }

        if (selection.showTeaser && selection.startDate.after(now)) {
            _areInputValid.value = ValidationResult.LapsedTeaserStartDate
            return
        }

        if (selection.startDate.after(now)) {
            _areInputValid.value = ValidationResult.LapsedStartDate
            return
        }

        _areInputValid.value = ValidationResult.Valid
    }

    fun createCampaign(selection : Selection) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param =
                    DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Create,
                        selection.campaignName,
                        selection.startDate,
                        selection.endDate,
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

    fun updateCampaign(selection : Selection, campaignId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param =
                    DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Update(campaignId),
                        selection.campaignName,
                        selection.startDate,
                        selection.endDate,
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
}