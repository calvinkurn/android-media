package com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel

/**
 * Created by Irfan Khoirul on 09/01/19.
 */

data class FragmentUiModel(
        var atcResponseModel: AtcResponseModel? = null,
        var totalPayment: Long? = 0,
        var lastQuantity: Int? = 0,
        var lastPrice: Int? = 0,
        var isStateChanged: Boolean? = false, // True if there are error or user activity (change qty / change courier / change note / change variant / check insurance)
        var shippingCourierUiModels: MutableList<ShippingCourierUiModel>? = null,
        var viewModels: ArrayList<Visitable<*>> = ArrayList(),
        var hasGenerateFingerprintPublicKey: Boolean = false,
        var fingerprintPublicKey: String? = null
) {

    fun getProfileViewModel(): ProfileUiModel? {
        for (visitable in viewModels) {
            if (visitable is ProfileUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getProductViewModel(): ProductUiModel? {
        for (visitable in viewModels) {
            if (visitable is ProductUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getQuantityViewModel(): QuantityUiModel? {
        for (visitable in viewModels) {
            if (visitable is QuantityUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getVariantTypeViewModel(): ArrayList<TypeVariantUiModel> {
        var variantTypeViewModels = ArrayList<TypeVariantUiModel>()
        for (visitable in viewModels) {
            if (visitable is TypeVariantUiModel) {
                variantTypeViewModels.add(visitable)
            }
        }

        return variantTypeViewModels
    }

    fun getSummaryViewModel(): SummaryUiModel? {
        for (visitable in viewModels) {
            if (visitable is SummaryUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getInsuranceRecommendationViewModel(): InsuranceRecommendationUiModel? {
        for (visitable in viewModels) {
            if (visitable is InsuranceRecommendationUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getInsuranceViewModel(): InsuranceUiModel? {
        for (visitable in viewModels) {
            if (visitable is InsuranceUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getNoteViewModel(): NoteUiModel? {
        for (visitable in viewModels) {
            if (visitable is NoteUiModel) {
                return visitable
            }
        }

        return null
    }

    fun getIndex(visitable: Visitable<*>): Int {
        return viewModels.indexOf(visitable)
    }

    fun getSelectedProductChild(): ProductChild? {
        if (getProductViewModel()?.productChildrenList?.isNotEmpty() == true) {
            val productChildrenList = getProductViewModel()?.productChildrenList
            if (productChildrenList != null) {
                for (productChild: ProductChild in productChildrenList) {
                    if (productChild.isSelected) {
                        return productChild
                    }
                }
            }
        }
        return null
    }
}