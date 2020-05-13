package com.tokopedia.vouchercreation.create.view.activity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.view.adapter.CreateMerchantVoucherStepsAdapter
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStepInfo
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TipsAndTrickBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.ReviewVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.SetVoucherPeriodFragment
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.CreateMerchantVoucherStepsViewModel
import kotlinx.android.synthetic.main.activity_create_merchant_voucher_steps.*
import javax.inject.Inject

class CreateMerchantVoucherStepsActivity : BaseStepperActivity() {

    companion object {
        private const val PROGRESS_ATTR_TAG = "progress"
        private const val PROGRESS_DURATION = 200L

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"

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
            put(VoucherCreationStepInfo.STEP_ONE, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_TWO, PromotionBudgetAndTypeFragment.createInstance(::onNextStep, viewModel::setVoucherPreviewBitmap))
            put(VoucherCreationStepInfo.STEP_THREE, SetVoucherPeriodFragment.createInstance(::onNextStep, ::getBannerVoucherUiModel))
            put(VoucherCreationStepInfo.STEP_FOUR, ReviewVoucherFragment.createInstance())
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

    private var currentStepPosition = 0

    private var currentProgress = 0

    private var voucherBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        setupView()
        observeLiveData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBackStep()
    }

    override fun getListFragment(): MutableList<Fragment> {
        return fragmentStepsHashMap.values.toMutableList()
    }

    override fun getLayoutRes(): Int = R.layout.activity_create_merchant_voucher_steps

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        setupToolbar()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        window.decorView.setBackgroundColor(Color.WHITE)
        setupStatusBar()
        viewModel.setMaxPosition(fragmentStepsHashMap.size - 1)
    }

    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(ContextCompat.getColor(this, R.color.transparent))
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    override fun updateToolbarTitle() {}

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

    private fun observeLiveData() {
        viewModel.stepPositionLiveData.observe(this, Observer { position ->
            val stepInfo = fragmentStepsHashMap.toList().getOrNull(position)?.first
            stepInfo?.run {
                changeStepsInformation(this)
            }
        })
        viewModel.voucherPreviewBitmapLiveData.observe(this, Observer { bitmap ->
            voucherBitmap = bitmap
        })
    }

    private fun changeStepsInformation(stepInfo: VoucherCreationStepInfo) {
        stepInfo.run {
            currentStepPosition = stepPosition
            createMerchantVoucherHeader?.headerSubTitle = resources?.getString(stepDescriptionRes).toBlankOrString()
        }
    }

    private fun onBackStep() {
        viewModel.setBackStep()
    }

    private fun onNextStep() {
        goToNextPage(null)
        viewModel.setNextStep()
    }

    private fun getBannerVoucherUiModel(): BannerVoucherUiModel? = BannerVoucherUiModel(
            VoucherImageType.FreeDelivery(10000),
            "harusnyadaristep1",
            "Ini Harusnya dari Backend",
            "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg",
            BANNER_BASE_URL
    )

}