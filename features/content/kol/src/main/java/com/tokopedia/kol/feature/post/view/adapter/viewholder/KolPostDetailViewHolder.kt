package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.view.View
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostUIViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.user.session.UserSessionInterface


/**
 * @author by nisie on 26/03/19.
 */
class KolPostDetailViewHolder(private val kolView: View,
                              listener: DynamicPostViewHolder.DynamicPostListener,
                              cardTitleListener: CardTitleView.CardTitleListener,
                              imagePostListener: ImagePostViewHolder.ImagePostListener,
                              youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                              pollOptionListener: PollAdapter.PollOptionListener,
                              gridItemListener: GridPostAdapter.GridItemListener,
                              videoViewListener: VideoViewHolder.VideoViewListener,
                              feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                              userSession : UserSessionInterface) :
        DynamicPostUIViewHolder(kolView, listener, cardTitleListener,
                imagePostListener, youtubePostListener, pollOptionListener, gridItemListener,
                videoViewListener, feedMultipleImageViewListener, userSession) {

}