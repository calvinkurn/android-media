package com.tokopedia.kolcomponent.view.viewholder

import android.os.Build
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.viewmodel.ImageViewModel

/**
 * @author by milhamj on 04/12/18.
 */
class ImageViewHolder(v: ViewGroup): BaseViewHolder<ImageViewModel>(v) {

    override var layoutRes = R.layout.item_shimmering_list

    override fun bind(element: ImageViewModel) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)

        itemView.setOnClickListener {
            //TODO milhamj
        }
        imageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = imageView.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        imageView.maxHeight = imageView.width
                        imageView.requestLayout()
                    }
                }
        )
        ImageHandler.loadImageRounded2(imageView.context, imageView, element.image)
    }
}