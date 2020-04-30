package com.tokopedia.vouchercreation.create.view.activity

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.CreateMerchantVoucherStepsAdapter
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TipsAndTrickBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStepInfo
import com.tokopedia.vouchercreation.create.view.viewmodel.CreateMerchantVoucherStepsViewModel
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import kotlinx.android.synthetic.main.activity_create_merchant_voucher_steps.*
import javax.inject.Inject

class CreateMerchantVoucherStepsActivity : FragmentActivity() {

    companion object {
        private const val PROGRESS_ATTR_TAG = "progress"
        private const val PROGRESS_DURATION = 200L
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
        LinkedHashMap<VoucherCreationStepInfo, BaseCreateMerchantVoucherFragment<*,*>>().apply {
            put(VoucherCreationStepInfo.STEP_ONE, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_TWO, PromotionBudgetAndTypeFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_THREE, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_FOUR, MerchantVoucherTargetFragment.createInstance(::onNextStep))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        initInjector()
        setupView()
        observeLiveData()
    }

    override fun onBackPressed() {
        if (currentStepPosition == 0) {
            super.onBackPressed()
        } else {
            onBackStep()
        }
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        setupStatusBar()
        setupToolbar()
        setupViewPager()
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(ContextCompat.getColor(this, R.color.white))
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    private fun setupToolbar() {
        createMerchantVoucherHeader?.run {
            try {
                addRightIcon(R.drawable.ic_tips).setOnClickListener {
                    bottomSheet.show(supportFragmentManager, TipsAndTrickBottomSheetFragment.javaClass.name)
                }
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
            setNavigationOnClickListener(onNavigationClickListener)
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

}