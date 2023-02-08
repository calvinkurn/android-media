package com.tokopedia.selleronboarding.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SobAdapter
import com.tokopedia.selleronboarding.analytic.SellerOnboardingV2Analytic
import com.tokopedia.selleronboarding.databinding.ActivitySobOnboardingBinding
import com.tokopedia.selleronboarding.model.*
import com.tokopedia.selleronboarding.utils.OnboardingUtils
import timber.log.Timber
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingActivity : BaseActivity() {

    companion object {
        private const val OBSERVER_DEF_SCALE_XY = 0.45f
        private const val OBSERVER_SCALE_XY_MULTIPLIER = 0.55f
        private const val VIEW_OBSERVER_Y_TRANSLATION = -75f
        private const val VIEW_OBSERVER_ALPHA = 0.1f
        private const val TRANSFORMER_SCALE_MULTIPLIER = 1
        private const val TRANSFORMER_ALPHA_MULTIPLIER = 1
        private const val TRANSFORMER_FULL_PAGE_POSITION = 1
        private const val SLIDER_FIRST_INDEX = 0
        private const val SLIDER_LAT_INDEX = 4
        private const val OFF_SCREEN_PAGE_LIMIT = 2
        private const val ADDITIONAL_INDEX = 1

        private const val PARAM_COACH_MARK = "coachmark"
        private const val DISABLED = "disabled"
    }

    private val sobAdapter by lazy { SobAdapter() }
    private val slideItems: List<BaseSliderUiModel> by lazy {
        listOf(
            SobSliderHomeUiModel(R.drawable.bg_sob_slide_header_home),
            SobSliderMessageUiModel(R.drawable.bg_sob_slide_header_message),
            SobSliderManageUiModel(R.drawable.bg_sob_slide_header_manage),
            SobSliderPromoUiModel(R.drawable.bg_sob_slide_header_promo),
            SobSliderStatisticsUiModel(R.drawable.bg_sob_slide_header_statistics)
        )
    }
    private var binding: ActivitySobOnboardingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySobOnboardingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        handleAppLink()
        setPageBackground()
        setWhiteStatusBar()
        setupViewsTopMargin()
        setupSlider()
        setupSliderTransformer()
        setupSliderItems()
        setupButtonClickListener()

        binding?.pageIndicatorSob?.setIndicator(sobAdapter.dataSize)
    }

    private fun handleAppLink() {
        val uri = intent?.data
        val coachMarkStatus = uri?.getQueryParameter(PARAM_COACH_MARK)
        val shouldDisableCoachMark = coachMarkStatus == DISABLED
        if (shouldDisableCoachMark) {
            CoachMark2.isCoachmmarkShowAllowed = false
            finish()
        }
    }

    private fun setPageBackground() {
        try {
            binding?.backgroundSob?.setImageResource(R.drawable.bg_sob_thematic_ramadhan)
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    @SuppressLint("WrongConstant")
    private fun setupSlider() {
        binding?.sobViewPager?.run {
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
            adapter = sobAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    setSlideIndicator(position)
                    setPreviousButtonVisibility(position)
                    updateNextButtonState(position)
                }
            })
        }
    }

    private fun setupSliderTransformer() {
        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer { page, position ->
            val viewObserver = page.findViewById<View>(R.id.viewObserver)
            val scrollPos = TRANSFORMER_SCALE_MULTIPLIER - abs(position)
            val absPos = abs(position)
            viewObserver.scaleX = OBSERVER_DEF_SCALE_XY + scrollPos * OBSERVER_SCALE_XY_MULTIPLIER
            viewObserver.scaleY = OBSERVER_DEF_SCALE_XY + scrollPos * OBSERVER_SCALE_XY_MULTIPLIER
            viewObserver.translationY = (absPos * VIEW_OBSERVER_Y_TRANSLATION)
            when {
                (position <= TRANSFORMER_FULL_PAGE_POSITION) -> {
                    viewObserver.alpha = VIEW_OBSERVER_ALPHA.coerceAtLeast(
                        TRANSFORMER_ALPHA_MULTIPLIER - abs(position)
                    )
                }
                else -> viewObserver.alpha = VIEW_OBSERVER_ALPHA
            }
        }
        binding?.sobViewPager?.setPageTransformer(compositeTransformer)
    }

    private fun updateNextButtonState(position: Int) {
        val isLastSlide = position == SLIDER_LAT_INDEX
        if (isLastSlide) {
            binding?.btnSobNext?.text = getString(R.string.sob_login)
        } else {
            binding?.btnSobNext?.text = getString(R.string.sob_next)
        }
    }

    private fun setPreviousButtonVisibility(position: Int) {
        binding?.run {
            val shouldShowButton = position != SLIDER_FIRST_INDEX
            if (shouldShowButton) {
                if (!btnSobPrev.isVisible) {
                    val animation = AnimationUtils.loadAnimation(
                        this@SellerOnboardingActivity,
                        R.anim.anim_sob_popin
                    )
                    btnSobPrev.startAnimation(animation)
                }
            } else {
                if (btnSobPrev.isVisible) {
                    val animation = AnimationUtils.loadAnimation(
                        this@SellerOnboardingActivity,
                        R.anim.anim_sob_popout
                    )
                    btnSobPrev.startAnimation(animation)
                }
            }
            btnSobPrev.isVisible = shouldShowButton
        }
    }

    private fun setSlideIndicator(position: Int) {
        binding?.pageIndicatorSob?.setCurrentIndicator(position)
        SellerOnboardingV2Analytic.sendEventImpressionOnboarding(getPositionViewPager())
    }

    private fun setupButtonClickListener() {
        binding?.run {
            btnSobNext.setOnClickListener {
                val lastSlideIndex = slideItems.size.minus(1)
                val isLastSlide = sobViewPager.currentItem == lastSlideIndex
                if (isLastSlide) {
                    goToLoginPage()
                    SellerOnboardingV2Analytic.sendEventClickGetIn()
                } else {
                    moveToNextSlide()
                    SellerOnboardingV2Analytic.sendEventClickNextPage(getPositionViewPager())
                }
            }

            btnSobPrev.setOnClickListener {
                moveToPreviousSlide()
            }
            tvSobSkip.setOnClickListener {
                SellerOnboardingV2Analytic.sendEventClickSkipPage(getPositionViewPager())
                goToLoginPage()
            }
        }
    }

    private fun moveToPreviousSlide() {
        val currentPosition = binding?.sobViewPager?.currentItem.orZero()
        binding?.sobViewPager?.setCurrentItem(currentPosition.minus(ADDITIONAL_INDEX), true)
    }

    private fun moveToNextSlide() {
        val currentPosition = binding?.sobViewPager?.currentItem.orZero()
        binding?.sobViewPager?.setCurrentItem(currentPosition.plus(ADDITIONAL_INDEX), true)
    }

    private fun goToLoginPage() {
        RouteManager.route(this, ApplinkConstInternalUserPlatform.SEAMLESS_LOGIN)
        finish()
    }

    private fun setupSliderItems() {
        sobAdapter.data.clear()
        sobAdapter.addElement(slideItems)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            requestStatusBarDark()
        }
    }

    private fun setupViewsTopMargin() {
        val statusBarHeight = OnboardingUtils.getStatusBarHeight(this)
        val btnSkipLp = binding?.tvSobSkip?.layoutParams as? ViewGroup.MarginLayoutParams
        btnSkipLp?.let { lp ->
            val btnSkipTopMargin = lp.topMargin.plus(statusBarHeight)
            lp.setMargins(
                lp.leftMargin,
                btnSkipTopMargin,
                lp.rightMargin,
                lp.bottomMargin
            )
        }

        val viewPagerLp = binding?.sobViewPager?.layoutParams as? ViewGroup.MarginLayoutParams
        viewPagerLp?.let { lp ->
            val viewPagerTopMargin = lp.topMargin.plus(statusBarHeight)
            lp.setMargins(
                lp.leftMargin,
                viewPagerTopMargin,
                lp.rightMargin,
                lp.bottomMargin
            )
        }
    }

    private fun getPositionViewPager(): Int {
        return binding?.sobViewPager?.currentItem.orZero() + ADDITIONAL_INDEX
    }
}
