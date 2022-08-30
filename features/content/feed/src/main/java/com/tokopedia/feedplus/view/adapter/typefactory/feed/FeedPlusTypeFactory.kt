package com.tokopedia.feedplus.view.adapter.typefactory.feed

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.RetryModel

/**
 * @author by nisie on 5/15/17.
 */

interface FeedPlusTypeFactory {

    fun type(emptyFeedBeforeLoginModel: EmptyFeedBeforeLoginModel): Int

    fun type(retryModel: RetryModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
