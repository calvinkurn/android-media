package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.databinding.ItemPlayWidgetLiveContentBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetVideoContentViewHolder(
    private val binding: ItemPlayWidgetLiveContentBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: PlayWidgetChannelUiModel) {
        binding.tvLiveBadge.showWithCondition(data.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = data.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name

        binding.viewPlayWidgetActionButton.root.setImage(
            if (data.isMuted) IconUnify.VOLUME_MUTE
            else IconUnify.VOLUME_UP
        )

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            listener.onMuteButtonClicked(this, !data.isMuted)
        }
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): PlayWidgetVideoContentViewHolder {
            return PlayWidgetVideoContentViewHolder(
                ItemPlayWidgetLiveContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }
    }

    interface Listener {
        fun onMuteButtonClicked(
            viewHolder: PlayWidgetVideoContentViewHolder,
            shouldMute: Boolean,
        )
    }
}
