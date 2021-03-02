package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.data.model.SectionContentItem
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.util.invertIfDarkMode

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceAdapter(val listener: HomeCategoryListener?): RecyclerView.Adapter<BalanceAdapter.Holder>() {

    companion object {
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val TITLE = "OVO"
        private const val WALLET_TYPE = "OVO"
        private const val BEBAS_ONGKIR_TYPE = "bebas ongkir"
        private const val KUPON_SAYA_URL_PATH = "kupon-saya"
        private const val CDN_URL = "https://ecs7.tokopedia.net/img/android/"
        private const val BG_CONTAINER_URL = CDN_URL + "bg_product_fintech_tokopoint_normal/" +
                "drawable-xhdpi/bg_product_fintech_tokopoint_normal.png"
    }

    private var itemMap: HashMap<Int, TokopointsDrawer> = hashMapOf()

    fun setItemMap(itemMap: HashMap<Int, TokopointsDrawer>) {
        this.itemMap = hashMapOf()
        this.itemMap = itemMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget, parent, false))
    }

    override fun getItemCount(): Int {
        return itemMap.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemMap[position], listener)
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        private var listener: HomeCategoryListener? = null
        fun bind(tokopointsDrawer: TokopointsDrawer?, listener: HomeCategoryListener?) {
            this.listener = listener
            renderTokoPoint(tokopointsDrawer)
        }


        private fun renderTokoPoint(element: TokopointsDrawer?) {
            val tokoPointHolder = itemView.findViewById<View>(R.id.container_tokopoint)
            val tvBalanceTokoPoint = itemView.findViewById<TextView>(R.id.tv_balance_tokopoint)
            val tvActionTokopoint = itemView.findViewById<TextView>(R.id.tv_btn_action_tokopoint)
            val ivLogoTokoPoint = itemView.findViewById<ImageView>(R.id.iv_logo_tokopoint)
            val tokopointProgressBarLayout = itemView.findViewById<View>(R.id.progress_bar_tokopoint_layout)
            val tokopointActionContainer = itemView.findViewById<View>(R.id.container_action_tokopoint)
            val mTextCouponCount = itemView.findViewById<TextView>(R.id.text_coupon_count)
            ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_green_24)
            mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            val isError = element?.isError ?: false
            if (element == null || isError) {
                tokoPointHolder.setOnClickListener {
                    tokopointProgressBarLayout.visibility = View.VISIBLE
                    listener?.onRefreshTokoPointButtonClicked()
                }
                tvActionTokopoint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvActionTokopoint.visibility = View.VISIBLE
                tvActionTokopoint.setText(R.string.home_header_tokopoint_unable_to_load_label)
                tvActionTokopoint.setTypeface(mTextCouponCount.typeface, Typeface.BOLD)
                tvActionTokopoint.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                mTextCouponCount.setText(R.string.home_header_tokopoint_refresh_label)
                mTextCouponCount.visibility = View.VISIBLE
                mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                tokopointProgressBarLayout.visibility = View.GONE
                tokopointActionContainer.visibility = View.VISIBLE
                ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_normal_24)
                tvBalanceTokoPoint.visibility = View.GONE
            } else if (element == null && !isError) {
                tokoPointHolder.setOnClickListener(null)
                tokopointProgressBarLayout.visibility = View.VISIBLE
                tokopointActionContainer.visibility = View.GONE
                tvBalanceTokoPoint.visibility = View.GONE
            } else {
                tokopointProgressBarLayout.visibility = View.GONE
                tokopointActionContainer.visibility = View.VISIBLE
                tvActionTokopoint.visibility = View.GONE
                tvBalanceTokoPoint.visibility = View.VISIBLE
                mTextCouponCount.visibility = View.VISIBLE

                ImageHandler.loadImageAndCache(ivLogoTokoPoint, element?.iconImageURL)
                mTextCouponCount.setTypeface(mTextCouponCount.typeface, Typeface.BOLD)
                element?.sectionContent?.let { sectionContent ->
                    if (sectionContent.isNotEmpty()) {
                        setTokopointHeaderData(sectionContent[0], tvBalanceTokoPoint)
                        if (sectionContent.size >= 2) {
                            setTokopointHeaderData(sectionContent[1], mTextCouponCount, R.dimen.sp_10)
                        }
                    } else {
                        tvBalanceTokoPoint.setText(R.string.home_header_tokopoint_no_tokopoints)
                        mTextCouponCount.setText(R.string.home_header_tokopoint_no_coupons)
                        tvBalanceTokoPoint.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    }
                }

                tokoPointHolder.setOnClickListener {
                    if (element != null) {
                        OvoWidgetTracking.eventUserProfileTokopoints()
                        element?.let {tokopointsDrawerHomeData->
                            listener?.actionTokoPointClicked(
                                    tokopointsDrawerHomeData.redirectAppLink,
                                    tokopointsDrawerHomeData.redirectURL,
                                    if (TextUtils.isEmpty(tokopointsDrawerHomeData.mainPageTitle))
                                        TITLE_HEADER_WEBSITE
                                    else
                                        tokopointsDrawerHomeData.mainPageTitle
                            )
                            if(tokopointsDrawerHomeData.sectionContent.isNotEmpty() &&
                                    tokopointsDrawerHomeData.sectionContent.first().textAttributes?.text?.contains(BEBAS_ONGKIR_TYPE, ignoreCase = true) == true){
                                OvoWidgetTracking.sendBebasOngkir(listener?.userId ?: "0")
                            } else if (tokopointsDrawerHomeData.sectionContent.isNotEmpty() &&
                                    tokopointsDrawerHomeData.redirectAppLink.contains(KUPON_SAYA_URL_PATH)) {
                                OvoWidgetTracking.sendClickOnTokopointsNewCouponTracker()
                            } else {
                                OvoWidgetTracking.sendTokopointTrackerClick()
                            }
                        }
                    }
                }
            }
        }

        private fun setTokopointHeaderData(sectionContentItem: SectionContentItem?, tokopointsTextView: TextView, textSize: Int = R.dimen.sp_12) {
            if (sectionContentItem != null) {

                //Initializing to default value to prevent stale data in case of onresume
                tokopointsTextView.background = null
                tokopointsTextView.text = null
                tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(textSize))

                if (sectionContentItem.tagAttributes != null && !TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                    if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.backgroundColour) && HexValidator.validate(sectionContentItem.tagAttributes.backgroundColour)) {
                        val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                        if (drawable is GradientDrawable) {
                            val shapeDrawable = drawable as GradientDrawable?
                            shapeDrawable!!.setColorFilter(Color.parseColor(sectionContentItem.tagAttributes.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                            tokopointsTextView.background = shapeDrawable
                            val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                            tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                            tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                            tokopointsTextView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                        }
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N0))
                    } else {
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                    if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                        tokopointsTextView.text = sectionContentItem.tagAttributes.text
                    }
                } else if (sectionContentItem.textAttributes != null && !TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                    if (!TextUtils.isEmpty(sectionContentItem.textAttributes.colour) && HexValidator.validate(sectionContentItem.textAttributes.colour)) {
                        tokopointsTextView.setTextColor(Color.parseColor(sectionContentItem.textAttributes.colour).invertIfDarkMode(itemView.context))
                    } else {
                        tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                    if (sectionContentItem.textAttributes.isBold) {
                        tokopointsTextView.setTypeface(null, Typeface.BOLD)
                    } else {
                        tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                    }
                    if (!TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                        tokopointsTextView.text = sectionContentItem.textAttributes.text
                    }

                }
            }
        }

    }

}