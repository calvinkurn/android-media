package com.tokopedia.selleronboarding.activity

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
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SobAdapter
import com.tokopedia.selleronboarding.model.*
import com.tokopedia.selleronboarding.old.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_sob_onboarding.*
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingActivity : BaseActivity() {

    private val sobAdapter by lazy { SobAdapter() }
    private val slideItems: List<BaseSliderUiModel> by lazy {
        listOf(SobSliderHomeUiModel(R.drawable.bg_sob_slide_header_home),
                SobSliderMessageUiModel(R.drawable.bg_sob_slide_header_message),
                SobSliderManageUiModel(R.drawable.bg_sob_slide_header_manage),
                SobSliderPromoUiModel(R.drawable.bg_sob_slide_header_promo),
                SobSliderStatisticsUiModel(R.drawable.bg_sob_slide_header_statistics))
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

        pageIndicatorSob.setIndicator(sobAdapter.dataSize)
    }

    private fun setupSlider() {
        sobViewPager.adapter = sobAdapter
        sobViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

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
            imgSobHeader.loadImage(slideItem.headerResBg)
        } catch (e: IndexOutOfBoundsException) {
            //do nothing
        }
    }

    private fun setupSliderTransformer() {
        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer { page, position ->
            val viewObserver = page.findViewById<View>(R.id.viewObserver)
            val r = 1 - abs(position)
            val absPos = abs(position)
            viewObserver.scaleX = (0.85f + r * 0.75f)
            viewObserver.scaleY = (0.85f + r * 0.75f)
            viewObserver.translationY = (absPos * -75f)
            when {
                (position < -1) -> viewObserver.alpha = 0.1f
                (position <= 1) -> viewObserver.alpha = 0.1f.coerceAtLeast(1 - abs(position))
                else -> viewObserver.alpha = 0.1f
            }
        }
        sobViewPager.setPageTransformer(compositeTransformer)
    }

    private fun updateNextButtonState(position: Int) {
        val lastSlideIndex = 4
        val isLastSlide = position == lastSlideIndex
        if (isLastSlide) {
            btnSobNext.text = getString(R.string.sob_login)
        } else {
            btnSobNext.text = getString(R.string.sob_next)
        }
    }

    private fun setPreviousButtonVisibility(position: Int) {
        val firstSlideIndex = 0
        val shouldShowButton = position != firstSlideIndex
        if (shouldShowButton) {
            if (!btnSobPrev.isVisible) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim_sob_popin)
                btnSobPrev.startAnimation(animation)
            }
        } else {
            if (btnSobPrev.isVisible) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim_sob_popout)
                btnSobPrev.startAnimation(animation)
            }
        }
        btnSobPrev.isVisible = shouldShowButton
    }

    private fun setSlideIndicator(position: Int) {
        pageIndicatorSob.setCurrentIndicator(position)
    }

    private fun setupButtonClickListener() {
        btnSobNext.setOnClickListener {
            val lastSlideIndex = slideItems.size.minus(1)
            val isLastSlide = sobViewPager.currentItem == lastSlideIndex
            if (isLastSlide) {
                goToLoginPage()
            } else {
                moveToNextSlide()
            }
        }

        btnSobPrev.setOnClickListener {
            moveToPreviousSlide()
        }
    }

    private fun moveToPreviousSlide() {
        val currentPosition = sobViewPager.currentItem
        sobViewPager.setCurrentItem(currentPosition.minus(1), true)
    }

    private fun moveToNextSlide() {
        val currentPosition = sobViewPager.currentItem
        sobViewPager.setCurrentItem(currentPosition.plus(1), true)
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
        val statusBarHeight = StatusBarHelper.getStatusBarHeight(this)
        val btnSkipLp = tvSobSkip.layoutParams as? ViewGroup.MarginLayoutParams
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
}