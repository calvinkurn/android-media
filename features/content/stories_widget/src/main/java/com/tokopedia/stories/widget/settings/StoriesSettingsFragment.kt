package com.tokopedia.stories.widget.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import javax.inject.Inject
import com.tokopedia.stories.widget.R

class StoriesSettingsFragment @Inject constructor(private val factory: StoriesSettingsFactory.Creator) :
    Fragment() {

    val entryPoint = StoriesSettingsEntryPoint(authorType = "shop", authorId = "479541")

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
        viewModel.getList()
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
