package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.tokopedia.feedplus.databinding.FragmentFeedIntermediaryBinding
import com.tokopedia.navigation_common.listener.FragmentListener

/**
 * Created by kenny.hadisaputra on 12/05/23
 */
class FeedIntermediaryFragment : Fragment(), FragmentListener {

    private var _binding: FragmentFeedIntermediaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedIntermediaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        childFragmentManager.commit {
            replace(
                binding.root.id,
                FeedBaseFragment().apply {
                    arguments = this@FeedIntermediaryFragment.arguments
                },
                FRAGMENT_TAG
            )
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        getCurrentFragment()?.userVisibleHint = isVisibleToUser
    }

    override fun onScrollToTop() {
        val fragment = getCurrentFragment() ?: return
        if (fragment is FragmentListener) fragment.onScrollToTop()
    }

    override fun isLightThemeStatusBar(): Boolean {
        val fragment = getCurrentFragment() ?: return true
        return if (fragment is FragmentListener) {
            fragment.isLightThemeStatusBar
        } else {
            false
        }
    }

    override fun isForceDarkModeNavigationBar(): Boolean {
        val fragment = getCurrentFragment() ?: return true
        return if (fragment is FragmentListener) {
            fragment.isForceDarkModeNavigationBar
        } else {
            false
        }
    }

    private fun getCurrentFragment(): Fragment? {
        if (!isAdded) return null
        return childFragmentManager.findFragmentByTag(FRAGMENT_TAG)
    }

    companion object {
        private const val FRAGMENT_TAG = "fragment"
    }
}
