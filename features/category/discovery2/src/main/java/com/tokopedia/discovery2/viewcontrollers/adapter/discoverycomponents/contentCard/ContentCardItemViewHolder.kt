package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.TIME_DISPLAY_FORMAT
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.DiscoContentCardItemBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

private const val DEFAULT_DESIGN = 2

class ContentCardItemViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoContentCardItemBinding = DiscoContentCardItemBinding.bind(itemView)
    private lateinit var contentCardItemViewModel: ContentCardItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        contentCardItemViewModel = discoveryBaseViewModel as ContentCardItemViewModel
        itemView.setOnClickListener {
            contentCardItemViewModel.getNavigationUrl()?.let {
                if (it.isNotEmpty()) {
                    RouteManager.route(fragment.activity, it)
                }
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackContentCardClick(contentCardItemViewModel.components)
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            contentCardItemViewModel.getComponentLiveData()
                .observe(fragment.viewLifecycleOwner) { componentItem ->
                    componentItem.data?.let {
                        if (it.isNotEmpty()) {
                            setupImage(it.first())
                            setupData(it.first())
                        }
                    }
                }
            contentCardItemViewModel.getTimerData().observe(it) { timerData ->
                binding.hoursLayout.text = String.format(TIME_DISPLAY_FORMAT, timerData.hours)
                binding.minutesLayout.text = String.format(TIME_DISPLAY_FORMAT, timerData.minutes)
                if (timerData.timeFinish) (fragment as DiscoveryFragment).reSync()
            }
            contentCardItemViewModel.getTimerText().observe(it) {
                binding.titleTv.text = it
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            contentCardItemViewModel.getComponentLiveData().removeObservers(it)
            contentCardItemViewModel.stopTimer()
            contentCardItemViewModel.getTimerData().removeObservers(it)
        }
    }

    private fun setupImage(itemData: DataItem) {
        with(binding) {
            try {
                contentCardImage.apply {
                    loadImage(itemData.product?.imageUrlMobile)
                }
            } catch (exception: NumberFormatException) {
                contentCardImage.hide()
                exception.printStackTrace()
            }
        }
    }

    private fun setupData(itemData: DataItem) {
        with(binding) {
            contentCardTitle.text = itemData.title
            contentCardHeaderSubtitle.text = itemData.totalItem?.itemCountWording
            contentCardBenefit.text = itemData.benefit

            if (!itemData.startDate.isNullOrEmpty() || !itemData.endDate.isNullOrEmpty()) {
                contentCardHeaderSubtitle.gone()
                timerContainerLayout.show()

            } else {
                contentCardHeaderSubtitle.show()
                timerContainerLayout.gone()
            }
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        contentCardItemViewModel.startTimer()
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            ?.trackContentCardImpression(contentCardItemViewModel.components)
    }

}
