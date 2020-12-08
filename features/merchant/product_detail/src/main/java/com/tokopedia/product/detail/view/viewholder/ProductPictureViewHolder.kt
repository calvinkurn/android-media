package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.pdp_picture_view_holder.view.*

/**
 * Created by Yehezkiel on 25/11/20
 */
class ProductPictureViewHolder(private val view: View,
                               private val listener: DynamicProductDetailListener?,
                               private val componentTrackDataModel: ComponentTrackDataModel?) : AbstractViewHolder<MediaDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.pdp_picture_view_holder
    }

    override fun bind(data: MediaDataModel) {
        view.setOnClickListener {
            listener?.onMainImageClicked(componentTrackDataModel, adapterPosition)
        }
        Glide.with(view.context)
                .load(data.urlOriginal)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .into(view.pdp_main_img)
    }
}