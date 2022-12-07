package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography

class ProductArViewHolder(
        val view: View,
        private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ArButtonDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.custom_button_view_holder
    }

    private val imgCustom: ImageView? = view.findViewById(R.id.img_custom_button)
    private val txtMessage: Typography? = view.findViewById(R.id.txt_custom_button)
    private val containerAr: ConstraintLayout? = view.findViewById(R.id.container_ar)

    override fun bind(element: ArButtonDataModel) {
        showOrHideElement(element.message)

        itemView.setBackgroundColor(ContextCompat.getColor(view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN50))
        imgCustom?.loadImage(element.imageUrl)
        txtMessage?.text = element.message
        itemView.setOnClickListener {
            listener.goToArPage(getComponentTrackData(element))
        }

        if (element.message.isNotEmpty()) {
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.showArCoachMark(containerAr)
            }
        }
    }

    private fun showOrHideElement(message: String) {
        if (message.isEmpty()) {
            listener.hideArCoachMark()
            itemView.layoutParams.height = 0
        } else {
            itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}