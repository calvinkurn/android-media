package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener
import com.tokopedia.shop_showcase.databinding.ShopShowcaseItemOldBinding
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
        val binding = ShopShowcaseItemOldBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(showcaseList[position], position)
    }


    inner class ViewHolder(itemViewBinding: ShopShowcaseItemOldBinding): RecyclerView.ViewHolder(itemViewBinding.root) {
        private var titleShowcase: TextView? = null
        private var buttonMenuMore: ImageView? = null
        private var campaignLabel: Typography? = null

        init {
            itemViewBinding.apply {
                titleShowcase = tvShowcaseName
                buttonMenuMore = imgMenuMore
                campaignLabel = tvCampaignLabel
            }
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