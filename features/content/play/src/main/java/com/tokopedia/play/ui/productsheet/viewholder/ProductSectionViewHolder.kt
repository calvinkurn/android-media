package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.view.uimodel.PlayProductSectionUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.unifycomponents.timer.TimerUnifySingle

/**
 * @author by astidhiyaa on 27/01/22
 */
class ProductSectionViewHolder(
    itemView: View, private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvSectionTitle: TextView = itemView.findViewById(R.id.tv_header_title)
    private val tvTimerInfo: TextView = itemView.findViewById(R.id.tv_header_info)
    private val timerSection: TimerUnifySingle = itemView.findViewById(R.id.section_timer)
    private val rvProducts: RecyclerView = itemView.findViewById(R.id.rv_product)

    private val adapter: ProductLineAdapter =
        ProductLineAdapter(object : ProductLineViewHolder.Listener {
            override fun onBuyProduct(product: PlayProductUiModel.Product) {

            }

            override fun onAtcProduct(product: PlayProductUiModel.Product) {
            }

            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
            }
        })

    init {
        rvProducts.layoutManager = LinearLayoutManager(itemView.context)
        rvProducts.adapter = adapter
    }

    fun bind(item: PlayProductSectionUiModel.ProductSection) {
        tvSectionTitle.text = item.title
        tvTimerInfo.text = item.timerInfo

        adapter.setItemsAndAnimateChanges(itemList = item.productList)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_play_product_section_header
    }

    interface Listener {
        //onTimerExpired
        //TODO() = setup listener
    }
}

