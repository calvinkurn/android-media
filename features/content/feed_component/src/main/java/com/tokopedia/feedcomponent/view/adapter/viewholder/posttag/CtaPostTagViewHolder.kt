package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.support.annotation.LayoutRes
import android.support.v7.widget.CardView
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel

/**
 * @author by yoasfs on 2019-07-18
 */

class CtaPostTagViewHolder(val mainView: View, val listener: DynamicPostViewHolder.DynamicPostListener)
    : AbstractViewHolder<CtaPostTagViewModel>(mainView) {

    private lateinit var itemCard: CardView
    private lateinit var titleCard: TextView

    override fun bind(item: CtaPostTagViewModel) {

        itemCard = itemView.findViewById(R.id.container)
        titleCard = itemView.findViewById(R.id.titleCard)
        titleCard.text = item.text
        itemCard.setOnClickListener(getItemClickNavigationListener(listener, item.positionInFeed, item.postTagItemPojo, adapterPosition))
    }

    private fun getItemClickNavigationListener(listener: DynamicPostViewHolder.DynamicPostListener,
                                               positionInFeed: Int,
                                               item: PostTagItem, itemPosition: Int)
            : View.OnClickListener {
        return View.OnClickListener {
            listener.onPostTagItemClick(positionInFeed, item.applink, item, itemPosition)
            listener.onAffiliateTrackClicked(mappingTracking(item.tracking), true)
        }
    }

    private fun mappingTracking(trackListPojo : List<Tracking>): MutableList<TrackingViewModel> {
        val trackList = ArrayList<TrackingViewModel>()
        for (trackPojo: Tracking in trackListPojo) {
            trackList.add(TrackingViewModel(
                    trackPojo.clickURL,
                    trackPojo.viewURL,
                    trackPojo.type,
                    trackPojo.source
            ))
        }
        return trackList
    }
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_producttag_cta
    }
}