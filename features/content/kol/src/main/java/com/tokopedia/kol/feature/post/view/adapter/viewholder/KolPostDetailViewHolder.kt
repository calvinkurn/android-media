package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.view.View
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Caption
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.user.session.UserSessionInterface


/**
 * @author by nisie on 26/03/19.
 */
class KolPostDetailViewHolder(private val kolView: View,
                              private val listener: DynamicPostListener,
                              private val cardTitleListener: CardTitleView.CardTitleListener,
                              private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                              private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                              private val pollOptionListener: PollAdapter.PollOptionListener,
                              private val gridItemListener: GridPostAdapter.GridItemListener,
                              private val videoViewListener: VideoViewHolder.VideoViewListener,
                              private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                              private val userSession : UserSessionInterface) :
        DynamicPostViewHolder(kolView, listener, cardTitleListener,
                imagePostListener, youtubePostListener, pollOptionListener, gridItemListener,
                videoViewListener, feedMultipleImageViewListener, userSession) {


    override fun bind(element: DynamicPostViewModel?) {
        super.bind(element)
        val footer = kolView.findViewById<View>(R.id.footer)
        val footerCta = footer.findViewById<View>(R.id.footerCta)
        footerCta.visibility = View.GONE
    }

    override fun bindCaption(caption: Caption, template: TemplateBody) {
        captionTv.shouldShowWithAction(template.caption) {
            if (caption.text.isEmpty()) {
                captionTv.visibility = View.GONE
            } else {
                captionTv.text = caption.text.replace(NEWLINE, " ")
            }
        }

    }

}