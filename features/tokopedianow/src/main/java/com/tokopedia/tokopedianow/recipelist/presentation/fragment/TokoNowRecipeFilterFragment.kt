package com.tokopedia.tokopedianow.recipelist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.setMaxHeight
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipelist.di.component.DaggerRecipeListComponent
import com.tokopedia.tokopedianow.recipelist.presentation.viewmodel.TokoNowRecipeFilterViewModel
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet
import java.util.*
import javax.inject.Inject

class TokoNowRecipeFilterFragment : Fragment() {

    companion object {

        const val EXTRA_SELECTED_FILTER_IDS = "extra_selected_filter_ids"

        fun newInstance(selectedFilterIds: List<String>): TokoNowRecipeFilterFragment {
            return TokoNowRecipeFilterFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(EXTRA_SELECTED_FILTER_IDS, ArrayList(selectedFilterIds))
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)
            .get(TokoNowRecipeFilterViewModel::class.java)
    }

    private var bottomSheet: TokoNowSortFilterBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedFilterIds = arguments
            ?.getStringArrayList(EXTRA_SELECTED_FILTER_IDS).orEmpty()

        setupBottomSheet()
        showBottomSheet()
        observeLiveData()

        viewModel.getSortFilterOptions(selectedFilterIds)
    }

    private fun observeLiveData() {
        observe(viewModel.visitableItems) {
            submitList(it)
        }
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun setupBottomSheet() {
        bottomSheet = TokoNowSortFilterBottomSheet.newInstance()
        bottomSheet?.setTitle(getString(R.string.tokopedianow_filter))
        bottomSheet?.sortFilterItems = listOf(LoadingMoreModel())
    }

    private fun showBottomSheet() {
        bottomSheet?.show(childFragmentManager)
    }

    private fun submitList(items: List<Visitable<*>>) {
        bottomSheet?.submitList(items)
        bottomSheet?.setMaxHeight()
    }

    private fun injectDependencies() {
        DaggerRecipeListComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}