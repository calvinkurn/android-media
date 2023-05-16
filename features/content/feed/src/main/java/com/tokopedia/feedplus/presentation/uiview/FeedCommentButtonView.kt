package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.databinding.LayoutFeedCommentButtonBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 15/05/23
 */
class FeedCommentButtonView(
    private val binding: LayoutFeedCommentButtonBinding,
    private val listener: FeedListener
) {

    fun bind(
        contentId: String,
        isPlayContent: Boolean,
        commentCount: String,
        trackerDataModel: FeedTrackerDataModel?,
        positionInFeed: Int
    ) {
        with(binding) {
            commentCountText.text = commentCount
            commentButton.setOnClickListener {
                trackerDataModel?.let {
                    listener.onCommentClick(
                        it,
                        contentId,
                        isPlayContent,
                        positionInFeed
                    )
                }
            }
        }
    }

    fun show() {
        binding.root.show()
    }

    fun hide() {
        binding.root.hide()
    }
}
