package com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_faq.view.*

class FaqSearchViewHolder(private val view: View,
                          private val faqSearchListener: FaqSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_faq
    }

    private var adapterFaq: ItemFaqSearchAdapter? = null

    override fun bind(element: SellerSearchUiModel) {
        adapterFaq = ItemFaqSearchAdapter(faqSearchListener)
        with(itemView) {
            element.takeIf { it.id == FAQ }?.let { faq ->
                if (faq.hasMore) {
                    tvMoreResultFaq?.apply {
                        show()
                        text = faq.actionTitle.orEmpty()
                        setOnClickListener {
                            faqSearchListener.onFaqMoreClicked(faq, adapterPosition)
                        }
                    }
                } else {
                    tvMoreResultFaq?.hide()
                }
                tvTitleResultFaq?.text = faq.title
                rvResultFaq?.apply {
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = adapterFaq
                }

                if (faq.sellerSearchList.isNotEmpty()) {
                    adapterFaq?.submitList(faq.sellerSearchList)
                }
            }
        }
    }
}