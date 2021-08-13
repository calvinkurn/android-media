package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 30/03/21
 */
class TagsDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val setChannelTagsUseCase: SetChannelTagsUseCase,
) : TagsDataStore {

    private val mTags: MutableSet<String> = mutableSetOf()

    override fun getTags(): Set<String> {
        return mTags.toSet()
    }

    override fun setTags(tags: Set<String>) {
        mTags.clear()
        mTags.addAll(tags)
    }

    override fun addTag(tag: String) {
        mTags.add(tag)
    }

    override suspend fun uploadTags(channelId: String): Boolean = withContext(dispatcher.io) {
        return@withContext setChannelTagsUseCase.apply {
            setParams(channelId, mTags)
        }.executeOnBackground().recommendedTags.success
    }
}