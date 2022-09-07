package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokomember_common_widget.TokomemberMultiTextView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponDetailViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

//Created by - Harshit Jain on Sept 2022

class TmDashCouponDetailFragment:BaseDaggerFragment(),TmCouponListRefreshCallback {

    private var voucherId:Int = 0

    private lateinit var header:HeaderUnify
    private lateinit var couponImage:ImageUnify
    private lateinit var couponTimerBadge:TimerUnifySingle
    private lateinit var couponStatusTv:Typography
    private lateinit var couponStartPeriodTv:Typography
    private lateinit var couponEndPeriodTv:Typography
    private lateinit var membershipStatusTv:Typography
    private lateinit var couponTypeTv:TokomemberMultiTextView
    private lateinit var cashbackTypeTv:TokomemberMultiTextView
    private lateinit var maxCashbackTv:TokomemberMultiTextView
    private lateinit var minTransaksiTv:TokomemberMultiTextView
    private lateinit var kuotaTv:TokomemberMultiTextView
    private lateinit var progressBar:ProgressBarUnify
    private lateinit var usedQuotaTv:Typography
    private lateinit var totalQuotaTv:Typography
    private lateinit var cta:UnifyButton
    private lateinit var flipper: ViewFlipper

    private var couponData:VouchersItem?=null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmCouponDetailVm: TmCouponDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmCouponDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            voucherId = it.getInt(VOUCHER_ID,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader(view)
        flipper = view.findViewById(R.id.coupon_detail_view_flip)
        observeViewModel()
        tmCouponDetailVm.getCouponDetails(voucherId)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun observeViewModel(){
        tmCouponDetailVm.couponDetailResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                val gson=Gson()
                Log.i("from coupon detail","data -> ${gson.toJson(it.data)}")
                when(it.status){
                    TokoLiveDataResult.STATUS.LOADING -> {
                       flipper.displayedChild = 0
                    }
                    TokoLiveDataResult.STATUS.SUCCESS -> {
                        flipper.displayedChild = 1
                        initViews()
                    }
                }
            }
        })
    }

    private fun setupHeader(view: View){
        header=view.findViewById(R.id.tm_coupon_detail_header)
        header.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initViews(){
        view?.let{
            couponImage = it.findViewById(R.id.coupon_detail_image)
            couponTimerBadge = it.findViewById(R.id.coupon_detail_timer)
            couponStatusTv = it.findViewById(R.id.coupon_detail_status_tv)
            couponStartPeriodTv = it.findViewById(R.id.coupon_detail_start_period)
            couponEndPeriodTv = it.findViewById(R.id.coupon_detail_end_period)
            membershipStatusTv = it.findViewById(R.id.coupon_detail_membership_status)
            couponTypeTv = it.findViewById(R.id.coupon_type_tv)
            cashbackTypeTv = it.findViewById(R.id.coupon_cashback_type_tv)
            maxCashbackTv = it.findViewById(R.id.coupon_maks_cashback_tv)
            minTransaksiTv = it.findViewById(R.id.coupon_min_transaksi_tv)
            kuotaTv = it.findViewById(R.id.coupon_kuota_tv)
            cta = it.findViewById(R.id.coupon_detail_cta)
            progressBar = it.findViewById(R.id.coupon_detail_progress_bar)
            usedQuotaTv = it.findViewById(R.id.coupon_detail_used_quota)
            totalQuotaTv = it.findViewById(R.id.coupon_detail_total_quota)
            cta.setOnClickListener { openQuotaBottomSheet() }
            renderCouponDetails()
        }
    }

    private fun renderCouponDetails(){
//        renderCouponPeriod()
        renderCouponMembershipStatus()
        renderCouponStatus()
        setupQuotaProgressBar()
    }


    //Render Date for the coupon
    private fun renderCouponPeriod(){
        tmCouponDetailVm.couponDetailResult?.value?.data?.let { res ->
            val startDate = res.merchantPromotionGetMVDataByID?.data?.voucherStartTime?.let { TmDateUtil.setDatePreview(it) } ?: ""
            val startTime = res.merchantPromotionGetMVDataByID?.data?.voucherStartTime?.let { TmDateUtil.setTime(it) } ?: ""
            val endDate = res.merchantPromotionGetMVDataByID?.data?.voucherFinishTime?.let { TmDateUtil.setDatePreview(it) } ?: ""
            val endTime = res.merchantPromotionGetMVDataByID?.data?.voucherFinishTime?.let { TmDateUtil.setTime(it) } ?: ""

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
    }



    //Render Coupon Membership Status
    private fun renderCouponMembershipStatus(){
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{ res ->
            res.minimumTierLevel?.let {
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
    }

    private fun renderCouponStatus(){
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{
                if(it.remaningQuota==null || it.remaningQuota==0) setCouponStateToEmpty()
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
        Log.i("from coupon detail","parent -> ${couponStatusTv.parent}")
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
        val params = couponStatusTv.layoutParams as ConstraintLayout.LayoutParams
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
        val params = couponStatusTv.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = dpToPx(16).toInt()
        couponStatusTv.layoutParams = params
        cta.visibility = View.GONE
    }

    //Set the quota progress
    private fun setupQuotaProgressBar(){
        tmCouponDetailVm.couponDetailResult.value.let {
            val remQuota = it?.data?.merchantPromotionGetMVDataByID?.data?.remaningQuota ?: 0
            val totalQuota = it?.data?.merchantPromotionGetMVDataByID?.data?.voucherQuota ?: 0
            val usedQuota = totalQuota-remQuota
            var progress:Int
            if(totalQuota==0) progress=0
            else progress = usedQuota/totalQuota
            progressBar.setValue(progress,true)
            usedQuotaTv.text = usedQuota.toString()
            totalQuotaTv.text= "/$totalQuota"
        }
    }

    private fun renderCouponInformation(){
        tmCouponDetailVm.couponDetailResult.value?.let{
            it.data?.merchantPromotionGetMVDataByID?.data?.let{ res ->
                when(res.voucherType){
                    COUPON_TYPE_CASHBACK -> {
                        couponTypeTv.valueTv.text = COUPON_CASHBACK_PREVIEW
                    }
                    COUPON_TYPE_SHIPPING -> {
                        couponTypeTv.valueTv.text = COUPON_SHIPPING_PREVIEW
                    }
                }
            }
        }
    }

    //Open the add quota bottomsheet
    private fun openQuotaBottomSheet(){
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{
            TmAddQuotaBottomsheet.show(
                childFragmentManager,
                it.voucherId.toString(),
                it.voucherQuota ?: 0,
                it.voucherTypeFormatted ?: "",
                this,
                it.voucherDiscountAmtMax ?: 0
            )
        }
    }






    companion object{
        fun newInstance(voucherId:Int) : TmDashCouponDetailFragment {
            val bundle = Bundle()
            bundle.putInt(VOUCHER_ID,voucherId)
            return TmDashCouponDetailFragment().apply {
                arguments = bundle
            }
        }
        private val layout = R.layout.tm_coupon_detail_container
    }

    override fun refreshCouponList(action: String) {}
}