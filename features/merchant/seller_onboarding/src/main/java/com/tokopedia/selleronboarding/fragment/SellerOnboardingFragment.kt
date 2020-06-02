package com.tokopedia.selleronboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.SlideAdapter
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalytic
import com.tokopedia.selleronboarding.model.SlideUiModel
import com.tokopedia.selleronboarding.utils.OnboardingLayoutManager
import kotlinx.android.synthetic.main.fragment_sob_onboarding.view.*
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

/**
 * Created By @ilhamsuaib on 13/04/20
 */

class SellerOnboardingFragment : Fragment() {

    companion object {
        fun newInstance(): SellerOnboardingFragment = SellerOnboardingFragment()
    }

    private val sliderAdapter by lazy { SlideAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sob_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        initOnboardingSlides()
    }

    private fun setupView() = view?.run {
        rvSliderSob.adapter = sliderAdapter
        rvSliderSob.layoutManager = OnboardingLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(rvSliderSob)

        btnSobOpenApp.setOnClickListener {
            openApp()
        }

        setupOnSlideListener()
    }

    private fun setupOnSlideListener() {
        view?.rvSliderSob?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                setSelectedPageIndicator()
            }
        })
    }

    private fun setSelectedPageIndicator() {
        val mLayoutManager = view?.rvSliderSob?.layoutManager as? LinearLayoutManager
        val position = mLayoutManager?.findFirstCompletelyVisibleItemPosition().orZero()
        if (position >= 0) {
            view?.pageIndicatorSob?.setCurrentIndicator(position)
        }
    }

    private fun initOnboardingSlides() {
        sliderAdapter.clearSlideItems()
        sliderAdapter.addSlideItem(getSlideItem(getString(R.string.sob_header_text_page_1), R.drawable.sob_vector_illustration_onboarding_1, R.drawable.sob_illustration_onboarding_1))
        sliderAdapter.addSlideItem(getSlideItem(getString(R.string.sob_header_text_page_2), R.drawable.sob_vector_illustration_onboarding_2, R.drawable.sob_illustration_onboarding_2))
        sliderAdapter.addSlideItem(getSlideItem(getString(R.string.sob_header_text_page_3), R.drawable.sob_vector_illustration_onboarding_3, R.drawable.sob_illustration_onboarding_3))

        view?.pageIndicatorSob?.setIndicator(sliderAdapter.itemCount)
    }

    private fun getSlideItem(headerText: String, @DrawableRes vectorDrawableRes: Int, @DrawableRes drawableRes: Int): SlideUiModel {
        return SlideUiModel(headerText, vectorDrawableRes, drawableRes)
    }

    private fun openApp() = view?.run {
        sendEventOpenApp()
        RouteManager.route(context, ApplinkConstInternalGlobal.SEAMLESS_LOGIN)
        activity?.finish()
    }

    private fun sendEventOpenApp() {
        val mLayoutManager = view?.rvSliderSob?.layoutManager as? LinearLayoutManager
        val position = mLayoutManager?.findFirstCompletelyVisibleItemPosition() ?: -1
        if (position >= 0) {
            val irisSession = IrisSession(context ?: return)
            SellerOnboardingAnalytic.sendEventClickOpenApp(position, irisSession.getSessionId(), irisSession.getDeviceId())
        }
    }
}