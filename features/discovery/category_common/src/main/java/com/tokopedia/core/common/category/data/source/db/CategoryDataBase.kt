package com.tokopedia.core.common.category.data.source.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBMetaData.DB_TABLE)
data class CategoryDataBase(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var name: String = "",
    var identifier: String = "",
    var weight: Int = 0,
    var parentId: Long?,
    var hasChild: Boolean = false
) {

    constructor() : this(null, "", "", 0, null, false)

    companion object {
        const val LEVEL_ONE_PARENT = -1
    }
}