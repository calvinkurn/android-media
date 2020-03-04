package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import com.tokopedia.sellerhome.settings.view.uimodel.ShopStatusUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.base.LoadableViewHolder

class ShopStatusViewHolder(itemView: View) : LoadableViewHolder<ShopStatusUiModel>(itemView) {

    companion object {
        val LAYOUT = null
    }

    override fun bind(element: ShopStatusUiModel) {
        observeUiState<ShopStatusUiModel>(element)
    }

    override fun renderSuccessLayout(uiModel: ShopStatusUiModel) {
        with(itemView) {

        }
    }

    override fun renderLoadingLayout() {

    }

    override fun renderNoDataLayout() {

    }

    override fun renderErrorLayout() {

    }
}