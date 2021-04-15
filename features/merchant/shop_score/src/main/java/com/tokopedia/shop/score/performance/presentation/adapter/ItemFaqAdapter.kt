package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.TooltipLevelItemDecoration
import com.tokopedia.shop.score.performance.presentation.model.ItemFaqUiModel
import kotlinx.android.synthetic.main.item_faq_shop_score.view.*

class ItemFaqAdapter(private var itemFaqListener: ItemFaqListener) : RecyclerView.Adapter<ItemFaqAdapter.ItemFaqViewHolder>() {

    private var itemFaqList = mutableListOf<ItemFaqUiModel>()

    fun updateArrowItemFaq(position: Int) {
        itemFaqList.mapIndexed { index, itemFaqUiModel ->
            if (index == position) {
                itemFaqUiModel.isShow = !itemFaqUiModel.isShow
                notifyItemChanged(position)
            }
        }
    }

    fun setItemFaqList(faqList: List<ItemFaqUiModel>) {
        if (faqList.isNullOrEmpty()) return
        itemFaqList.clear()
        itemFaqList.addAll(faqList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_shop_score, parent, false)
        return ItemFaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemFaqViewHolder, position: Int) {
        val data = itemFaqList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemFaqList.size

    inner class ItemFaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var cardTooltipLevelAdapter: CardTooltipLevelAdapter? = null
        private var itemParameterFaqAdapter: ItemParameterFaqAdapter? = null

        fun bind(data: ItemFaqUiModel) {
            with(itemView) {
                tv_title_faq_shop_score?.text = data.title
                tv_desc_faq_shop_score?.showWithCondition(data.isShow)
                tv_desc_faq_shop_score?.text = MethodChecker.fromHtml(data.desc_first)

                card_shop_score_parameter_faq?.showWithCondition(data.isShow && data.isCalculationScore)
                tv_desc_parameter_performance_faq?.showWithCondition(data.isShow && data.isCalculationScore)
                rv_card_level_faq?.showWithCondition(data.isShow && data.isCalculationScore)
                rv_shop_score_parameter_faq?.showWithCondition(data.isShow && data.isCalculationScore)

                if (data.isShow) {
                    ic_info_toggle_faq?.rotation = REVERSE_ROTATION
                } else {
                    ic_info_toggle_faq?.rotation = NO_ROTATION
                }

                if (data.isShow && data.isCalculationScore) {
                    initAdapterCardLevel(data)
                    initAdapterParameterFaq(data)
                    tv_desc_parameter_performance_faq?.text = data.desc_second
                }

                ic_info_toggle_faq?.setOnClickListener {
                    itemFaqListener.onArrowClicked(adapterPosition)
                }
            }
        }

        private fun initAdapterCardLevel(data: ItemFaqUiModel) {
            with(itemView) {
                cardTooltipLevelAdapter = CardTooltipLevelAdapter()
                rv_card_level_faq?.apply {
                    if (itemDecorationCount.isZero()) {
                        addItemDecoration(TooltipLevelItemDecoration())
                    }
                    layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    adapter = cardTooltipLevelAdapter
                    isNestedScrollingEnabled = false
                }
                cardTooltipLevelAdapter?.setCardToolTipLevelList(data.cardLevelList)
            }
        }

        private fun initAdapterParameterFaq(data: ItemFaqUiModel) {
            with(itemView) {
                itemParameterFaqAdapter = ItemParameterFaqAdapter()
                rv_shop_score_parameter_faq?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemParameterFaqAdapter
                    isNestedScrollingEnabled = false
                }
                itemParameterFaqAdapter?.setParameterFaqList(data.parameterFaqList)
            }
        }
    }

    companion object {
        const val NO_ROTATION = 0F
        const val REVERSE_ROTATION = 180F
    }
}