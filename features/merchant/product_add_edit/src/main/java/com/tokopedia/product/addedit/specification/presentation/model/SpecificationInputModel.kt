package com.tokopedia.product.addedit.specification.presentation.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecificationInputModel (
        var id: String = "",
        var data: String = "",
        var specificationVariant: String = "",
        var required: Boolean = false,
        @StringRes var errorMessageRes: Int = 0
): Parcelable {
    val requiredFieldNotFilled: Boolean get() = id.isEmpty() && required

    fun getValidatedData(@StringRes errorMessageRes: Int): SpecificationInputModel {
        return if (requiredFieldNotFilled) apply { this.errorMessageRes = errorMessageRes }
                else this
    }
}