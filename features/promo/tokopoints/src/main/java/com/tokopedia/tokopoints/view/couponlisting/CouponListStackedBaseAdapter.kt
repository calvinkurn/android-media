package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailActivity
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment.Companion.REQUEST_CODE_STACKED_ADAPTER
import com.tokopedia.tokopoints.view.model.CouponValueEntity
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.DEFAULT_TIME_STRING
import com.tokopedia.tokopoints.view.util.convertLongToHourMinuteSec

import java.util.Arrays
import java.util.HashMap
import java.util.Locale

class CouponListStackedBaseAdapter(private val mPresenter: CouponLisitingStackedViewModel, callback: AdapterCallback) : BaseAdapter<CouponValueEntity>(callback) {
    private var mRecyclerView: RecyclerView? = null
    private var mStackedID : String? = null

    fun couponCodeVisible(code: String, isStacked: Boolean) {
        for (i in 0 until items.size) {
            val data = getItem(i)
            if ((!isStacked && code == data.code) || (isStacked && data.isStacked && data.stackId == code)) {
                if (data.isNewCoupon) {
                    data.isNewCoupon = false
                    notifyItemChanged(i)
                }
                break
            }
        }
    }

    fun couponStackedVisible(){
        val id = mStackedID
        id?.let {
            couponCodeVisible(it,true)
        }
    }

    fun onSuccess(data: TokoPointPromosEntity) {
        loadCompleted(data.coupon.coupons, data)
        isLastPage = !data.coupon.paging.isHasNext
        loadCompletedWithError()
    }

    fun onError(){
        loadCompletedWithError()
    }

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var label: TextView
        internal var value: TextView
        internal var tvMinTxnValue: TextView
        internal var tvMinTxnLabel: TextView
        internal var tvStackCount: TextView
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
            tvStackCount = view.findViewById(R.id.text_stack_count)
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

        fun onDetach() {
            if (timer != null) {
                timer!!.cancel()
                timer = null
            }
        }
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val data = items[vh.getAdapterPosition()] ?: return

            if (!vh.isVisited) {
                val item = HashMap<String, String?>()
                item["id"] = data.catalogId.toString()
                item["name"] = data.title
                item["position"] = vh.getAdapterPosition().toString()
                item["creative"] = data.title
                item["creative_url"] = data.imageUrlMobile
                item["promo_code"] = data.code

                val promotions = HashMap<String, List<Map<String, String?>>>()
                promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)

                val promoView = HashMap<String, Map<String, List<Map<String, String?>>>>()
                promoView["promoView"] = promotions

                AnalyticsTrackerUtil.sendECommerceEvent(vh.value.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                        data.title, promoView)

                vh.isVisited = true
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

        val promoClick = HashMap<String, Map<String, List<Map<String, String?>>>>()
        promoClick["promoView"] = promotions

        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.title, promoClick)
    }

    override fun getItemViewHolder(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_my_coupon_stacked, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int) {
        super.loadData(pageNumber)
        mPresenter.getList(pageNumber)
    }

    private fun setData(holder: ViewHolder, item: CouponValueEntity) {
        ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner, item.imageUrlMobile)

        if (item.isNewCoupon) {
            holder.itemView.setBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), R.color.tp_new_coupon_background_color))
            holder.cv1.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), R.color.tp_new_coupon_background_color))
            holder.cv2.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.tp_new_coupon_background_color))
        } else {
            holder.cv1.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.design.R.color.white))
            holder.cv2.setCardBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.design.R.color.white))
            holder.itemView.setBackgroundColor(MethodChecker.getColor(holder.itemView.getContext(), com.tokopedia.design.R.color.white))
        }

        if (item.usage != null) {
            holder.label.show()
            holder.value.show()
            holder.imgLabel.show()
            holder.value.text = item.usage.usageStr.trim(' ' )
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
            holder.tvMinTxnLabel.setPadding(0, holder.imgBanner.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_5), 0, 0)
        } else {
            holder.tvMinTxnLabel.setPadding(0, 0, 0, 0)
            holder.tvMinTxnValue.show()
            holder.tvMinTxnValue.text = item.minimumUsage
        }

        val layoutParamsCv1 = holder.cvShadow1.layoutParams as ConstraintLayout.LayoutParams
        val layoutParamsCvData = holder.cvData.layoutParams as ConstraintLayout.LayoutParams
        if (item.isStacked) {
            layoutParamsCv1.setMargins(holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12),
                    0,
                    holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12),
                    holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_5))
            layoutParamsCvData.setMargins(0, 0, 0,
                    holder.cvShadow1.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_10))
            holder.cvShadow1.show()
            holder.cvShadow2.show()
            holder.cvShadow1.layoutParams = layoutParamsCv1
            holder.cvData.layoutParams = layoutParamsCvData
        } else {
            holder.cvShadow1.hide()
            holder.cvShadow2.hide()
            layoutParamsCv1.setMargins(0, 0, 0, 0)
            layoutParamsCvData.setMargins(0, 0, 0, 0)
            holder.cvShadow1.layoutParams = layoutParamsCv1
            holder.cvData.layoutParams = layoutParamsCvData
        }

        if (item.upperLeftSection == null
                || item.upperLeftSection.textAttributes == null
                || item.upperLeftSection.textAttributes.isEmpty()
                || item.upperLeftSection.textAttributes[0] == null
                || TextUtils.isEmpty(item.upperLeftSection.textAttributes[0].text)) {
            holder.tvStackCount.hide()
        } else {
            holder.tvStackCount.show()
            holder.tvStackCount.text = item.upperLeftSection.textAttributes[0].text

            if (item.upperLeftSection.textAttributes[0].isBold) {
                holder.tvStackCount.setTypeface(holder.tvStackCount.typeface, Typeface.BOLD)
            } else {
                holder.tvStackCount.setTypeface(holder.tvStackCount.typeface, Typeface.NORMAL)
            }

            if (TextUtils.isEmpty(item.upperLeftSection.textAttributes[0].color)) {
                holder.tvStackCount.setTextColor(ContextCompat.getColor(holder.tvStackCount.context, com.tokopedia.design.R.color.medium_green))
            } else {
                try {
                    holder.tvStackCount.setTextColor(Color.parseColor(item.upperLeftSection.textAttributes[0].color))
                } catch (iae: IllegalArgumentException) {
                    holder.tvStackCount.setTextColor(ContextCompat.getColor(holder.tvStackCount.context, com.tokopedia.design.R.color.medium_green))
                }

            }

            if (!TextUtils.isEmpty(item.upperLeftSection.backgroundColor)) {
                val shape = getShape(item.upperLeftSection.backgroundColor, holder.tvStackCount.context)
                if (shape != null) {
                    holder.tvStackCount.background = shape
                }
            }
        }

        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer!!.cancel()
        }
        enableOrDisableImages(holder, item)

        if (item.usage != null) {
            if (item.usage.activeCountDown < 1) {
                if (item.usage.expiredCountDown > 0 && item.usage.expiredCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    holder.progressTimer.max = CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S.toInt()
                    holder.progressTimer.show()

                    if (holder.timer != null)
                        holder.timer!!.cancel()
                    holder.timer = object : CountDownTimer(item.usage.expiredCountDown * 1000, 1000) {
                        override fun onTick(l: Long) {
                            item.usage.expiredCountDown = l / 1000
                            holder.value.text = convertLongToHourMinuteSec(l)
                            holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.design.R.color.medium_green))
                            holder.progressTimer.progress = l.toInt() / 1000
                            try {
                                holder.value.setPadding(holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                        holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                        holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                        holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall))
                            } catch (e: Exception) {

                            }

                        }

                        override fun onFinish() {
                            holder.value.text = DEFAULT_TIME_STRING
                        }
                    }.start()
                } else {
                    holder.progressTimer.hide()
                    holder.value.setPadding(0, 0, 0, 0)
                    holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.design.R.color.black_70))
                }
            } else {
                holder.progressTimer.hide()
                holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.design.R.color.black_70))
            }
            holder.itemView?.let {
                holder.itemView.setOnClickListener{ v ->
                    if (item.isStacked) {
                        mStackedID = item.stackId
                        mPresenter.getCouponInStack(item.stackId)
                    } else {
                        val bundle = Bundle()
                        bundle.putString(CommonConstant.EXTRA_COUPON_CODE, item.code)
                        if (item.isNewCoupon) {
                            (holder.imgBanner.context as FragmentActivity).startActivityForResult(CouponDetailActivity.getCouponDetail(holder.imgBanner.context, bundle), REQUEST_CODE_STACKED_ADAPTER)
                        } else {
                            holder.imgBanner.context.startActivity(CouponDetailActivity.getCouponDetail(holder.imgBanner.context, bundle))
                        }
                        sendClickEvent(holder.imgBanner.context, item, holder.getAdapterPosition())
                    }
                }
            }

        }
    }

    private fun enableOrDisableImages(holder: ViewHolder, item: CouponValueEntity) {
        if (item.usage != null) {
            if (item.usage.activeCountDown > 0 || item.usage.expiredCountDown <= 0) {
                disableImages(holder)
            } else {
                enableImages(holder)
            }
        } else {
            disableImages(holder)
        }
    }

    private fun disableImages(holder: ViewHolder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN)
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun enableImages(holder: ViewHolder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN)
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun getShape(hex: String, context: Context): GradientDrawable? {
        try {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat(), context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_4).toFloat())
            shape.setColor(Color.parseColor(hex))
            shape.setStroke(context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_2), Color.parseColor(hex))
            return shape
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ViewHolder) {
            holder.onDetach()
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun onDestroyView() {
        for (i in 0 until mRecyclerView!!.childCount) {
            val viewHolder = mRecyclerView!!.getChildViewHolder(mRecyclerView!!.getChildAt(i)) as ViewHolder
            viewHolder.onDetach()
        }
    }


}
