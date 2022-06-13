package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on November 22, 2021
 */
class ActionBarLiveViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.view_play_action_bar_live) {

    private val tvChannelTitle = findViewById<Typography>(R.id.tv_bro_channel_title)
    private val ivShopIcon = findViewById<ImageUnify>(R.id.iv_bro_shop_icon)
    private val icEndStream = findViewById<IconUnify>(R.id.ic_bro_end_stream)
    private val icSwitchCamera = findViewById<IconUnify>(R.id.ic_bro_switch_camera)

    init {
        icEndStream.setOnClickListener { listener.onEndStreamClicked() }
        icSwitchCamera.setOnClickListener { listener.onCameraIconClicked() }
    }

    fun setTitle(title: String) {
        tvChannelTitle.text = title
    }

    fun setShopIcon(iconUrl: String) {
        ivShopIcon.setImageUrl(iconUrl)
    }

    interface Listener {
        fun onCameraIconClicked()
        fun onEndStreamClicked()
    }
}