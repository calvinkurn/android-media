package com.tokopedia.scp_rewards_widgets.medal

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.common.GridSpacing
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalSectionBinding

class MedalSectionViewHolder(itemView: View) : AbstractViewHolder<MedalData>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_medal_section
        private const val VERTICAL_SPACING = 20
        private const val HORIZONTAL_SPACING = 20
    }

    private var binding: ItemMedalSectionBinding = ItemMedalSectionBinding.bind(itemView)

    private val medalsAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory())
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
        if ((item.medalList.isNullOrEmpty().not()) and (item.medalList!!.size >= 6)) {
            binding.btnSeeMore.visible()
        } else {
            binding.btnSeeMore.gone()
        }
        val remainder = item.medalList.size % 3
        if (remainder != 0) {
            val placeHolderList = mutableListOf<MedalItem>()
            repeat(3 - remainder) {
                placeHolderList.add(MedalItem(isPlaceHolder = true))
            }
            medalsAdapter.setVisitables(item.medalList + placeHolderList)
        } else {
            medalsAdapter.setVisitables(item.medalList)
        }
    }
}
