package com.tokopedia.data_explorer.db_explorer.data.models.cursor.input

import android.database.sqlite.SQLiteDatabase
import androidx.annotation.DrawableRes
import com.tokopedia.data_explorer.db_explorer.data.Data
import com.tokopedia.iconunify.IconUnify

internal data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Order = Order.ASCENDING,
    val pageSize: Int = Data.Constants.Limits.PAGE_SIZE,
    val page: Int? = Data.Constants.Limits.INITIAL_PAGE
)

internal enum class Order(val rawValue: String, @DrawableRes val icon: Int) {
    ASCENDING("ASC", IconUnify.CHEVRON_UP),
    DESCENDING("DESC", IconUnify.CHEVRON_DOWN);

    companion object {

        operator fun invoke(value: String) = values().firstOrNull { it.rawValue == value }
            ?: ASCENDING
    }
}