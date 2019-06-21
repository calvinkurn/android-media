package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kol.R

/**
 * @author by nisie on 02/04/19.
 */
class EmptyPostDetailViewHolder(v: View) : EmptyViewHolder(v) {

    companion object {
        const val EMPTY_IMG_FORMAT = "https://ecs7.tokopedia.net/img/android/toped_logo_crying/%s/toped_logo_crying.png"
    }

    override fun bind(element: EmptyModel?) {
        super.bind(element)
        val screenDensity = DisplayMetricUtils.getScreenDensity(itemView.context)
        emptyIconImageView.loadImage(String.format(EMPTY_IMG_FORMAT, screenDensity))
    }

}