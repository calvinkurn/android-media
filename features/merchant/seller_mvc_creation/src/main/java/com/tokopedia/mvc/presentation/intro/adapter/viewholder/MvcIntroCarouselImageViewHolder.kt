package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.ImageUnify

class MvcIntroCarouselImageViewHolder(private val itemView: View?) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_item_intro_carousel
    }

    private var image: ImageUnify? = null

    init {
        bindViews()
    }

    fun bind(element: String?) {
        if (element != null) {
            image?.setImageUrl(element)
        }
    }

    private fun bindViews() {
        image = itemView?.findViewById(R.id.image)
    }
}
