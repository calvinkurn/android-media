package com.tokopedia.play.broadcaster.ui.viewholder

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
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
class PlayEtalaseViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    private val tvEtalaseTitle: TextView = itemView.findViewById(R.id.tv_etalase_title)
    private val tvEtalaseAmount: TextView = itemView.findViewById(R.id.tv_etalase_amount)
    private val rvProductPreview: RecyclerView = itemView.findViewById(R.id.rv_product_preview)

    private val productPreviewAdapter = PlayProductPreviewAdapter(object : ProductPreviewViewHolder.Listener {
        override fun onProductImagesClicked() {
            itemView.performClick()
        }
    })

    private val context: Context = itemView.context

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
        rvProductPreview.adapter = productPreviewAdapter
        rvProductPreview.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (rvProductPreview.itemDecorationCount == 0) rvProductPreview.addItemDecoration(ProductPreviewItemDecoration())
                rvProductPreview.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    fun bind(item: PlayEtalaseUiModel) {
        tvEtalaseTitle.text = item.name
        tvEtalaseAmount.text = getString(R.string.play_etalase_product_amount, item.totalProduct)
        productPreviewAdapter.setItemsAndAnimateChanges(item.productList)

        itemView.setOnClickListener {
            listener.onEtalaseClicked(item.id)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_etalase_play
    }

    interface Listener {
        fun onEtalaseClicked(etalaseId: Long)
    }
}