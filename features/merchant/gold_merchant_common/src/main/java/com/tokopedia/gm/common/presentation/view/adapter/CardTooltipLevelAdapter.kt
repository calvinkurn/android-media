package com.tokopedia.gm.common.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.databinding.GmcItemCardLevelInformationBinding
import com.tokopedia.gm.common.presentation.model.CardTooltipLevelUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

class CardTooltipLevelAdapter :
    RecyclerView.Adapter<CardTooltipLevelAdapter.CardTooltipLevelViewHolder>() {

    private val cardToolTipLevelList = mutableListOf<CardTooltipLevelUiModel>()

    fun setCardToolTipLevelList(cardToolTipLevelList: List<CardTooltipLevelUiModel>) {
        if (cardToolTipLevelList.isNullOrEmpty()) return
        this.cardToolTipLevelList.clear()
        this.cardToolTipLevelList.addAll(cardToolTipLevelList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTooltipLevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gmc_item_card_level_information, parent, false)
        return CardTooltipLevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardTooltipLevelViewHolder, position: Int) {
        val data = cardToolTipLevelList.getOrNull(position) ?: CardTooltipLevelUiModel()
        holder.bind(data)
    }

    override fun getItemCount(): Int = cardToolTipLevelList.size

    inner class CardTooltipLevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding : GmcItemCardLevelInformationBinding? by viewBinding()

        fun bind(data: CardTooltipLevelUiModel) {
            binding?.run {
                tvTitleLevelCard.text = data.title?.let { root.context.getString(it) }
                tvDescriptionLevelCard.text =
                    MethodChecker.fromHtml(data.desc?.let { root.context.getString(it) })
                if (data.isMyShop) {
                    cardLevelInformation.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_G200
                        )
                    )
                    levelLabel.show()
                } else {
                    cardLevelInformation.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_dms_card_level
                        )
                    )
                    levelLabel.hide()
                }
            }
        }
    }
}