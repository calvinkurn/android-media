package com.tkpd.remoteresourcerequest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resource_entry")
data class ResourceEntry(

    @PrimaryKey
    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "absolutePath")
    var absolutePath: String,

    @ColumnInfo(name = "enc_key")
    var encryptionKey: String,

    @ColumnInfo(name = "appVersion")
    var appVersion: String,

    @ColumnInfo(name = "createdOn")
    var createdOnMillis: Long,

    @ColumnInfo(name = "lastAccessed")
    var lastAccessed: Long
)
