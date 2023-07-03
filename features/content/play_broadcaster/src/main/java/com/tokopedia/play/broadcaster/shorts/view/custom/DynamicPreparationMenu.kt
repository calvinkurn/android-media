package com.tokopedia.play.broadcaster.shorts.view.custom

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
data class DynamicPreparationMenu(
    val menu: Menu,
    val iconId: Int,
    val textResId: Int,
    val isMandatory: Boolean,
    val isChecked: Boolean,
    val isEnabled: Boolean,
) {
    enum class Menu(val id: String) {
        Title("TITLE"),
        Cover("COVER"),
        Product("PRODUCT"),
        Schedule("SCHEDULE"),
        FaceFilter("FACE_FILTER"),
    }

    companion object {

        fun createTitle(isMandatory: Boolean) = DynamicPreparationMenu(
            menu = Menu.Title,
            iconId = IconUnify.TEXT,
            textResId = R.string.play_bro_title_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createCover(isMandatory: Boolean) = DynamicPreparationMenu(
            menu = Menu.Cover,
            iconId = IconUnify.IMAGE,
            textResId = R.string.play_bro_cover_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createProduct(isMandatory: Boolean) = DynamicPreparationMenu(
            menu = Menu.Product,
            iconId = IconUnify.PRODUCT,
            textResId = R.string.play_bro_product_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createSchedule(isMandatory: Boolean) = DynamicPreparationMenu(
            menu = Menu.Schedule,
            iconId = IconUnify.CALENDAR_TIME,
            textResId = R.string.play_bro_schedule_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )

        fun createFaceFilter(isMandatory: Boolean) = DynamicPreparationMenu(
            menu = Menu.FaceFilter,
            iconId = IconUnify.SMILE,
            textResId = R.string.play_bro_face_filter_label,
            isMandatory = isMandatory,
            isChecked = false,
            isEnabled = isMandatory,
        )
    }
}

fun List<DynamicPreparationMenu>.isMenuExists(menu: DynamicPreparationMenu.Menu): Boolean {
    return this.firstOrNull { it.menu.id == menu.id } != null
}
