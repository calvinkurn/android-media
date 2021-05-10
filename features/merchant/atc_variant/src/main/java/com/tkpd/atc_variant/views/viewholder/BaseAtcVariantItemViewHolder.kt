package com.tkpd.atc_variant.views.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Yehezkiel on 06/05/21
 */
abstract class BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(element: VariantOptionWithAttribute)
    abstract fun bind(element: VariantOptionWithAttribute, payload: Int)
}