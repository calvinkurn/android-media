package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import com.tokopedia.content.common.util.toPercent
import com.tokopedia.content.common.util.toUnitInterval
import com.tokopedia.iconunify.IconUnify
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
@Parcelize
data class FaceFilterUiModel(
    val id: String,
    val name: String,
    val active: Boolean,
    val minValue: Double,
    val maxValue: Double,
    val defaultValue: Double,
    val value: Double,
    val isSelected: Boolean,
) : Parcelable {

    val isRemoveEffect: Boolean
        get() = id == Type.None.id

    val iconUnifyId: Int
        get() = Type.getIconUnifyById(id)

    val minValueForSlider: Int
        get() = minValue.toPercent()

    val maxValueForSlider: Int
        get() = maxValue.toPercent()

    val defaultValueForSlider: Int
        get() = defaultValue.toPercent()

    val valueForSlider: Int
        get() = value.toPercent()

    fun copyWithNewValue(newValueFromSlider: Int): FaceFilterUiModel {
        return copy(
            active = newValueFromSlider > 0,
            value = newValueFromSlider.toUnitInterval(),
        )
    }

    enum class Type(val id: String, val iconUnifyId: Int) {
        Unknown("", UNKNOWN_ICON_UNIFY),
        None("none", IconUnify.BLOCK),
        Blur("buffing", IconUnify.BLUR),
        Sharpen("sharpen", IconUnify.SHARPEN),
        Clarity("toning", IconUnify.CLARITY);

        companion object {
            fun getIconUnifyById(id: String): Int {
                return values().firstOrNull { it.id == id }?.iconUnifyId ?: Unknown.iconUnifyId
            }
        }
    }

    companion object {
        private const val UNKNOWN_ICON_UNIFY = -1
    }
}
