package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.DynamicChannelViewModel

class DynamicChannelViewHolder(view: View?): AbstractViewHolder<DynamicChannelViewModel>(view){

    private var textView: AppCompatTextView? = null

    init {
        textView = view?.findViewById(R.id.sample_text)
    }

    override fun bind(element: DynamicChannelViewModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_dynamic_channel
    }

}