package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import com.tokopedia.iconunify.IconUnify
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
@Parcelize
data class FaceFilterUiModel(
    val id: String,
    val name: String,
    val minValue: Double,
    val maxValue: Double,
    val defaultValue: Double,
    val value: Double,
    val assetLink: String,
    val isSelected: Boolean,
    val assetStatus: BeautificationAssetStatus,
) : Parcelable {

    val isChecked: Boolean
        get() = value != 0.0

    val isRemoveEffect: Boolean
        get() = id == Type.None.id

    val iconUnifyId: Int
        get() = Type.getIconUnifyById(id)

    enum class Type(val id: String, val iconUnifyId: Int) {
        Unknown("", UNKNOWN_ICON_UNIFY),
        None("none", IconUnify.BLOCK),
        /** TODO: adjust IconUnify */
        Blur("blur", IconUnify.SORT_FILTER),
        Sharpen("sharpen", IconUnify.CONTRAST),
        Clarity("clarity", IconUnify.SMILE);

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
