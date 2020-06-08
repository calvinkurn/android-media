package com.tokopedia.play.ui.variantsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel

/**
 * Created by jegul on 14/03/20
 */
class VariantOptionPlaceholderAdapterDelegate :
        TypedAdapterDelegate<VariantPlaceholderUiModel.Option, VariantPlaceholderUiModel.Option, RecyclerView.ViewHolder>(R.layout.item_play_variant_option_placeholder) {

    override fun onBindViewHolder(item: VariantPlaceholderUiModel.Option, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
        return BaseViewHolder(basicView)
    }
}