package com.tokopedia.expresscheckout.view.variant.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel

/**
 * Created by Irfan Khoirul on 09/01/19.
 */

data class FragmentViewModel(
        var atcResponseModel: AtcResponseModel? = null,
        var totalPayment: Long? = 0,
        var lastQuantity: Int? = 0,
        var lastPrice: Int? = 0,
        var isStateChanged: Boolean? = false, // True if there are error or user activity (change qty / change courier / change note / change variant / check insurance)
        var shippingCourierViewModels: MutableList<ShippingCourierViewModel>? = null,
        var viewModels: ArrayList<Visitable<*>> = ArrayList()
) {

    fun getProfileViewModel(): ProfileViewModel? {
        for (visitable in viewModels) {
            if (visitable is ProfileViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getProductViewModel(): ProductViewModel? {
        for (visitable in viewModels) {
            if (visitable is ProductViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getQuantityViewModel(): QuantityViewModel? {
        for (visitable in viewModels) {
            if (visitable is QuantityViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getVariantTypeViewModel(): ArrayList<TypeVariantViewModel> {
        var variantTypeViewModels = ArrayList<TypeVariantViewModel>()
        for (visitable in viewModels) {
            if (visitable is TypeVariantViewModel) {
                variantTypeViewModels.add(visitable)
            }
        }

        return variantTypeViewModels
    }

    fun getSummaryViewModel(): SummaryViewModel? {
        for (visitable in viewModels) {
            if (visitable is SummaryViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getInsuranceViewModel(): InsuranceViewModel? {
        for (visitable in viewModels) {
            if (visitable is InsuranceViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getNoteViewModel(): NoteViewModel? {
        for (visitable in viewModels) {
            if (visitable is NoteViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getIndex(visitable: Visitable<*>): Int {
        return viewModels.indexOf(visitable)
    }

}