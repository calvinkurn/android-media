package com.tokopedia.selleronboarding.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.viewpager2.widget.CompositePageTransformer
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SobAdapter
import com.tokopedia.selleronboarding.model.*
import com.tokopedia.selleronboarding.old.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_sob_old_onboarding.*
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
        setupView()
    }

    private fun setupView() {
        sobViewPager.adapter = sobAdapter
        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.85f + r * 0.15f)
            page.scaleX = (0.85f + r * 0.15f)
            when {
                position < -1 ->
                    page.alpha = 0.1f
                position <= 1 -> {
                    page.alpha = 0.1f.coerceAtLeast(1 - abs(position))
                }
                else -> page.alpha = 0.1f
            }
        }
        sobViewPager.setPageTransformer(compositeTransformer)

        sobAdapter.clearAllElements()
        sobAdapter.addElement(getSliderItems())
    }

    private fun getSliderItems(): List<Visitable<*>> {
        return listOf(SobSliderHomeUiModel, SobSliderMessageUiModel,
                SobSliderManageUiModel, SobSliderPromoUiModel, SobSliderStatisticsUiModel)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            requestStatusBarDark()

            //addParentLayoutPadding()
        }
    }

    private fun addParentLayoutPadding() {
        val statusBarHeight = StatusBarHelper.getStatusBarHeight(this)
        onboardingContainer.setPadding(0, statusBarHeight, 0, 0)
    }
}