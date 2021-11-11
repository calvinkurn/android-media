package com.tokopedia.db_inspector.data.models.cursor.input

import android.database.sqlite.SQLiteDatabase
import androidx.annotation.DrawableRes
import com.tokopedia.db_inspector.data.Data

internal data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Order = Order.ASCENDING,
    val pageSize: Int = Data.Constants.Limits.PAGE_SIZE,
    val page: Int? = Data.Constants.Limits.INITIAL_PAGE
)

internal enum class Order(val rawValue: String, @DrawableRes val icon: Int) {
    ASCENDING("ASC", com.tokopedia.iconunify.R.drawable.iconunify_arrow_up),
    DESCENDING("DESC", com.tokopedia.iconunify.R.drawable.iconunify_arrow_down);

    companion object {

        operator fun invoke(value: String) = values().firstOrNull { it.rawValue == value }
            ?: ASCENDING
    }
}