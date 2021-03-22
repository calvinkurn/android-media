package com.tokopedia.shop.settings.notes.view.viewholder

import android.text.Spanned
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.notes.data.ShopNoteBuyerViewUiModel
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography

class ShopNoteBuyerViewViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_note_buyer_view
    }

    private var tpTitle: Typography? = null
    private var tpDescription: Typography? = null
    private var ivChevron: ImageView? = null
    private var divider: DividerUnify? = null

    private fun setupUi(title: String, description: Spanned, isTheLastPosition: Boolean) {
        itemView.apply {

            tpTitle = findViewById(R.id.tp_title)
            tpDescription = findViewById(R.id.tp_description)
            ivChevron = findViewById(R.id.iv_chevron)
            divider = findViewById(R.id.divider)

            tpTitle?.text = title
            tpDescription?.text = description
            if (isTheLastPosition && tpDescription?.isVisible != true) {
                divider?.gone()
            }

        }.setOnClickListener {

            if (tpDescription?.isVisible == true) {
                tpDescription?.gone()
                ivChevron?.loadImageDrawable(R.drawable.ic_shop_chevron_down)
                if (isTheLastPosition) {
                    divider?.gone()
                }
            } else {
                tpDescription?.show()
                ivChevron?.loadImageDrawable(R.drawable.ic_shop_chevron_up)
                if (isTheLastPosition) {
                    divider?.show()
                }
            }

        }
    }

    fun bind(model: ShopNoteBuyerViewUiModel) {
        setupUi(
                title = model.title,
                description = MethodChecker.fromHtml(model.description),
                isTheLastPosition = model.isTheLastPosition
        )
    }

}