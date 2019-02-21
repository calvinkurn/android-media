package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.expresscheckout.R

/**
 * Created by Irfan Khoirul on 30/01/19.
 */

class LoadingViewHolder(val view: View) : LoadingViewholder(view) {

    companion object {
        val LAYOUT = R.layout.item_loading_shimmering_atc_express
    }

}