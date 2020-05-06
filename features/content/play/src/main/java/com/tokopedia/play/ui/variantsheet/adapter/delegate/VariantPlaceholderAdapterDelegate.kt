package com.tokopedia.play.ui.variantsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.variantsheet.adapter.viewholder.VariantPlaceholderViewHolder
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel

/**
 * Created by jegul on 14/03/20
 */
class VariantPlaceholderAdapterDelegate :
        TypedAdapterDelegate<VariantPlaceholderUiModel.Category, Any, VariantPlaceholderViewHolder>(R.layout.item_play_variant_placeholder) {

    override fun onBindViewHolder(item: VariantPlaceholderUiModel.Category, holder: VariantPlaceholderViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): VariantPlaceholderViewHolder {
        return VariantPlaceholderViewHolder(basicView)
    }
}