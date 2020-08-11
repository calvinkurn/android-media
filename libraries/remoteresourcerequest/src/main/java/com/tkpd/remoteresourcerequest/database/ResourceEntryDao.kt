package com.tkpd.remoteresourcerequest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResourceEntryDao {

    @Query("Select * FROM resource_entry")
    fun getAllResourceEntry(): List<ResourceEntry>

    @Query("Select url FROM resource_entry")
    fun getAllDownloadedResourceURLEntry() : List<String>

    @Query("SELECT * from resource_entry where lastAccessed < :beforeMillis")
    fun beforeMIllisLastAccessed(beforeMillis: Long): List<ResourceEntry>

    @Query("Select * FROM resource_entry where url = :url LIMIT 1")
    fun getResourceEntry(url: String): ResourceEntry?

    @Insert
    fun createResourceEntry(resourceEntry: ResourceEntry)

    @Query("UPDATE resource_entry SET lastAccessed = :lastAccessMillis  where url = :url")
    fun updateLastAccess(lastAccessMillis: Long, url: String)

    @Query("DELETE FROM resource_entry where url = :url")
    fun deleteEntry(url: String)

}
