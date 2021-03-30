package com.tokopedia.feedplus.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.Button
import android.widget.ImageView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel

/**
 * Created by meyta on 1/29/18.
 */

class EmptyFeedBeforeLoginViewHolder(itemView: View, listener: EmptyFeedBeforeLoginListener) : AbstractViewHolder<EmptyFeedBeforeLoginModel>(itemView) {

    companion object {

        private val ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android"
        private val FINISH_IMAGE_NAME = "ic_empty_feed_nonlogin"
        private val IMAGE_URL_FORMAT = "%s/%s/%s/%s.png"

        @LayoutRes
        val LAYOUT = R.layout.list_feed_before_login
    }

    init {
        val imageUrl = String.format(IMAGE_URL_FORMAT,
                ANDROID_IMAGE_URL,
                FINISH_IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(itemView.context),
                FINISH_IMAGE_NAME
        )

        val button = itemView.findViewById<Button>(R.id.btn_login)
        button.setOnClickListener { view -> listener.onGoToLogin() }
        val imageView = itemView.findViewById<ImageView>(R.id.iv_image)
        ImageHandler.loadImage2(imageView, imageUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
    }

    override fun bind(element: EmptyFeedBeforeLoginModel) {}

    interface EmptyFeedBeforeLoginListener {
        fun onGoToLogin()
    }

}