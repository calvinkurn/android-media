package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.EmptyAffiliateViewModel
import kotlinx.android.synthetic.main.item_profile_affiliate_empty.view.*

/**
 * @author by milhamj on 31/10/18.
 */
class EmptyAffiliateViewHolder(val v: View, val listener: OnEmptyItemClickedListener) : AbstractViewHolder<EmptyAffiliateViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_profile_affiliate_empty

        private const val PATH_FORMAT = "%s/%s/%s/%s.jpg"
        private const val ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android"
        private const val IMAGE_FOLDER = "profile"
        private const val IMAGE_SIZE = "xxxhdpi"
        private const val IMAGE_NAME = "img_empty_profile"
    }
    override fun bind(element: EmptyAffiliateViewModel?) {
        val imageUrl = String.format(
                PATH_FORMAT,
                ANDROID_IMAGE_URL,
                IMAGE_FOLDER,
                IMAGE_SIZE,
                IMAGE_NAME
        )
        ImageHandler.loadImage2(itemView.image, imageUrl, R.drawable.ic_loading_image)
        itemView.card_image.setOnClickListener(onEmptyItemClicked())
        itemView.tv_see_more_product.setOnClickListener(onEmptyItemClicked())
        itemView.tv_title.text = MethodChecker.fromHtml(itemView.context.resources.getString(R.string.profile_empty_title))
    }

    private fun onEmptyItemClicked() : View.OnClickListener {
        return View.OnClickListener {
            listener.onEmptyComponentClicked()
        }
    }

    interface OnEmptyItemClickedListener {
        fun onEmptyComponentClicked()
    }
}