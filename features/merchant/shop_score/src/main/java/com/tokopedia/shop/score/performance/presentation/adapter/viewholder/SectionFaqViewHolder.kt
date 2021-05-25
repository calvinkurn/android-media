package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.presentation.adapter.ItemFaqAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemFaqListener
import com.tokopedia.shop.score.performance.presentation.adapter.SectionFaqListener
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import kotlinx.android.synthetic.main.section_faq_shop_score.view.*

class SectionFaqViewHolder(view: View, private val sectionFaqListener: SectionFaqListener) : AbstractViewHolder<SectionFaqUiModel>(view), ItemFaqListener {

    companion object {
        val LAYOUT = R.layout.section_faq_shop_score
    }

    private var itemFaqAdapter: ItemFaqAdapter? = null

    override fun bind(element: SectionFaqUiModel?) {
        with(itemView) {
            itemFaqAdapter = ItemFaqAdapter(this@SectionFaqViewHolder)

            tv_label_help_center.text = MethodChecker.fromHtml(getString(R.string.title_help_center_tokopedia))

            rv_faq_shop_score?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemFaqAdapter
                isNestedScrollingEnabled = false
                element?.itemFaqUiModelList?.let { itemFaqAdapter?.setItemFaqList(it) }
            }

            tv_label_help_center?.setTextMakeHyperlink(getString(R.string.title_help_center_tokopedia)) {
                sectionFaqListener.onHelpCenterClicked()
            }

            sectionFaqListener.onImpressHelpCenter()
        }
    }

    override fun onArrowClicked(position: Int) {
        itemFaqAdapter?.updateArrowItemFaq(position)
    }
}