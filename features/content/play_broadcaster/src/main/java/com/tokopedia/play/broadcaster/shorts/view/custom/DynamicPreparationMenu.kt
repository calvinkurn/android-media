package com.tokopedia.play.broadcaster.shorts.view.custom

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
data class DynamicPreparationMenu(
    val menuId: String,
    val iconId: Int,
    val textResId: Int,
    val isMandatory: Boolean,
    val isChecked: Boolean,
    val isEnabled: Boolean,
) {

    companion object {
        const val TITLE = "TITLE"
        const val COVER = "COVER"
        const val SCHEDULE = "SCHEDULE"
        const val PRODUCT = "PRODUCT"

        fun createTitle(isMandatory: Boolean) = DynamicPreparationMenu(
            menuId = TITLE,
            iconId = IconUnify.TEXT,
            textResId = R.string.play_bro_title_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createCover(isMandatory: Boolean) = DynamicPreparationMenu(
            menuId = COVER,
            iconId = IconUnify.IMAGE,
            textResId = R.string.play_bro_cover_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createProduct(isMandatory: Boolean) = DynamicPreparationMenu(
            menuId = PRODUCT,
            iconId = IconUnify.PRODUCT,
            textResId = R.string.play_bro_product_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createSchedule(isMandatory: Boolean) = DynamicPreparationMenu(
            menuId = SCHEDULE,
            iconId = IconUnify.CALENDAR_TIME,
            textResId = R.string.play_bro_schedule_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )
    }
}
