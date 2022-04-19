package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.TooltipLevelItemDecoration
import com.tokopedia.shop.score.databinding.ItemFaqShopScoreBinding
import com.tokopedia.shop.score.performance.presentation.model.ItemFaqUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemFaqAdapter(private var itemFaqListener: ItemFaqListener) :
    RecyclerView.Adapter<ItemFaqAdapter.ItemFaqViewHolder>() {

    private val itemFaqList = mutableListOf<ItemFaqUiModel>()

    fun updateArrowItemFaq(position: Int) {
        itemFaqList.mapIndexed { index, itemFaqUiModel ->
            if (itemFaqUiModel.isShow) {
                itemFaqUiModel.isShow = false
                notifyItemChanged(index, PAYLOAD_TOGGLE_FAQ)
            } else if (index == position) {
                itemFaqUiModel.isShow = true
                notifyItemChanged(index, PAYLOAD_TOGGLE_FAQ)
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_faq_shop_score, parent, false)
        return ItemFaqViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemFaqViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val keyPayload = payloads.getOrNull(0) as? Int
            if (keyPayload == PAYLOAD_TOGGLE_FAQ) {
                holder.bindPayload(itemFaqList[position])
            }
        }
    }

    override fun onBindViewHolder(holder: ItemFaqViewHolder, position: Int) {
        val data = itemFaqList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemFaqList.size

    inner class ItemFaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemFaqShopScoreBinding? by viewBinding()
        private var cardTooltipLevelAdapter: CardTooltipLevelAdapter? = null
        private var itemParameterFaqAdapter: ItemParameterFaqAdapter? = null

        fun bind(data: ItemFaqUiModel) {
            binding?.run {
                tvTitleFaqShopScore.text =
                    data.title?.let { root.context.getString(it) }.orEmpty()
                tvDescFaqShopScore.showWithCondition(data.isShow)
                tvDescFaqShopScore.text = MethodChecker.fromHtml(
                    data.desc_first?.let { root.context.getString(it) }.orEmpty()
                )
                tvDescParameterPerformanceFaq.text = data.desc_second?.let {
                    root.context.getString(it)
                }.orEmpty()

                initAdapterCardLevel(data)
                initAdapterParameterFaq(data)

                cardShopScoreParameterFaq.showWithCondition(
                    data.isShow && data.isCalculationScore
                )
                tvDescParameterPerformanceFaq.showWithCondition(
                    data.isShow && data.isCalculationScore
                )
                rvCardLevelFaq.showWithCondition(
                    data.isShow && data.isCalculationScore
                )
                rvShopScoreParameterFaq.showWithCondition(
                    data.isShow && data.isCalculationScore
                )

                if (data.isShow) {
                    icInfoToggleFaq.rotation = REVERSE_ROTATION
                } else {
                    icInfoToggleFaq.rotation = NO_ROTATION
                }

                icInfoToggleFaq.setOnClickListener {
                    itemFaqListener.onArrowClicked(adapterPosition)
                }
            }
        }

        fun bindPayload(data: ItemFaqUiModel) {
            binding?.run {
                tvDescFaqShopScore.showWithCondition(data.isShow)

                cardShopScoreParameterFaq.showWithCondition(data.isShow && data.isCalculationScore)
                tvDescParameterPerformanceFaq.showWithCondition(data.isShow && data.isCalculationScore)
                rvCardLevelFaq.showWithCondition(data.isShow && data.isCalculationScore)
                rvShopScoreParameterFaq.showWithCondition(data.isShow && data.isCalculationScore)

                if (data.isShow) {
                    icInfoToggleFaq.rotation = REVERSE_ROTATION
                } else {
                    icInfoToggleFaq.rotation = NO_ROTATION
                }
            }
        }

        private fun initAdapterCardLevel(data: ItemFaqUiModel) {
            binding?.run {
                cardTooltipLevelAdapter = CardTooltipLevelAdapter()
                rvCardLevelFaq.run {
                    if (itemDecorationCount.isZero()) {
                        addItemDecoration(TooltipLevelItemDecoration())
                    }
                    layoutManager = GridLayoutManager(
                        context, 2,
                        GridLayoutManager.VERTICAL, false
                    )
                    adapter = cardTooltipLevelAdapter
                    isNestedScrollingEnabled = false
                }
                cardTooltipLevelAdapter?.setCardToolTipLevelList(data.cardLevelList)
            }
        }

        private fun initAdapterParameterFaq(data: ItemFaqUiModel) {
            if (data.parameterFaqList.isNotEmpty()) {
                binding?.run {
                    itemParameterFaqAdapter = ItemParameterFaqAdapter()
                    rvShopScoreParameterFaq.run {
                        layoutManager =
                            if (DeviceScreenInfo.isTablet(context)) {
                                GridLayoutManager(context, data.parameterFaqList.size)
                            } else {
                                LinearLayoutManager(context)
                            }
                        adapter = itemParameterFaqAdapter
                        isNestedScrollingEnabled = false
                    }
                    itemParameterFaqAdapter?.setParameterFaqList(data.parameterFaqList)
                }
            }
        }
    }

    companion object {
        const val NO_ROTATION = 0F
        const val REVERSE_ROTATION = 180F
        const val PAYLOAD_TOGGLE_FAQ = 805
    }
}