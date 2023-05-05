package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.widget.databinding.ItemPlayWidgetLiveContentBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play_common.util.extension.updateLayoutParams

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetLiveContentViewHolder(
    private val binding: ItemPlayWidgetLiveContentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = data.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name
    }

    companion object {

        fun create(parent: ViewGroup): PlayWidgetLiveContentViewHolder {
            return PlayWidgetLiveContentViewHolder(
                ItemPlayWidgetLiveContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
