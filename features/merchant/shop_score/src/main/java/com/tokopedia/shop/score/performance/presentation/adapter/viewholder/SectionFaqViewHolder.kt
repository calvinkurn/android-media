package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.databinding.SectionFaqShopScoreBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemFaqAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemFaqListener
import com.tokopedia.shop.score.performance.presentation.adapter.SectionFaqListener
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SectionFaqViewHolder(view: View, private val sectionFaqListener: SectionFaqListener) :
    AbstractViewHolder<SectionFaqUiModel>(view), ItemFaqListener {

    companion object {
        val LAYOUT = R.layout.section_faq_shop_score
    }

    private var itemFaqAdapter: ItemFaqAdapter? = null

    private val binding: SectionFaqShopScoreBinding? by viewBinding()

    override fun bind(element: SectionFaqUiModel?) {
        binding?.run {
            itemFaqAdapter = ItemFaqAdapter(this@SectionFaqViewHolder)

            tvLabelHelpCenter.text =
                MethodChecker.fromHtml(getString(R.string.title_help_center_tokopedia))

            rvFaqShopScore.run {
                layoutManager = LinearLayoutManager(context)
                adapter = itemFaqAdapter
                isNestedScrollingEnabled = false
                element?.itemFaqUiModelList?.let { itemFaqAdapter?.setItemFaqList(it) }
            }

            tvLabelHelpCenter.setTextMakeHyperlink(getString(R.string.title_help_center_tokopedia)) {
                sectionFaqListener.onHelpCenterClicked()
            }

            sectionFaqListener.onImpressHelpCenter()
        }
    }

    override fun onArrowClicked(position: Int) {
        itemFaqAdapter?.updateArrowItemFaq(position)
    }
}