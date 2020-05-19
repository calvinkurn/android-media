package com.tokopedia.vouchercreation.create.view.activity

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.adapter.CreateMerchantVoucherStepsAdapter
import com.tokopedia.vouchercreation.create.view.dialog.CreateVoucherCancelDialog
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStepInfo
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.ChangeDetailPromptBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TipsAndTrickBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.ReviewVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.SetVoucherPeriodFragment
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.CreateMerchantVoucherStepsViewModel
import kotlinx.android.synthetic.main.activity_create_merchant_voucher_steps.*
import javax.inject.Inject

class CreateMerchantVoucherStepsActivity : FragmentActivity() {

    companion object {
        private const val PROGRESS_ATTR_TAG = "progress"
        private const val PROGRESS_DURATION = 200L

        //These are default url for banners that we will use in case the server returned error
        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
        private const val FREE_DELIVERY_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_gratis_ongkir.png"
        private const val CASHBACK_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback.png"
        private const val CASHBACK_UNTIL_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback_hingga.png"
        private const val POST_IMAGE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/ig_post.jpg"

        private const val FIRST_STEP_INDEX = 0
        private const val LAST_STEP_INDEX = 3

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CreateMerchantVoucherStepsViewModel::class.java)
    }

    private val viewPagerAdapter by lazy {
        CreateMerchantVoucherStepsAdapter(this, fragmentStepsHashMap.values.toList())
    }

    private val fragmentStepsHashMap by lazy {
        LinkedHashMap<VoucherCreationStepInfo, Fragment>().apply {
            put(VoucherCreationStepInfo.STEP_ONE, MerchantVoucherTargetFragment.createInstance(::onSetVoucherName, ::getPromoCodePrefix))
            put(VoucherCreationStepInfo.STEP_TWO, PromotionBudgetAndTypeFragment.createInstance(::onNextStep, ::getBannerVoucherUiModel, viewModel::setVoucherPreviewBitmap, ::getBannerBaseUiModel))
            put(VoucherCreationStepInfo.STEP_THREE, SetVoucherPeriodFragment.createInstance(::onNextStep, ::getBannerVoucherUiModel, ::getBannerBaseUiModel))
            put(VoucherCreationStepInfo.STEP_FOUR, ReviewVoucherFragment.createInstance(::getVoucherReviewUiModel, ::getToken, ::getIgPostVoucherUrl))
        }
    }

    private val viewPagerOnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setStepPosition(position)
            }
        }
    }

    private val progressBarInterpolator by lazy { AccelerateDecelerateInterpolator() }

    private val onNavigationClickListener by lazy {
        View.OnClickListener {
            onBackPressed()
        }
    }

    private val bottomSheet by lazy {
        TipsAndTrickBottomSheetFragment.createInstance(this).apply {
            this.setTitle(bottomSheetViewTitle.toBlankOrString())
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val backPromptBottomSheet by lazy {
        ChangeDetailPromptBottomSheetFragment.createInstance(this, ::onCancelVoucher).apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val cancelDialog by lazy {
        CreateVoucherCancelDialog(this) {
            finish()
        }
    }

    private var currentStepPosition = 0

    private var currentProgress = 0

    private var voucherBitmap: Bitmap? = null

    private var promoCodePrefix = ""

    private var bannerBaseUiModel =
            BannerBaseUiModel(
                    BANNER_BASE_URL,
                    FREE_DELIVERY_URL,
                    CASHBACK_URL,
                    CASHBACK_UNTIL_URL
            )

    private var token = ""

    private var igPostVoucherUrl = POST_IMAGE_URL

    private var bannerVoucherUiModel =
            BannerVoucherUiModel(
                    VoucherImageType.FreeDelivery(0),
                    "",
                    "",
                    "")

    private var voucherReviewUiModel = VoucherReviewUiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        initInjector()
        setupView()
        observeLiveData()
    }

    override fun onBackPressed() {
        when(currentStepPosition) {
            FIRST_STEP_INDEX -> {
                cancelDialog.show()
            }
            LAST_STEP_INDEX -> {
                backPromptBottomSheet.show(supportFragmentManager, ChangeDetailPromptBottomSheetFragment.TAG)
            }
            else -> {
                onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.flush()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(ContextCompat.getColor(this, R.color.transparent))
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    private fun setupView() {
        window.decorView.setBackgroundColor(Color.WHITE)
        setupStatusBar()
        setupToolbar()
        initiateVoucherPage()
    }

    private fun setupToolbar() {
        createMerchantVoucherHeader?.run {
            setBackgroundColor(Color.TRANSPARENT)
            try {
                addRightIcon(R.drawable.ic_tips).setOnClickListener {
                    bottomSheet.show(supportFragmentManager, TipsAndTrickBottomSheetFragment::javaClass.name)
                }
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
            setNavigationOnClickListener(onNavigationClickListener)
        }
    }

    private fun initiateVoucherPage() {
        viewModel.initiateVoucherPage()
    }

    private fun setupViewPager() {
        viewModel.setMaxPosition(fragmentStepsHashMap.size - 1)
        createMerchantVoucherViewPager.run {
            isUserInputEnabled = false
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(viewPagerOnPageChangeCallback)
        }
    }

    private fun observeLiveData() {
        viewModel.stepPositionLiveData.observe(this, Observer { position ->
            val stepInfo = fragmentStepsHashMap.toList().getOrNull(position)?.first
            stepInfo?.run {
                changeStepsInformation(this)
                createMerchantVoucherViewPager?.currentItem = stepPosition
            }
        })
        viewModel.voucherPreviewBitmapLiveData.observe(this, Observer { bitmap ->
            voucherBitmap = bitmap
        })
        viewModel.initiateVoucherLiveData.observe(this, Observer { result ->
            createMerchantVoucherLoader?.gone()
            when(result) {
                is Success -> {
                    result.data.run {
                        if (bannerBaseUrl.isNotEmpty()) {
                            bannerBaseUiModel =
                                    BannerBaseUiModel(
                                            bannerBaseUrl,
                                            bannerFreeShippingLabelUrl,
                                            bannerCashbackLabelUrl,
                                            bannerCashbackUntilLabelUrl
                                    )
                        }
                        this@CreateMerchantVoucherStepsActivity.token = token
                        promoCodePrefix = voucherCodePrefix
                        if (bannerIgPostUrl.isNotBlank()) {
                            igPostVoucherUrl = bannerIgPostUrl
                        }
                    }

                    setupViewPager()
                }
                is Fail -> {
                    finish()
                }
            }
        })
    }

    private fun changeStepsInformation(stepInfo: VoucherCreationStepInfo) {
        stepInfo.run {
            currentStepPosition = stepPosition
            createMerchantVoucherHeader?.headerSubTitle = resources?.getString(stepDescriptionRes).toBlankOrString()
            createMerchantVoucherProgressBar?.run {
                ObjectAnimator.ofInt(createMerchantVoucherProgressBar, PROGRESS_ATTR_TAG, currentProgress, progressPercentage).run {
                    duration = PROGRESS_DURATION
                    interpolator = progressBarInterpolator
                    start()
                }
                currentProgress = progressPercentage
            }
        }
    }

    private fun onBackStep() {
        viewModel.setBackStep()
    }

    private fun onNextStep() {
        viewModel.setNextStep()
    }

    private fun onNextStep(imageType: VoucherImageType, minPurchase: Int, quota: Int) {
        bannerVoucherUiModel.imageType = imageType
        voucherReviewUiModel.run {
            voucherType = imageType
            this.minPurchase = minPurchase
            voucherQuota = quota
        }
        onNextStep()
    }

    private fun onNextStep(dateStart: String,
                           dateEnd: String,
                           hourStart: String,
                           hourEnd: String) {
        voucherReviewUiModel.run {
            startDate = dateStart
            endDate = dateEnd
            startHour = hourStart
            endHour = hourEnd
        }
        onNextStep()
    }

    private fun onSetVoucherName(@VoucherTargetType targetType: Int, voucherName: String, promoCode: String) {
        bannerVoucherUiModel.promoName = voucherName
        voucherReviewUiModel.run {
            this.targetType = targetType
            this.voucherName = voucherName
            this.promoCode = promoCode
        }
        viewModel.setNextStep()
    }

    private fun getBannerVoucherUiModel() = bannerVoucherUiModel

    private fun getVoucherReviewUiModel() = voucherReviewUiModel

    private fun getPromoCodePrefix(): String = promoCodePrefix

    private fun getBannerBaseUiModel() = bannerBaseUiModel

    private fun getToken() = token

    private fun getIgPostVoucherUrl() = igPostVoucherUrl

    private fun onCancelVoucher() {
        cancelDialog.show()
    }
}