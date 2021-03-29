package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.R
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

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
        return ViewHolder(parent.inflateLayout(R.layout.item_shop_showcase_list_image))
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(showcaseList[position], position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context = itemView.context
        private var titleShowcase: Typography? = null
        private var showcaseImage: ImageUnify? = null
        private var showcaseCount: Typography? = null
        private var buttonMenuMore: ImageView? = null
        private var campaignLabel: Label? = null
        private var verticalLineGuide: Guideline? = null

        init {
            titleShowcase = itemView.findViewById(R.id.tvShowcaseName)
            showcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
            showcaseImage = itemView.findViewById(R.id.ivShowcaseImage)
            campaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
            buttonMenuMore = itemView.findViewById(R.id.img_menu_more)
            verticalLineGuide = itemView.findViewById(R.id.guideline_action_picker2)
        }

        fun bindData(dataShowcase: ShopEtalaseModel, position: Int) {
            titleShowcase?.text = dataShowcase.name
            showcaseCount?.text = context.getString(
                    R.string.shop_page_showcase_product_count_text,
                    dataShowcase.count.toString()
            )

            // try catch to avoid crash ImageUnify on loading image with Glide
            try {
                if (showcaseImage?.context?.isValidGlideContext() == true) {
                    showcaseImage?.setImageUrl(dataShowcase.imageUrl)
                }
            } catch (e: Throwable) {
            }

            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CUSTOM && isMyShop) {
                buttonMenuMore?.visibility = View.VISIBLE
            } else {
                buttonMenuMore?.visibility = View.GONE
            }

            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CAMPAIGN) {
                campaignLabel?.show()
                adjustShowcaseNameConstraint()
            } else {
                campaignLabel?.hide()
                adjustShowcaseNameConstraint()
            }

            itemView.setOnClickListener {
                listener.sendClickShowcase(dataShowcase, position)
            }

            buttonMenuMore?.setOnClickListener {
                listener.sendClickShowcaseMenuMore(dataShowcase, position)
            }
        }

        private fun adjustShowcaseNameConstraint() {
            val parentConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.parent_layout)
            val constraintSet = ConstraintSet()
            val tvShowcaseNameId = R.id.tvShowcaseName
            val labelShowcaseCampaignId = R.id.showcaseCampaignLabel
            val verticalGuidelineId = R.id.guideline_action_picker2
            constraintSet.clone(parentConstraintLayout)
            if (campaignLabel?.visibility == View.VISIBLE) {
                constraintSet.connect(
                        tvShowcaseNameId,
                        ConstraintSet.RIGHT,
                        labelShowcaseCampaignId,
                        ConstraintSet.LEFT,
                        0
                )
            } else {
                constraintSet.connect(
                        tvShowcaseNameId,
                        ConstraintSet.RIGHT,
                        verticalGuidelineId,
                        ConstraintSet.LEFT,
                        0
                )
            }
            constraintSet.applyTo(parentConstraintLayout)
        }

    }

}