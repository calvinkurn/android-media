package com.tokopedia.product.addedit.specification.presentation.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.tokopedia.product.addedit.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecificationInputModel (
        var id: String = "",
        var data: String = "",
        var specificationVariant: String = "",
        var required: Boolean = false,
        var isTextInput: Boolean = false,
        var variantId: String = "", // used only on text field input
        @StringRes var errorMessageRes: Int = 0
): Parcelable {
    val requiredFieldNotFilled: Boolean get() = id.isEmpty() && required
    val dataHasEmojiChar: Boolean get() = data.any { c -> c > 0x7F.toChar() }

    fun getValidatedData(): SpecificationInputModel {
        return if (requiredFieldNotFilled) {
            apply { this.errorMessageRes = R.string.error_specification_required_empty }
        } else if (dataHasEmojiChar) {
            apply { this.errorMessageRes = R.string.error_specification_emoji_input }
        } else {
            apply { this.errorMessageRes = 0 }
        }
    }
}
