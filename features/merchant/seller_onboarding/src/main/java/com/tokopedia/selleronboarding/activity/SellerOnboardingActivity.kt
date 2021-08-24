package com.tokopedia.selleronboarding.activity

import android.annotation.SuppressLint
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SobAdapter
import com.tokopedia.selleronboarding.analytic.SellerOnboardingV2Analytic
import com.tokopedia.selleronboarding.model.BaseSliderUiModel
import com.tokopedia.selleronboarding.model.SobSliderHomeUiModel
import com.tokopedia.selleronboarding.model.SobSliderManageUiModel
import com.tokopedia.selleronboarding.model.SobSliderMessageUiModel
import com.tokopedia.selleronboarding.model.SobSliderStatisticsUiModel
import com.tokopedia.selleronboarding.model.SobSliderPromoUiModel
import com.tokopedia.selleronboarding.utils.OnboardingUtils
import kotlinx.android.synthetic.main.activity_sob_onboarding.*
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
    }

    private val sobAdapter by lazy { SobAdapter() }
    private val slideItems: List<BaseSliderUiModel> by lazy {
        listOf(
            SobSliderHomeUiModel(R.drawable.ic_toped_anniv),
            SobSliderMessageUiModel(R.drawable.ic_toped_anniv),
            SobSliderManageUiModel(R.drawable.ic_toped_anniv),
            SobSliderPromoUiModel(R.drawable.ic_toped_anniv),
            SobSliderStatisticsUiModel(R.drawable.ic_toped_anniv)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sob_onboarding)

        setWhiteStatusBar()
        setupViewsTopMargin()
        setupSlider()
        setupSliderTransformer()
        setupSliderItems()
        setupButtonClickListener()

        pageIndicatorSob?.setIndicator(sobAdapter.dataSize)
    }

    @SuppressLint("WrongConstant")
    private fun setupSlider() {
        sobViewPager?.offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
        sobViewPager?.adapter = sobAdapter
        sobViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                setSlideIndicator(position)
                setPreviousButtonVisibility(position)
                updateNextButtonState(position)
                updateHeaderBackground(position)
            }
        })
    }

    private fun updateHeaderBackground(position: Int) {
        try {
            val slideItem = slideItems[position]
            imgSobHeader?.loadImage(slideItem.headerResBg)
        } catch (e: IndexOutOfBoundsException) {
            //do nothing
        }
    }

    private fun setupSliderTransformer() {
        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer { page, position ->
            val viewObserver = page.findViewById<View>(R.id.viewObserver)
            val r = TRANSFORMER_SCALE_MULTIPLIER - abs(position)
            val absPos = abs(position)
            viewObserver.scaleX = (OBSERVER_DEF_SCALE_XY + r * OBSERVER_SCALE_XY_MULTIPLIER)
            viewObserver.scaleY = (OBSERVER_DEF_SCALE_XY + r * OBSERVER_SCALE_XY_MULTIPLIER)
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
        sobViewPager?.setPageTransformer(compositeTransformer)
    }

    private fun updateNextButtonState(position: Int) {
        val isLastSlide = position == SLIDER_LAT_INDEX
        if (isLastSlide) {
            btnSobNext?.text = getString(R.string.sob_login)
        } else {
            btnSobNext?.text = getString(R.string.sob_next)
        }
    }

    private fun setPreviousButtonVisibility(position: Int) {
        val shouldShowButton = position != SLIDER_FIRST_INDEX
        if (shouldShowButton) {
            if (btnSobPrev?.isVisible == false) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim_sob_popin)
                btnSobPrev?.startAnimation(animation)
            }
        } else {
            if (btnSobPrev?.isVisible == true) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim_sob_popout)
                btnSobPrev?.startAnimation(animation)
            }
        }
        btnSobPrev?.isVisible = shouldShowButton
    }

    private fun setSlideIndicator(position: Int) {
        pageIndicatorSob?.setCurrentIndicator(position)
        SellerOnboardingV2Analytic.sendEventImpressionOnboarding(getPositionViewPager())
    }

    private fun setupButtonClickListener() {
        btnSobNext?.setOnClickListener {
            val lastSlideIndex = slideItems.size.minus(1)
            val isLastSlide = sobViewPager?.currentItem == lastSlideIndex
            if (isLastSlide) {
                goToLoginPage()
                SellerOnboardingV2Analytic.sendEventClickGetIn()
            } else {
                moveToNextSlide()
                SellerOnboardingV2Analytic.sendEventClickNextPage(getPositionViewPager())
            }
        }

        btnSobPrev?.setOnClickListener {
            moveToPreviousSlide()
        }
        tvSobSkip?.setOnClickListener {
            SellerOnboardingV2Analytic.sendEventClickSkipPage(getPositionViewPager())
            goToLoginPage()
        }
    }

    private fun moveToPreviousSlide() {
        val currentPosition = sobViewPager?.currentItem.orZero()
        sobViewPager.setCurrentItem(currentPosition.minus(1), true)
    }

    private fun moveToNextSlide() {
        val currentPosition = sobViewPager?.currentItem.orZero()
        sobViewPager?.setCurrentItem(currentPosition.plus(1), true)
    }

    private fun goToLoginPage() {
        RouteManager.route(this, ApplinkConstInternalGlobal.SEAMLESS_LOGIN)
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
        val btnSkipLp = tvSobSkip?.layoutParams as? ViewGroup.MarginLayoutParams
        btnSkipLp?.let { lp ->
            val btnSkipTopMargin = lp.topMargin.plus(statusBarHeight)
            lp.setMargins(
                lp.leftMargin,
                btnSkipTopMargin,
                lp.rightMargin,
                lp.bottomMargin
            )
        }

        val viewPagerLp = sobViewPager.layoutParams as? ViewGroup.MarginLayoutParams
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

    private fun getPositionViewPager(): Int = sobViewPager?.currentItem.orZero() + ADDITIONAL_INDEX
}