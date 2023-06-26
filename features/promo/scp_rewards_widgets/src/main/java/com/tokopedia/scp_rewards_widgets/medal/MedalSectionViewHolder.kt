package com.tokopedia.scp_rewards_widgets.medal

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.common.GridSpacing
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalSectionBinding

class MedalSectionViewHolder(
    itemView: View,
    private val medalClickListener: MedalClickListener?
) : AbstractViewHolder<MedalData>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_medal_section
        private const val VERTICAL_SPACING = 20
        private const val HORIZONTAL_SPACING = 20
    }

    private var binding: ItemMedalSectionBinding = ItemMedalSectionBinding.bind(itemView)

    private val medalsAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory(medalClickListener))
    }

    init {
        binding.rvMedals.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = medalsAdapter
            addItemDecoration(GridSpacing(HORIZONTAL_SPACING, VERTICAL_SPACING))
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
            medalClickListener?.let {
                binding.btnSeeMore.setOnClickListener { medalClickListener.onSeeMoreClick(item) }
            }
        } else {
            binding.btnSeeMore.gone()
        }
        handleList(item.medalList, item.bannerData)
        handleDivider()
    }

    private fun handleDivider() {
        if (bindingAdapterPosition == (this.bindingAdapter?.itemCount?.minus(1) ?: 0)) {
            binding.divider.gone()
        } else {
            binding.divider.visible()
        }
    }

    private fun handleList(medalList: List<MedalItem>?, bannerData: BannerData?) {
        if (medalList.isNullOrEmpty()) {
            if (bannerData?.imageUrl.isNullOrEmpty()) {
                binding.ivEmptyList.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_error_medal_list))
            } else {
                binding.ivEmptyList.setImageUrl(bannerData?.imageUrl.orEmpty())
            }
            binding.ivEmptyList.visible()
            binding.rvMedals.gone()
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
