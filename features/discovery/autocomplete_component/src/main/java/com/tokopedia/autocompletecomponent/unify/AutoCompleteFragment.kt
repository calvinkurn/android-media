package com.tokopedia.autocompletecomponent.unify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.autocompletecomponent.util.SCREEN_UNIVERSEARCH
import com.tokopedia.iris.Iris
import com.tokopedia.nest.principles.ui.NestTheme
import javax.inject.Inject

class AutoCompleteFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val iris: Iris
) : TkpdBaseV4Fragment() {

    private val viewModel: AutoCompleteViewModel by viewModels { viewModelFactory }

    override fun getScreenName(): String = SCREEN_UNIVERSEARCH

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)

        setContent {
            NestTheme {
                AutoCompleteScreen(viewModel, iris, (activity as? AutoCompleteListener))
            }
        }

        viewModel.onScreenInitialized()
    }

    fun updateParameter(parameter: Map<String, String>) {
        viewModel.onScreenUpdateParameter(parameter)
    }

    companion object {
        const val AUTO_COMPLETE_FRAGMENT_TAG = "AUTO_COMPLETE_TAG"

        fun newInstance(
            classLoader: ClassLoader,
            fragmentFactory: FragmentFactory
        ): AutoCompleteFragment =
            fragmentFactory.instantiate(
                classLoader,
                AutoCompleteFragment::class.java.name
            ) as AutoCompleteFragment
    }
}
