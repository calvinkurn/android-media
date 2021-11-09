package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight

class BannerTimerViewHolder(private val customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView, fragment.viewLifecycleOwner) {

    private lateinit var bannerTimerViewModel: BannerTimerViewModel
    private var constraintLayout: ConstraintLayout = customItemView.findViewById(R.id.banner_timer_container_layout)
    private var timer:TimerUnifyHighlight = customItemView.findViewById(R.id.banner_timer)
    private var context: Context
    private var bannerImageView: ImageView = customItemView.findViewById(R.id.banner_image_view)

    init {
        context = constraintLayout.context
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannerTimerViewModel = discoveryBaseViewModel as BannerTimerViewModel
        initView()
    }

    private fun initView() {
        val viewHeight = bannerTimerViewModel.getBannerUrlHeight()
        val viewWidth = bannerTimerViewModel.getBannerUrlWidth()
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(bannerImageView.id, "H, $viewWidth : $viewHeight")
        constraintSet.applyTo(constraintLayout)
        configureTimerUI()
        constraintLayout.setOnClickListener {
            handleUIClick(it)
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            constraintLayout -> {
                bannerTimerViewModel.getApplink()?.let {
                    RouteManager.route(context, it)
                }
            }
        }
    }


    private fun configureTimerUI() {
        bannerTimerViewModel.getComponent().let {
            if (!it.data.isNullOrEmpty()) {
                bannerImageView.loadImageFitCenter(it.data?.firstOrNull()?.backgroundUrlMobile ?: "")
            }
        }
        timer.size = TimerUnifyHighlight.SIZE_MEDIUM
        timer.variant = bannerTimerViewModel.getTimerVariant()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {lifecycle ->
            bannerTimerViewModel.getSyncPageLiveData().observe(lifecycle, { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
            bannerTimerViewModel.getRestartTimerAction().observe(lifecycle, { shouldStartTimer ->
                if (shouldStartTimer)
                    bannerTimerViewModel.startTimer(timer)
            })
        }
    }


    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            bannerTimerViewModel.stopTimer()
            timer.pause()
            bannerTimerViewModel.getSyncPageLiveData().removeObservers(it)
            bannerTimerViewModel.getRestartTimerAction().removeObservers(it)
        }
    }

    override fun onViewDetachedToWindow() {
        bannerTimerViewModel.stopTimer()
        timer.pause()
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        bannerTimerViewModel.startTimer(timer)
        timer.onFinish = {
            bannerTimerViewModel.checkTimerEnd()
        }
    }
}