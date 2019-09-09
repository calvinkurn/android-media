package com.tokopedia.officialstore.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.presentation.adapter.viewmodel.BrandPopulerViewModel

class BrandPopulerViewHolder(view: View?) : AbstractViewHolder<BrandPopulerViewModel>(view) {


    override fun bind(element: BrandPopulerViewModel) {

    }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_brand_populer
    }
}