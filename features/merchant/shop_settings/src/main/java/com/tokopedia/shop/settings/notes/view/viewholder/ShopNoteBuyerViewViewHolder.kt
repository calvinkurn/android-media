package com.tokopedia.shop.settings.notes.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.notes.data.ShopNoteBuyerViewUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopNoteBuyerViewViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_note_buyer_view
    }

    private var tpTitle: Typography? = null
    private var tpDescription: Typography? = null
    private var ivChevron: ImageView? = null

    private fun setupUi(title: String, description: String) {
        itemView.apply {

            tpTitle = findViewById(R.id.tp_title)
            tpDescription = findViewById(R.id.tp_description)
            ivChevron = findViewById(R.id.iv_chevron)

            tpTitle?.text = title
            tpDescription?.text = description

        }.setOnClickListener {

            if (tpDescription?.isVisible == true) {
                tpDescription?.gone()
                ivChevron?.loadImageDrawable(R.drawable.ic_shop_settings_chevron_down)
            } else {
                tpDescription?.show()
                ivChevron?.loadImageDrawable(R.drawable.ic_shop_settings_chevron_up)
            }

        }
    }

    fun bind(model: ShopNoteBuyerViewUiModel) {
        setupUi(
                title = model.title,
                description = model.description
        )
    }

}