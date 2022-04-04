package com.tokopedia.review.feature.createreputation.presentation.uimodel

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.kotlin.extensions.view.ZERO

data class CreateReviewStringRes(@StringRes val id: Int = Int.ZERO) {
    fun getString(context: Context): String {
        return try { context.getString(id) } catch (_: Throwable) { "" }
    }
}
