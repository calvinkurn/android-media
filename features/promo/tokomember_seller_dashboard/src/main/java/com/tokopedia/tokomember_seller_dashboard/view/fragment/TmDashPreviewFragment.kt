package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponPreviewData
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_CREATE_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_PREVIEW_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CREATE_SCREEN_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDate
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmCouponPreviewAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_preview.*
import javax.inject.Inject

class TmDashPreviewFragment : BaseDaggerFragment() {

    private var tmTracker: TmTracker? = null
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var tmShopCardModel = TokomemberShopCardModel()
    private var tmCouponPreviewData = TmCouponPreviewData()
    private var tmCouponCreateUnifyRequest = TmMerchantCouponUnifyRequest()
    private val tmCouponPreviewAdapter: TmCouponPreviewAdapter by lazy {
        TmCouponPreviewAdapter(arrayListOf())
    }

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
        tmTracker = TmTracker()
        tmCouponPreviewData =
            arguments?.getParcelable(BUNDLE_COUPON_PREVIEW_DATA) ?: TmCouponPreviewData()
        tmCouponCreateUnifyRequest =
            arguments?.getParcelable(BUNDLE_COUPON_CREATE_DATA) ?: TmMerchantCouponUnifyRequest()
        tmDashCreateViewModel.getCardInfo(arguments?.getInt(BUNDLE_CARD_ID)?:0)
        containerViewFlipper.displayedChild = SHIMMER
        renderHeader()
        observeViewModel()
        renderButton()
        renderCouponList(tmCouponPreviewData)
        renderCardCarousel(arguments?.getParcelable(BUNDLE_CARD_DATA))
        renderProgramPreview(arguments?.getParcelable(BUNDLE_PROGRAM_DATA)?:ProgramUpdateDataInput())
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun observeViewModel() {

        tmDashCreateViewModel.tmCouponCreateLiveData.observe(viewLifecycleOwner, {

            when (it) {
                is Success -> {
                    //Open Dashboard
                    //startActivityForResult(Intent(Tok))
                }
                is Fail -> {
                    //handleError
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
        headerPreview?.apply {
            title = "Buat Program"
            subtitle = "Langkah 4 dari 4"
            isShowBackButton = true
            setNavigationOnClickListener {
                if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_BUAT){
                    tmTracker?.clickSummaryBackFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
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
            setValue(90, false)
        }
    }

    private fun renderButton(){
        btnPreview.setOnClickListener {
            if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.PREVIEW_BUAT){
                tmTracker?.clickSummaryButtonFromProgramList(
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
            tmDashCreateViewModel.createCoupon(tmCouponCreateUnifyRequest)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(){
        rvPreview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        tmCouponPreviewAdapter.list = tmCouponPreviewData.voucherList
        rvPreview.adapter = tmCouponPreviewAdapter
        tmCouponPreviewAdapter.notifyDataSetChanged()
    }

    private fun renderCardCarousel(data: CardDataTemplate?) {
        context?.let {
            shopViewPremium = TokomemberShopView(it)
            shopViewVip = TokomemberShopView(it)
            tmShopCardModel = TokomemberShopCardModel(
                shopName = data?.card?.name ?: "",
                numberOfLevel = data?.card?.numberOfLevel ?: 0,
                backgroundColor = data?.cardTemplate?.backgroundColor ?: "",
                backgroundImgUrl = data?.cardTemplate?.backgroundImgUrl ?: "",
                shopType = 0
            )
            shopViewPremium?.apply {
                setShopCardData(tmShopCardModel)
            }
            shopViewVip?.apply {
                setShopCardData(tmShopCardModel)
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
        tvCouponMulaiValue.text = tmCouponPreviewData.startDate +","+ tmCouponPreviewData.startTime
        tvCouponBerakhirValue.text = tmCouponPreviewData.endDate +","+ tmCouponPreviewData.endTime
        tvMaxTransactionValue.text = tmCouponPreviewData.maxValue
    }

    @SuppressLint("SetTextI18n")
    private fun renderProgramPreview(programUpdateDataInput: ProgramUpdateDataInput) {
        tvProgramMulaiValue.text = setDate(programUpdateDataInput.timeWindow?.startTime?:"") + "," + "00:00 WIB"
        tvProgramBerakhirValue.text = setDate(programUpdateDataInput.timeWindow?.endTime?:"") + "," + "00:00 WIB"
        tvProgramMinTransaksiPremiumValue.text =
            "Rp${CurrencyFormatHelper.convertToRupiah(programUpdateDataInput.tierLevels?.getOrNull(0)?.threshold.toString())}"
        tvProgramMinTransaksiVipValue.text =
            "Rp${CurrencyFormatHelper.convertToRupiah(programUpdateDataInput.tierLevels?.getOrNull(1)?.threshold.toString())}"
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