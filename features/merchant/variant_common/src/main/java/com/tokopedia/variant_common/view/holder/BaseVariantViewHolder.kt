package com.tokopedia.variant_common.view.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Yehezkiel on 08/03/20
 */
abstract class BaseVariantViewHolder<VariantOptionWithAttribute>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(element: VariantOptionWithAttribute)
    abstract fun bind(element: VariantOptionWithAttribute, payload: Int)
}
