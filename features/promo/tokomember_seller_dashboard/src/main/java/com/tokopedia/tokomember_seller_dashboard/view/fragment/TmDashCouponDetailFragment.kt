package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokomember_common_widget.TokomemberMultiTextView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography

//Created by - Harshit Jain on Sept 2022

class TmDashCouponDetailFragment:Fragment(),TmCouponListRefreshCallback {

    private lateinit var header:HeaderUnify
    private lateinit var couponImage:ImageUnify
    private lateinit var couponTimerBadge:TimerUnifySingle
    private lateinit var couponStatusTv:Typography
    private lateinit var couponStartPeriodTv:Typography
    private lateinit var couponEndPeriodTv:Typography
    private lateinit var membershipStatusTv:Typography
    private lateinit var couponTypeTv:TokomemberMultiTextView
    private lateinit var maxCashbackTv:TokomemberMultiTextView
    private lateinit var minTransaksiTv:TokomemberMultiTextView
    private lateinit var kuotaTv:TokomemberMultiTextView
    private lateinit var cta:UnifyButton

    private var couponData:VouchersItem?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_coupon_detail_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        renderCouponDetails()
        cta.setOnClickListener {
            openQuotaBottomSheet()
        }
    }

    private fun initViews(view: View){
        header=view.findViewById(R.id.tm_coupon_detail_header)
        couponImage = view.findViewById(R.id.coupon_detail_image)
        couponTimerBadge = view.findViewById(R.id.coupon_detail_timer)
        couponStatusTv = view.findViewById(R.id.coupon_detail_status_tv)
        couponStartPeriodTv = view.findViewById(R.id.coupon_detail_start_period)
        couponEndPeriodTv = view.findViewById(R.id.coupon_detail_end_period)
        membershipStatusTv = view.findViewById(R.id.coupon_detail_membership_status)
        couponTypeTv = view.findViewById(R.id.coupon_type_tv)
        maxCashbackTv = view.findViewById(R.id.coupon_maks_cashback_tv)
        minTransaksiTv = view.findViewById(R.id.coupon_min_transaksi_tv)
        kuotaTv = view.findViewById(R.id.coupon_kuota_tv)
        cta = view.findViewById(R.id.coupon_detail_cta)
        header.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun renderCouponDetails(){
        renderCouponPeriod()
        renderCouponMembershipStatus()
        renderCouponStatus()
    }


    //Render Date for the coupon
    private fun renderCouponPeriod(){
        val startDate = couponData?.voucherStartTime?.let { TmDateUtil.setDatePreview(it) } ?: ""
        val startTime = couponData?.voucherStartTime?.let { TmDateUtil.setTime(it) } ?: ""
        val endDate = couponData?.voucherFinishTime?.let { TmDateUtil.setDatePreview(it) } ?: ""
        val endTime = couponData?.voucherFinishTime?.let { TmDateUtil.setTime(it) } ?: ""

        val ss1 = SpannableString("$startDate\n$startTime")
        val ss2 = SpannableString("$endDate\n$endTime")

        ss1.setSpan(
            StyleSpan(Typeface.BOLD),
            0,startDate.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        ss2.setSpan(
            StyleSpan(Typeface.BOLD),
            0,endDate.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        couponStartPeriodTv.text=ss1
        couponEndPeriodTv.text = ss2
    }



    //Render Coupon Membership Status
    private fun renderCouponMembershipStatus(){
        couponData?.minimumTierLevel?.let {
            when(it){
                COUPON_VIP -> {
                    membershipStatusTv.text = context?.getString(R.string.tm_vip_kupon)
                    membershipStatusTv.setTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                com.tokopedia.unifyprinciples.R.color.Unify_YN500
                            )
                        )
                    )
                    membershipStatusTv.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_YN100
                        )
                    )
                }

                COUPON_MEMBER -> {
                    membershipStatusTv.text = context?.getString(R.string.tm_premium_kupon)
                    membershipStatusTv.setTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                com.tokopedia.unifyprinciples.R.color.Unify_NN600
                            )
                        )
                    )
                    membershipStatusTv.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_NN100
                        )
                    )
                }
            }
        }
    }

    private fun renderCouponStatus(){
        couponData?.let{
            if(it.remainingQuota==0) setCouponStateToEmpty()
            else{
                when(it.voucherStatus){
                    COUPON_ON_GOING -> {setCouponStateToActive()}
                    COUPON_ENDED -> {setCouponStateToEnded()}
                    COUPON_NOT_STARTED -> {setCouponStateToNotStarted()}
                }
            }
        }
    }

    //Set the coupon to empty state
    private fun setCouponStateToEmpty(){
        couponStatusTv.text = COUPON_STATE_EMPTY_LABEL
        couponStatusTv.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_YN300
                )
            )
        )
    }

    //Set the coupon to active state
    private fun setCouponStateToActive(){
        couponStatusTv.text = COUPON_STATE_ACTIVE_LABEL
        couponStatusTv.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
        )
    }

    //Set the coupon to active state
    private fun setCouponStateToNotStarted(){
        couponStatusTv.text = COUPON_STATE_NOT_STARTED_LABEL
        couponStatusTv.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        couponTimerBadge.visibility=View.GONE
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = dpToPx(16).toInt()
        couponStatusTv.layoutParams = params
    }

    //Set the coupon to ended state
    private fun setCouponStateToEnded(){
        couponStatusTv.text = COUPON_STATE_ENDED_LABEL
        couponStatusTv.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        couponTimerBadge.visibility=View.GONE
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = dpToPx(16).toInt()
        couponStatusTv.layoutParams = params
        cta.visibility = View.GONE
    }


    private fun openQuotaBottomSheet(){
        couponData?.let{
            TmAddQuotaBottomsheet.show(
                childFragmentManager,
                it.voucherId,
                it.voucherQuota,
                it.voucherTypeFormatted,
                this,
                it.voucherQuota
            )
        }
    }







    companion object{
        fun newInstance() : TmDashCouponDetailFragment {
            return TmDashCouponDetailFragment()
        }
//        private val layout = R.layout.tm_dash_coupon_detail_main
    }

    override fun refreshCouponList(action: String) {}
}