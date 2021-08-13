package com.tokopedia.topads.common.view.adapter.tips.viewholder

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.unifyprinciples.Typography

class TipsUiViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private val headerText = itemView.findViewById<Typography>(R.id.headerText)
    private val headerImage = itemView.findViewById<ImageView>(R.id.headerImage)

    private val rowText = itemView.findViewById<Typography>(R.id.rowText)
    private val rowImage = itemView.findViewById<ImageView>(R.id.rowImage)

    fun bindHeader(headerModel: TipsUiHeaderModel) {
        if (headerModel.headerRes != 0) {
            headerImage.setImageDrawable(getDrawable(itemView.context, headerModel.headerRes))
            headerImage.show()
        } else {
            headerImage.hide()
        }
        headerText.text = getString(headerModel.headerText)
    }

    fun bindRow(rowModel: TipsUiRowModel) {
        if (rowModel.rowRes != 0) {
            rowImage.setImageDrawable(getDrawable(itemView.context, rowModel.rowRes))
            rowImage.show()
        } else {
            rowImage.hide()
        }
        rowText.text = getString(rowModel.rowText)
    }
}