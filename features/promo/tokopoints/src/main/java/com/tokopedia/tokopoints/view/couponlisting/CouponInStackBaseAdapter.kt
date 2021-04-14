package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailActivity
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment.Companion.REQUEST_CODE_STACKED_IN_ADAPTER
import com.tokopedia.tokopoints.view.model.CouponValueEntity
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.DEFAULT_TIME_STRING
import com.tokopedia.tokopoints.view.util.convertLongToHourMinuteSec
import java.util.*
import kotlin.collections.HashMap

class CouponInStackBaseAdapter(callback: AdapterCallback, val data: TokoPointPromosEntity) : BaseAdapter<CouponValueEntity>(callback) {


    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var label: TextView
        internal var value: TextView
        internal var tvMinTxnValue: TextView
        internal var tvMinTxnLabel: TextView
        internal var imgBanner: ImageView
        internal var imgLabel: ImageView
        internal var ivMinTxn: ImageView
        var isVisited = false

        /*This section is exclusively for handling timer*/
        var timer: CountDownTimer? = null
        var progressTimer: ProgressBar
        var cvShadow1: CardView
        var cvShadow2: CardView
        var cvData: CardView
        var cv1: CardView
        var cv2: CardView

        init {
            label = view.findViewById(R.id.text_time_label)
            value = view.findViewById(R.id.text_time_value)
            imgBanner = view.findViewById(R.id.img_banner)
            imgLabel = view.findViewById(R.id.img_time)
            ivMinTxn = view.findViewById(R.id.iv_rp)
            tvMinTxnValue = view.findViewById(R.id.tv_min_txn_value)
            tvMinTxnLabel = view.findViewById(R.id.tv_min_txn_label)
            progressTimer = view.findViewById(R.id.progress_timer)
            cvShadow1 = view.findViewById(R.id.cv_shadow_1)
            cvShadow2 = view.findViewById(R.id.cv_shadow_2)
            cvData = view.findViewById(R.id.cv_data)
            cv1 = view.findViewById(R.id.cv_1)
            cv2 = view.findViewById(R.id.cv_2)
        }

        override fun bindView(item: CouponValueEntity, position: Int) {
            setData(this, item)

        }
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder

            val data = items[holder.getAdapterPosition()] ?: return

            if (!holder.isVisited) {
                val item = HashMap<String, String?>()
                item["id"] = data.catalogId.toString()
                item["name"] = data.title
                item["position"] = holder.getAdapterPosition().toString()
                item["creative"] = data.title
                item["creative_url"] = data.imageUrlMobile
                item["promo_code"] = data.code

                val promotions = HashMap<String, List<Map<String, String?>>>()
                promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)

                val promoView = HashMap<String, Map<String, List<Map<String, String?>>>>()
                promoView["promoView"] = promotions

                var eventLabel = ""
                if (data.title != null && data.title.isNotEmpty()) {
                    eventLabel = data.title
                }
                AnalyticsTrackerUtil.sendECommerceEvent(holder.itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                        eventLabel, promoView)
                holder.isVisited = true
            }
        }
    }

    private fun sendClickEvent(context: Context, data: CouponValueEntity, position: Int) {
        val item = HashMap<String, String?>()
        item["id"] = data.catalogId.toString()
        item["name"] = data.title
        item["position"] = position.toString()
        item["creative"] = data.title
        item["creative_url"] = data.imageUrlMobile
        item["promo_code"] = data.code

        val promotions = HashMap<String, List<Map<String, String?>>>()
        promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)

        val promoClick: HashMap<String, Map<String, List<Map<String, String?>>>> = HashMap()
        promoClick["promoClick"] = promotions

        var eventLabel = ""
        if (data.title != null && data.title.isNotEmpty()) {
            eventLabel = data.title
        }
        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                eventLabel, promoClick)
    }

    override fun getItemViewHolder(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_my_coupon_stacked, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int) {
        super.loadData(pageNumber)
        loadCompleted(data.coupon.coupons, data)
        isLastPage = true //true mean no paging
    }

    private fun setData(holder: ViewHolder, item: CouponValueEntity) {
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.imageUrlMobile)

        if (item.isNewCoupon) {
            holder.itemView.setBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G200))
            holder.cv1.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G200))
            holder.cv2.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G200))
        } else {
            holder.cv1.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
            holder.cv2.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
            holder.itemView.setBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }

        if (item.usage != null) {
            holder.label.show()
            holder.value.show()
            holder.imgLabel.show()
            holder.value.text = item.usage.usageStr.trim { it <= ' ' }
            holder.label.text = item.usage.text
        } else {
            holder.label.hide()
            holder.value.hide()
            holder.imgLabel.hide()
        }

        if (TextUtils.isEmpty(item.minimumUsageLabel)) {
            holder.tvMinTxnLabel.hide()
            holder.ivMinTxn.hide()
        } else {
            holder.ivMinTxn.show()
            holder.tvMinTxnLabel.show()
            holder.tvMinTxnLabel.text = item.minimumUsageLabel

        }

        if (TextUtils.isEmpty(item.minimumUsage)) {
            holder.tvMinTxnValue.hide()
        } else {
            holder.tvMinTxnValue.show()
            holder.tvMinTxnValue.text = item.minimumUsage
        }

        val layoutParamsCv1 = holder.cvShadow1.layoutParams as ConstraintLayout.LayoutParams
        val layoutParamsCvData = holder.cvData.layoutParams as ConstraintLayout.LayoutParams
        if (item.isStacked) {
            layoutParamsCv1.setMargins(holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    0,
                    holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    holder.cvShadow1.resources.getDimensionPixelOffset(R.dimen.dp_5))
            layoutParamsCvData.setMargins(0, 0, 0,
                    holder.cvShadow1.resources.getDimensionPixelOffset(R.dimen.dp_10))
            holder.cvShadow1.show()
            holder.cvShadow2.show()
            holder.cvShadow1.layoutParams = layoutParamsCv1
            holder.cvData.layoutParams = layoutParamsCvData
        } else {
            holder.cvShadow1.hide()
            holder.cvShadow2.hide()
            layoutParamsCv1.setMargins(0, 0, 0, 0)
            layoutParamsCvData.setMargins(0, 0, 0, 0)
            holder.cvData.setPadding(0, 0, 0, 0)
            holder.cvShadow1.layoutParams = layoutParamsCv1
            holder.cvData.layoutParams = layoutParamsCvData
        }

        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer!!.cancel()
        }

        if (item.usage.activeCountDown < 1) {
            if (item.usage.expiredCountDown > 0 && item.usage.expiredCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                holder.progressTimer.max = CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S.toInt()
                holder.progressTimer.show()
                holder.label.hide()
                holder.timer = object : CountDownTimer(item.usage.expiredCountDown * 1000, 1000) {
                    override fun onTick(l: Long) {
                        item.usage.expiredCountDown = l / 1000
                        val timeToExpire = convertLongToHourMinuteSec(l)
                        val hours = timeToExpire.first
                        val minutes = timeToExpire.second
                        val seconds = timeToExpire.third
                        holder.value.text = String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)

                        try {
                            holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                        } catch (e: Exception) {
                        }
                        holder.progressTimer.progress = l.toInt() / 1000
                        holder.value.setPadding(holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall))
                    }

                    override fun onFinish() {
                        holder.value.text = DEFAULT_TIME_STRING
                    }
                }.start()
            } else {
                holder.progressTimer.hide()
                holder.value.setPadding(0, 0, 0, 0)
                holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
        } else {
            holder.progressTimer.hide()
            holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }

        if (item.usage.activeCountDown > 0) {
            holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, com.tokopedia.unifyprinciples.R.color.Unify_N200), android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, com.tokopedia.unifyprinciples.R.color.Unify_N200), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, com.tokopedia.unifyprinciples.R.color.Unify_G400), android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, com.tokopedia.unifyprinciples.R.color.Unify_G400), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        if (holder.itemView != null) {
            holder.itemView.setOnClickListener { v ->
                val bundle = Bundle()
                bundle.putString(CommonConstant.EXTRA_COUPON_CODE, item.code)
                (holder.imgBanner.context as FragmentActivity).startActivityForResult(CouponDetailActivity.getCouponDetail(holder.imgBanner.context, bundle), REQUEST_CODE_STACKED_IN_ADAPTER)

                sendClickEvent(holder.imgBanner.context, item, holder.getAdapterPosition())
            }
        }
    }
}
