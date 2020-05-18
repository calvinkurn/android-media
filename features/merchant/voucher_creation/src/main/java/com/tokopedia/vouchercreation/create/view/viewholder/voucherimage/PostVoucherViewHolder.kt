package com.tokopedia.vouchercreation.create.view.viewholder.voucherimage

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import kotlinx.android.synthetic.main.mvc_post_image.view.*

class PostVoucherViewHolder(itemView: View?) : AbstractViewHolder<PostVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.mvc_post_image

    }

    override fun bind(element: PostVoucherUiModel) {
        itemView.run {
            Glide.with(context)
                    .load(element.postImageUrl)
                    .into(postVoucherImage)
        }
    }
}