package com.tokopedia.gallery.adapter

import android.support.v4.app.FragmentManager
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gallery.GalleryView
import com.tokopedia.gallery.adapter.viewholder.GalleryItemViewHolder
import com.tokopedia.gallery.viewmodel.ImageReviewItem

/**
 * Created by errysuprayogi on 1/26/18.
 */

class GalleryAdapter(private val listener: GalleryView) : BaseAdapterTypeFactory(), TypeFactory {

    override fun type(viewModel: ImageReviewItem): Int {
        return GalleryItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == GalleryItemViewHolder.LAYOUT) {
            GalleryItemViewHolder(parent, listener)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}
