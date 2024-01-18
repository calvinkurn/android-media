package com.tokopedia.tokopedianow.annotation.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowAllAnnotationBinding
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class AnnotationViewHolder(
    itemView: View,
    private val listener: AnnotationListener? = null
): AbstractViewHolder<AnnotationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_all_annotation
    }

    private var binding: ItemTokopedianowAllAnnotationBinding? by viewBinding()

    override fun bind(data: AnnotationUiModel) {
        binding?.apply {
            tpAnnotationTitle.text = data.name

            iuAnnotationImage.clearImage()
            iuAnnotationImage.loadImage(data.imageUrl)

            root.setOnClickListener {
                directToAnotherPage(data.appLink)
                listener?.onClick(data, layoutPosition)
            }
            root.addOnImpressionListener(data) {
                listener?.onImpress(data, layoutPosition)
            }
        }
    }

    private fun ItemTokopedianowAllAnnotationBinding.directToAnotherPage(appLink: String) {
        if (appLink.isNotBlank()) {
            RouteManager.route(root.context, appLink)
        }
    }

    interface AnnotationListener {
        fun onClick(data: AnnotationUiModel, position: Int)
        fun onImpress(data: AnnotationUiModel, position: Int)
    }
}
