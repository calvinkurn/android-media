package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseReorderListener

class ShopShowcaseListReorderAdapterOld(
        val listener: ShopShowcaseReorderListener,
        val onStartDragListener: OnStartDragListener?,
        private val isMyShop: Boolean
) : RecyclerView.Adapter<ShopShowcaseListReorderAdapterOld.ViewHolder>(), ItemTouchHelperAdapter {

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
        return ViewHolder(parent.inflateLayout(R.layout.shop_showcase_item_reorder_old))
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(showcaseList[position], position)
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        private var titleShowcase: TextView? = null
        private var buttonMove: ImageView? = null

        init {
            context = itemView.context
            titleShowcase = itemView.findViewById(R.id.tv_showcase_name)
            buttonMove = itemView.findViewById(R.id.img_move)
        }

        fun bindData(dataShowcase: ShopEtalaseModel, position: Int) {
            titleShowcase?.text = dataShowcase.name

            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
                buttonMove?.visibility = View.VISIBLE
            } else {
                buttonMove?.visibility = View.INVISIBLE
            }

            buttonMove?.setOnTouchListener { _, event ->
                @Suppress("DEPRECATION")
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this@ViewHolder)
                }
                false
            }
        }

    }

}