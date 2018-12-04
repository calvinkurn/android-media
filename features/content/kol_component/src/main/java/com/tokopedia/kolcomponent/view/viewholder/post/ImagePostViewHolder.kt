package com.tokopedia.kolcomponent.view.viewholder.post

import android.os.Build
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.viewmodel.post.ImagePostViewModel
import kotlinx.android.synthetic.main.item_post_image.view.*

/**
 * @author by milhamj on 04/12/18.
 */
class ImagePostViewHolder : BasePostViewHolder<ImagePostViewModel>() {

    override var layoutRes = R.layout.item_post_image

    override fun bind(element: ImagePostViewModel) {
        itemView.image.setOnClickListener {
            //TODO milhamj
        }
        itemView.image.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.image.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        itemView.image.maxHeight = itemView.image.width
                        itemView.image.requestLayout()
                    }
                }
        )
        ImageHandler.loadImageRounded2(context, itemView.image, element.image)
    }
}