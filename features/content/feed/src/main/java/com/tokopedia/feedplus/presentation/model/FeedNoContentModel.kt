package com.tokopedia.feedplus.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedNoContentModel(
    val imageId: Int,
    val title: String,
    val subtitle: String,
    val buttonText: String,
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    companion object {
        fun getNoContentInstance(context: Context) =
            FeedNoContentModel(
                IconUnify.IMAGE,
                context.getString(R.string.feed_label_no_content_title),
                context.getString(R.string.feed_label_no_content_subtitle),
                context.getString(R.string.feed_label_no_content_button)
            )

        fun getNoMoreContentInstance(context: Context) =
            FeedNoContentModel(
                IconUnify.CHECK_CIRCLE,
                context.getString(R.string.feed_label_no_more_content_title),
                context.getString(R.string.feed_label_no_more_content_subtitle),
                context.getString(R.string.feed_label_no_content_button)
            )
    }
}
