package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.viewmodel.EmptyHomeModel

class EmptyHomeEventViewHolder(itemView: View): AbstractViewHolder<EmptyHomeModel>(itemView)  {

    override fun bind(element: EmptyHomeModel) {

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_shimering_home
    }
}