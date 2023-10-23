package com.tokopedia.topads.view.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.AutoBidSelectionFragment
import com.tokopedia.topads.view.fragment.BudgetingAdsFragment
import com.tokopedia.topads.view.fragment.ProductAdsListFragment
import com.tokopedia.topads.view.fragment.ProductRecommendationBidAdsFragment
import com.tokopedia.topads.view.fragment.ProductSummaryAdsFragment
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.common.R as topadscommonR

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_BACK_BUTTON = "click-back button"

class StepperActivity : BaseStepperActivity(), HasComponent<CreateAdsComponent> {

    private var fragmentList: MutableList<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToFragment(UrlConstant.FRAGMENT_NUMBER_1, stepperModel)
    }

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(
            ProductAdsListFragment.createInstance(),
            AutoBidSelectionFragment.createInstance(),
            BudgetingAdsFragment.createInstance(),
            ProductRecommendationBidAdsFragment.createInstance(),
            ProductSummaryAdsFragment.createInstance())
        return fragmentList!!
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()
    }

    fun addIcon() {
        findViewById<HeaderUnify>(toolbarResourceID).addRightIcon(0).apply {
            clearImage()
            setImageDrawable(
                com.tokopedia.iconunify.getIconUnifyDrawable(
                    context,
                    IconUnify.LIGHT_BULB,
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_NN950
                    )
                )
            )
            setOnClickListener {
                val tipsList: ArrayList<TipsUiModel> = ArrayList()
                tipsList.apply {
                    add(TipsUiRowModel(R.string.pilih_produk_yang_berada_dalam,
                        topadscommonR.drawable.topads_create_ic_checklist))
                    add(TipsUiRowModel(R.string.pilih_produk_dengan_ulasan_terbanyak,
                        topadscommonR.drawable.topads_create_ic_checklist))
                    add(TipsUiRowModel(R.string.pilih_produk_terpopuler,
                        topadscommonR.drawable.topads_create_ic_checklist))
                }
                val tipsListSheet =
                    context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
                tipsListSheet?.showHeader = true
                tipsListSheet?.showKnob = false
                tipsListSheet?.setTitle(getString(topadscommonR.string.tip_memilih_produk))
                tipsListSheet?.show(supportFragmentManager, "")
            }

        }

    }

    fun removeIcon() {
        findViewById<HeaderUnify>(toolbarResourceID).rightContentView.removeAllViews()
    }

    override fun onBackEvent() {
        if (currentPosition <= fragmentList?.size ?: 0 && fragmentList?.get(currentPosition - 1) is BudgetingAdsFragment) {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BACK_BUTTON, "")
        }
        super.onBackEvent()
    }

    override fun onBackPressed() {
        try {
            val fragment = fragmentList?.find { it is AutoBidSelectionFragment }
            if (fragment is TkpdBaseV4Fragment && fragment.isVisible) {
                val handled = fragment.onFragmentBackPressed()
                if (handled) {
                    return
                }
            }
        } catch (_: Exception) {
        }

        super.onBackPressed()
    }

}
