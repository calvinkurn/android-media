package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeAllCategoryUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_all_category.view.*

class HomeAllCategoryViewHolder(itemView: View): AbstractViewHolder<HomeAllCategoryUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_home_all_category
    }

    override fun bind(data: HomeAllCategoryUiModel) {
        itemView.apply {
            textTitle.text = data.title
        }
    }
}