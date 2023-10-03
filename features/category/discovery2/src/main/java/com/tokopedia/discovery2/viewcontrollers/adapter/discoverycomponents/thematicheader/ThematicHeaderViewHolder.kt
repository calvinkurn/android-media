package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.RenderMode
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThematicHeaderViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var thematicHeaderViewModel: ThematicHeaderViewModel? = null
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.subtitle)
    private var backgroundImageView: ImageView = itemView.findViewById(R.id.background_image)
    private var backgroundLottie: LottieAnimationView = itemView.findViewById(R.id.lottieBgAnimation)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        thematicHeaderViewModel = discoveryBaseViewModel as ThematicHeaderViewModel
        if (thematicHeaderViewModel?.fetchLottieState() == true) {
            backgroundLottie.progress = 1f
            backgroundLottie.cancelAnimation()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            thematicHeaderViewModel?.getComponentLiveData()?.observe(it) { componentItem ->
                componentItem.data?.firstOrNull()?.let { dataItem ->
                    setupView(dataItem)
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            thematicHeaderViewModel?.getComponentLiveData()?.removeObservers(it)
        }
    }

    private fun setupView(dataItem: DataItem) {
        with(dataItem) {
            if (!title.isNullOrEmpty()) {
                lihatTitleTextView.text = title
            }
            if (!subtitle.isNullOrEmpty()) {
                lihatSubTitleTextView.text = subtitle
            }
            if (!lottieImage.isNullOrEmpty()) {
                thematicHeaderViewModel?.let { viewModel ->
                    if (!viewModel.fetchLottieState()) {
                        thematicHeaderViewModel?.setLottieState(true)
                        setupLottie(color, lottieImage)
                    }
                }
            } else {
                backgroundImageView.visible()
                backgroundLottie.invisible()
                setupBackground(color, image)
            }
        }
    }

    private fun setupBackground(color: String?, backgroundImageURL: String) {
        try {
            backgroundImageView.show()
            if (!color.isNullOrEmpty()) {
                backgroundImageView.setBackgroundColor(Color.parseColor(color))
            }
            if (backgroundImageURL.isNotEmpty()) {
                backgroundImageView.loadImageWithoutPlaceholder(backgroundImageURL)
            }
        } catch (e: Exception) {
            backgroundImageView.hide()
        }
    }

    private fun setupLottie(color: String?, lottieImageURL: String) {
        try {
            backgroundImageView.show()
            backgroundLottie.visible()
            if (!color.isNullOrEmpty()) {
                backgroundImageView.setBackgroundColor(Color.parseColor(color))
            }
            if (lottieImageURL.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(240)
                    LottieCompositionFactory.fromUrl(itemView.context, lottieImageURL)?.addListener { result ->
                        backgroundLottie.setComposition(result)
                        backgroundLottie.setRenderMode(RenderMode.HARDWARE)
                        backgroundLottie.playAnimation()
                    }
                }
            }
        } catch (e: Exception) {
            backgroundImageView.hide()
            backgroundLottie.hide()
        }
    }
}
