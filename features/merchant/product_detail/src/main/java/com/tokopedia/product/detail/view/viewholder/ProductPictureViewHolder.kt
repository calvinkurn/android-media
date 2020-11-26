package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.pdp_picture_view_holder.view.*

/**
 * Created by Yehezkiel on 25/11/20
 */
class ProductPictureViewHolder(private val view: View) : AbstractViewHolder<MediaDataModel>(view) {

    companion object {
        const val MEDIA_IMAGE_TYPE = "image"
        val LAYOUT = R.layout.pdp_picture_view_holder
    }

    override fun bind(data: MediaDataModel) {
        view.pdp_main_img.loadImageWithoutPlaceholder(data.urlOriginal)
    }
}