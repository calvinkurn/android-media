package com.tokopedia.shop.note.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel

/**
 * Created by normansyahputa on 2/8/18.
 */
interface ShopNoteDetailView : CustomerView {
    fun onErrorGetShopNoteList(e: Throwable?)
    fun onSuccessGetShopNoteList(shopNoteModel: ShopNoteModel?)
}