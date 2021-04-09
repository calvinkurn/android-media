package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener

class ShopShowcaseListAdapter(
        val listener: ShopShowcaseManagementListener,
        val isMyShop: Boolean
) : RecyclerView.Adapter<ShopShowcaseListAdapter.ViewHolder>() {

    private var showcaseList: MutableList<ShopEtalaseModel> = mutableListOf()

    fun updateDataShowcaseList(showcaseListData: List<ShopEtalaseModel>) {
        showcaseList = showcaseListData.toMutableList()
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

    inner class ViewHolder(itemView: View) : ShopShowcaseListImageBaseViewHolder(itemView) {

        override var showcaseActionButton: Any? = null

        init {
            showcaseActionButton = itemView.findViewById(R.id.img_menu_more)
        }

        override fun bind(element: Any) {

            renderShowcaseMainInfo(element)

            val showcaseItem = element as ShopEtalaseModel

            // showcase action button condition
            showcaseCampaignLabel?.shouldShowWithAction(
                    isShowCampaignLabel(showcaseItem.type),
                    action = {
                        adjustShowcaseNameConstraintPosition()
                    }
            )
            // set showcase campaign label title
            showcaseCampaignLabel?.setLabel(getCampaignLabelTitle(showcaseItem.type))

            // handle item showcase click
            setItemShowcaseClickListener {
                listener.sendClickShowcase(showcaseItem, adapterPosition)
            }

            // showcase action button show condition
            val actionButton = (showcaseActionButton as? ImageView)
            actionButton?.apply {
                shouldShowWithAction(
                        shouldShow = (isMyShop && isShowActionButton(showcaseItem.type)),
                        action = { adjustShowcaseNameConstraintPosition() }
                )

                // action button click listener
                setOnClickListener {
                    listener.sendClickShowcaseMenuMore(element, adapterPosition)
                }
            }
        }
    }
}