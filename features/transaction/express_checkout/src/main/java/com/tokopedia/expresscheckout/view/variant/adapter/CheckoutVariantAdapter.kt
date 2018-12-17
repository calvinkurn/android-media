package com.tokopedia.expresscheckout.view.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
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

    fun getProductDataViewModel(): CheckoutVariantProductViewModel? {
        for (visitable in visitables) {
            if (visitable is CheckoutVariantProductViewModel) {
                return visitable
            }
        }

        return null
    }

    fun getVariantTypeViewModel(): ArrayList<CheckoutVariantTypeVariantViewModel> {
        var variantTypeViewModels = ArrayList<CheckoutVariantTypeVariantViewModel>()
        for (visitable in visitables) {
            if (visitable is CheckoutVariantTypeVariantViewModel) {
                variantTypeViewModels.add(visitable)
            }
        }

        return variantTypeViewModels
    }

    fun getIndex(visitable: Visitable<*>): Int {
        return visitables.indexOf(visitable)
    }

    fun addProfileDataViewModel(checkoutVariantProfileViewModel: CheckoutVariantProfileViewModel) {
        visitables.add(checkoutVariantProfileViewModel)
    }

    fun addProductDataViewModel(checkoutVariantProductViewModel: CheckoutVariantProductViewModel) {
        visitables.add(checkoutVariantProductViewModel)
    }

    fun addQuantityDataViewModel(checkoutVariantQuantityViewModel: CheckoutVariantQuantityViewModel) {
        visitables.add(checkoutVariantQuantityViewModel)
    }

    fun addNoteDataViewModel(checkoutVariantNoteViewModel: CheckoutVariantNoteViewModel) {
        visitables.add(checkoutVariantNoteViewModel)
    }

    fun addSummaryDataViewModel(checkoutVariantSummaryViewModel: CheckoutVariantSummaryViewModel) {
        visitables.add(checkoutVariantSummaryViewModel)
    }

    fun addVariantDataViewModel(variantDataViewModels: ArrayList<CheckoutVariantTypeVariantViewModel>) {
        visitables.addAll(variantDataViewModels)
    }

}