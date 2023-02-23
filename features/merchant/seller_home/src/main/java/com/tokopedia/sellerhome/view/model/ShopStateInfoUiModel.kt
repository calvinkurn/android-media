package com.tokopedia.sellerhome.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

data class ShopStateInfoUiModel(
    val dataKey: String = String.EMPTY,
    val imageUrl: String = String.EMPTY,
    val title: String = String.EMPTY,
    val subtitle: String = String.EMPTY,
    val button: Button = Button(),
    val buttonAlt: Button = Button(),
    val dataSign: String = String.EMPTY,
    val subType: SubType = SubType.NONE,
    val isNewSellerState: Boolean = false,
) {
    data class Button(
        val name: String = String.EMPTY,
        val appLink: String = String.EMPTY,
    )

    enum class SubType {
        NONE, TOAST, POPUP
    }
}