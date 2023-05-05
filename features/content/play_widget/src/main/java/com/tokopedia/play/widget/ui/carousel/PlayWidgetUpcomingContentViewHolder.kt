package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.databinding.ItemPlayWidgetUpcomingContentBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetUpcomingContentViewHolder(
    private val binding: ItemPlayWidgetUpcomingContentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: PlayWidgetChannelUiModel) {
        binding.tvStartTime.text = data.startTime
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name
    }

    companion object {

        fun create(parent: ViewGroup): PlayWidgetUpcomingContentViewHolder {
            return PlayWidgetUpcomingContentViewHolder(
                ItemPlayWidgetUpcomingContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
