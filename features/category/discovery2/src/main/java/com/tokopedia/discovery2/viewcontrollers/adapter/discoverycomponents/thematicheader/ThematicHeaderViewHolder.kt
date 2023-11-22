package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.RenderMode
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.DiscoThematicHeaderLayoutBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThematicHeaderViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        private const val PROGRESS_BAR_FINISH_PROGRESS = 1F
        private const val DELAY_BEFORE_STARTING_ANIMATION = 240L
    }

    private var binding: DiscoThematicHeaderLayoutBinding? by viewBinding()

    private var viewModel: ThematicHeaderViewModel? = null
    private var lottieAnimatedStateMap: MutableMap<Int, Boolean> = mutableMapOf()

    private val mFragment: DiscoveryFragment
        by lazy { fragment as DiscoveryFragment }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = (discoveryBaseViewModel as ThematicHeaderViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        binding?.apply {
            viewModel?.thematicData?.observe(lifecycleOwner){ thematicData ->
                if (thematicData != null) {
                    setupUi(thematicData)
                } else {
                    hideWidget()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return
        viewModel?.thematicData?.removeObservers(lifecycleOwner)
    }

    private fun DiscoThematicHeaderLayoutBinding.hideWidget() {
        title.hide()
        subtitle.hide()
        backgroundImage.hide()
        lottieAnimation.hide()
    }

    private fun DiscoThematicHeaderLayoutBinding.setupUi(
        thematicData: Pair<Int, DataItem?>
    ) {
        thematicData.second?.let { dataItem ->
            root.show()
            setupTitleSubtitle(dataItem)
            setupLottie(thematicData.first, dataItem)
            setupBackground(dataItem)
        }
    }

    private fun DiscoThematicHeaderLayoutBinding.setupTitleSubtitle(
        dataItem: DataItem
    ) {
        if (!dataItem.title.isNullOrEmpty() || !dataItem.subtitle.isNullOrEmpty()) {
            title.show()
            subtitle.show()
            title.text = dataItem.title
            subtitle.text = dataItem.subtitle
        } else {
            hideWidget()
        }
    }

    private fun DiscoThematicHeaderLayoutBinding.setupLottie(
        tabIndex: Int,
        dataItem: DataItem
    ) {
        lottieAnimation.showIfWithBlock(!dataItem.lottieImage.isNullOrEmpty()) {
            if (lottieAnimatedStateMap[tabIndex] != true) {
                try {
                    lottieAnimatedStateMap[tabIndex] = true
                    lottieAnimation.cancelAnimation()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(DELAY_BEFORE_STARTING_ANIMATION)
                        LottieCompositionFactory.fromUrl(context, dataItem.lottieImage)?.addListener { result ->
                            lottieAnimation.setComposition(result)
                            lottieAnimation.setRenderMode(RenderMode.HARDWARE)
                            lottieAnimation.playAnimation()
                        }
                    }
                } catch (e: Exception) {
                    lottieAnimatedStateMap[tabIndex] = false
                    lottieAnimation.hide()
                }
            } else {
                LottieCompositionFactory.fromUrl(context, dataItem.lottieImage)?.addListener { result ->
                    lottieAnimation.setComposition(result)
                    lottieAnimation.progress = PROGRESS_BAR_FINISH_PROGRESS
                }
            }
        }
    }

    private fun DiscoThematicHeaderLayoutBinding.setupBackground(dataItem: DataItem) {
        backgroundImage.showIfWithBlock(!dataItem.color.isNullOrEmpty() && title.isVisible || subtitle.isVisible || lottieAnimation.isVisible) {
            try {
                mFragment.setupBackgroundColorForHeader(dataItem.color)
                backgroundImage.setBackgroundColor(Color.parseColor(dataItem.color))
                backgroundImage.loadImageWithoutPlaceholder(dataItem.imageUrl)
            } catch (e: Exception) {
                hideWidget()
            }
        }
        try {
            if (!dataItem.color.isNullOrEmpty()) {
                balanceWidgetView.setBackgroundColor(Color.parseColor(dataItem.color))
            }
        } catch (e: Exception) {
            /* do nothing */
        }
    }
}
