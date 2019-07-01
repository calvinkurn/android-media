package com.tokopedia.home_recom.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.TitleDataModel

class TitleViewHolder(private val view: View) : AbstractViewHolder<TitleDataModel>(view){
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }

    override fun bind(element: TitleDataModel?) {
        title.text = element?.title
    }

}