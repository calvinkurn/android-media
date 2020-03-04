package com.tokopedia.product.detail.view.viewholder.variant

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Yehezkiel on 2020-02-28
 */
abstract class BaseVariantViewHolder<VariantOptionWithAttribute>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(element: VariantOptionWithAttribute)
    abstract fun bind(element: VariantOptionWithAttribute, payload: Int)
}