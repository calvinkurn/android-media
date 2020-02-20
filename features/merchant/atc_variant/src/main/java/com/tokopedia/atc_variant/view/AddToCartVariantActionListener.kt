package com.tokopedia.atc_variant.view

import com.tokopedia.atc_variant.view.viewmodel.*
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface AddToCartVariantActionListener {

    fun onBindProductUpdateQuantityViewModel(productViewModel: ProductViewModel, stockWording: String)

    fun onBindVariantGetProductViewModel(): ProductViewModel?

    fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel)

    fun onChangeQuantity(quantityViewModel: QuantityViewModel)

    fun onChangeNote(noteViewModel: NoteViewModel)

    fun onVariantGuidelineClick(variantGuideline: String)

    fun onInsuranceSelectedStateChanged(element: InsuranceRecommendationViewModel?, isSelected: Boolean)

    fun onGetCompositeSubscriber(): CompositeSubscription?
    fun sendEventInsuranceSelectedStateChanged(isChecked: Boolean, title: String)
    fun sendEventInsuranceInfoClicked()
}