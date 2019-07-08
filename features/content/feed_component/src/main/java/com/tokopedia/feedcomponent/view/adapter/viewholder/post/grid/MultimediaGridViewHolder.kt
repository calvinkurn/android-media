package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.os.Build
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import kotlinx.android.synthetic.main.item_post_multimedia.view.*

/**
 * @author by yoasfs on 2019-07-01
 */
class MultimediaGridViewHolder()
    : BasePostViewHolder<MultimediaGridViewModel>() {


    override var layoutRes: Int = R.layout.item_post_multimedia

    override fun bind(element: MultimediaGridViewModel) {
        itemView.feedMultipleImageView.bind(element.mediaItemList)
        itemView.feedMultipleImageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.feedMultipleImageView.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        itemView.feedMultipleImageView.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
                        itemView.feedMultipleImageView.requestLayout()
                    }
                }
        )
    }
}