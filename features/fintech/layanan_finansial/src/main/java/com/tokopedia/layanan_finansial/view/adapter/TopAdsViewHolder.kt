package com.tokopedia.layanan_finansial.view.adapter

import android.util.Log
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel

class TopAdsViewHolder(val view: View): AbstractViewHolder<Visitable<*>>(view) {

    companion object{
        val LAYOUT:Int = R.layout.layout_topads_sdk
    }


    override fun bind(element: Visitable<*>?) {
        Log.e("Hiiii", "bind: " )
    }
}