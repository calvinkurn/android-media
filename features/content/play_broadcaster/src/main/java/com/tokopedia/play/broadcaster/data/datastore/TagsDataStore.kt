package com.tokopedia.play.broadcaster.data.datastore

/**
 * Created by jegul on 29/03/21
 */
interface TagsDataStore {

    fun getTags(): Set<String>

    fun setTags(tags: Set<String>)

    fun addTag(tag: String)

    suspend fun uploadTags(channelId: String): Boolean
}