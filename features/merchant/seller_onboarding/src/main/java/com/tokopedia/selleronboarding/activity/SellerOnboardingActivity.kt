package com.tokopedia.selleronboarding.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.CompositePageTransformer
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SobAdapter
import com.tokopedia.selleronboarding.adapter.SobAdapterFactory
import com.tokopedia.selleronboarding.model.*
import com.tokopedia.selleronboarding.old.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_sob_onboarding.*
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingActivity : BaseActivity() {

    private val sobAdapter by lazy {
        SobAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sob_onboarding)

        setWhiteStatusBar()
        setupSlider()
        setupSliderItems()
        setupButtonClickListener()

        pageIndicatorSob.setIndicator(sobAdapter.dataSize)
    }

    private fun setupSlider() {
        sobViewPager.adapter = sobAdapter
        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer { page, position ->
            val viewObserver = page.findViewById<View>(R.id.viewObserver)
            val r = 1 - abs(position)
            val absPos = abs(position)
            viewObserver.scaleX = (0.85f + r * 0.75f)
            viewObserver.scaleY = (0.85f + r * 0.75f)
            viewObserver.translationY = absPos * -75f
            when {
                position < -1 -> {
                    viewObserver.alpha = 0.1f
                }
                position <= 1 -> {
                    viewObserver.alpha = 0.1f.coerceAtLeast(1 - abs(position))
                }
                else -> {
                    viewObserver.alpha = 0.1f
                }
            }

            if (position == 0.0f) {
                setSlideIndicator(sobViewPager.currentItem)
                setPreviousButtonVisibility()
                updateNextButtonState()
            }
        }
        sobViewPager.setPageTransformer(compositeTransformer)
    }

    private fun updateNextButtonState() {
        val lastSlideIndex = 4
        val isLastSlide = sobViewPager.currentItem == lastSlideIndex
        if (isLastSlide) {
            btnSobNext.text = getString(R.string.sob_login)
        } else {
            btnSobNext.text = getString(R.string.sob_next)
        }
    }

    private fun setPreviousButtonVisibility() {
        val firstSlideIndex = 0
        val shouldShowButton = sobViewPager.currentItem != firstSlideIndex
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

    private fun setSlideIndicator(currentItem: Int) {
        pageIndicatorSob.setCurrentIndicator(currentItem)
    }

    private fun setupButtonClickListener() {
        btnSobNext.setOnClickListener {
            val lastSlideIndex = 4
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
        val slides: List<Visitable<SobAdapterFactory>> = listOf(SobSliderHomeUiModel(),
                SobSliderMessageUiModel, SobSliderManageUiModel,
                SobSliderPromoUiModel, SobSliderStatisticsUiModel)

        sobAdapter.data.clear()
        sobAdapter.addElement(slides)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            requestStatusBarDark()

            addParentLayoutPadding()
        }
    }

    private fun addParentLayoutPadding() {
        val statusBarHeight = StatusBarHelper.getStatusBarHeight(this)
        containerSob.setPadding(0, statusBarHeight, 0, 0)
    }
}