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
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.tokomember_common_widget.TokomemberMultiTextView
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailData
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmShareBottomSheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponDetailViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

//Created by - Harshit Jain on Sept 2022

class TmDashCouponDetailFragment:BaseDaggerFragment(),TmCouponListRefreshCallback,ShareBottomsheetListener {

    private var voucherId:Int = 0
    private var shopId = ""
    private var shareBottomSheet : TmShareBottomSheet?=null


    private var header:HeaderUnify?=null
    private var couponImage:ImageUnify?=null
    private var couponTimerBadge:TimerUnifySingle?=null
    private var couponStatusTv:Typography?=null
    private var couponStartPeriodTv:Typography?=null
    private var couponEndPeriodTv:Typography?=null
    private var membershipStatusTv:Typography?=null
    private var couponTypeTv:TokomemberMultiTextView?=null
    private var cashbackTypeTv:TokomemberMultiTextView?=null
    private var maxCashbackTv:TokomemberMultiTextView?=null
    private var minTransaksiTv:TokomemberMultiTextView?=null
    private var currExpenseTv:Typography?=null
    private var kuotaTv:TokomemberMultiTextView?=null
    private var progressBar:ProgressBarUnify?=null
    private var cashbackPercentTv:TokomemberMultiTextView?=null
    private var usedQuotaTv:Typography?=null
    private var totalQuotaTv:Typography?=null
    private var cta:UnifyButton?=null
    private var flipper: ViewFlipper?=null
    private var shareCouponBtn:LinearLayout?=null

    @Inject
    lateinit var tmTracker:TmTracker
    @Inject
    lateinit var userSession:UserSessionInterface

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
        shopId = userSession.shopId
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
        if(shopId.isNotEmpty()){
            tmTracker.viewCouponDetail(shopId)
        }
    }

    override fun getScreenName() = PATH_TOKOMEMBER_COUPON_DETAIL

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun observeViewModel(){
        tmCouponDetailVm.couponDetailResult.observe(viewLifecycleOwner){
            it?.let {
                when(it.status){
                    TokoLiveDataResult.STATUS.LOADING -> {
                       flipper?.displayedChild = 0
                    }
                    TokoLiveDataResult.STATUS.SUCCESS -> {
                        flipper?.displayedChild = 1
                        initViews()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupHeader(view: View){
        header=view.findViewById(R.id.tm_coupon_detail_header)
        header?.setNavigationOnClickListener {
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
            currExpenseTv = it.findViewById(R.id.curr_expense_tv)
            shareCouponBtn = it.findViewById(R.id.coupon_detail_share_btn)
            cashbackPercentTv = it.findViewById(R.id.coupon_cashback_percent_tv)

            renderCouponDetails()
            setupCtaButton()
        }
    }


    private fun renderCouponDetails(){
        renderCouponImage()
        setupTimer()
        renderCouponStatus()
        renderCouponPeriod()
        renderCouponMembershipStatus()
        setupQuotaProgressBar()
        renderCouponInformation()
        renderCurrentExpenses()
    }

    private fun setupTimer(){
        tmCouponDetailVm.couponDetailResult.value?.data?.let {
            val finishTime = it.merchantPromotionGetMVDataByID?.data?.voucherFinishTime
            TmDateUtil.getDateFromISO(finishTime)?.let{ it1 ->
                val calender = Calendar.getInstance(locale)
                calender.time = it1
                couponTimerBadge?.isShowClockIcon = true
                couponTimerBadge?.targetDate = calender
            }
        }
    }


    //Render Date for the coupon
    private fun renderCouponPeriod(){
        tmCouponDetailVm.couponDetailResult.value?.data?.let { res ->
            val startDate = res.merchantPromotionGetMVDataByID?.data?.voucherStartTime?.let {
                if(it.isNotEmpty()) TmDateUtil.setDatePreview(it, ISO_8601_UTC_DATE_FORMAT) else ""
            } ?: ""
            val startTime = res.merchantPromotionGetMVDataByID?.data?.voucherStartTime?.let {
                if(it.isNotEmpty()) TmDateUtil.setTime(it) else ""
            } ?: ""
            val endDate = res.merchantPromotionGetMVDataByID?.data?.voucherFinishTime?.let {
                if(it.isNotEmpty()) TmDateUtil.setDatePreview(it, ISO_8601_UTC_DATE_FORMAT) else ""
            } ?: ""
            val endTime = res.merchantPromotionGetMVDataByID?.data?.voucherFinishTime?.let {
                if(it.isNotEmpty()) TmDateUtil.setTime(it) else ""
            } ?: ""

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

            couponStartPeriodTv?.text=ss1
            couponEndPeriodTv?.text = ss2
        }
    }



    //Render Coupon Membership Status
    private fun renderCouponMembershipStatus(){
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{ res ->
            res.minimumTierLevel?.let {
                when(it){
                    COUPON_VIP -> {
                        membershipStatusTv?.text = context?.getString(R.string.tm_vip_kupon)
                        membershipStatusTv?.setTextColor(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    com.tokopedia.unifyprinciples.R.color.Unify_YN500
                                )
                            )
                        )
                        membershipStatusTv?.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                com.tokopedia.unifyprinciples.R.color.Unify_YN100
                            )
                        )
                    }

                    COUPON_MEMBER -> {
                        membershipStatusTv?.text = context?.getString(R.string.tm_premium_kupon)
                        membershipStatusTv?.setTextColor(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                                )
                            )
                        )
                        membershipStatusTv?.backgroundTintList = ColorStateList.valueOf(
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
                if((it.remaningQuota==null || it.remaningQuota==0) && it.voucherStatus == COUPON_ON_GOING) setCouponStateToEmpty()
                else{
                    when(it.voucherStatus){
                        COUPON_ON_GOING -> setCouponStateToActive()
                        COUPON_ENDED, COUPON_STOPPED -> setCouponStateToEnded()
                        COUPON_NOT_STARTED -> setCouponStateToNotStarted()
                        COUPON_DELETED -> setCouponStateToDeleted()
                        COUPON_PROCESSING -> setCouponStateToProcessing()
                    }
                }
        }
    }

    //Set the coupon to empty state
    private fun setCouponStateToEmpty(){
        couponStatusTv?.text = COUPON_STATE_EMPTY_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_YN300
                )
            )
        )
        showShareButton()
    }

    //Set the coupon to active state
    private fun setCouponStateToActive(){
        couponStatusTv?.text = COUPON_STATE_ACTIVE_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
        )
        showShareButton()
    }

    //Set the coupon to active state
    private fun setCouponStateToNotStarted(){
        couponStatusTv?.text = COUPON_STATE_NOT_STARTED_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        removeCouponTimer()
        hideShareButton()
    }

    //Set the coupon to ended state
    private fun setCouponStateToEnded(){
        couponStatusTv?.text = COUPON_STATE_ENDED_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        removeCouponTimer()
        cta?.visibility = View.GONE
        hideShareButton()
    }

    //Set the coupon to deleted state
    private fun setCouponStateToDeleted(){
        couponStatusTv?.text = COUPON_STATE_DELETED_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        removeCouponTimer()
        cta?.visibility = View.GONE
        hideShareButton()
    }


    //Set the coupon to processing state
    private fun setCouponStateToProcessing(){
        couponStatusTv?.text = COUPON_STATE_PROCESSING_LABEL
        couponStatusTv?.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        )
        showShareButton()
    }


    private fun removeCouponTimer(){
        couponTimerBadge?.visibility=View.GONE
        val params = couponStatusTv?.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = dpToPx(16).toInt()
        couponStatusTv?.layoutParams = params
    }

    //Set the quota progress
    private fun setupQuotaProgressBar(){
        tmCouponDetailVm.couponDetailResult.value.let {
            val usedQuota = it?.data?.merchantPromotionGetMVDataByID?.data?.confirmedGlobalQuota ?: 0
            val totalQuota = it?.data?.merchantPromotionGetMVDataByID?.data?.voucherQuota ?: 0
           usedQuotaTv?.text = it?.data?.merchantPromotionGetMVDataByID?.data?.confirmedGlobalQuota.toString()
            totalQuotaTv?.text = "/${it?.data?.merchantPromotionGetMVDataByID?.data?.voucherQuota.toString()}"
            if(totalQuota!=0){
                val progress = (usedQuota*1.0/totalQuota)*100
                progressBar?.setValue(progress.toInt(),true)
            }
            else progressBar?.setValue(0)
        }
    }

    private fun renderCouponInformation(){
        tmCouponDetailVm.couponDetailResult.value?.let{
            it.data?.merchantPromotionGetMVDataByID?.data?.let{ res ->
               renderCouponType(res.voucherType)
                renderCashbackType(res)
            }
        }
    }


    // To render the type of coupon
    private fun renderCouponType(type:Int?){
        when(type){
            COUPON_TYPE_CASHBACK -> {
                couponTypeTv?.valueTv?.text = COUPON_CASHBACK_PREVIEW
            }
            COUPON_TYPE_SHIPPING -> {
                couponTypeTv?.valueTv?.text = COUPON_SHIPPING_PREVIEW
            }
            COUPON_TYPE_DISCOUNT -> {
                couponTypeTv?.valueTv?.text = COUPON_DISCOUNT_PREVIEW
            }
        }
    }


    private fun renderCashbackType(data : TmCouponDetailData){
        when(data.voucherDiscountType){
            COUPON_DISCOUNT_TYPE_IDR -> {
                cashbackPercentTv?.visibility = View.GONE
                cashbackTypeTv?.valueTv?.text = requireContext().resources.getString(R.string.tm_coupon_cashback_idr)
                maxCashbackTv?.valueTv?.text = requireContext().resources.getString(R.string.tm_currency,CurrencyFormatHelper.convertToRupiah(data.voucherDiscountAmtMax.toString()))
            }
            COUPON_DISCOUNT_TYPE_PERCENT -> {
               cashbackPercentTv?.visibility = View.VISIBLE
                cashbackPercentTv?.valueTv?.text = "${data.voucherDiscountAmt}%"
                cashbackTypeTv?.valueTv?.text = requireContext().resources.getString(R.string.tm_coupon_cashback_percent)
                val cashback = (data.voucherDiscountAmt?:0).toFloat() / 100 * (data.voucherDiscountAmtMax ?: 0)
                maxCashbackTv?.valueTv?.text = requireContext().resources.getString(R.string.tm_currency,CurrencyFormatHelper.convertToRupiah(cashback.roundToInt().toString()))
            }
        }
        minTransaksiTv?.valueTv?.text = requireContext().resources.getString(R.string.tm_currency,CurrencyFormatHelper.convertToRupiah(data.voucherMinimumAmt.toString()))
        kuotaTv?.valueTv?.text = data.voucherQuota.toString()
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
                this,
                it.voucherDiscountAmtMax ?: 0,
            )
        }
    }

    private fun renderCurrentExpenses(){
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{
            val expense =it.voucherDiscountAmtMax?.times(it.confirmedGlobalQuota ?: 0) ?: 0
            currExpenseTv?.text = requireContext().resources.getString(R.string.tm_currency,CurrencyFormatHelper.convertToRupiah(expense.toString()))
        }
    }



    // To render the Coupon Image
   private fun renderCouponImage(){
        couponImage?.let { it1 ->
            tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let{
                Glide.with(requireContext())
                    .load(it.voucherImageSquare)
                    .into(it1)
            }
        }
   }


    private fun setupCtaButton(){
        tmCouponDetailVm.couponDetailResult.value?.data.let{
            val status = it?.merchantPromotionGetMVDataByID?.data?.voucherStatus
            if(status == COUPON_NOT_STARTED){
                cta?.text = requireContext().resources.getString(R.string.tm_ubah_kupon)
                cta?.setOnClickListener {
                    TmDashCreateActivity.openActivity(activity, CreateScreenType.COUPON_SINGLE, voucherId, this, edit = true)
                }
            }
            else{
                cta?.text = requireContext().resources.getString(R.string.tm_tambah_kuota)
                cta?.setOnClickListener {
                    tmTracker.clickTambahQuotaCouponDetail(shopId)
                    openQuotaBottomSheet()
                }
            }
        }
    }

    private fun hideShareButton(){
        shareCouponBtn?.visibility= View.GONE
    }

    private fun showShareButton(){
        shareCouponBtn?.visibility = View.VISIBLE
        shareCouponBtn?.setOnClickListener {
            showShareBottomSheet()
        }
    }


    private fun showShareBottomSheet(){
        var shareBmThumbnailTitle = ""
        val couponImages:ArrayList<String> = ArrayList()
        tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data?.let {
           couponImages.apply {
               it.voucherImageSquare?.let { it1 -> add(it1) }
               it.voucherImage?.let { it1 -> add(it1) }
               it.voucherImagePortrait?.let { it1 -> add(it1) }
           }
            shareBmThumbnailTitle = it.voucherName ?: ""
        }
        shareBottomSheet = TmShareBottomSheet().apply {
            init(this@TmDashCouponDetailFragment)
            setMetaData(
                tnTitle = shareBmThumbnailTitle,
                tnImage = if(couponImages.isNotEmpty()) couponImages[0] else AVATAR_IMAGE,
                imageList = couponImages
            )
            setOgImageUrl(if(couponImages.isNotEmpty()) couponImages[0] else AVATAR_IMAGE)
        }
        val bottomSheetTitle = context?.resources?.getString(R.string.tm_share_bottomsheet_title).orEmpty()
        shareBottomSheet?.setTmShareBottomSheetTitle(bottomSheetTitle)
        shareBottomSheet?.setTmShareBottomImgOptionsTitle(context?.resources?.getString(R.string.tm_share_bottomsheet_img_options_title).orEmpty())
        shareBottomSheet?.show(childFragmentManager,this,null)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val couponData = tmCouponDetailVm.couponDetailResult.value?.data?.merchantPromotionGetMVDataByID?.data
       val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
           id = couponData?.galadrielVoucherId.toString()
           name = couponData?.voucherName.orEmpty()
           type = LinkerData.MERCHANT_VOUCHER
           uri  = couponData?.weblink.orEmpty()
           deepLink = couponData?.applink.orEmpty()
           feature = shareModel.feature
           channel = shareModel.channel
           campaign = shareModel.campaign
           if (shareModel.ogImgUrl?.isNotEmpty() == true) {
               ogImageUrl = shareModel.ogImgUrl
           }
           isThrowOnError = true
       })
        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(
            0,linkerShareData,object : ShareCallback{
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = requireContext().resources.getString(R.string.tm_share_coupon_string,couponData?.voucherName,linkerShareData?.url)
                    SharingUtil.executeShareIntent(
                        shareModel,
                        linkerShareData,
                        requireActivity(),
                        view,
                        shareString
                    )
                    shareBottomSheet?.dismiss()
                }

                override fun onError(linkerError: LinkerError?) {
                    shareBottomSheet?.dismiss()
                }
            }
        ))
    }

    override fun onCloseOptionClicked() {
        shareBottomSheet?.dismiss()
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
        const val AVATAR_IMAGE = "https://s3-alpha-sig.figma.com/img/cbf3/9bb1/cbcb65989d6c190137df523b9fdc8c6d?Expires=1664150400&Signature=Y94MI9p13XBDqPCBDfQJJfLmoVvrgcvF8qE5E8agot48lwtGUdE6aecszIoQFkRxynCBklr~DP6BMxhNNlE6lfksUsA4uwvz8R8A9GBGiorVELMtwOsS95xd6SvYfoIz1FXsSAggw9~CoeH0ZihcIyPdpF4dQXYCP8GD2AbuPiMIfiD3eACNgGFOye5pzsXnu~ch5K9LhFklA3iiRIn-UjfsyPYwLBhVpsY0U5T8bUZJB46O8KlBaFkNEUu2m32rfMZKt9pehXLZcCxFQM3o8W58Rg5rr2L3dGZbBkUdCAS3Sfjf9rUhEH3h8eDMq7waIpVwqsiW~t3Vs2hiomUHMQ__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
    }

    override fun refreshCouponList(action: String) {
        tmCouponDetailVm.getCouponDetails(voucherId)
    }
}

