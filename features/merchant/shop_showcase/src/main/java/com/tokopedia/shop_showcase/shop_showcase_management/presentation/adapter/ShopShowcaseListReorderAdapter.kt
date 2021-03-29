package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.R
import com.tokopedia.shop_showcase.common.ShopShowcaseReorderListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label

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
        return ViewHolder(parent.inflateLayout(R.layout.item_shop_showcase_list_image))
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val context: Context = itemView.context
        private var titleShowcase: TextView? = null
        private var countShowcase: TextView? = null
        private var imageShowcase: ImageUnify? = null
        private var buttonMove: ImageView? = null
        private var campaignLabel: Label? = null
        private var verticalLineGuide: Guideline? = null

        init {
            titleShowcase = itemView.findViewById(R.id.tvShowcaseName)
            countShowcase = itemView.findViewById(R.id.tvShowcaseCount)
            imageShowcase = itemView.findViewById(R.id.ivShowcaseImage)
            buttonMove = itemView.findViewById(R.id.img_move_showcase)
            campaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
            verticalLineGuide = itemView.findViewById(R.id.guideline_action_picker2)
        }

        fun bindData(dataShowcase: ShopEtalaseModel, position: Int) {

            titleShowcase?.text = dataShowcase.name
            countShowcase?.text = context.getString(
                    R.string.shop_page_showcase_product_count_text,
                    dataShowcase.count.toString()
            )

            // try catch to avoid crash ImageUnify on loading image with Glide
            try {
                if (imageShowcase?.context?.isValidGlideContext() == true) {
                    imageShowcase?.setImageUrl(dataShowcase.imageUrl)
                }
            } catch (e: Throwable) {
            }


            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
                buttonMove?.visibility = View.VISIBLE
            } else {
                buttonMove?.visibility = View.GONE
            }

            if (dataShowcase.type == ShopEtalaseTypeDef.ETALASE_CAMPAIGN) {
                campaignLabel?.show()
                adjustShowcaseNameConstraint()
            } else {
                campaignLabel?.hide()
                adjustShowcaseNameConstraint()
            }

            buttonMove?.setOnTouchListener { _, event ->
                @Suppress("DEPRECATION")
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this@ViewHolder)
                }
                false
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
