package com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import javax.inject.Inject

class VpsPackageViewModel @Inject constructor() : ViewModel() {

    private var vpsPackage: VpsPackageUiModel? = null

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