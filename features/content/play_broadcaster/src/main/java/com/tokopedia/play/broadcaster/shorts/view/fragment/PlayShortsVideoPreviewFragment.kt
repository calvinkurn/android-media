package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.shorts.view.compose.VideoPreviewLayout
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
class PlayShortsVideoPreviewFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : PlayShortsBaseFragment() {

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                NestTheme(
                    darkTheme = true
                ) {
                    Surface {
                        VideoPreviewLayout(
                            videoUri = viewModel.productVideo.videoUrl,
                            onClose = {
                                activity?.onBackPressed()
                            }
                        )
                    }
                }
            }
        }

        return composeView
    }

    companion object {
        const val TAG = "PlayShortsVideoPreviewFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsVideoPreviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayShortsVideoPreviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsVideoPreviewFragment::class.java.name
            ) as PlayShortsVideoPreviewFragment
        }
    }
}
