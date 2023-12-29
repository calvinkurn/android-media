package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_common.utils.loadImageOrFallback
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.MedalViewEmptyLayoutBinding
import com.tokopedia.scp_rewards_common.R as scp_rewards_commonR

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
                safeBinding.errorImage.loadImageOrFallback(it.imageUrl, scp_rewards_commonR.drawable.ic_error_medal_list) {
                    val layoutParams = safeBinding.errorImage.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.dimensionRatio = "H, 0.4"
                    safeBinding.errorImage.layoutParams = layoutParams
                }
            }
        }
    }
}
