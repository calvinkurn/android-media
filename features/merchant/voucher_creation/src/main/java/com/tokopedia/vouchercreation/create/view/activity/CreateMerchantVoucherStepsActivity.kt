package com.tokopedia.vouchercreation.create.view.activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoring
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringInterface
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringType
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.dismissBottomSheetWithTags
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

class CreateMerchantVoucherStepsActivity : BaseActivity(){

    companion object {
        private const val PROGRESS_ATTR_TAG = "progress"
        private const val PROGRESS_DURATION = 200L

        private const val GET_INITIAL_VOUCHER_ERROR = "Get initial voucher error"

        const val DUPLICATE_VOUCHER = "duplicate_voucher"
        const val IS_DUPLICATE = "is_duplicate"

        const val EDIT_VOUCHER = "edit_voucher"
        const val IS_EDIT = "is_edit"
        const val EDIT_STEP = "edit_step"

        const val ERROR_INITIATE = "error_initiate"
        const val REQUEST_CODE = 7137

        const val FROM_VOUCHER_LIST = "from_voucher_list"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CreateMerchantVoucherStepsViewModel::class.java)
    }

    private val performanceMonitoring: MvcPerformanceMonitoringInterface by lazy {
        MvcPerformanceMonitoring(MvcPerformanceMonitoringType.Create)
    }

    private val viewPagerAdapter by lazy {
        CreateMerchantVoucherStepsAdapter(this, fragmentStepsHashMap.values.toList())
    }

    private val fragmentStepsHashMap by lazy {
        LinkedHashMap<VoucherCreationStepInfo, Fragment>().apply {
            put(VoucherCreationStepInfo.STEP_ONE,
                    MerchantVoucherTargetFragment.createInstance(
                            ::setVoucherName,
                            ::getPromoCodePrefix,
                            ::getVoucherReviewUiModel,
                            isCreateNew,
                            isEditVoucher))
                put(VoucherCreationStepInfo.STEP_TWO,
                    PromotionBudgetAndTypeFragment.createInstance(
                            ::setVoucherBenefit,
                            ::setVoucherRecommendationStatus,
                            ::getBannerVoucherUiModel,
                            ::getBannerBaseUiModel,
                            ::onSetShopInfo,
                            ::getVoucherReviewUiModel,
                            isCreateNew))
            put(VoucherCreationStepInfo.STEP_THREE,
                    SetVoucherPeriodFragment.createInstance(
                            ::setVoucherPeriod,
                            ::getBannerVoucherUiModel,
                            ::getBannerBaseUiModel,
                            ::onSuccessGetBannerBitmap,
                            ::getVoucherReviewUiModel,
                            isCreateNew,
                            isEditVoucher))
            put(VoucherCreationStepInfo.STEP_FOUR,
                    ReviewVoucherFragment.createInstance(
                            ::getVoucherReviewUiModel,
                            ::getToken,
                            ::getRecommendationStatus,
                            ::getPostBaseUiModel,
                            ::onReturnToStep,
                            ::getBannerBitmap,
                            ::getVoucherId,
                            ::getPromoCodePrefix,
                            ::getBannerBaseUiModel,
                            ::getBannerVoucherUiModel,
                            this@CreateMerchantVoucherStepsActivity.isEditVoucher))
        }
    }

    private val viewPagerOnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                runOnUiThread {
                    viewModel.setStepPosition(position)
                }
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
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.REVIEW,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.REVIEW_PROCESS_CLICK_CANCEL_BUTTON,
                        userId = userSession.userId
                )
                this.dismiss()
            }
            setBackButtonClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.REVIEW,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.REVIEW_PROCESS_CLICK_BACK_BUTTON,
                        userId = userSession.userId
                )
            }
        }
    }

    private val cancelDialog by lazy {
        CreateVoucherCancelDialog(this,
                onPrimaryClick = {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = VoucherCreationStep.REVIEW,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_VOUCHER_CREATION_BACK,
                            userId = userSession.userId
                    )
                },
                onSecondaryClick = {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = VoucherCreationStep.REVIEW,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_VOUCHER_CREATION_CANCELLED,
                            userId = userSession.userId
                    )
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                })
    }

    private var currentStepPosition = 0

    private var currentProgress = 0

    private var promoCodePrefix = ""

    private var isDuplicateVoucher = false

    private var isEditVoucher = false

    private var voucherId: Int? = null

    private var isCreateNew = false

    private var bannerBaseUiModel =
            BannerBaseUiModel(
                    VoucherUrl.BANNER_BASE_URL,
                    VoucherUrl.FREE_DELIVERY_URL,
                    VoucherUrl.CASHBACK_URL,
                    VoucherUrl.CASHBACK_UNTIL_URL
            )

    private var postBaseUiModel =
            PostBaseUiModel(
                    VoucherUrl.POST_IMAGE_URL,
                    VoucherUrl.FREE_DELIVERY_URL,
                    VoucherUrl.CASHBACK_URL,
                    VoucherUrl.CASHBACK_UNTIL_URL
            )

    private var token = ""

    private var recommendationStatus = 0

    private var bannerVoucherUiModel =
            BannerVoucherUiModel(
                    VoucherImageType.FreeDelivery(0),
                    "",
                    "",
                    "")

    private var voucherReviewUiModel = VoucherReviewUiModel()

    private var bannerBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceMonitoring.initMvcPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        // Finish activity when fragments on view pager are being restored because of low memory or don`t keep activity.
        // When restored, the lambdas that being passed to fragments in view pager are not working as intended (eg. next action is not running).
        // While you can save lambdas into savedInstanceState, it is not recommended to do that.
        // So, the best option right now is to finish activity when state is being restored to avoid anomalies in those fragments.
//        if (savedInstanceState != null) {
//            finish()
//        }
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        initInjector()
        setupView()
        observeLiveData()
    }

    override fun onBackPressed() {
        if (isEditVoucher) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            when(currentStepPosition) {
                VoucherCreationStep.TARGET -> {
                    cancelDialog.show()
                }
                VoucherCreationStep.REVIEW -> {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = currentStepPosition,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.BACK_BUTTON,
                            userId = userSession.userId)
                    backPromptBottomSheet.show(supportFragmentManager, ChangeDetailPromptBottomSheetFragment.TAG)
                }
                else -> {
                    onBackStep()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.dismissBottomSheetWithTags(
                TipsAndTrickBottomSheetFragment.TAG,
                ChangeDetailPromptBottomSheetFragment.TAG
        )
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
                setStatusBarColor(Color.TRANSPARENT)
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
                    KeyboardHandler.hideSoftKeyboard(this@CreateMerchantVoucherStepsActivity)
                    bottomSheet.show(supportFragmentManager, TipsAndTrickBottomSheetFragment.TAG)
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = currentStepPosition,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.LAMP_ICON,
                            label = "",
                            userId = userSession.userId,
                            isDuplicate = isDuplicateVoucher)
                }
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
            setNavigationOnClickListener(onNavigationClickListener)
        }
    }

    private fun initiateVoucherPage() {
        performanceMonitoring.startNetworkMvcPerformanceMonitoring()
        when {
            checkIfDuplicate() -> return
            checkIfEdit() -> {
                isEditVoucher = true
                return
            }
            else -> {
                isCreateNew = true
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
            offscreenPageLimit = fragmentStepsHashMap.size - 1
        }
    }

    private fun observeLiveData() {
        viewModel.stepPositionLiveData.observe(this, Observer { position ->
            val stepInfo = fragmentStepsHashMap.toList().getOrNull(position)?.first
            stepInfo?.run {
                runOnUiThread {
                    changeStepsInformation(this)
                    createMerchantVoucherViewPager?.currentItem = stepPosition
                }
            }
        })
        viewModel.initiateVoucherLiveData.observe(this, Observer { result ->
            performanceMonitoring.startRenderMvcPerformanceMonitoring()
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

                    voucherReviewUiModel.promoCode = voucherReviewUiModel.promoCode.replace(promoCodePrefix, "")
                    if (isDuplicateVoucher) {
                        viewModel.setStepPosition(VoucherCreationStep.REVIEW)
                    } else if (isEditVoucher) {
                        intent?.getIntExtra(EDIT_STEP, VoucherCreationStep.REVIEW)?.let { step ->
                            viewModel.setStepPosition(step)
                        }
                    }
                    createMerchantVoucherViewPager?.setOnLayoutListenerReady()
                }
                is Fail -> {
                    // send crash report to firebase crashlytics
                    MvcErrorHandler.logToCrashlytics(result.throwable, GET_INITIAL_VOUCHER_ERROR)
                    // log error type to scalyr
                    val errorMessage = ErrorHandler.getErrorMessage(applicationContext, result.throwable)
                    ServerLogger.log(Priority.P2, "MVC_GET_INITIAL_VOUCHER_ERROR", mapOf("type" to errorMessage))
                    // set activity result
                    val returnIntent = Intent().apply {
                        putExtra(ERROR_INITIATE, errorMessage)
                    }
                    setResult(Activity.RESULT_CANCELED, returnIntent)
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
            setDuplicateEditVoucherData(voucherUiModel, false)
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
                        viewModel.initiateEditDuplicateVoucher(true)
                        setDuplicateEditVoucherData(voucherUiModel, true)
                        this@CreateMerchantVoucherStepsActivity.isEditVoucher = true
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

    private fun setDuplicateEditVoucherData(voucherUiModel: VoucherUiModel, isEdit: Boolean) {
        with(voucherUiModel) {
            val targetType =
                    if (isPublic) {
                        VoucherTargetType.PUBLIC
                    } else {
                        VoucherTargetType.PRIVATE
                    }
            val promoCode =
                    if (isPublic) {
                        ""
                    } else {
                        code
                    }
            setVoucherName(targetType, name, promoCode)

            if (isEdit && startTime.isNotEmpty() && finishTime.isNotEmpty()) {
                val startDate = DateTimeUtils.convertFullDateToDateParam(startTime, DateTimeUtils.DASH_DATE_FORMAT)
                val endDate = DateTimeUtils.convertFullDateToDateParam(finishTime, DateTimeUtils.DASH_DATE_FORMAT)
                val startHour = DateTimeUtils.convertFullDateToDateParam(startTime, DateTimeUtils.HOUR_FORMAT)
                val endHour = DateTimeUtils.convertFullDateToDateParam(finishTime, DateTimeUtils.HOUR_FORMAT)
                setVoucherPeriod(startDate, endDate, startHour, endHour)
            }

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
        if (isEditVoucher) {
            viewModel.setStepPosition(VoucherCreationStep.REVIEW)
        } else {
            viewModel.setNextStep()
        }
    }

    private fun setVoucherName(@VoucherTargetType targetType: Int, voucherName: String, promoCode: String) {
        bannerVoucherUiModel.promoName = voucherName
        voucherReviewUiModel.run {
            this.targetType = targetType
            this.voucherName = voucherName
            this.promoCode = promoCode
        }
        onNextStep()
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

    private fun setVoucherRecommendationStatus(@VoucherRecommendationStatus status: Int) {
        this.recommendationStatus = status
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

    private fun onSuccessGetBannerBitmap(bitmap: Bitmap) {
        bannerBitmap = bitmap
    }

    private fun getBannerVoucherUiModel() = bannerVoucherUiModel

    private fun getVoucherReviewUiModel() = voucherReviewUiModel

    private fun getPromoCodePrefix(): String = promoCodePrefix

    private fun getBannerBaseUiModel() = bannerBaseUiModel

    private fun getToken() = token

    private fun getRecommendationStatus() = recommendationStatus

    private fun getPostBaseUiModel() = postBaseUiModel

    private fun getBannerBitmap() = bannerBitmap

    private fun getVoucherId() = voucherId

    private fun onCancelVoucher() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.REVIEW_PROCESS_CLICK_IN_HERE,
                userId = userSession.userId
        )
        cancelDialog.show()
    }

    private fun View.setOnLayoutListenerReady() {
        viewTreeObserver?.run {
            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    performanceMonitoring.stopPerformanceMonitoring()
                    if (isAlive) {
                        removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }
    }

    private fun onReturnToStep(@VoucherCreationStep step: Int) {
        viewModel.setStepPosition(step)
    }
}