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

    @LayoutRes
    val LAYOUT: Int = R.layout.ent_banner_view

    constructor(itemView: View?) : super(itemView)

    override fun bind(element: BannerViewModel) {

    }
}