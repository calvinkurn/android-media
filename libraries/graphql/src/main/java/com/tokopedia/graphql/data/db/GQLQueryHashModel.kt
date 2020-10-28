package com.tokopedia.graphql.data.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "query_hash_map")
class GQLQueryHashModel {
    @NonNull
    @PrimaryKey
    var key = ""
    var value = ""

}