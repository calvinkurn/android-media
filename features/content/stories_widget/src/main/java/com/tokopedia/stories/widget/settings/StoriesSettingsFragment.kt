package com.tokopedia.stories.widget.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Flow: 1) check if shop available (contentCreatorStoryGetAuthorConfig)
 * 2) get list of options (contentCreatorStoryGetAuthorOptions)
 * 3) update toggle (contentCreatorStoryUpdateAuthorOptions)
 */
class StoriesSettingsFragment @Inject constructor(private val factory: StoriesSettingsFactory.Creator) :
    BottomSheetUnify() {

    val entryPoint = StoriesSettingsEntryPoint(authorType = "shop", authorId = "")

    private val viewModel by viewModels<StoriesSettingsViewModel> { factory.create(entryPoint) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent { StoriesSettingsScreen() }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getList()
    }

    fun show(childFragmentManager: FragmentManager) {
        super.show(childFragmentManager, TAG)
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
