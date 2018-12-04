package com.tokopedia.expresscheckout.view.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapter(
        var adapterTypefactory: CheckoutVariantAdapterTypefactory
) : BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypefactory>(adapterTypefactory) {

    fun addDataViewModel(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
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