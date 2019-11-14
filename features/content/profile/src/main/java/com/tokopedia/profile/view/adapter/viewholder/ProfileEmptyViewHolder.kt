package com.tokopedia.profile.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel
import kotlinx.android.synthetic.main.item_profile_empty.view.*

/**
 * @author by milhamj on 31/10/18.
 */
class ProfileEmptyViewHolder(val v: View) : AbstractViewHolder<ProfileEmptyViewModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_profile_empty

        private const val PATH_FORMAT = "%s/%s/%s/%s.jpg"
        private const val ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android"
        private const val IMAGE_NAME = "af_teaser"
    }
    override fun bind(element: ProfileEmptyViewModel?) {
        val imageUrl = String.format(
                PATH_FORMAT,
                ANDROID_IMAGE_URL,
                IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(itemView.context),
                IMAGE_NAME
        )
        ImageHandler.loadImage2(itemView.image, imageUrl, com.tokopedia.design.R.drawable.ic_loading_image)
    }
}