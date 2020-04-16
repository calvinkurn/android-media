package com.tokopedia.vouchercreation.view.activity

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.view.adapter.CreateMerchantVoucherStepsAdapter
import com.tokopedia.vouchercreation.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.view.fragment.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.view.uimodel.VoucherCreationStepInfo
import com.tokopedia.vouchercreation.view.viewmodel.CreateMerchantVoucherStepsViewModel
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
        LinkedHashMap<VoucherCreationStepInfo, BaseCreateMerchantVoucherFragment>().apply {
            put(VoucherCreationStepInfo.STEP_ONE, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_TWO, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_THREE, MerchantVoucherTargetFragment.createInstance(::onNextStep))
            put(VoucherCreationStepInfo.STEP_FOUR, MerchantVoucherTargetFragment.createInstance(::onNextStep))
        }
    }

    private val viewPagerOnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val stepInfo = fragmentStepsHashMap.toList()[position].first
                viewModel.setStepPosition(stepInfo)
            }
        }
    }

    private val progressBarInterpolator by lazy { AccelerateDecelerateInterpolator() }

    private var currentStepPosition = 0

    private var currentProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        initInjector()
        setupView()
        observeLiveData()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        setupStatusBar()
        setupViewPager()
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun setupViewPager() {
        createMerchantVoucherViewPager.run {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(viewPagerOnPageChangeCallback)
        }
    }

    private fun observeLiveData() {
        viewModel.stepPositionLiveData.observe(this, Observer { stepInfo ->
            changeStepsInformation(stepInfo)
        })
    }

    private fun changeStepsInformation(stepInfo: VoucherCreationStepInfo) {
        stepInfo.run {
            currentStepPosition = stepPosition
            createMerchantVoucherHeader?.headerSubTitle = stepDescription
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

    private fun onNextStep() {

    }

}