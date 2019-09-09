package com.tokopedia.officialstore.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.presentation.adapter.viewmodel.ExclusiveBrandViewModel

class ExclusiveBrandViewHolder(view: View?): AbstractViewHolder<ExclusiveBrandViewModel>(view) {

    override fun bind(element: ExclusiveBrandViewModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_exclusive_brand
    }

}