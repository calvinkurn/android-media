package com.tokopedia.entertainment.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel

/**
 * Author errysuprayogi on 27,January,2020
 */
class BannerViewHolder: HomeViewHolder<BannerViewModel> {



    constructor(itemView: View) : super(itemView)

    override fun bind(element: BannerViewModel) {

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_banner_view
    }
}