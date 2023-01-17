package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.databinding.PdpPictureViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 25/11/20
 */
class ProductPictureViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener?,
    private val componentTrackDataModel: ComponentTrackDataModel?,
    private val containerType: MediaContainerType
) : AbstractViewHolder<MediaDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.pdp_picture_view_holder
    }

    private val binding = PdpPictureViewHolderBinding.bind(view)

    init {
        setImageScale()
    }

    override fun bind(data: MediaDataModel) {
        view.setOnClickListener {
            listener?.onMainImageClicked(componentTrackDataModel, adapterPosition)
        }

        Glide.with(view.context)
            .load(data.urlOriginal)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .fitCenter()
            .into(binding.pdpMainImg)
    }

    private fun setImageScale() {
        when (containerType) {
            is MediaContainerType.Portrait -> {
                setFashionScale()
            }
            else -> {
                setNormalScale()
            }
        }
    }

    private fun setFashionScale() {
        binding.pdpMainImg.doCustomImageScale(
            scale = ImageView.ScaleType.CENTER_CROP,
            height = ViewGroup.LayoutParams.WRAP_CONTENT,
            adjustViewBounds = true,
            cropToPadding = true
        )
    }

    private fun setNormalScale() {
        binding.pdpMainImg.doCustomImageScale(
            scale = ImageView.ScaleType.FIT_CENTER,
            height = ViewGroup.LayoutParams.MATCH_PARENT,
            adjustViewBounds = false,
            cropToPadding = false
        )
    }

    private fun ImageView.doCustomImageScale(
        scale: ImageView.ScaleType,
        height: Int,
        adjustViewBounds: Boolean,
        cropToPadding: Boolean
    ) {
        updateLayoutParams<ViewGroup.LayoutParams> {
            this?.height = height
        }
        setAdjustViewBounds(adjustViewBounds)
        setCropToPadding(cropToPadding)

        // set scaleType must always be after setAdjustViewBounds
        scaleType = scale
    }
}