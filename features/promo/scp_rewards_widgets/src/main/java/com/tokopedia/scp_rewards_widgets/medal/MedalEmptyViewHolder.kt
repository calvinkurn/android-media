package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.MedalViewEmptyLayoutBinding

class MedalEmptyViewHolder(itemView: View) : AbstractViewHolder<MedalError>(itemView) {

    companion object {
        val LAYOUT = R.layout.medal_view_empty_layout
    }

    private var binding: MedalViewEmptyLayoutBinding? = null

    init {
        binding = MedalViewEmptyLayoutBinding.bind(itemView)
    }

    override fun bind(data: MedalError?) {
        binding?.let { safeBinding ->
            data?.let {
                it.imageUrl?.let { imageUrl ->
                    safeBinding.errorImage.setImageUrl(imageUrl)
                } ?: run {
                    safeBinding.errorImage.setImageDrawable(ContextCompat.getDrawable(safeBinding.root.context, R.drawable.ic_error_medal_list))
                }
            }
        }
    }
}
