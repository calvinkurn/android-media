package com.tokopedia.play.broadcaster.data.datastore

import javax.inject.Inject

/**
 * Created by jegul on 30/03/21
 */
class TagsDataStoreImpl @Inject constructor() : TagsDataStore {

    private val mTags: MutableSet<String> = mutableSetOf()

    override fun getTags(): Set<String> {
        return mTags
    }

    override fun setTags(tags: Set<String>) {
        mTags.clear()
        mTags.addAll(tags)
    }

    override fun addTag(tag: String) {
        mTags.add(tag)
    }
}