package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography

class ProductArViewHolder(
        val view: View,
        private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ArButtonDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.custom_button_view_holder
    }

    private val imgCustom: ImageView? = view.findViewById(R.id.img_custom_button)
    private val txtMessage: Typography? = view.findViewById(R.id.txt_custom_button)

    override fun bind(element: ArButtonDataModel) {
        showOrHideElement(element.message)

        imgCustom?.loadImage(element.imageUrl)
        txtMessage?.text = element.message
        itemView.setOnClickListener {
            listener.goToApplink(element.applink)
        }
    }

    private fun showOrHideElement(message: String) {
        if (message.isEmpty()) {
            itemView.layoutParams.height = 0
        } else {
            itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

}