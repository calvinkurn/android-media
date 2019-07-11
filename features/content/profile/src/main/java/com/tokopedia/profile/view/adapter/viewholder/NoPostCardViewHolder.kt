package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.NoPostCardViewModel
import kotlinx.android.synthetic.main.item_profile_kol_no_post.view.*

class NoPostCardViewHolder(val v: View) : AbstractViewHolder<NoPostCardViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_profile_kol_no_post
        val imagePath = "https://ecs7.tokopedia.net/img/android/profile/xxxhdpi/img_empty_profile.png"
    }

    override fun bind(element: NoPostCardViewModel?) {
        with(itemView) {
            image_view.loadImage(imagePath)
            tv_description.text = context.getString(R.string.follow_x_fox_inspiration, element?.name)
        }
    }

}