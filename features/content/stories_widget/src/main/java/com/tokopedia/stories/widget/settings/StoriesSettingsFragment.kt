package com.tokopedia.stories.widget.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView

/**
 * Flow: 1) check if shop available (contentCreatorStoryGetAuthorConfig)
 * 2) get list of options (contentCreatorStoryGetAuthorOptions)
 * 3) update toggle (contentCreatorStoryUpdateAuthorOptions)
 */
class StoriesSettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent { StoriesSettingsScreen() }
        }
        return composeView
    }
}
