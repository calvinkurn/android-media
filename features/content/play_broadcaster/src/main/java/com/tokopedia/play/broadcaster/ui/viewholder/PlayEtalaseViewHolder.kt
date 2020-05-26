package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.ProductPreviewItemDecoration
import com.tokopedia.play.broadcaster.view.adapter.PlayProductPreviewAdapter
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val rvProductPreview: RecyclerView = itemView.findViewById(R.id.rv_product_preview)

    private val productPreviewAdapter = PlayProductPreviewAdapter()

    init {
        rvProductPreview.layoutManager = GridLayoutManager(rvProductPreview.context, 2, RecyclerView.HORIZONTAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return when (itemCount) {
                        3 -> if (position / 2 > 0) 2 else 1
                        4 -> 1
                        else -> 2
                    }
                }
            }
        }
//        rvProductPreview.layoutManager = GridLayoutManager(rvProductPreview.context, 2)
        rvProductPreview.adapter = productPreviewAdapter
        rvProductPreview.viewTreeObserver.addOnPreDrawListener {
            if (rvProductPreview.itemDecorationCount == 0)
                rvProductPreview.addItemDecoration(ProductPreviewItemDecoration(rvProductPreview.context))
            true
        }
    }

    fun bind(item: PlayEtalaseUiModel) {
        productPreviewAdapter.setItemsAndAnimateChanges(item.productPreviewList)
    }

    companion object {
        val LAYOUT = R.layout.item_etalase_play
    }
}