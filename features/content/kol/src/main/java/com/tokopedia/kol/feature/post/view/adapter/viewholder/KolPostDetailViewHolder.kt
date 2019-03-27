package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.view.View
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kol.R

/**
 * @author by nisie on 26/03/19.
 */
class KolPostDetailViewHolder(private val kolView: View,
                              private val listener: DynamicPostListener,
                              private val cardTitleListener: CardTitleView.CardTitleListener,
                              private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                              private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                              private val pollOptionListener: PollAdapter.PollOptionListener,
                              private val gridItemListener: GridPostAdapter.GridItemListener) :
        DynamicPostViewHolder(kolView, listener, cardTitleListener,
                imagePostListener, youtubePostListener, pollOptionListener, gridItemListener) {


    override fun bind(element: DynamicPostViewModel?) {
        super.bind(element)
        val footer = kolView.findViewById<View>(R.id.footer)
        footer.visibility = View.GONE
    }

}