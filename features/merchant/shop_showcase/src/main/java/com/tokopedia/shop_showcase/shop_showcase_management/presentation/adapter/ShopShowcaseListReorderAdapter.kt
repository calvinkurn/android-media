package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder
import com.tokopedia.shop_showcase.common.ShopShowcaseReorderListener

class ShopShowcaseListReorderAdapter(
        val listener: ShopShowcaseReorderListener,
        val onStartDragListener: OnStartDragListener?,
        private val isMyShop: Boolean
) : RecyclerView.Adapter<ShopShowcaseListReorderAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private var generatedSowcaseList: Int = 0
    private var showcaseList: MutableList<ShopEtalaseModel> = mutableListOf()
    val _showcaseList: List<ShopEtalaseModel>
        get() = showcaseList

    fun updateDataShowcaseList(showcaseListData: ArrayList<ShopEtalaseModel>) {
        showcaseList = showcaseListData.toMutableList()

        // Handling undragable list
        for (showcase in showcaseList) {
            if (showcase.type == ShopEtalaseTypeDef.ETALASE_DEFAULT) {
                generatedSowcaseList += 1
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateLayout(
                ShopShowcaseListImageBaseViewHolder.LAYOUT
        ))
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(showcaseList[position])
    }

    override fun onItemDismiss(position: Int) {
        // no-op
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val modelFrom = showcaseList[fromPosition]

        // Handling drag and undragable list
        if (toPosition < generatedSowcaseList) {
            return false
        } else {
            showcaseList.removeAt(fromPosition)
            showcaseList.add(toPosition, modelFrom)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }
    }

    inner class ViewHolder(itemView: View) : ShopShowcaseListImageBaseViewHolder(itemView) {

        init {
            showcaseActionButton = itemView.findViewById(R.id.img_move_showcase)
        }

        override fun bind(element: Any) {
            // cast to actual ui model
            val elementUiModel = element as ShopEtalaseModel

            // render showcase info
            renderShowcaseMainInfo(elementUiModel, isMyShop = true)

            // set listener for showcase action button
            showcaseActionButton?.apply {
                // handle showcase action drag listener
                setOnTouchListener { _, event ->
                    @Suppress("DEPRECATION")
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        onStartDragListener?.onStartDrag(this@ViewHolder)
                    }
                    false
                }
            }
        }
    }
}
