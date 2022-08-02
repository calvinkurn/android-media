package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.LoadingUiModel

class LoadingViewHolder(itemView: View): AbstractViewHolder<LoadingUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_detail_shimmer
    }

    override fun bind(element: LoadingUiModel) {
    }
}