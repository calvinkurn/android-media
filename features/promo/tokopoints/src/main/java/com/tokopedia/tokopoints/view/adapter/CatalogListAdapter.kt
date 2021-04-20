package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogDetailsActivity.Companion.getCatalogDetail
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.model.section.CountDownInfo
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.HASH
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.TIMER_RED_BACKGROUND_HEX
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*
import kotlin.collections.HashMap

class CatalogListAdapter(private val list: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var quota: TextView
        var description: TextView
        var pointValue: TextView
        var timeLabel: TextView
        var timeValue: TextView
        var disabledError: TextView
        var btnContinue: TextView
        var labelPoint: TextView
        var textDiscount: TextView
        var imgBanner: ImageView
        var imgTime: ImageView
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
            labelPoint = view.findViewById(R.id.text_point_label)
            textDiscount = view.findViewById(R.id.text_point_discount)
            pbQuota = view.findViewById(R.id.progress_timer_quota)
        }
    }

    inner class TimerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var timerUnifySingle : TimerUnifySingle = view.findViewById(R.id.timerunify_catalog)

        fun onDetach() {
            timerUnifySingle.timer?.cancel()
            timerUnifySingle.timer = null
        }
    }

    private fun setData(holder: ViewHolder, rawItem: Any?, position: Int) {
        val item = rawItem as CatalogsValueEntity
        holder.btnContinue.isEnabled = !item.isDisabledButton
        holder.description.text = item.title
        holder.btnContinue.setText(R.string.tp_label_exchange) //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.thumbnailUrlMobile)
        //setting points info if exist in response
        if (item.pointsStr.isNullOrEmpty()) {
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
            for (i in item.upperTextDesc!!.indices) {
                if (i == 1) {
                    if (item.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) { //for flash sale progress bar handling
                        holder.pbQuota.progress = item.quota
                        upperText.append(item.upperTextDesc!![i])
                    } else { //exclusive case for handling font color of second index.
                        upperText.append("<font color='${ColorUtil.getColorFromResToString(holder.quota.context,com.tokopedia.unifyprinciples.R.color.Unify_Y400)}>" + item?.upperTextDesc?.get(i) + "</font>")
                    }
                } else {
                    upperText.append(item.upperTextDesc!![i]).append(" ")
                }
            }
            holder.quota.text = MethodChecker.fromHtml(upperText.toString())
        }
        //Quota text handling
        if (item.disableErrorMessage.isNullOrEmpty()) {
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
            bundle.putString(CommonConstant.EXTRA_CATALOG_CODE, item.slug)
            holder.imgBanner.context.startActivity(getCatalogDetail(holder.imgBanner.context, bundle), bundle)
            sendClickEvent(holder.imgBanner.context, item, position)
        }
        holder.btnContinue.visibility = if (item.isShowTukarButton) View.VISIBLE else View.GONE
    }


    private fun setDataTimer(holder: TimerViewHolder, countDownInfo: CountDownInfo) {
        if (holder.timerUnifySingle.timer != null) {
            holder.timerUnifySingle.timer!!.cancel()
        }
        var timerValue = countDownInfo.countdownUnix
        val timerFlagType = countDownInfo.backgroundColor
        if (timerFlagType == HASH + TIMER_RED_BACKGROUND_HEX) {
            holder.timerUnifySingle.timerVariant = TimerUnifySingle.VARIANT_MAIN
        } else {
            holder.timerUnifySingle.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
        }
        holder.timerUnifySingle.timerText = countDownInfo.label
        if (countDownInfo.type == 1) {
            if (timerValue != null) {
                val timeToExpire = convertSecondsToHrMmSs(timerValue)
                holder.timerUnifySingle.targetDate = timeToExpire
            }
        } else {
            holder.timerUnifySingle.timerFormat = TimerUnifySingle.FORMAT_DAY
            val timerString = countDownInfo.countdownStr
            val noOfDay = timerString?.replace("[^0-9]".toRegex(), "")

            val cal = Calendar.getInstance()
            noOfDay?.toInt()?.let { cal.add(Calendar.DAY_OF_MONTH, it + 1) }

            holder.timerUnifySingle.targetDate = cal
        }

        holder.timerUnifySingle.apply {
            onFinish = {
                holder.timerUnifySingle.hide()
            }
            onTick = {
                countDownInfo.countdownUnix = it / 1000
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CATALOG) {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tp_item_coupon, parent, false)
            ViewHolder(itemView)
        } else {
            val timerView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tp_catalog_timer_item, parent, false)
            TimerViewHolder(timerView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && list[0] is CountDownInfo) {
            VIEW_TYPE_TIMER
        } else VIEW_TYPE_CATALOG
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)
        if (vh is ViewHolder) {
            val data = list[vh.getAdapterPosition()] ?: return
            if (!vh.isVisited && vh.adapterPosition > 0) {
                val item: MutableMap<String, String?> = HashMap()
                data as CatalogsValueEntity
                item["id"] = data.id.toString()
                item["name"] = data.title
                item["position"] = vh.adapterPosition.toString()
                item["creative"] = data.title
                item["creative_url"] = data.imageUrlMobile
                item["promo_code"] = data.baseCode
                val promotions: HashMap<String, List<Map<String, String?>>> = HashMap()
                promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)
                val promoView: HashMap<String, Map<String, List<Map<String, String?>>>> = HashMap()
                promoView["promoView"] = promotions
                data.title?.let {
                    AnalyticsTrackerUtil.sendECommerceEvent(vh.btnContinue.context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                            AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                            it, promoView)
                }
                vh.isVisited = true
            }
        }
    }

    private fun sendClickEvent(context: Context, data: CatalogsValueEntity, position: Int) {
        val item: MutableMap<String, String?> = HashMap()
        item["id"] = data.id.toString()
        item["name"] = data.title
        item["position"] = position.toString()
        item["creative"] = data.title
        item["creative_url"] = data.imageUrlMobile
        item["promo_code"] = data.baseCode
        val promotions: HashMap<String, List<Map<String, String?>>> = HashMap()
        promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)
        val promoClick: HashMap<String, Map<String, List<Map<String, String?>>>> = HashMap()
        promoClick["promoClick"] = promotions
        data.title?.let {
            AnalyticsTrackerUtil.sendECommerceEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                    it, promoClick)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            setData(holder, list[position], position)
        } else if (holder is TimerViewHolder) {
            setDataTimer(holder, list[VIEW_TYPE_TIMER] as CountDownInfo)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is TimerViewHolder) {
            holder.onDetach()
        }
    }


    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is TimerViewHolder){
            holder.onDetach()
        }
    }

    companion object {
        const val VIEW_TYPE_TIMER = 0
        const val VIEW_TYPE_CATALOG = 1
    }

}