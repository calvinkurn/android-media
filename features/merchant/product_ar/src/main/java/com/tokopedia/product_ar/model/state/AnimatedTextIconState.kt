package com.tokopedia.product_ar.model.state

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product_ar.R

data class AnimatedTextIconState(
        val view1ClickMode: AnimatedTextIconClickMode? = null,
        val view2ClickMode: AnimatedTextIconClickMode? = null
)

enum class AnimatedTextIconClickMode(val textId: Int, val iconUnify: Int) {
    CHOOSE_FROM_GALLERY(R.string.txt_animated_text_icon_take_from_gallery, IconUnify.IMAGE),
    USE_CAMERA(R.string.txt_animated_text_icon_use_camera, IconUnify.CAMERA),
    CHANGE_PHOTO(R.string.txt_animated_text_icon_change_photo, IconUnify.IMAGE)
}
