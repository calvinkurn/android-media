package com.tokopedia.shop.note.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */
class ShopNoteUiModel : Visitable<ShopNoteAdapterTypeFactory?> {
    var shopNoteId: Long = 0
    var title: String? = null
    var position: Long = 0
    var url: String? = null
    var lastUpdate: String? = null


    override fun type(typeFactory: ShopNoteAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}