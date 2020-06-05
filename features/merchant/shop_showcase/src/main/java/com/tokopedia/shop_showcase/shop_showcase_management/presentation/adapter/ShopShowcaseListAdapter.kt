package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener
import com.tokopedia.shop_showcase.common.ShowcaseType
import com.tokopedia.shop_showcase.common.TOTAL_GENERATED_ID
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem

class ShopShowcaseListAdapter (
        val listener: ShopShowcaseManagementListener,
        val isMyShop: Boolean
): RecyclerView.Adapter<ShopShowcaseListAdapter.ViewHolder>() {

    private var showcaseList: MutableList<ShowcaseItem> = mutableListOf()

    fun updateDataShowcaseList(showcaseListData: List<ShowcaseItem>) {
        showcaseList = showcaseListData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateLayout(R.layout.shop_showcase_item))
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(showcaseList[position], position)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        private var titleShowcase: TextView? = null
        private var buttonMenuMore: ImageView? = null

        init {
            context = itemView.context
            titleShowcase = itemView.findViewById(R.id.tv_showcase_name)
            buttonMenuMore = itemView.findViewById(R.id.img_menu_more)
        }

        fun bindData(dataShowcase: ShowcaseItem, position: Int) {
            titleShowcase?.text = dataShowcase.name

            if (dataShowcase.type == ShowcaseType.GENERATED || !isMyShop) {
                buttonMenuMore?.visibility = View.INVISIBLE
            } else {
                buttonMenuMore?.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                listener.sendClickShowcase(dataShowcase, position)
            }

            buttonMenuMore?.setOnClickListener {
                listener.sendClickShowcaseMenuMore(dataShowcase, position)
            }
        }

    }

}