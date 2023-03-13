package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.databinding.LayoutFeedIconWithCounterBinding

/**
 * Created By : Muhammad Furqan on 13/03/23
 */
class FeedActionIconWithCounterView(private val binding: LayoutFeedIconWithCounterBinding) {

    fun bind(icon: Int, label: String) {
        with(binding) {
            icFeedActionButton.setImage(icon)
            icFeedActionLabel.text = label
        }
    }

}
