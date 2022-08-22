package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.FragmentAutocompleteBinding
import com.tokopedia.content.common.producttag.util.extension.hideKeyboard
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2022
 */
class AutocompleteFragment @Inject constructor(

) : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "AutocompleteFragment"

    private var _binding: FragmentAutocompleteBinding? = null
    private val binding: FragmentAutocompleteBinding
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
        _binding = FragmentAutocompleteBinding.inflate(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.sbAutocomplete.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.sbAutocomplete.searchBarTextField.text.toString()
                submitQuery(query)

                true
            }
            else false
        }

        binding.sbAutocomplete.clearListener = {
            submitQuery("")
        }
    }

    private fun submitQuery(query: String) {
        viewModel.submitAction(
            ProductTagAction.SetDataFromAutoComplete(ProductTagSource.GlobalSearch, query, "0", "")
        )

        binding.sbAutocomplete.searchBarTextField.apply {
            clearFocus()
            hideKeyboard()
        }
    }

    companion object {
        const val TAG = "AutocompleteFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): AutocompleteFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? AutocompleteFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                AutocompleteFragment::class.java.name
            ) as AutocompleteFragment
        }
    }
}