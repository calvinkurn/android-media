package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplate
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponPreviewData
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_CREATE_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_PREVIEW_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CREATE_SCREEN_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_EXTEND_CTA
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDatePreview
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setTime
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashHomeActivity
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmCouponPreviewAdapter
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_preview.*
import javax.inject.Inject

private const val PROGRESS_90 = 90
private const val RESULT_SUCCESS = 200

class TmDashPreviewFragment : BaseDaggerFragment() {

    private var cardId = 0
    private var tmTracker: TmTracker? = null
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var tmShopCardModel = TokomemberShopCardModel()
    private var tmCouponPreviewData = TmCouponPreviewData()
    private var tmCouponCreateUnifyRequest = TmMerchantCouponUnifyRequest()
    private var programActionType = ProgramActionType.CREATE
    private var isShowBottomSheet = true
    private val tmCouponPreviewAdapter: TmCouponPreviewAdapter by lazy {
        TmCouponPreviewAdapter(arrayListOf())
    }
    private var loaderDialog: LoaderDialog? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_PROGRAM_TYPE, 0)?.let {
            programActionType = it
        }
        observeViewModel()
        tmTracker = TmTracker()
        tmCouponPreviewData =
            arguments?.getParcelable(BUNDLE_COUPON_PREVIEW_DATA) ?: TmCouponPreviewData()
        tmCouponCreateUnifyRequest =
            arguments?.getParcelable(BUNDLE_COUPON_CREATE_DATA) ?: TmMerchantCouponUnifyRequest()

        cardId = arguments?.getInt(BUNDLE_CARD_ID_IN_TOOLS) ?: 0
        if(cardId == 0) {
            val prefManager = context?.let { TmPrefManager(it) }
            prefManager?.cardId?.let {
                cardId = it
            }
        }
        if (programActionType == ProgramActionType.EXTEND) {
            isShowBottomSheet = false
            viewBgPreview.hide()
            carouselPreview.hide()
            tmDashCreateViewModel.getProgramInfo(
                arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS) ?: 0,
                arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
                "create"
            )
        }
        else if(programActionType == ProgramActionType.CREATE_BUAT || programActionType == ProgramActionType.CREATE_FROM_COUPON){
            isShowBottomSheet = false
            viewBgPreview.show()
            carouselPreview.show()
            tmDashCreateViewModel.getCardInfo(cardId)
        }
        else {
            isShowBottomSheet = true
            viewBgPreview.show()
            carouselPreview.show()
            tmDashCreateViewModel.getCardInfo(cardId)
        }
        renderHeader()
        renderButton()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun observeViewModel() {

        tmDashCreateViewModel.tmCardResultLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    tmDashCreateViewModel.getProgramInfo(
                        arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS) ?: 0,
                        arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
                        "create"
                    )
                    renderCardCarousel(it.data?.cardTemplate, it.data?.shopAvatar?:"")
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleErrorUiOnErrorData()
                }
            }
        })

        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        containerViewFlipper.displayedChild = DATA
                        renderProgramPreview(it.data.membershipGetProgramForm)
                        renderCouponList(tmCouponPreviewData)
                    }
                    else{
                        handleErrorUiOnErrorData()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleErrorUiOnErrorData()
                }
            }
        })

        tmDashCreateViewModel.tmCouponCreateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    closeLoadingDialog()
                    if (it.data.merchantPromotionCreateMultipleMV?.status == RESULT_SUCCESS) {
                        activity?.finish()
                        TokomemberDashHomeActivity.openActivity(arguments?.getInt(BUNDLE_SHOP_ID)?:0,cardId, context, isShowBottomSheet, programActionType)
                    } else {
                        view?.let { v ->
                            Toaster.build(
                                v,
                                it.data.merchantPromotionCreateMultipleMV?.message ?: "",
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
                is Fail -> {
                    closeLoadingDialog()
                    view?.let { v ->
                        Toaster.build(
                            v,
                            RETRY,
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun handleErrorUiOnErrorData(){
        containerViewFlipper.displayedChild = ERROR
        globalErrorPreview.setActionClickListener {
            tmDashCreateViewModel.getCardInfo(arguments?.getInt(BUNDLE_CARD_ID)?:0)
        }
    }

    private fun renderHeader() {
        var subtitle = ""
        var title = "Buat Program"
        when(programActionType){
            ProgramActionType.CREATE ->{
                subtitle = "Langkah 4 dari 4"
            }
            ProgramActionType.CREATE_BUAT ->{
                subtitle = "Langkah 3 dari 3"
            }
            ProgramActionType.CREATE_FROM_COUPON ->{
                subtitle = "Langkah 3 dari 3"
            }
            ProgramActionType.EXTEND ->{
                subtitle = "Langkah 3 dari 3"
                title = "Perpanjang TokoMember"
            }
        }
        headerPreview?.apply {
            this.title = title
            this.subtitle = subtitle
            isShowBackButton = true
            setNavigationOnClickListener {
                if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_BUAT){
                    tmTracker?.clickSummaryBackFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                else if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_EXTEND){
                    tmTracker?.clickProgramExtensionSummaryBack(arguments?.getInt(BUNDLE_SHOP_ID).toString(), arguments?.getInt(BUNDLE_PROGRAM_ID).toString())
                }
                else {
                    tmTracker?.clickSummaryBack(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                activity?.onBackPressed()
            }
        }
        progressPreview?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(PROGRESS_90, false)
        }
    }

    private fun renderButton(){
        if(programActionType == ProgramActionType.EXTEND){
            btnPreview.text = PROGRAM_EXTEND_CTA
        }
        btnPreview.setOnClickListener {
            openLoadingDialog()
            if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_BUAT){
                tmTracker?.clickSummaryButtonFromProgramList(
                    arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                    arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                )
            }
            else if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_EXTEND){
                tmTracker?.clickProgramExtensionSummaryButton(
                    arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                    arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                )
            }
            else {
                tmTracker?.clickSummaryButton(
                    arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                    arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                )
            }

            createCoupon(tmCouponCreateUnifyRequest)
        }
    }

    private fun createCoupon(tmCouponCreateUnifyRequest: TmMerchantCouponUnifyRequest) {
        if (com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.createCoupon(tmCouponCreateUnifyRequest)
        }
        else {
            noInternetUi {
                createCoupon(tmCouponCreateUnifyRequest)
            }
        }
    }

    private fun noInternetUi(action: () -> Unit) {
        //show no internet bottomsheet

        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            ERROR_CREATING_TITLE_NO_INTERNET,
            ERROR_CREATING_DESC_NO_INTERNET,
            "",
            RETRY,
            errorCount = 0,
            showSecondaryCta = true
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                action()
            }})
        if(programActionType == ProgramActionType.CREATE){
            bottomsheet.setSecondaryCta {
                arguments?.getInt(BUNDLE_SHOP_ID)?.let {
                    TokomemberDashIntroActivity.openActivity(
                        it,
                        arguments?.getString(BUNDLE_SHOP_AVATAR).toString(),
                        arguments?.getString(BUNDLE_SHOP_NAME).toString(),
                        context = context
                    )
                }
                activity?.finish()
            }
        }
        bottomsheet.show(childFragmentManager,"")
    }

    private fun openLoadingDialog() {
        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(""))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(){
        rvPreview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        tmCouponPreviewAdapter.list.clear()
        tmCouponPreviewAdapter.list = tmCouponPreviewData.voucherList
        rvPreview.adapter = tmCouponPreviewAdapter
        tmCouponPreviewAdapter.notifyDataSetChanged()
    }

    private fun renderCardCarousel(data: CardTemplate?, shopAvatar: String) {
        context?.let {
            shopViewPremium = TokomemberShopView(it)
            shopViewVip = TokomemberShopView(it)
            tmShopCardModel = TokomemberShopCardModel(
                shopName = arguments?.getString(BUNDLE_SHOP_NAME)?:"",
                backgroundColor = data?.backgroundColor ?: "",
                backgroundImgUrl = data?.backgroundImgUrl ?: "",
                shopType = 0,
                shopIconUrl = shopAvatar
            )
            shopViewPremium?.apply {
                setShopCardData(tmShopCardModel)
            }
            shopViewVip?.apply {
               val vipCardModel =  tmShopCardModel.apply {
                    shopType = 1
                }
                setShopCardData(vipCardModel)
            }

            carouselPreview?.apply {
                indicatorPosition = CarouselUnify.INDICATOR_BC
                slideToShow = 1f
                slideToScroll = 1
                freeMode = false
                centerMode = true
                autoplay = false
                addItem(shopViewPremium ?: TokomemberShopView(context))
                addItem(shopViewVip ?: TokomemberShopView(context))
                onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                    override fun onActiveIndexChanged(prev: Int, current: Int) {

                    }
                }
                onDragEventListener = object : CarouselUnify.OnDragEventListener {
                    override fun onDrag(progress: Float) {

                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderCouponList(tmCouponPreviewData: TmCouponPreviewData) {
        initRecyclerView()
        tvCouponMulaiValue.text = tmCouponPreviewData.startDate.substringAfter(",") +", "+ tmCouponPreviewData.startTime + " WIB"
        tvCouponBerakhirValue.text = tmCouponPreviewData.endDate.substringAfter(",") +", "+ tmCouponPreviewData.endTime + " WIB"
        tvMaxTransactionValue.text = "Rp${CurrencyFormatHelper.convertToRupiah(tmCouponPreviewData.maxValue)}"
    }

    @SuppressLint("SetTextI18n")
    private fun renderProgramPreview(membershipGetProgramForm: MembershipGetProgramForm?) {
        tvProgramMulaiValue.text = setDatePreview(membershipGetProgramForm?.programForm?.timeWindow?.startTime?:"") + ", " + setTime(membershipGetProgramForm?.programForm?.timeWindow?.startTime?:"")
        tvProgramBerakhirValue.text = setDatePreview(membershipGetProgramForm?.programForm?.timeWindow?.endTime?:"") + ", " +  setTime(membershipGetProgramForm?.programForm?.timeWindow?.endTime?:"")
        tvProgramMinTransaksiPremiumValue.text =
            "Rp${CurrencyFormatHelper.convertToRupiah(membershipGetProgramForm?.programForm?.tierLevels?.getOrNull(0)?.threshold.toString())}"
        tvProgramMinTransaksiVipValue.text =
            "Rp${CurrencyFormatHelper.convertToRupiah(membershipGetProgramForm?.programForm?.tierLevels?.getOrNull(1)?.threshold.toString())}"
    }

    companion object {

        const val DATA = 1
        const val SHIMMER = 0
        const val ERROR = 2
        fun newInstance(bundle: Bundle) = TmDashPreviewFragment().apply {
            arguments = bundle
        }
    }
}
