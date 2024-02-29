package com.tokopedia.buy_more_get_more.minicart.presentation.model

import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 04/12/23.
 */

sealed interface GwpMiniCartEditorVisitable : BmgmMiniCartVisitable {

    data class GiftMessageUiModel(val messages: List<String>) : GwpMiniCartEditorVisitable {

        override fun getItemId(): String = "gwp_mini_cart_editor_message"

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object MiniCartEditorLoadingState  : GwpMiniCartEditorVisitable {
        override fun getItemId(): String = "gwp_mini_cart_editor_loading"

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object MiniCartEditorErrorState  : GwpMiniCartEditorVisitable {
        override fun getItemId(): String = "gwp_mini_cart_editor_error"

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}