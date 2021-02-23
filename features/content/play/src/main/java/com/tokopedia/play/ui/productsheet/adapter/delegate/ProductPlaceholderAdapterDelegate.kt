package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 13/03/20
 */
class ProductPlaceholderAdapterDelegate : TypedAdapterDelegate<PlayProductUiModel.Placeholder, PlayProductUiModel, RecyclerView.ViewHolder>(R.layout.item_play_product_placeholder) {

    override fun onBindViewHolder(item: PlayProductUiModel.Placeholder, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
        return BaseViewHolder(basicView)
    }
}