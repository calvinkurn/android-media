package com.tokopedia.stories.widget.settings.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.content.common.util.Router
import com.tokopedia.stories.widget.R
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingEvent
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsAction
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsViewModel
import com.tokopedia.stories.widget.settings.tracking.StoriesSettingsTracking
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesSettingsFragment @Inject constructor(
    private val factory: ViewModelFactory,
    private val analytic: StoriesSettingsTracking,
    private val router: Router
) :
    Fragment() {

    private val viewModel by viewModels<StoriesSettingsViewModel> { factory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoriesSettingsScreen(viewModel)
            }
        }
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent()

        viewModel.onAction(StoriesSettingsAction.FetchPageInfo)
        analytic.openScreen()
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).collect {
                when (val event = it) {
                    is StoriesSettingEvent.ShowErrorToaster -> {
                        Toaster.build(
                            requireView(),
                            text = event.message.message ?: getString(R.string.stories_settings_general_error),
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }

                    is StoriesSettingEvent.ClickTrack -> {
                        if (event.option.optionType == OPTION_TYPE_ALL) {
                            analytic.clickToggle(event.option)
                        } else {
                            analytic.clickCheck(event.option)
                        }
                    }

                    is StoriesSettingEvent.Navigate -> {
                        if (event.appLink == getString(R.string.stories_redirect_settings)) {
                            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            router.route(requireActivity(), intent)
                        } else {
                            router.route(requireContext(), event.appLink)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        const val TAG = "StoriesSettingsFragment"
        private const val OPTION_TYPE_ALL = "all"

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
