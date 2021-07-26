package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener
import com.tokopedia.unifyprinciples.Typography

class ShopShowcaseListAdapterOld (
        val listener: ShopShowcaseManagementListener,
        val isMyShop: Boolean
): RecyclerView.Adapter<ShopShowcaseListAdapterOld.ViewHolder>() {

    private var showcaseList: MutableList<ShopEtalaseModel> = mutableListOf()

    fun updateDataShowcaseList(showcaseListData: List<ShopEtalaseModel>) {
        showcaseList = showcaseListData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateLayout(R.layout.shop_showcase_item_old))
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
        private var campaignLabel: Typography? = null

        init {
            context = itemView.context
            titleShowcase = itemView.findViewById(R.id.tv_showcase_name)
            buttonMenuMore = itemView.findViewById(R.id.img_menu_more)
            campaignLabel = itemView.findViewById(R.id.tv_campaign_label)
        }

        fun bindData(dataShowcase: ShopEtalaseModel, position: Int) {
            titleShowcase?.text = dataShowcase.name

            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CUSTOM && isMyShop) {
                buttonMenuMore?.visibility = View.VISIBLE
            } else {
                buttonMenuMore?.visibility = View.INVISIBLE
            }

            if(dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CAMPAIGN){
                campaignLabel?.show()
            }else{
                campaignLabel?.hide()
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