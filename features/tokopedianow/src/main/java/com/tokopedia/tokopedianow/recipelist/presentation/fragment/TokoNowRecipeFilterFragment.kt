package com.tokopedia.tokopedianow.recipelist.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.setMaxHeight
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.viewholder.TokoNowSectionHeaderViewHolder.SectionHeaderListener
import com.tokopedia.tokopedianow.recipelist.di.component.DaggerRecipeListComponent
import com.tokopedia.tokopedianow.recipelist.presentation.viewmodel.TokoNowRecipeFilterViewModel
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import java.util.*
import javax.inject.Inject

class TokoNowRecipeFilterFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE_FILTER_INGREDIENTS = 1001

        fun newInstance(selectedFilters: ArrayList<SelectedFilter>): TokoNowRecipeFilterFragment {
            return TokoNowRecipeFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_SELECTED_FILTER, selectedFilters)
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
        val selectedFilters = arguments
            ?.getParcelableArrayList<SelectedFilter>(EXTRA_SELECTED_FILTER).orEmpty()

        setupBottomSheet()
        showBottomSheet()
        observeLiveData()

        viewModel.getSortFilterOptions(selectedFilters)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            REQUEST_CODE_FILTER_INGREDIENTS -> {
                activity?.setResult(Activity.RESULT_OK, data)
                activity?.finish()
            }
        }
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun observeLiveData() {
        observe(viewModel.visitableItems) {
            submitList(it)
        }
    }

    private fun setupBottomSheet() {
        bottomSheet = TokoNowSortFilterBottomSheet.newInstance()
        bottomSheet?.setTitle(getString(R.string.tokopedianow_filter))
        bottomSheet?.sortFilterItems = listOf(LoadingMoreModel())
        bottomSheet?.sectionHeaderListener = sectionHeaderListener()
    }

    private fun showBottomSheet() {
        bottomSheet?.show(childFragmentManager)
    }

    private fun submitList(items: List<Visitable<*>>) {
        bottomSheet?.submitList(items)
        bottomSheet?.setMaxHeight()
    }

    private fun sectionHeaderListener() = object : SectionHeaderListener {
        override fun onClickSeeAllText(appLink: String) {
            val selectedFilters = bottomSheet?.getSelectedFilters()
            val intent = RouteManager.getIntent(context, appLink)
            intent.putParcelableArrayListExtra(EXTRA_SELECTED_FILTER, selectedFilters)
            startActivityForResult(intent, REQUEST_CODE_FILTER_INGREDIENTS)
        }
    }

    private fun injectDependencies() {
        DaggerRecipeListComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}