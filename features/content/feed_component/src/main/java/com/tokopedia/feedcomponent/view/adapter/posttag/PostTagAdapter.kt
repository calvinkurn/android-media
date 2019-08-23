package com.tokopedia.feedcomponent.view.adapter.posttag

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemTag
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.SOURCE_DETAIL
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.RatingBarReview
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import kotlin.math.roundToInt


/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(itemList: List<Visitable<*>>,
                     typeFactory: PostTagTypeFactory)
    : BaseAdapter<PostTagTypeFactory>(typeFactory, itemList) {

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}