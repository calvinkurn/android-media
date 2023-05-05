package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.databinding.ItemPlayWidgetLiveContentBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetUpcomingContentViewHolder(
    private val binding: ItemPlayWidgetLiveContentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: PlayWidgetChannelUiModel) {
        binding.tvLiveBadge.showWithCondition(data.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = data.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name
    }

    companion object {

        fun create(parent: ViewGroup): PlayWidgetUpcomingContentViewHolder {
            return PlayWidgetUpcomingContentViewHolder(
                ItemPlayWidgetLiveContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
