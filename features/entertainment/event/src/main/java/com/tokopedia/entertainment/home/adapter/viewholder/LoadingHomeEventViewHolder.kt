package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.viewmodel.LoadingHomeModel

class LoadingHomeEventViewHolder(itemView: View): AbstractViewHolder<LoadingHomeModel>(itemView)  {

    override fun bind(element: LoadingHomeModel) {

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_shimering_home
    }
}