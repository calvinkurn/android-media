package com.tokopedia.homenav.view.fragment

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.FragmentHomeNavBinding
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragmentArgs
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.utils.resources.isDarkMode

class HomeNavFragment : Fragment() {

    private var pageSource: String = ""
    private var pageSourcePath: String = ""

    private var _binding: FragmentHomeNavBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeNavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupView()
    }

    private fun setupNavigation() {
        val navFragment = childFragmentManager.findFragmentById(R.id.fragment_container) ?: return
        val navController = findNavController(navFragment)
        binding.toolbar.setOnBackButtonClickListener {
            navController.navigateUp()
        }
        navController.setGraph(R.navigation.nav_graph,
            MainNavFragmentArgs(StringMainNavArgsSourceKey = pageSource, StringMainNavArgsSourcePathKey = pageSourcePath).toBundle())
    }

    private fun setupView() {
        pageSource = arguments?.getString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE).orEmpty()
        pageSourcePath = arguments?.getString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE_PATH).orEmpty()

        with(binding.toolbar) {
            setToolbarTitle(getString(R.string.title_main_nav))

            val activity = this@HomeNavFragment.activity
            if (activity != null) {
                setupToolbarWithStatusBar(
                    activity,
                    if (activity.applicationContext.isDarkMode()) NavToolbar.Companion.StatusBar.STATUS_BAR_DARK else NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT,
                    true
                )
            }

            setShowShadowEnabled(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onSupportNavigateUp(activity: AppCompatActivity): Boolean {
        return Navigation.findNavController(activity, R.id.fragment_container).navigateUp()
    }
}
