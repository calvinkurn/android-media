package com.tokopedia.minicart.chatlist

import androidx.lifecycle.LiveData
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel

interface MiniCartChatListViewModel {

    fun getCartList(isFirstLoad: Boolean = false)

    fun getCurrentShopIds(): List<String>?

    fun getMiniCartChatListBottomSheetUiModel(): LiveData<MiniCartListUiModel>?
}
