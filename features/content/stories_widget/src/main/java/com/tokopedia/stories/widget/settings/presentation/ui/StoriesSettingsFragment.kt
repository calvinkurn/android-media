package com.tokopedia.stories.widget.settings.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tokopedia.stories.widget.R
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingEvent
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsAction
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsFactory
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsViewModel
import com.tokopedia.stories.widget.settings.tracking.StoriesSettingsTracking
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesSettingsFragment @Inject constructor(private val factory: StoriesSettingsFactory.Creator, private val analytic: StoriesSettingsTracking) :
    Fragment() {

    val entryPoint = StoriesSettingsEntryPoint(
        authorType = "shop",
        authorId = "479541"
    ) //TODO: get from Intent/appLink path/ userInterface

    private val viewModel by viewModels<StoriesSettingsViewModel> { factory.create(entryPoint) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stories_setting, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.container_stories_settings)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoriesSettingsScreen(viewModel)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent()

        viewModel.onEvent(StoriesSettingsAction.FetchPageInfo)
        analytic.openScreen()
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).collectLatest {
                when (val event = it) {
                    is StoriesSettingEvent.ShowErrorToaster -> {
                        Toaster.build(
                            requireView(),
                            text = event.message.message.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                    is StoriesSettingEvent.ClickTrack -> {
                        if (event.option.optionType == "all") {
                            analytic.clickToggle(event.option)
                        } else {
                            analytic.clickCheck(event.option)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        const val TAG = "StoriesSettingsFragment"

        fun get(fragmentManager: FragmentManager): StoriesSettingsFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? StoriesSettingsFragment
        }

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesSettingsFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesSettingsFragment::class.java.name
            ) as StoriesSettingsFragment
        }
    }
}
