package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.tokomember_common_widget.TokomemberShopView
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DATA
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_preview.*
import javax.inject.Inject

class TokomemberDashPreviewFragment : BaseDaggerFragment() {
    var shopViewPremium: TokomemberShopView? = null
    var shopViewVip: TokomemberShopView? = null
    private var tokomemberShopCardModel = TokomemberShopCardModel()

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
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
        renderHeader()
        renderPreviewUI(
            arguments?.getParcelable(BUNDLE_CARD_DATA),
            arguments?.getParcelable(BUNDLE_PROGRAM_DATA)
        )
        observeViewModel()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashCreateViewModel.tokomemberCardPreviewLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                }
                is Fail -> {

                }
            }
        })
    }

    private fun renderHeader() {
        headerPreview?.apply {
            title = "Buat Program"
            subtitle = "Langkah 4 dari 4"
            isShowBackButton = true
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        progressPreview?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(90, false)
        }
    }

    private fun renderPreviewUI(
        tokomemberShopCardModel: TokomemberShopCardModel?,
        programUpdateDataInput: ProgramUpdateDataInput?
    ) {
        if (tokomemberShopCardModel != null) {
            renderCardCarousel(tokomemberShopCardModel)
        }
        if (programUpdateDataInput != null) {
            renderProgramPreview(programUpdateDataInput)
        }
    }

    private fun renderCardCarousel(data: TokomemberShopCardModel) {
        context?.let {
            shopViewPremium = TokomemberShopView(it)
            shopViewVip = TokomemberShopView(it)
            tokomemberShopCardModel = TokomemberShopCardModel(
                shopName = data.shopName,
                numberOfLevel = data.numberOfLevel,
                backgroundColor = data.backgroundColor,
                backgroundImgUrl = data.backgroundImgUrl,
                shopType = 0
            )
            shopViewPremium?.apply {
                setShopCardData(tokomemberShopCardModel)
            }
            shopViewVip?.apply {
                setShopCardData(tokomemberShopCardModel)
            }

            carouselPreview?.apply {
                indicatorPosition = CarouselUnify.INDICATOR_BC
                slideToShow = 1f
                slideToScroll = 1
                freeMode = false
                centerMode = true
                autoplay = false
                addItem(shopViewPremium?: TokomemberShopView(context))
                addItem(shopViewVip?: TokomemberShopView(context))
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
    private fun renderProgramPreview(programUpdateDataInput: ProgramUpdateDataInput) {
        tvProgramMulaiValue.text = programUpdateDataInput.timeWindow?.startTime
        tvProgramBerakhirValue.text = programUpdateDataInput.timeWindow?.endTime
        tvProgramMinTransaksiPremiumValue.text ="Rp${CurrencyFormatHelper.convertToRupiah(programUpdateDataInput.tierLevels?.getOrNull(0)?.threshold.toString())}"
        tvProgramMinTransaksiVipValue.text = "Rp${CurrencyFormatHelper.convertToRupiah(programUpdateDataInput.tierLevels?.getOrNull(1)?.threshold.toString())}"
    }

    companion object {

        fun newInstance(bundle: Bundle) = TokomemberDashPreviewFragment().apply {
            arguments = bundle
        }
    }
}