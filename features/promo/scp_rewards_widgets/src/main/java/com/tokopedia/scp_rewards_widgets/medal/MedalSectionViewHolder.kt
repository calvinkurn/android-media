package com.tokopedia.scp_rewards_widgets.medal

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.utils.loadImageOrFallback
import com.tokopedia.scp_rewards_common.utils.parseColor
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.common.GridSpacingItemDecoration
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalSectionBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.scp_rewards_common.R as scp_rewards_commonR

class MedalSectionViewHolder(
    itemView: View,
    private val listener: MedalCallbackListener?
) : AbstractViewHolder<MedalData>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_medal_section
        private const val VERTICAL_SPACING = 8
        private const val HORIZONTAL_SPACING = 30
    }

    private var binding: ItemMedalSectionBinding = ItemMedalSectionBinding.bind(itemView)

    private val medalsAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory(listener))
    }

    init {
        binding.rvMedals.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = medalsAdapter
            addItemDecoration(GridSpacingItemDecoration(HORIZONTAL_SPACING.toPx(), VERTICAL_SPACING.toPx(), false))
            isNestedScrollingEnabled = false
        }
    }

    override fun bind(item: MedalData) {
        with(binding) {
            tvSectionTitle.text = item.title
            tvSectionTitle.setTextColor(parseColor(item.textColor) ?: Color.BLACK)
        }
        if (item.cta?.isShown == true) {
            binding.btnSeeMore.text = item.cta.text
            binding.btnSeeMore.visible()
            listener?.let {
                listener.onSeeMoreLoad(item)
                binding.btnSeeMore.setOnClickListener { listener.onSeeMoreClick(item) }
            }
        } else {
            binding.btnSeeMore.gone()
        }
        handleList(item.medalList, item.bannerData, item.id)
        handleDivider()
    }

    private fun handleDivider() {
        if (bindingAdapterPosition == (this.bindingAdapter?.itemCount?.minus(1) ?: 0)) {
            binding.divider.gone()
        } else {
            binding.divider.visible()
        }
    }

    private fun handleList(medalList: List<MedalItem>?, bannerData: BannerData?, position: Int?) {
        if (medalList.isNullOrEmpty()) {
            binding.ivEmptyList.loadImageOrFallback(bannerData?.imageUrl, scp_rewards_commonR.drawable.ic_error_medal_list) {
                val layoutParams = binding.ivEmptyList.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.dimensionRatio = "H, 0.4"
                binding.ivEmptyList.layoutParams = layoutParams
            }
            binding.ivEmptyList.visible()
            binding.rvMedals.gone()
            binding.ivEmptyList.setOnClickListener { listener?.onBannerClick(bannerData, position) }
        } else {
            val placeHolderList = mutableListOf<MedalItem>().apply { addAll(medalList) }
            val remainder = medalList.size % 3
            if (remainder != 0) {
                repeat(3 - remainder) {
                    placeHolderList.add(MedalItem(isPlaceHolder = true))
                }
            }
            medalsAdapter.setVisitables(placeHolderList.toList())
            binding.ivEmptyList.gone()
            binding.rvMedals.visible()
        }
    }
}
