package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.databinding.BannerTimerLayoutBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight

class BannerTimerViewHolder(customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView, fragment.viewLifecycleOwner) {

    private var bannerTimerViewModel: BannerTimerViewModel? = null
    private val binding: BannerTimerLayoutBinding = BannerTimerLayoutBinding.bind(itemView)
    private var context: Context = binding.bannerTimerContainerLayout.context

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannerTimerViewModel = discoveryBaseViewModel as BannerTimerViewModel
        initView()
    }

    private fun initView() {
        with(binding) {
            val viewHeight = bannerTimerViewModel?.getBannerUrlHeight()
            val viewWidth = bannerTimerViewModel?.getBannerUrlWidth()
            val constraintSet = ConstraintSet()
            constraintSet.clone(bannerTimerContainerLayout)
            constraintSet.setDimensionRatio(bannerImageView.id, "H, $viewWidth : $viewHeight")
            constraintSet.applyTo(bannerTimerContainerLayout)
            configureTimerUI()
            bannerTimerContainerLayout.setOnClickListener {
                handleUIClick(it)
            }
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            binding.bannerTimerContainerLayout -> {
                bannerTimerViewModel?.getApplink()?.let {
                    RouteManager.route(context, it)
                }
            }
        }
    }

    private fun configureTimerUI() {
        with(binding) {
            bannerTimerViewModel?.getComponent().let {
                if (!it?.data.isNullOrEmpty()) {
                    bannerImageView.loadImageFitCenter(
                        it?.data?.firstOrNull()?.backgroundUrlMobile
                            ?: ""
                    )
                }
            }
            bannerTimer.size = TimerUnifyHighlight.SIZE_MEDIUM
            bannerTimerViewModel?.let {
                bannerTimer.variant = it.getTimerVariant()
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            bannerTimerViewModel?.getSyncPageLiveData()?.observe(lifecycle) { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            }
            bannerTimerViewModel?.getRestartTimerAction()?.observe(lifecycle) { shouldStartTimer ->
                if (shouldStartTimer) {
                    bannerTimerViewModel?.startTimer(binding.bannerTimer)
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            bannerTimerViewModel?.stopTimer()
            binding.bannerTimer.pause()
            bannerTimerViewModel?.getSyncPageLiveData()?.removeObservers(it)
            bannerTimerViewModel?.getRestartTimerAction()?.removeObservers(it)
        }
    }

    override fun onViewDetachedToWindow() {
        bannerTimerViewModel?.stopTimer()
        binding.bannerTimer.pause()
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        bannerTimerViewModel?.startTimer(binding.bannerTimer)
        binding.bannerTimer.onFinish = {
            bannerTimerViewModel?.checkTimerEnd()
        }
    }
}
