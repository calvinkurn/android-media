package com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_faq.view.*

class FaqSearchViewHolder(private val view: View,
                          private val faqSearchListener: FaqSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_faq
    }

    private val adapterFaq by lazy { ItemFaqSearchAdapter(faqSearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        with(view) {
            if(element.hasMore) {
                tvMoreResultFaq?.apply {
                    show()
                    text = element.actionTitle.orEmpty()
                    setOnClickListener {
                        faqSearchListener.onFaqMoreClicked(element, adapterPosition)
                    }
                }
            }
            tvTitleResultFaq?.text = element.title
            rvResultFaq?.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = adapterFaq
            }
        }

        if (element.sellerSearchList.isNotEmpty()) {
            element.takeIf { it.id == GlobalSearchSellerConstant.FAQ }?.sellerSearchList?.let {
                adapterFaq.submitList(it)
            }
        }
    }
}