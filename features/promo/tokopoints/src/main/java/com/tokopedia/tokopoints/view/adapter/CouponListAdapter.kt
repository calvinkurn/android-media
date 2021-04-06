package com.tokopedia.tokopoints.view.adapter

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailActivity.Companion.getCouponDetail
import com.tokopedia.tokopoints.view.model.CouponValueEntity
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import java.util.*

class CouponListAdapter(private val mItems: MutableList<CouponValueEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mRecyclerView: RecyclerView? = null

    // Define a ViewHolder for Footer view
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { v: View ->
                RouteManager.route(v.context, ApplinkConstInternalPromo.TOKOPOINTS_COUPON)
                AnalyticsTrackerUtil.sendEvent(itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_COUPON, ""
                )
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var label: TextView
        var value: TextView
        var tvMinTxnValue: TextView
        var tvMinTxnLabel: TextView
        var imgBanner: ImageView
        var imgLabel: ImageView
        var ivMinTxn: ImageView
        var isVisited = false

        /*This section is exclusively for handling timer*/
        var timer: CountDownTimer? = null
        var progressTimer: ProgressBar
        fun onDetach() {
            if (timer != null) {
                timer!!.cancel()
                timer = null
            }
        }

        init {
            label = view.findViewById(R.id.text_time_label)
            value = view.findViewById(R.id.text_time_value)
            imgBanner = view.findViewById(R.id.img_banner)
            imgLabel = view.findViewById(R.id.img_time)
            ivMinTxn = view.findViewById(R.id.iv_rp)
            tvMinTxnValue = view.findViewById(R.id.tv_min_txn_value)
            tvMinTxnLabel = view.findViewById(R.id.tv_min_txn_label)
            progressTimer = view.findViewById(R.id.progress_timer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        if (viewType == VIEW_HEADER) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tp_item_my_coupon_section_header, parent, false)
            return HeaderViewHolder(itemView)
        }
        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_my_coupon_section, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(pHolder: RecyclerView.ViewHolder, position: Int) {

        if (position > 0 && mItems.size > position - 1) {
            val item = mItems[position - 1]
            if (pHolder is ViewHolder) {
                val holder = pHolder
                ImageHandler.loadImageFitCenter(holder.imgBanner.context, holder.imgBanner,
                        if (TextUtils.isEmpty(item.thumbnailUrlMobile)) item.imageUrlMobile else item.thumbnailUrlMobile)
                if (item.usage != null) {
                    holder.label.visibility = View.VISIBLE
                    holder.value.visibility = View.VISIBLE
                    holder.imgLabel.visibility = View.VISIBLE
                    holder.value.text = item.usage.usageStr.trim { it <= ' ' }
                    holder.label.text = item.usage.text
                }
                if (TextUtils.isEmpty(item.minimumUsageLabel)) {
                    holder.tvMinTxnLabel.visibility = View.GONE
                    holder.ivMinTxn.visibility = View.GONE
                } else {
                    holder.ivMinTxn.visibility = View.VISIBLE
                    holder.tvMinTxnLabel.visibility = View.VISIBLE
                    holder.tvMinTxnLabel.text = item.minimumUsageLabel
                }
                if (TextUtils.isEmpty(item.minimumUsage)) {
                    holder.tvMinTxnValue.visibility = View.GONE
                } else {
                    holder.tvMinTxnValue.visibility = View.VISIBLE
                    holder.tvMinTxnValue.text = item.minimumUsage
                }
                holder.imgBanner.setOnClickListener { v: View? ->
                    val bundle = Bundle()
                    bundle.putString(CommonConstant.EXTRA_COUPON_CODE, mItems[position - 1].code)
                    holder.imgBanner.context.startActivity(getCouponDetail(holder.imgBanner.context, bundle), bundle)
                }
                /*This section is exclusively for handling flash-sale timer*/if (holder.timer != null) {
                    holder.timer!!.cancel()
                }
                if (item.usage != null && item.usage.activeCountDown < 1) {
                    if (item.usage.expiredCountDown > 0
                            && item.usage.expiredCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                        holder.progressTimer.max = CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S.toInt()
                        holder.progressTimer.visibility = View.VISIBLE
                        holder.value.visibility = View.VISIBLE
                        holder.label.hide()
                        if (holder.timer != null) {
                            holder.timer!!.cancel()
                        }
                        holder.timer = object : CountDownTimer(item.usage.expiredCountDown * 1000, 1000) {
                            override fun onTick(l: Long) {
                                item.usage.expiredCountDown = l / 1000
                                val seconds = (l / 1000).toInt() % 60
                                val minutes = (l / (1000 * 60) % 60).toInt()
                                val hours = (l / (1000 * 60 * 60) % 24).toInt()
                                holder.value.text = String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
                                try {
                                    holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                                } catch (e: Exception) {
                                }
                                holder.progressTimer.progress = l.toInt() / 1000
                                try {
                                    holder.value.setPadding(holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_small),
                                            holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                            holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_small),
                                            holder.label.resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall))
                                } catch (e: Exception) {
                                }
                            }

                            override fun onFinish() {
                                holder.value.text = "00 : 00 : 00"
                            }
                        }.start()
                    } else {
                        holder.progressTimer.visibility = View.GONE
                        holder.value.setPadding(0, 0, 0, 0)
                        holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                } else {
                    holder.progressTimer.visibility = View.GONE
                    holder.value.setTextColor(ContextCompat.getColor(holder.value.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                }
                enableOrDisableImages(holder, item)
            } else if (pHolder is HeaderViewHolder) {
            }
        }
    }

    private fun enableOrDisableImages(holder: ViewHolder, item: CouponValueEntity) {
        if (item.usage != null) {
            if (item.usage.activeCountDown > 0
                    || item.usage.expiredCountDown <= 0) {
                disableImages(holder)
            } else {
                enableImages(holder)
            }
        } else {
            disableImages(holder)
        }
    }

    private fun disableImages(holder: ViewHolder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, com.tokopedia.unifyprinciples.R.color.Unify_N100), PorterDuff.Mode.SRC_IN)
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, com.tokopedia.unifyprinciples.R.color.Unify_N100), PorterDuff.Mode.SRC_IN)
    }

    private fun enableImages(holder: ViewHolder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.context, com.tokopedia.unifyprinciples.R.color.Unify_G400), PorterDuff.Mode.SRC_IN)
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.context, com.tokopedia.unifyprinciples.R.color.Unify_G400), PorterDuff.Mode.SRC_IN)
    }

    override fun getItemCount(): Int {
        return mItems?.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (mItems == null) {
            return super.getItemViewType(position)
        }
        return if (position == 0) { // This is where we'll add footer.
            VIEW_HEADER
        } else VIEW_DATA
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
        if (mRecyclerView != null) {
            for (i in 0 until mRecyclerView!!.childCount) {
                val holder = mRecyclerView!!.getChildViewHolder(mRecyclerView!!.getChildAt(i))
                if (holder is ViewHolder) {
                    holder.onDetach()
                }
            }
        }
    }

    companion object {
        private const val VIEW_HEADER = 0
        private const val VIEW_DATA = 1
    }
}