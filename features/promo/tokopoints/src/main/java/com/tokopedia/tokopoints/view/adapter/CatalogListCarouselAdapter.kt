package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogDetailsActivity.Companion.getCatalogDetail
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil.sendECommerceEvent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil.sendECommerceEventBanner
import com.tokopedia.tokopoints.view.util.ColorUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.ImageUtil
import java.util.*

class CatalogListCarouselAdapter(var items: List<CatalogsValueEntity>,
                                 private val mRecyclerView: RecyclerView?) : RecyclerView.Adapter<CatalogListCarouselAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var quota: TextView
        var description: TextView
        var pointLabel: TextView? = null
        var pointValue: TextView
        var timeLabel: TextView
        var timeValue: TextView
        var disabledError: TextView
        var btnContinue: TextView
        var labelPoint: TextView
        var textDiscount: TextView
        var imgBanner: ImageView
        var imgTime: ImageView
        var imgPoint: ImageView
        var pbQuota: ProgressBar
        var isVisited = false

        init {
            quota = view.findViewById(R.id.text_quota_count)
            description = view.findViewById(R.id.text_description)
            pointValue = view.findViewById(R.id.text_point_value)
            timeLabel = view.findViewById(R.id.text_time_label)
            timeValue = view.findViewById(R.id.text_time_value)
            disabledError = view.findViewById(R.id.text_disabled_error)
            btnContinue = view.findViewById(R.id.button_continue)
            imgBanner = view.findViewById(R.id.img_banner)
            imgTime = view.findViewById(R.id.img_time)
            imgPoint = view.findViewById(R.id.img_points_stack)
            labelPoint = view.findViewById(R.id.text_point_label)
            textDiscount = view.findViewById(R.id.text_point_discount)
            pbQuota = view.findViewById(R.id.progress_timer_quota)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_coupon_carousel, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.btnContinue.isEnabled = !item.isDisabledButton
        holder.description.text = item.title
        holder.btnContinue.setText(R.string.tp_label_exchange) //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.thumbnailUrlMobile)
        holder.pointValue.visibility = View.VISIBLE
        //setting points info if exist in response
        if (item.pointsStr == null || item.pointsStr!!.isEmpty()) {
            holder.pointValue.visibility = View.GONE
        } else {
            holder.pointValue.visibility = View.VISIBLE
            holder.pointValue.text = item.pointsStr
        }

        //setting expiry time info if exist in response
        if (item.expiredLabel.isNullOrEmpty()) {
            holder.timeLabel.visibility = View.GONE
            holder.timeValue.visibility = View.GONE
            holder.imgTime.visibility = View.GONE
        } else {
            holder.timeLabel.visibility = View.VISIBLE
            holder.timeValue.visibility = View.VISIBLE
            holder.imgTime.visibility = View.VISIBLE
            holder.timeLabel.text = item.expiredLabel
            holder.timeValue.text = item.expiredStr
        }

        //Quota text handling
        if (item.upperTextDesc.isNullOrEmpty()) {
            holder.quota.visibility = View.GONE
            holder.pbQuota.visibility = View.GONE
        } else {
            holder.quota.visibility = View.VISIBLE
            holder.pbQuota.visibility = View.VISIBLE
            holder.pbQuota.progress = 0
            val upperText = StringBuilder()
            if (item.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, com.tokopedia.unifyprinciples.R.color.Unify_Y600))
            } else {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            }
            if (!item.upperTextDesc.isNullOrEmpty()) {
                for (i in item.upperTextDesc!!.indices) {
                    if (i == 1) {
                        if (item.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                            //for flash sale progress bar handling
                            holder.pbQuota.progress = item.quota
                            upperText.append(item.upperTextDesc!![i])
                        } else {
                            //exclusive case for handling font color of second index.
                            upperText.append("<font color='${ColorUtil.getColorFromResToString(holder.quota.context,com.tokopedia.unifyprinciples.R.color.Unify_Y400)}>" + item?.upperTextDesc?.get(i) + "</font>")
                        }
                    } else {
                        upperText.append(item.upperTextDesc!![i]).append(" ")
                    }
                }
            }
            holder.quota.text = MethodChecker.fromHtml(upperText.toString())
        }

        //Quota text handling
        if (item.disableErrorMessage == null || item.disableErrorMessage!!.isEmpty()) {
            holder.disabledError.visibility = View.GONE
        } else {
            holder.disabledError.visibility = View.VISIBLE
            holder.disabledError.text = item.disableErrorMessage
        }

        //disabling the coupons if not eligible for current membership
        if (item.isDisabled) {
            ImageUtil.dimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
        } else {
            ImageUtil.unDimImage(holder.imgBanner)
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
        }
        if (item.isDisabledButton) {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_20))
        } else {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        if (item.pointsSlash <= 0) {
            holder.labelPoint.visibility = View.GONE
        } else {
            holder.labelPoint.visibility = View.VISIBLE
            holder.labelPoint.text = item.pointsSlashStr
            holder.labelPoint.paintFlags = holder.labelPoint.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        if (item.discountPercentage <= 0) {
            holder.textDiscount.visibility = View.GONE
        } else {
            holder.textDiscount.visibility = View.VISIBLE
            holder.textDiscount.text = item.discountPercentageStr
        }
        holder.imgBanner.setOnClickListener { v: View? ->
            val bundle = Bundle()
            bundle.putString(CommonConstant.EXTRA_CATALOG_CODE, items[position].slug)
            holder.imgBanner.context.startActivity(getCatalogDetail(holder.imgBanner.context, bundle), bundle)
            sendClickEvent(holder.imgBanner.context, item, position)
        }
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size
    }

    fun updateItems(items: List<CatalogsValueEntity>) {
        this.items = items
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val (baseCode, _, _, _, _, _, _, _, _, title) = items[holder.adapterPosition] ?: return
        if (!holder.isVisited) {
            val item: MutableMap<String, Any?> = HashMap()
            item["name"] = "/tokopoints/penukaran point - p(x) - promo list"
            item["position"] = holder.adapterPosition.toString()
            item["creative"] = title
            item["promo_code"] = baseCode
            val promotions = HashMap<String, Any>()
            promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
            sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_ON_CATALOG,
                    "$title - $baseCode", promotions)
            holder.isVisited = true
        }
    }

    private fun sendClickEvent(context: Context, data: CatalogsValueEntity, position: Int) {
        val item: MutableMap<String, Any?> = HashMap()
        item["name"] = "/tokopoints/penukaran point - p(x) - promo list"
        item["position"] = position.toString()
        item["creative"] = data.title
        item["promo_code"] = data.baseCode
        val promotions = HashMap<String, Any>()
        promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
        sendECommerceEventBanner(AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_CATALOG_HOME,
                data.title + " - " + data.baseCode, promotions)
    }

    private fun setUpHeight(data: CatalogsValueEntity?) {
        if (data == null || mRecyclerView == null) {
            return
        }
        if (data.pointsSlash > 0 && !TextUtils.isEmpty(data.expiredLabel)
                && !TextUtils.isEmpty(data.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_xxlarge)
        } else if (data.pointsSlash > 0
                && !TextUtils.isEmpty(data.expiredLabel)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_xlarge)
        } else if (!TextUtils.isEmpty(data.expiredLabel)
                && !TextUtils.isEmpty(data.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_xlarge)
        } else if (data.pointsSlash > 0
                && !TextUtils.isEmpty(data.disableErrorMessage)) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_large)
        } else if (data.pointsSlash > 0) {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_medium)
        } else {
            mRecyclerView.layoutParams.height = mRecyclerView.resources.getDimensionPixelOffset(R.dimen.tp_coupon_card_medium)
        }
        mRecyclerView.requestLayout()
    }

}