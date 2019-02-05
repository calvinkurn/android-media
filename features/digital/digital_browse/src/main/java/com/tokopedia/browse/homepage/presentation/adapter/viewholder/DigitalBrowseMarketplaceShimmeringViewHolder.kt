package com.tokopedia.browse.homepage.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.browse.R

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseMarketplaceShimmeringViewHolder(itemView: View) :
        AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) {}

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_digital_browser_marketplace_shimmering_loading
    }
}
