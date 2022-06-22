package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramDateType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.*
import com.tokopedia.tokomember_seller_dashboard.model.mapper.TmCouponCreateMapper
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTime
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getTimeInMillis
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getTimeInMillisEnd
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDate
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setTimeEnd
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TmCouponListItemPreview
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmExpandView.collapse
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmExpandView.expand
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmSingleCouponView
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_kupon_create_multiple.*
import kotlinx.android.synthetic.main.tm_dash_kupon_create_multiple.containerViewFlipper
import kotlinx.android.synthetic.main.tm_dash_kupon_create_multiple.globalError
import kotlinx.android.synthetic.main.tm_dash_tnc_coupon_creation.view.*
import java.util.*
import javax.inject.Inject

class TmMultipleCuponCreateFragment : BaseDaggerFragment() {

    private var tmTracker: TmTracker? = null
    private var selectedChipPositionDate: Int = 0
    private var selectedCalendar: Calendar? = null
    private var selectedTime = ""
    private var startTime: Calendar? = null
    private var programData: ProgramUpdateDataInput? = null
    private var couponPremiumData: TmSingleCouponData? = null
    private var couponVip: TmSingleCouponData? = null
    private var tmCouponPreviewData = TmCouponPreviewData()
    private var shopName = ""
    private var shopAvatar = ""
    private var retryCount = 0
    private val errorState = ErrorState()
    private var loaderDialog: LoaderDialog? = null
    private var tmCouponListVipItemPreview = TmCouponListItemPreview()
    private var tmCouponListPremiumItemPreview = TmCouponListItemPreview()
    private var tmCouponPremiumUploadId = ""
    private var tmCouponVipUploadId = ""
    private var tmToken = ""
    private var imageSquare = ""
    private var imagePortrait = ""
    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    private var isCollapsedVip = true
    private var isCollapsedPremium = true
    private var manualStartTimeProgram = ""
    private var manualEndTimeProgram = ""
    private var couponStartDate = ""
    private var couponStartTime = ""
    private var couponEndDate = ""
    private var couponEndTime = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TmOpenFragmentCallback) {
            tmOpenFragmentCallback = context
        } else {
            throw Exception(context.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_kupon_create_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmTracker = TmTracker()
        renderHeader()
        renderButton()
        observeViewModel()
        shopName = arguments?.getString(BUNDLE_SHOP_NAME) ?: ""
        shopAvatar = arguments?.getString(BUNDLE_SHOP_AVATAR) ?: ""
        programData = arguments?.getParcelable(BUNDLE_PROGRAM_DATA)
        tmDashCreateViewModel.getInitialCouponData(CREATE, "")
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val dagger = DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
        dagger.inject(this)
    }

    private fun observeViewModel() {

        tmDashCreateViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    tmToken = it.data?.getInitiateVoucherPage?.data?.token ?: ""
                    imageSquare = it.data?.getInitiateVoucherPage?.data?.imgBannerIgPost ?: ""
                    imagePortrait = it.data?.getInitiateVoucherPage?.data?.imgBannerIgStory ?: ""
                    tmDashCreateViewModel.getProgramInfo(arguments?.getInt(
                        BUNDLE_PROGRAM_ID_IN_TOOLS)?:0,arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,"create")
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleDataError()
                }
            }
        })

        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    containerViewFlipper.displayedChild = DATA
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        manualStartTimeProgram = it.data.membershipGetProgramForm.programForm?.timeWindow?.startTime?:""
                        manualEndTimeProgram= it.data.membershipGetProgramForm.programForm?.timeWindow?.endTime?:""
                        renderProgram()
                        renderMultipleCoupon()
                    }
                    else{
                        handleDataError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleDataError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponPreValidateSingleCouponLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    openLoadingDialog()
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    errorState.isPreValidateVipError = false
                    if (it.data?.voucherValidationPartial?.header?.messages?.size == 1) {
                        preValidateCouponVip(couponVip)
                    } else {
                        handleProgramValidateError(
                            it.data?.voucherValidationPartial?.data?.validationError,
                            "premium"
                        )
                        setButtonState()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    setButtonState()
                    errorState.isPreValidateVipError = true
                    closeLoadingDialog()
                    handleProgramValidateNetworkError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponPreValidateMultipleCouponLiveData.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    TokoLiveDataResult.STATUS.SUCCESS -> {
                        errorState.isPreValidatePremiumError = false
                        if (it.data?.voucherValidationPartial?.header?.messages?.size == 1) {
                            tmDashCreateViewModel.validateProgram(
                                arguments?.getInt(
                                    BUNDLE_SHOP_ID
                                ).toString(),
                                getTimeInMillis(manualStartTimeProgram),
                                getTimeInMillisEnd(manualEndTimeProgram),
                                SOURCE_MULTIPLE_COUPON_CREATE
                            )
                        } else {
                            setButtonState()
                            handleProgramValidateError(
                                it.data?.voucherValidationPartial?.data?.validationError,
                                "vip"
                            )
                        }
                    }
                    TokoLiveDataResult.STATUS.ERROR -> {
                        errorState.isPreValidatePremiumError = true
                        setButtonState()
                        closeLoadingDialog()
                        handleProgramValidateNetworkError()
                    }
                    else -> {
                    }
                }
            })

        tmDashCreateViewModel.tmProgramValidateLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipValidateBenefit?.resultStatus?.code == "200") {
                        errorState.isValidateCouponError = false
                        uploadImageVip()
                        closeLoadingDialog()
                    } else {
                        closeLoadingDialog()
                        setButtonState()
                        setButtonState()
                        handleProgramPreValidateError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isValidateCouponError = false
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateServerError()
                }
                else -> {
                }
            }
        })

        tmDashCreateViewModel.tmCouponUploadLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when (it.data) {
                        is UploadResult.Success -> {
                            errorState.isUploadPremium = false
                            tmCouponPremiumUploadId = it.data.uploadId
                            uploadImagePremium()
                        }
                        is UploadResult.Error -> {
                            closeLoadingDialog()
                            setButtonState()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    it.data.message,
                                    Toaster.TYPE_ERROR,
                                    Toaster.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isUploadPremium = true
                    setButtonState()
                    closeLoadingDialog()
                    view?.let { v ->
                        Toaster.build(
                            v,
                            RETRY,
                            Toaster.TYPE_ERROR,
                            Toaster.LENGTH_LONG
                        ).show()
                    }
                }
                else -> {
                }
            }
        })

        tmDashCreateViewModel.tmCouponUploadMultipleLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when (it.data) {
                        is UploadResult.Success -> {
                            errorState.isUploadVipError = false
                            tmCouponVipUploadId = it.data.uploadId
                            openPreviewPage()
                        }
                        is UploadResult.Error -> {
                            closeLoadingDialog()
                            setButtonState()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    it.data.message,
                                    Toaster.TYPE_ERROR,
                                    Toaster.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isUploadVipError = true
                    setButtonState()
                    closeLoadingDialog()
                    view?.let { v ->
                        Toaster.build(
                            v,
                            RETRY,
                            Toaster.TYPE_ERROR,
                            Toaster.LENGTH_LONG
                        ).show()
                    }
                }
                else -> {
                }
            }
        })
    }

    private fun openLoadingDialog() {
        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    private fun handleDataError(){
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {
            tmDashCreateViewModel.getInitialCouponData(CREATE, "")
        }
    }

    private fun handleProgramValidateNetworkError() {
        view?.let { Toaster.build(it, RETRY, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
        btnContinue.apply {
            isEnabled = true
            isClickable = true
        }
    }

    private fun handleProgramValidateError(validationError: ValidationError?, couponType: String) {
        when (couponType) {

            PREMIUM -> {
                tmPremiumCoupon?.setErrorMaxBenefit(validationError?.benefitMax ?: "")
                tmPremiumCoupon?.setErrorCashbackPercentage(validationError?.benefitPercent ?: "")
                tmPremiumCoupon?.setErrorMinTransaction(validationError?.minPurchase ?: "")
                tmPremiumCoupon?.setErrorQuota(validationError?.quota ?: "")
                if (!tmPremiumCoupon.isVisible) {
                    icArrowPremium.performClick()
                }
            }

            VIP -> {
                tmVipCoupon?.setErrorMaxBenefit(validationError?.benefitMax ?: "")
                tmVipCoupon?.setErrorCashbackPercentage(validationError?.benefitPercent ?: "")
                tmVipCoupon?.setErrorMinTransaction(validationError?.minPurchase ?: "")
                tmVipCoupon?.setErrorQuota(validationError?.quota ?: "")
                if (!tmVipCoupon.isVisible) {
                    icArrowVip.performClick()
                }
            }
        }
    }

    private fun setButtonState() {
        btnContinue.apply {
            isClickable = true
            isEnabled = true
        }
    }

    private fun handleProgramPreValidateError() {
        setButtonState()
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            PROGRAM_VALIDATION_ERROR_TITLE,
            PROGRAM_VALIDATION_ERROR_DESC,
            TM_ERROR_PROGRAM,
            PROGRAM_VALIDATION_CTA_TEXT
        )
        bundle.putString(
            TokomemberBottomsheet.ARG_BOTTOMSHEET,
            Gson().toJson(tmIntroBottomSheetModel)
        )
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                bottomSheet.dismiss()
            }
        })
        bottomSheet.show(childFragmentManager, "")
    }

    private fun handleProgramValidateServerError() {

        val title = when (retryCount) {
            0 -> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when (retryCount) {
            0 -> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            ERROR_CREATING_DESC,
            "",
            cta,
            errorCount = retryCount
        )
        bundle.putString(
            TokomemberBottomsheet.ARG_BOTTOMSHEET,
            Gson().toJson(tmIntroBottomSheetModel)
        )
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                when (errorCount) {
                    0 -> tmDashCreateViewModel.validateProgram(  arguments?.getInt(
                        BUNDLE_SHOP_ID
                    ).toString(),
                        getTimeInMillis(manualStartTimeProgram),
                        getTimeInMillisEnd(manualEndTimeProgram),
                        SOURCE_MULTIPLE_COUPON_CREATE)
                    else -> {
                        (TokomemberDashIntroActivity.openActivity(
                            arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,arguments?.getString(BUNDLE_SHOP_AVATAR)?:"" ,arguments?.getString(
                                BUNDLE_SHOP_NAME)?:"",
                            false,
                            context
                        ))
                    }
                }
            }
        })
        bottomSheet.show(childFragmentManager, "")
        retryCount += 1
    }

    private fun openPreviewPage() {

        val maxBenefit =
            (CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback?:"")* CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.quota?:"")) +
        CurrencyFormatHelper.convertRupiahToInt(couponVip?.maxCashback?:"") * CurrencyFormatHelper.convertRupiahToInt(couponVip?.quota?:"")

        //GET Coupon Time
        tmCouponPreviewData.apply {
            startDate = textFieldProgramStartDate.editText.text.toString()
            endDate = textFieldProgramEndDate.editText.text.toString()
            startTime = textFieldProgramStartTime.editText.text.toString()
            endTime = textFieldProgramEndTime.editText.text.toString()
            maxValue = maxBenefit.toString()
            voucherList.add(0, tmCouponListPremiumItemPreview)
            voucherList.add(1, tmCouponListVipItemPreview)
        }

        //Map data
        val tmMerchantCouponCreateData = TmCouponCreateMapper.mapCreateData(
            couponPremiumData,
            couponVip,
            tmCouponPremiumUploadId,
            tmCouponVipUploadId,
            couponStartDate,
            couponEndDate,
            couponStartTime,
            couponEndTime,
            tmToken, imageSquare, imagePortrait, maxBenefit
        )

        val bundle = Bundle()
        bundle.putInt(BUNDLE_CARD_ID, arguments?.getInt(BUNDLE_CARD_ID)?:0)
        bundle.putString(BUNDLE_SHOP_AVATAR, arguments?.getString(BUNDLE_SHOP_AVATAR))
        bundle.putString(BUNDLE_SHOP_NAME, arguments?.getString(BUNDLE_SHOP_NAME))
        bundle.putInt(BUNDLE_CARD_ID_IN_TOOLS,arguments?.getInt(BUNDLE_CARD_ID_IN_TOOLS) ?: 0)
        bundle.putInt(BUNDLE_SHOP_ID, arguments?.getInt(BUNDLE_SHOP_ID) ?: 0)
        bundle.putInt(BUNDLE_PROGRAM_ID_IN_TOOLS, arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS) ?: 0)
        bundle.putParcelable(BUNDLE_COUPON_PREVIEW_DATA, tmCouponPreviewData)
        bundle.putParcelable(BUNDLE_COUPON_CREATE_DATA, tmMerchantCouponCreateData)
        tmOpenFragmentCallback.openFragment(CreateScreenType.PREVIEW, bundle)
        closeLoadingDialog()
    }

    private fun initTotalTransactionAmount() {
        tmPremiumCoupon?.setMaxTransactionListener(object : TmSingleCouponView.MaxTransactionListener{
            override fun onQuotaCashbackChange() {
                setTotalTransactionAmount()
            }
        })

        tmVipCoupon?.setMaxTransactionListener(object : TmSingleCouponView.MaxTransactionListener{
            override fun onQuotaCashbackChange() {
                setTotalTransactionAmount()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalTransactionAmount() {
        couponPremiumData = tmPremiumCoupon?.getSingleCouponData()
        couponVip = tmVipCoupon?.getSingleCouponData()
        tvTotalTransaction.text = "Rp" + CurrencyFormatHelper.convertToRupiah(
            ((CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback ?: "") *
                    couponPremiumData?.quota.toIntSafely()) +
                    (CurrencyFormatHelper.convertRupiahToInt(couponVip?.maxCashback ?: "") *
                            couponVip?.quota.toIntSafely())).toString())
    }

    private fun renderHeader() {

        headerKupon?.apply {
            title = COUPON_HEADER_TITLE
            subtitle = COUPON_HEADER_SUBTITLE
            isShowBackButton = true
            setNavigationOnClickListener {
                if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_BUAT){
                    tmTracker?.clickCouponCreationBackFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                else{
                    tmTracker?.clickCouponCreationBack(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                activity?.onBackPressed()
            }
        }

        progressKupon?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(80, false)
        }
    }

    private fun renderButton() {

        val webViewTnc = View.inflate(context,R.layout.tm_dash_tnc_coupon_creation,null)
        webViewTnc.webview.loadUrl(TM_TNC)
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            setTitle(TERNS_AND_CONDITION)
            setChild(webViewTnc)
            showCloseIcon = true
            isDragable = true
            setCloseClickListener {
                dismiss()
            }
        }
        val ss = SpannableString(COUPON_TERMS_CONDITION)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                RouteManager.route(context,String.format("%s?url=%s", ApplinkConst.WEBVIEW, TM_TNC))
               // bottomSheetUnify.show(childFragmentManager, "")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val firstIndex = COUPON_TERMS_CONDITION.indexOf(TERMS)
        ss.setSpan(
            clickableSpan,
            firstIndex,
            firstIndex + TERNS_AND_CONDITION.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvTermsAndCondition.text = ss
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        btnContinue.setOnClickListener {
            if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_BUAT) {
                tmTracker?.clickCouponCreationFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }
            else {
                tmTracker?.clickCouponCreationButton(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }
            it.isEnabled = false
            it.isClickable = false
            preValidateCouponPremium(couponPremiumData)
        }
    }

    private fun preValidateCouponVip(coupon: TmSingleCouponData?) {
        tmDashCreateViewModel.preValidateMultipleCoupon(
            TmCouponValidateRequest(
                targetBuyer = 3,
                benefitType = IDR,
                couponType = coupon?.typeCoupon,
                benefitMax = coupon?.maxCashback.toIntSafely(),
                benefitPercent = coupon?.cashBackPercentage,
                minPurchase = coupon?.minTransaki.toIntSafely(),
                source = ANDROID
            )
        )
    }

    private fun preValidateCouponPremium(couponPremiumData: TmSingleCouponData?) {
        tmDashCreateViewModel.preValidateCoupon(
            TmCouponValidateRequest(
                targetBuyer = 3,
                benefitType = IDR,
                couponType = couponPremiumData?.typeCoupon,
                benefitMax = CurrencyFormatHelper.convertRupiahToInt(
                    couponPremiumData?.maxCashback ?: ""
                ),
                benefitPercent = couponPremiumData?.cashBackPercentage,
                minPurchase = CurrencyFormatHelper.convertRupiahToInt(
                    couponPremiumData?.minTransaki ?: ""
                ),
                source = ANDROID
            )
        )
    }

    private fun uploadImagePremium() {
        context?.let { ctx ->
            tmVipCoupon?.post {
                val coupon = tmPremiumCoupon.getCouponView()
                val file =  TmFileUtil.saveBitMap(ctx, coupon)
                tmCouponListPremiumItemPreview = TmCouponListItemPreview(
                    file.absolutePath, "Premium", couponPremiumData?.quota ?: ""
                )
                tmDashCreateViewModel.uploadImagePremium(file)
            }
        }
    }

    private fun uploadImageVip() {
        context?.let { ctx ->
            tmVipCoupon?.post {
                val coupon = tmVipCoupon.getCouponView()
               val file =  TmFileUtil.saveBitMap(ctx, coupon)
                tmCouponListVipItemPreview = TmCouponListItemPreview(
                    file.absolutePath, "VIP", couponVip?.quota ?: ""
                )
                tmDashCreateViewModel.uploadImageVip(file)
            }
        }
    }

    private fun renderMultipleCoupon() {

        setTotalTransactionAmount()
        tvTotalTransaction.text
        tmPremiumCoupon?.setShopData(shopName, shopAvatar)
        tmVipCoupon?.setShopData(shopName, shopAvatar)

        icArrowPremium.setOnClickListener {
            isCollapsedPremium = if (isCollapsedPremium) {
                icArrowPremium?.animate()?.rotation(180f)?.duration = 100L
                expand(tmPremiumCoupon)
                !isCollapsedPremium
            } else{
                icArrowPremium?.animate()?.rotation(0f)?.duration = 100L
                collapse(tmPremiumCoupon)
                !isCollapsedPremium
            }
        }

        icArrowVip.setOnClickListener {
            isCollapsedVip = if (isCollapsedVip) {
                icArrowVip?.animate()?.rotation(180f)?.duration = 100L
                expand(tmVipCoupon)
                !isCollapsedVip
            } else{
                icArrowVip?.animate()?.rotation(0f)?.duration = 100L
                collapse(tmVipCoupon)
                !isCollapsedVip
            }
        }

        initTotalTransactionAmount()

    }

    private fun renderProgram() {
        setProgramDateAuto()
        chipDateSelection.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionDate = position
                   setProgramDateAuto()
            }
        })
        chipDateSelection.setDefaultSelection(selectedChipPositionDate)
        chipDateSelection.addChips(arrayListOf(PROGRAM_TYPE_AUTO, PROGRAM_TYPE_MANUAL))

        textFieldProgramStartDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramStartTime.setFirstIcon(R.drawable.tm_dash_clock)
        textFieldProgramEndDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramEndTime.setFirstIcon(R.drawable.tm_dash_clock)

        textFieldProgramStartDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramStartDate, 0)
        }

        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramStartTime, 0)
        }

        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramEndDate, 1)
        }

        textFieldProgramEndTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramEndTime, 1)
        }

    }

    private fun setProgramDateAuto() {
        when (selectedChipPositionDate) {
            ProgramDateType.AUTO -> {

                textFieldProgramStartDate.editText.setText(
                    setDate(manualStartTimeProgram)
                )
                textFieldProgramStartTime.editText.setText(
                    setTimeEnd(manualStartTimeProgram)
                )
                textFieldProgramEndDate.editText.setText(
                    setDate(manualEndTimeProgram)
                )
                textFieldProgramEndTime.editText.setText(
                    setTimeEnd(manualEndTimeProgram)
                )

                textFieldProgramStartDate.isEnabled = false
                textFieldProgramStartDate.iconContainer.isEnabled = false
                textFieldProgramStartTime.isEnabled = false
                textFieldProgramStartTime.iconContainer.isEnabled = false
                textFieldProgramEndDate.isEnabled = false
                textFieldProgramEndDate.iconContainer.isEnabled = false
                textFieldProgramEndTime.isEnabled = false
                textFieldProgramEndTime.iconContainer.isEnabled = false

                couponStartDate = manualStartTimeProgram.substringBefore(" ")
                couponStartTime = setTimeEnd(manualStartTimeProgram)
                couponEndDate = manualEndTimeProgram.substringBefore(" ")
                couponEndTime = setTimeEnd(manualEndTimeProgram)
            }
            ProgramDateType.MANUAL -> {
                textFieldProgramStartDate.isEnabled = true
                textFieldProgramStartDate.iconContainer.isEnabled = true
                textFieldProgramStartTime.isEnabled = true
                textFieldProgramStartTime.iconContainer.isEnabled = true
                textFieldProgramEndDate.isEnabled = true
                textFieldProgramEndDate.iconContainer.isEnabled = true
                textFieldProgramEndTime.isEnabled = true
                textFieldProgramEndTime.iconContainer.isEnabled = true
                textFieldProgramStartDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramStartTime.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndTime.editText.inputType = InputType.TYPE_NULL
            }
        }
    }

    private fun clickDatePicker(textField: TextFieldUnify2, type: Int) {
        var date = ""
        var month = ""
        var year = ""
        var day = 0
        context?.let {
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.YEAR, TmProgramFragment.MAX_YEAR)
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            val calMin = Calendar.getInstance()
            calMin.add(Calendar.YEAR, TmProgramFragment.MIN_YEAR)
            val yearMin = calMin.get(Calendar.YEAR)
            val monthMin = calMin.get(Calendar.MONTH)
            val dayMin = calMin.get(Calendar.DAY_OF_MONTH)

            val minDate = GregorianCalendar(yearMin, monthMin, dayMin)
            val datepickerObject = DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                setTitle(DATE_TITLE)
                setInfo(DATE_DESC)
                setInfoVisible(true)
                datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        selectedTime = selectedCalendar?.time.toString()
                        date = selectedCalendar?.get(Calendar.DATE).toString()
                        day = selectedCalendar?.get(Calendar.DAY_OF_WEEK)?:0
                        month = selectedCalendar?.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            LocaleUtils.getIDLocale()
                        ).toString()
                        year = selectedCalendar?.get(Calendar.YEAR).toString()
                        when (type) {
                            0 -> {
                                couponStartDate = selectedCalendar?.time?.let { dateTime ->
                                    convertDateTime(dateTime).substringBefore("")
                                }.toString()
                            }
                            1 -> {
                                couponEndDate = selectedCalendar?.time?.let { dateTime ->
                                    convertDateTime(dateTime).substringBefore("")
                                }.toString()
                            }
                        }
                        dismiss()
                    }
                }
            }
            childFragmentManager.let { fragmentManager ->
                datepickerObject.show(fragmentManager, "")
            }
            datepickerObject.setOnDismissListener {
                val dayInId =  when(day){
                    1 -> "Min"
                    2 -> "Sen"
                    3 -> "Sel"
                    4 -> "Rab"
                    5 -> "Kam"
                    6 -> "Jum"
                    7 -> "Sab"
                    else -> ""
                }
                selectedTime = selectedCalendar?.time.toString()
                textField.textInputLayout.editText?.setText(("$dayInId,$date $month $year"))
            }
        }
    }

    private fun clickTimePicker(textField: TextFieldUnify2, type: Int) {
        var selectedHour = ""
        var selectedMinute = ""
        context?.let { ctx ->
            val minTime =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.MINUTE, 30)
                }
            val defaultTime =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx))
            val maxTime =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 30)
                }

            val timerPickerUnify = DateTimePickerUnify(
                context = ctx,
                minDate = minTime,
                defaultDate = defaultTime,
                maxDate = maxTime,
                type = DateTimePickerUnify.TYPE_TIMEPICKER
            ).apply {
                setTitle(TIME_TITLE)
                setInfo(TIME_DESC)
                setInfoVisible(true)
                datePickerButton.setOnClickListener {
                    startTime = getDate()
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue
                    when (type) {
                        0 -> {
                            couponStartTime = "$selectedHour:$selectedMinute"
                        }
                        1 -> {
                            couponEndTime = "$selectedHour:$selectedMinute"
                        }
                    }
                    dismiss()
                }
            }

            childFragmentManager.let {
                timerPickerUnify.show(it, "")
            }
            timerPickerUnify.setOnDismissListener {
                textField.textInputLayout.editText?.setText(("$selectedHour : $selectedMinute WIB"))
            }
        }
    }

    companion object {
        const val DATA = 1
        const val SHIMMER = 0
        const val ERROR = 2

        fun newInstance(bundle: Bundle): TmMultipleCuponCreateFragment {
            return TmMultipleCuponCreateFragment().apply {
                arguments = bundle
            }
        }
    }
}
