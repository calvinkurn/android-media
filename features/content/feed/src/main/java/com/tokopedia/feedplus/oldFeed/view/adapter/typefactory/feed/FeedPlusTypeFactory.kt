package com.tokopedia.feedplus.oldFeed.view.adapter.typefactory.feed

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by nisie on 5/15/17.
 */

interface FeedPlusTypeFactory {

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
