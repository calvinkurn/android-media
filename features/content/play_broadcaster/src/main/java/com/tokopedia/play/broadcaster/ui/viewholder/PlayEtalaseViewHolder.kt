package com.tokopedia.play.broadcaster.ui.viewholder

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.ProductPreviewItemDecoration
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.view.adapter.PlayProductPreviewAdapter
import com.tokopedia.play_common.util.extension.doOnPreDraw
import kotlin.math.max
import kotlin.math.min

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    private val tvEtalaseTitle: TextView = itemView.findViewById(R.id.tv_etalase_title)
    private val tvEtalaseAmount: TextView = itemView.findViewById(R.id.tv_etalase_amount)
    private val rvProductPreview: RecyclerView = itemView.findViewById(R.id.rv_product_preview)
    private val vClickArea: View = itemView.findViewById(R.id.v_click_area)

    private val productPreviewAdapter = PlayProductPreviewAdapter()

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
        rvProductPreview.doOnPreDraw {
            if (rvProductPreview.itemDecorationCount == 0)
                rvProductPreview.addItemDecoration(ProductPreviewItemDecoration())
        }
    }

    fun bind(item: EtalaseContentUiModel) {
        setupProductPreview(item.productMap, item.totalProduct)

        if (item.productMap.isEmpty() && item.totalProduct > 0) listener.onEtalaseBound(item.id)

        vClickArea.setOnClickListener {
            listener.onEtalaseClicked(item.id, item.name, (0 until rvProductPreview.childCount).mapNotNull {
                val childView = rvProductPreview.getChildAt(it)
                val viewHolder = rvProductPreview.getChildViewHolder(childView)
                if (viewHolder is ProductPreviewViewHolder) viewHolder.ivImage else null
            })
        }
        tvEtalaseTitle.text = item.name
        tvEtalaseAmount.text = getString(R.string.play_etalase_product_amount, item.totalProduct)
    }

    private fun setupProductPreview(productPreviewMap: Map<Int, List<ProductContentUiModel>>, totalProduct: Int) {
        if (productPreviewMap.isEmpty()) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) return

            val totalProductValidated = min(MAX_PREVIEW, max(MIN_PREVIEW, totalProduct))
            productPreviewAdapter.setItemsAndAnimateChanges(List(totalProductValidated) { ProductLoadingUiModel })
        } else {
            productPreviewAdapter.setItemsAndAnimateChanges(productPreviewMap.values.flatten())
        }
    }

    companion object {
        private const val MAX_PREVIEW = 4
        private const val MIN_PREVIEW = 1

        val LAYOUT = R.layout.item_play_etalase
    }

    interface Listener {

        fun onEtalaseBound(etalaseId: String)
        fun onEtalaseClicked(etalaseId: String, etalaseName: String, sharedElements: List<View>)
    }
}