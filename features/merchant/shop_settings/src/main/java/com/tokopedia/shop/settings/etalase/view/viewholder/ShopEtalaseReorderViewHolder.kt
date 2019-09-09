package com.tokopedia.shop.settings.etalase.view.viewholder

import androidx.core.view.MotionEventCompat
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseReorderViewHolder(itemView: View,
                                   private val onStartDragListener: OnStartDragListener?) : AbstractViewHolder<ShopEtalaseViewModel>(itemView) {

    private val tvEtalaseName: TextView
    private val tvEtalaseCount: TextView
    private val handler: View

    init {
        tvEtalaseName = itemView.findViewById(R.id.tvEtalaseName)
        tvEtalaseCount = itemView.findViewById(R.id.tvEtalaseCount)
        handler = itemView.findViewById(R.id.ivReorder)
    }

    override fun bind(shopEtalaseViewModel: ShopEtalaseViewModel) {
        tvEtalaseName.text = shopEtalaseViewModel.name
        tvEtalaseCount.text = tvEtalaseCount.context.getString(R.string.x_products, shopEtalaseViewModel.count)
        if (shopEtalaseViewModel.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
            handler.setOnTouchListener { _, event ->
                @Suppress("DEPRECATION")
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this@ShopEtalaseReorderViewHolder)
                }
                false
            }
            handler.visibility = View.VISIBLE
        } else {
            handler.visibility = View.GONE
        }
    }

    companion object {

        val LAYOUT = R.layout.item_shop_etalase_reorder
    }

}
