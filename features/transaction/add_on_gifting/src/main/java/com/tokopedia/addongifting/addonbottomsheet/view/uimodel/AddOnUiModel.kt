package com.tokopedia.addongifting.addonbottomsheet.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.addongifting.addonbottomsheet.view.adapter.AddOnListAdapterTypeFactory

data class AddOnUiModel(
        var isTokoCabang: Boolean = false,
        var productCount: Int = 0,
        var mainProductQuantity: Int = 0,
        var addOnId: String = "",
        var addOnName: String = "",
        var addOnType: String = "",
        var addOnDescription: String = "",
        var addOnQty: Int = 0,
        var addOnPrice: Long = 0,
        var addOnSquareImageUrl: String = "",
        var addOnAllImageUrls: List<String> = emptyList(),
        var isAddOnSelected: Boolean = false,
        var initialAddOnNoteTo: String = "",
        var initialAddOnNoteFrom: String = "",
        var initialAddOnNote: String = "",
        var initialSelectedState: Boolean = false,
        var addOnNoteTo: String = "",
        var addOnNoteFrom: String = "",
        var addOnNote: String = "",
        var packagingAndGreetingCardInfo: String = "",
        var onlyGreetingCardInfo: String = "",
        var invoiceNotSentToRecipientInfo: String = "",
        var isCustomNote: Boolean = false,
        var isLoadingNoteState: Boolean = false
) : Visitable<AddOnListAdapterTypeFactory> {

    override fun type(typeFactory: AddOnListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}