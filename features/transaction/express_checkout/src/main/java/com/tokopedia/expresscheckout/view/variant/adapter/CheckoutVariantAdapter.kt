package com.tokopedia.expresscheckout.view.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapter(adapterTypefactory: CheckoutVariantAdapterTypefactory) :
        BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypefactory>(adapterTypefactory) {

    fun addDataViewModel(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
    }

    fun getProductDataViewModel(): ProductViewModel? {
        for (visitable in visitables) {
            if (visitable is ProductViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getQuantityViewModel(): QuantityViewModel? {
        for (visitable in visitables) {
            if (visitable is QuantityViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getVariantTypeViewModel(): ArrayList<TypeVariantViewModel> {
        var variantTypeViewModels = ArrayList<TypeVariantViewModel>()
        for (visitable in visitables) {
            if (visitable is TypeVariantViewModel) {
                variantTypeViewModels.add(visitable)
            }
        }

        return variantTypeViewModels
    }

    fun getIndex(visitable: Visitable<*>): Int {
        return visitables.indexOf(visitable)
    }

    fun addProfileDataViewModel(profileViewModel: ProfileViewModel) {
        visitables.add(profileViewModel)
    }

    fun addProductDataViewModel(productViewModel: ProductViewModel) {
        visitables.add(productViewModel)
    }

    fun addQuantityDataViewModel(quantityViewModel: QuantityViewModel) {
        visitables.add(quantityViewModel)
    }

    fun addNoteDataViewModel(noteViewModel: NoteViewModel) {
        visitables.add(noteViewModel)
    }

    fun addSummaryDataViewModel(summaryViewModel: SummaryViewModel) {
        visitables.add(summaryViewModel)
    }

    fun addVariantDataViewModel(variantDataViewModels: ArrayList<TypeVariantViewModel>) {
        visitables.addAll(variantDataViewModels)
    }

}