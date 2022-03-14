package com.tokopedia.play.broadcaster.view.fragment.summary

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastSummaryBinding
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 08, 2022
 */
class PlayBroadcastSummaryFragment @Inject constructor(
    private val fragmentFactory: FragmentFactory,
    private val summaryViewModelFactory: PlayBroadcastSummaryViewModel.Factory,
) : PlayBaseBroadcastFragment(), ViewModelFactoryProvider {

    override fun getScreenName(): String = "Play Summary page"

    private lateinit var summaryViewModelProviderFactory: ViewModelProvider.Factory

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private var _binding: FragmentPlayBroadcastSummaryBinding? = null
    private val binding: FragmentPlayBroadcastSummaryBinding
        get() = _binding!!

    private val reportListener = object : PlayBroadcastReportFragment.Listener {
        override fun onClickPostButton() {
            navigateToFragment(PlayBroadcastPostVideoFragment::class.java, true)
        }
    }

    override fun getFactory(): ViewModelProvider.Factory {
        if (!::summaryViewModelProviderFactory.isInitialized) {
            summaryViewModelProviderFactory = object : AbstractSavedStateViewModelFactory(
                this,
                arguments
            ) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return summaryViewModelFactory.create(
                        parentViewModel.channelId,
                        parentViewModel.productSectionList,
                        parentViewModel.summaryLeaderboardInfo,
                    ) as T
                }
            }
        }
        return summaryViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(requireActivity())[PlayBroadcastViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastSummaryBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInsets()
        setupView()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
            is PlayBroadcastReportFragment -> childFragment.setListener(reportListener)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.root.requestApplyInsetsWhenAttached()
    }

    override fun onResume() {
        super.onResume()
        if(!isDarkMode()) activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun isDarkMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    private fun setupInsets() {
        binding.flBroSummaryContainer.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(
                top = padding.top + insets.systemWindowInsetTop,
                bottom = padding.bottom + insets.systemWindowInsetBottom,
            )
        }
    }

    private fun setupView() {
        navigateToFragment(PlayBroadcastReportFragment::class.java)
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(requireActivity().classLoader, fragmentClass.name)
    }

    private fun navigateToFragment(fragmentClass: Class<out Fragment>, isAddToBackstack: Boolean = false) {
        val fragment = getFragmentByClassName(fragmentClass)
        childFragmentManager.beginTransaction()
            .replace(binding.flBroSummaryContainer.id, fragment, fragmentClass.name).apply {
                if(isAddToBackstack) addToBackStack(null)
            }
            .commit()
    }

    override fun onBackPressed(): Boolean {
        val currentFragment = childFragmentManager.findFragmentById(binding.flBroSummaryContainer.id)
        if(currentFragment != null && currentFragment is PlayBroadcastPostVideoFragment) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }
}