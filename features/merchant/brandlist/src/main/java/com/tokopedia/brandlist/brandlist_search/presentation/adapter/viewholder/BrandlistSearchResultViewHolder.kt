package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchViewModel

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

class BrandlistSearchResultViewHolder(view: View): AbstractViewHolder<BrandlistSearchViewModel>(view) {

    private var context: Context
    private var imgIconNotFound: ImageView? = null

    init {
        imgIconNotFound = view?.findViewById(R.id.)
    }

    override fun bind(element: BrandlistSearchViewModel?) {
        ImageHandler.loadImage()
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_item_search_result
    }

}