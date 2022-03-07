package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype.PromotionTypeInputAdapter
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import kotlinx.android.synthetic.main.mvc_promo_type_list.view.*

class PromotionTypeInputListViewHolder(itemView: View) : AbstractViewHolder<PromotionTypeInputListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_promo_type_list
    }

    private val promotionTypeItemAdapterFactory by lazy {
        PromotionTypeItemAdapterFactory()
    }

    override fun bind(element: PromotionTypeInputListUiModel) {
        itemView.recycler_view?.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = PromotionTypeInputAdapter(promotionTypeItemAdapterFactory)
            (adapter as? PromotionTypeInputAdapter)?.setVisitables(element.inputList)
        }
    }
}