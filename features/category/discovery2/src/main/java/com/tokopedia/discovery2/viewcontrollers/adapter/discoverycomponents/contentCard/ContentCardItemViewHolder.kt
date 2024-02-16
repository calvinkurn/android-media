package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.TIME_DISPLAY_FORMAT
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.DiscoContentCardItemBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage

class ContentCardItemViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoContentCardItemBinding = DiscoContentCardItemBinding.bind(itemView)
    private var viewModel: ContentCardItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ContentCardItemViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }

        onClick()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            viewModel?.getComponentLiveData()
                ?.observe(fragment.viewLifecycleOwner) { componentItem ->
                    componentItem.data?.let {
                        if (it.isNotEmpty()) {
                            setupImage(it.first())
                            setupData(it.first())
                        }
                    }
                }
            viewModel?.getTimerData()?.observe(it) { timerData ->
                if (timerData.days > 0) {
                    binding.hoursLayout.text = String.format(TIME_DISPLAY_FORMAT, timerData.days)
                    binding.minutesLayout.text = itemView.context?.getString(R.string.hari_small)
                    binding.hoursSeparatorTextView.gone()
                    binding.minutesSeparatorTextView.gone()
                    binding.secondsLayout.gone()
                } else {
                    binding.hoursLayout.text = String.format(TIME_DISPLAY_FORMAT, timerData.hours)
                    binding.minutesLayout.text =
                        String.format(TIME_DISPLAY_FORMAT, timerData.minutes)
                    binding.secondsLayout.text =
                        String.format(TIME_DISPLAY_FORMAT, timerData.seconds)
                    binding.hoursSeparatorTextView.show()
                    binding.minutesSeparatorTextView.show()
                    binding.secondsLayout.show()
                }
            }
            viewModel?.getTimerText()?.observe(it) {
                binding.titleTv.text = it
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getComponentLiveData()?.removeObservers(it)
            viewModel?.stopTimer()
            viewModel?.getTimerText()?.removeObservers(it)
            viewModel?.getTimerData()?.removeObservers(it)
        }
    }

    private fun setupImage(itemData: DataItem) {
        with(binding) {
            try {
                contentCardImage.visible()
                contentCardImage.loadImage(itemData.product?.imageUrlMobile)
            } catch (exception: Exception) {
                contentCardImage.invisible()
                exception.printStackTrace()
            }
        }
    }

    private fun setupData(itemData: DataItem) {
        with(binding) {
            contentCardTitle.text = itemData.title.orEmpty()
            contentCardHeaderSubtitle.text = itemData.totalItem?.itemCountWording ?: ""
            contentCardBenefit.text = itemData.benefit.orEmpty()

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
        viewModel?.startTimer()
        viewModel?.components?.let { componentItem ->
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                ?.trackContentCardImpression(
                    componentItem,
                    Utils.getUserId(fragment.context)
                )
        }
    }

    private fun onClick() {
        itemView.setOnClickListener {
            viewModel?.getNavigationAction()?.let { moveAction ->
                Utils.routingBasedOnMoveAction(moveAction, fragment)
            }
            viewModel?.components?.let { componentItem ->
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackContentCardClick(
                        componentItem,
                        Utils.getUserId(fragment.context)
                    )
            }
        }
    }
}
