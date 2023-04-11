package com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import javax.inject.Inject

class VpsPackageViewModel @Inject constructor() : ViewModel() {

    fun markAsSelected(
        selectedVpsPackageId: Long,
        vpsPackages: List<VpsPackageUiModel>
    ): List<VpsPackageUiModel> {
        return vpsPackages.map { vpsPackage ->
            if (vpsPackage.packageId == selectedVpsPackageId) {
                vpsPackage.copy(isSelected = true)
            } else {
                vpsPackage.copy(isSelected = false)
            }
        }
    }

    fun findSelectedVpsPackage(vpsPackages: List<VpsPackageUiModel>): VpsPackageUiModel? {
        return vpsPackages.find { vpsPackage -> vpsPackage.isSelected }
    }

}