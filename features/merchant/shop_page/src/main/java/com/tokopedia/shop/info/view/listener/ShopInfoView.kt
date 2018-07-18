package com.tokopedia.shop.info.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.note.view.model.ShopNoteViewModel

interface ShopInfoView: CustomerView {
    fun renderListNote(notes: List<ShopNoteViewModel>)
    fun showListNoteError(throwable: Throwable?)

}