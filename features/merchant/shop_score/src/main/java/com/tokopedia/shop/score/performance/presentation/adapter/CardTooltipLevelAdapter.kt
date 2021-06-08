package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.CardTooltipLevelUiModel
import kotlinx.android.synthetic.main.item_card_level_information.view.*

class CardTooltipLevelAdapter: RecyclerView.Adapter<CardTooltipLevelAdapter.CardTooltipLevelViewHolder>() {

    private var cardToolTipLevelList = mutableListOf<CardTooltipLevelUiModel>()

    fun setCardToolTipLevelList(cardToolTipLevelList: List<CardTooltipLevelUiModel>) {
        if (cardToolTipLevelList.isNullOrEmpty()) return
        this.cardToolTipLevelList.clear()
        this.cardToolTipLevelList.addAll(cardToolTipLevelList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTooltipLevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_level_information, parent,false)
        return CardTooltipLevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardTooltipLevelViewHolder, position: Int) {
        val data = cardToolTipLevelList.getOrNull(position) ?: CardTooltipLevelUiModel()
        holder.bind(data)
    }

    override fun getItemCount(): Int = cardToolTipLevelList.size

    inner class CardTooltipLevelViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(data: CardTooltipLevelUiModel) {
            with(itemView) {
                tv_title_level_card?.text = data.title?.let { context.getString(it) }
                tv_description_level_card?.text = MethodChecker.fromHtml(data.desc?.let { context.getString(it) })
                if (data.isMyShop) {
                    cardLevelInformation?.setCardBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G200))
                    levelLabel?.show()
                } else {
                    cardLevelInformation?.setCardBackgroundColor(ContextCompat.getColor(context, R.color.shop_score_dms_card_level))
                    levelLabel?.hide()
                }
            }
        }
    }
}