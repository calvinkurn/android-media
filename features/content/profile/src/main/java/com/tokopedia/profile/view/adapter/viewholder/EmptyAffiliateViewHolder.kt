package com.tokopedia.profile.view.adapter.viewholder

import android.os.Build
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.loadImage
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
        val imagePath = "https://ecs7.tokopedia.net/img/android/profile/xxxhdpi/img_empty_profile.png"
    }
    override fun bind(element: EmptyAffiliateViewModel?) {
        itemView.card_image.setOnClickListener(onEmptyItemClicked())
        itemView.tv_see_more_product.setOnClickListener(onEmptyItemClicked())
        itemView.tv_title.text = itemView.context.resources.getString(R.string.profile_empty_title)

        itemView.image.loadImage(imagePath)
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