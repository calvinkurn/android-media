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

//sealed interface DynamicPreparationMenu {
//
//    val menuId: String
//    val iconId: Int
//    val textResId: Int
//    val isMandatory:
//    val isChecked: Boolean
//
//    object Title : DynamicPreparationMenu {
//        override val menuId: String
//            get() = TITLE
//        override val iconId: Int
//            get() = IconUnify.TEXT
//        override val textResId: Int
//            get() = R.string.play_bro_title_label
//        override val isChecked: Boolean
//            get() = false
//    }
//
//    object Cover : DynamicPreparationMenu {
//        override val menuId: String
//            get() = COVER
//        override val iconId: Int
//            get() = IconUnify.IMAGE
//        override val textResId: Int
//            get() = R.string.play_bro_cover_label
//    }
//
//    object Product : DynamicPreparationMenu {
//        override val menuId: String
//            get() = PRODUCT
//        override val iconId: Int
//            get() = IconUnify.PRODUCT
//        override val textResId: Int
//            get() = R.string.play_bro_product_label
//    }
//
//    object Schedule : DynamicPreparationMenu {
//        override val menuId: String
//            get() = SCHEDULE
//        override val iconId: Int
//            get() = IconUnify.CALENDAR_TIME
//        override val textResId: Int
//            get() = R.string.play_bro_schedule_label
//    }
//
//    companion object {
//        const val TITLE = "TITLE"
//        const val COVER = "COVER"
//        const val SCHEDULE = "SCHEDULE"
//        const val PRODUCT = "PRODUCT"
//    }
//}
