package com.tokopedia.notifications.data.mapper

import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.*
import com.tokopedia.unifycomponents.UnifyButton

object InAppButtonMapper {

    fun mapBtnType(type: String): Int {
        return when(type) {
            BUTTON_MAIN_TYPE -> UnifyButton.Type.MAIN
            BUTTON_TRANSACTION_TYPE -> UnifyButton.Type.TRANSACTION
            BUTTON_ALTERNATE_TYPE -> UnifyButton.Type.ALTERNATE
            else -> UnifyButton.Type.MAIN
        }
    }

    fun mapBtnVariant(variant: String): Int {
        return when(variant) {
            BUTTON_FILLED_VARIANT -> UnifyButton.Variant.FILLED
            BUTTON_GHOST_VARIANT -> UnifyButton.Variant.GHOST
            BUTTON_TEXT_ONLY_VARIANT -> UnifyButton.Variant.TEXT_ONLY
            else -> UnifyButton.Variant.FILLED
        }
    }

    fun mapBtnSize(size: String): Int {
        return when(size) {
            BUTTON_LARGE_SIZE -> UnifyButton.Size.LARGE
            BUTTON_MEDIUM_SIZE -> UnifyButton.Size.MEDIUM
            BUTTON_SMALL_SIZE -> UnifyButton.Size.SMALL
            BUTTON_MICRO_SIZE -> UnifyButton.Size.MICRO
            else -> UnifyButton.Size.MEDIUM
        }
    }

}