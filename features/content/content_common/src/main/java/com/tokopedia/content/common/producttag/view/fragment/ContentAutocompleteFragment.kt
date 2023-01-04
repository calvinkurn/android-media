package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.databinding.FragmentContentAutocompleteBinding
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.content.common.util.showKeyboard
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2022
 */
@Suppress("LateinitUsage")
class ContentAutocompleteFragment @Inject constructor() : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "AutocompleteFragment"

    private var _binding: FragmentContentAutocompleteBinding? = null
    private val binding: FragmentContentAutocompleteBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentAutocompleteBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        showSearchKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.icBack.setOnClickListener {
            viewModel.submitAction(ProductTagAction.BackPressed)
        }

        binding.sbAutocomplete.searchBarContainer.fitsSystemWindows = false

        binding.sbAutocomplete.searchBarTextField.apply {
            setText(viewModel.globalSearchQuery)
            setSelection(viewModel.globalSearchQuery.length)

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = binding.sbAutocomplete.searchBarTextField.text.toString()
                    submitQuery(query)

                    true
                } else {
                    false
                }
            }
        }

        binding.sbAutocomplete.clearListener = {
            submitQuery("")
        }
    }

    private fun submitQuery(query: String) {
        binding.sbAutocomplete.searchBarTextField.apply {
            clearFocus()
            hideKeyboard()
        }

        viewModel.submitAction(
            ProductTagAction.SetDataFromAutoComplete(
                ProductTagSource.GlobalSearch,
                query,
                "0",
                SearchComponentTrackingConst.Component.AUTO_COMPLETE_MANUAL_ENTER
            )
        )
    }

    private fun showSearchKeyboard() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_SHOW_KEYBOARD)

            binding.sbAutocomplete.searchBarTextField.apply {
                requestFocus()
                showKeyboard()
            }
        }
    }

    companion object {
        const val TAG = "AutocompleteFragment"

        private const val DELAY_SHOW_KEYBOARD = 200L

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentAutocompleteFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ContentAutocompleteFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentAutocompleteFragment::class.java.name
            ) as ContentAutocompleteFragment
        }
    }
}
