package com.tokopedia.addongifting.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.addongifting.view.adapter.AddOnListAdapterTypeFactory

data class AddOnUiModel(
        var isTokoCabang: Boolean = false,
        var productCount: Int = 0,
        var addOnName: String = "",
        var addOnDescription: String = "",
        var addOnPrice: Long = 0,
        var addOnSquareImageUrl: String = "",
        var addOnAllImageUrls: List<String> = emptyList(),
        var isAddOnSelected: Boolean = false,
        var addOnNoteTo: String = "",
        var addOnNoteFrom: String = "",
        var addOnNote: String = "",
        var addOnFooterMessages: List<String> = emptyList(),
        var isCustomNote: Boolean = false,
        var isLoadingNoteState: Boolean = false
) : Visitable<AddOnListAdapterTypeFactory> {

    override fun type(typeFactory: AddOnListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}