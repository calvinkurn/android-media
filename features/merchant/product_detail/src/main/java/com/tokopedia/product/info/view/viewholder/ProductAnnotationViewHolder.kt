package com.tokopedia.product.info.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.AnnotationValueDataModel

class ProductAnnotationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_specification_body
    }

    private val keyTxt: TextView = view.findViewById(R.id.key_specification)
    private val valueTxt: TextView = view.findViewById(R.id.value_specification)

    fun bind(element: AnnotationValueDataModel) {
        keyTxt.text = element.title
        valueTxt.text = MethodChecker.fromHtml(element.description)
    }
}