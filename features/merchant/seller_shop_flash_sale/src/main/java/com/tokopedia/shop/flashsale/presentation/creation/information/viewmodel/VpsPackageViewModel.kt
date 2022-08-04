package com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.epochToDate
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class VpsPackageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignPackageListUseCase: GetSellerCampaignPackageListUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val SHOP_TIER_BENEFIT_PACKAGE_ID = -1
        private const val EMPTY_QUOTA = 0
    }

    private val _vpsPackages = MutableLiveData<Result<List<VpsPackageUiModel>>>()
    val vpsPackages: LiveData<Result<List<VpsPackageUiModel>>>
        get() = _vpsPackages

    private var vpsPackage : VpsPackageUiModel? = null

    fun getVpsPackages(selectedPackageId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignPackageListUseCase.execute()
                val vpsPackages = applySelectionRule(selectedPackageId, result)
                _vpsPackages.postValue(Success(vpsPackages))
            },
            onError = { error ->
                _vpsPackages.postValue(Fail(error))
            }
        )

    }

    private fun applySelectionRule(
        selectedPackageId: Long,
        vpsPackages: List<VpsPackage>
    ): List<VpsPackageUiModel> {
        return vpsPackages
            .map { vpsPackage ->
                VpsPackageUiModel(
                    vpsPackage.currentQuota,
                    vpsPackage.isDisabled,
                    vpsPackage.originalQuota,
                    vpsPackage.packageEndTime.epochToDate(),
                    vpsPackage.packageId.toLongOrZero(),
                    vpsPackage.packageName,
                    vpsPackage.packageStartTime.epochToDate(),
                    vpsPackage.isSelected(selectedPackageId),
                    vpsPackage.isDisabled(),
                    vpsPackage.isShopTierBenefit() ,
                    vpsPackage.packageStartTime.epochToDate().formatTo(DateConstant.DATE),
                    vpsPackage.packageEndTime.epochToDate().formatTo(DateConstant.DATE)
                )
            }
            .sortedBy { it.packageEndTime.time }
    }

    private fun VpsPackage.isSelected(selectedPackageId: Long) : Boolean {
        return selectedPackageId == packageId.toLong()
    }

    private fun VpsPackage.isShopTierBenefit() : Boolean {
        return packageId.toIntOrZero() == SHOP_TIER_BENEFIT_PACKAGE_ID
    }

    private fun VpsPackage.isDisabled(): Boolean {
        if (isDisabled) {
            return true
        }

        if (currentQuota == EMPTY_QUOTA) {
            return true
        }

        return false
    }

    fun setSelectedVpsPackage(vpsPackage: VpsPackageUiModel) {
        this.vpsPackage = vpsPackage
    }

    fun getSelectedVpsPackage(): VpsPackageUiModel? {
        return this.vpsPackage
    }

    fun markAsSelected(
        selectedVpsPackageId: Long,
        vpsPackages: List<VpsPackageUiModel>
    ): List<VpsPackageUiModel> {
        return vpsPackages.map { vpsPackage ->
            if (vpsPackage.packageId == selectedVpsPackageId) {
                vpsPackage.copy(isSelected = true)
            } else {
                vpsPackage
            }
        }
    }

}