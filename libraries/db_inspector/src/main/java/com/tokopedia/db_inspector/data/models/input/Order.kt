package com.tokopedia.db_inspector.data.models.input

import androidx.annotation.DrawableRes

internal enum class Order(val rawValue: String, @DrawableRes val icon: Int) {
    ASCENDING("ASC", com.tokopedia.iconunify.R.drawable.iconunify_arrow_up),
    DESCENDING("DESC", com.tokopedia.iconunify.R.drawable.iconunify_arrow_down);

    companion object {

        operator fun invoke(value: String) = values().firstOrNull { it.rawValue == value }
            ?: ASCENDING
    }
}