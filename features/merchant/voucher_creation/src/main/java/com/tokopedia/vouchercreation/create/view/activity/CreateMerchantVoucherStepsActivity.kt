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
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStepInfo
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.getVoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.ChangeDetailPromptBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TipsAndTrickBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.ReviewVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.SetVoucherPeriodFragment
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.CreateMerchantVoucherStepsViewModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.activity_create_merchant_voucher_steps.*
import timber.log.Timber
import javax.inject.Inject

class CreateMerchantVoucherStepsActivity : FragmentActivity() {

    companion object {
        private const val PROGRESS_ATTR_TAG = "progress"
        private const val PROGRESS_DURATION = 200L

        //These are default url for banners that we will use in case the server returned error
        const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
        const val FREE_DELIVERY_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_gratis_ongkir.png"
        const val CASHBACK_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback.png"
        const val CASHBACK_UNTIL_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback_hingga.png"
        const val POST_IMAGE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/ig_post.jpg"

        const val DUPLICATE_VOUCHER = "duplicate_voucher"
        const val IS_DUPLICATE = "is_duplicate"

        const val EDIT_VOUCHER = "edit_voucher"
        const val IS_EDIT = "is_edit"
        const val EDIT_STEP = "edit_step"
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
            put(VoucherCreationStepInfo.STEP_ONE, MerchantVoucherTargetFragment.createInstance(::setVoucherName, ::getPromoCodePrefix))
            put(VoucherCreationStepInfo.STEP_TWO, PromotionBudgetAndTypeFragment.createInstance(::setVoucherBenefit, ::getBannerVoucherUiModel, viewModel::setVoucherPreviewBitmap, ::getBannerBaseUiModel, ::onSetShopInfo))
            put(VoucherCreationStepInfo.STEP_THREE, SetVoucherPeriodFragment.createInstance(::setVoucherPeriod, ::getBannerVoucherUiModel, ::getBannerBaseUiModel, ::onSuccessGetSquareBitmap))
            put(VoucherCreationStepInfo.STEP_FOUR, ReviewVoucherFragment.createInstance(::getVoucherReviewUiModel, ::getToken, ::getPostBaseUiModel, ::onReturnToStep, ::getBannerBitmap, ::getVoucherId))
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

    private var isDuplicateVoucher = false

    private var isEditVoucher = false

    private var voucherId: Int? = null

    private var bannerBaseUiModel =
            BannerBaseUiModel(
                    BANNER_BASE_URL,
                    FREE_DELIVERY_URL,
                    CASHBACK_URL,
                    CASHBACK_UNTIL_URL
            )

    private var postBaseUiModel =
            PostBaseUiModel(
                    POST_IMAGE_URL,
                    FREE_DELIVERY_URL,
                    CASHBACK_URL,
                    CASHBACK_UNTIL_URL
            )

    private var token = ""

    private var bannerVoucherUiModel =
            BannerVoucherUiModel(
                    VoucherImageType.FreeDelivery(0),
                    "",
                    "",
                    "")

    private var voucherReviewUiModel = VoucherReviewUiModel()

    private var bannerBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        initInjector()
        setupView()
        observeLiveData()
    }

    override fun onBackPressed() {
        when(currentStepPosition) {
            VoucherCreationStep.TARGET -> {
                cancelDialog.show()
            }
            VoucherCreationStep.REVIEW -> {
                backPromptBottomSheet.show(supportFragmentManager, ChangeDetailPromptBottomSheetFragment.TAG)
            }
            else -> {
                onBackStep()
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
                Timber.e(ex)
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
        when {
            checkIfDuplicate() -> return
            checkIfEdit() -> return
            else -> {
                viewModel.initiateVoucherPage()
            }
        }
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
                        if (bannerIgPostUrl.isNotBlank()) {
                            postBaseUiModel =
                                    PostBaseUiModel(
                                            bannerIgPostUrl,
                                            bannerFreeShippingLabelUrl,
                                            bannerCashbackLabelUrl,
                                            bannerCashbackUntilLabelUrl
                                    )
                        }
                        this@CreateMerchantVoucherStepsActivity.token = token
                        promoCodePrefix = voucherCodePrefix
                    }
                    setupViewPager()
                    if (isDuplicateVoucher) {
                        createMerchantVoucherViewPager?.currentItem = VoucherCreationStep.REVIEW
                    } else if (isEditVoucher) {
                        intent?.getIntExtra(EDIT_STEP, VoucherCreationStep.REVIEW)?.let { step ->
                            createMerchantVoucherViewPager?.currentItem = step
                        }
                    }
                }
                is Fail -> {
                    finish()
                }
            }
        })
        viewModel.basicShopInfoLiveData.observe(this, Observer { result ->
            if (result is Success) {
                onSetShopInfo(result.data.shopName, result.data.shopAvatar)
            }
        })
    }

    private fun checkIfDuplicate(): Boolean {
        intent?.getParcelableExtra<VoucherUiModel>(DUPLICATE_VOUCHER)?.let { voucherUiModel ->
            viewModel.initiateEditDuplicateVoucher()
            setDuplicateEditVoucherData(voucherUiModel)
            isDuplicateVoucher = true
            createMerchantVoucherHeader?.setTitle(R.string.mvc_duplicate_voucher)
            return true
        }
        return false
    }

    private fun checkIfEdit(): Boolean {
        intent?.run {
            getParcelableExtra<VoucherUiModel>(EDIT_VOUCHER)?.let { voucherUiModel ->
                voucherUiModel.id.let { id ->
                    return if (id != 0) {
                        viewModel.initiateEditDuplicateVoucher()
                        setDuplicateEditVoucherData(voucherUiModel)
                        isEditVoucher = true
                        voucherId = id
                        createMerchantVoucherHeader?.setTitle(R.string.mvc_edit_voucher)
                        true
                    } else {
                        false
                    }
                }
            }
        }
        return false
    }

    private fun setDuplicateEditVoucherData(voucherUiModel: VoucherUiModel) {
        with(voucherUiModel) {
            val targetType =
                    if (isPublic) {
                        VoucherTargetType.PUBLIC
                    } else {
                        VoucherTargetType.PRIVATE
                    }
            setVoucherName(targetType, name, code)

            val imageType = getVoucherImageType(type, discountTypeFormatted, discountAmt, discountAmtMax)
            imageType?.let { type ->
                setVoucherBenefit(type, minimumAmt, quota)
            }
        }
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

    private fun setVoucherName(@VoucherTargetType targetType: Int, voucherName: String, promoCode: String) {
        bannerVoucherUiModel.promoName = voucherName
        voucherReviewUiModel.run {
            this.targetType = targetType
            this.voucherName = voucherName
            this.promoCode = promoCode
        }
        viewModel.setNextStep()
    }

    private fun setVoucherBenefit(imageType: VoucherImageType, minPurchase: Int, quota: Int) {
        bannerVoucherUiModel.imageType = imageType
        voucherReviewUiModel.run {
            voucherType = imageType
            this.minPurchase = minPurchase
            voucherQuota = quota
        }
        onNextStep()
    }

    private fun setVoucherPeriod(dateStart: String,
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

    private fun onSetShopInfo(shopName: String, shopAvatarUrl: String) {
        bannerVoucherUiModel.shopName = shopName
        bannerVoucherUiModel.shopAvatar = shopAvatarUrl
        voucherReviewUiModel.shopName = shopName
        voucherReviewUiModel.shopAvatarUrl = shopAvatarUrl
    }

    private fun onSuccessGetSquareBitmap(bitmap: Bitmap) {
        bannerBitmap = bitmap
    }

    private fun getBannerVoucherUiModel() = bannerVoucherUiModel

    private fun getVoucherReviewUiModel() = voucherReviewUiModel

    private fun getPromoCodePrefix(): String = promoCodePrefix

    private fun getBannerBaseUiModel() = bannerBaseUiModel

    private fun getToken() = token

    private fun getPostBaseUiModel() = postBaseUiModel

    private fun getBannerBitmap() = bannerBitmap

    private fun getVoucherId() = voucherId

    private fun onCancelVoucher() {
        cancelDialog.show()
    }

    private fun onReturnToStep(@VoucherCreationStep step: Int) {
        viewModel.setStepPosition(step)
    }
}