package com.tokopedia.tokofood.feature.search.searchresult.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.tokofood.databinding.BottomsheetTokofoodSearchQuickSortBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.sort.TokofoodQuickSortAdapter
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokofoodQuickSortBottomSheet: BottomSheetUnify(), TokofoodQuickSortAdapter.Listener {

    private var listener: Listener? = null

    private var binding by autoClearedNullable<BottomsheetTokofoodSearchQuickSortBinding>()
    private var quickSortAdapter: TokofoodQuickSortAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomSheetTitle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetTokofoodSearchQuickSortBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onSelectSortItem(uiModel: TokofoodQuickSortUiModel?) {
        dismiss()
        uiModel?.let {
            listener?.onApplySort(it)
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setBottomSheetTitle() {
        setTitle(getString(com.tokopedia.tokofood.R.string.search_srp_quick_sort_title))
    }

    private fun setupView() {
        setupAdapter()
        setupRecyclerView()
        applyAdapter()
    }

    private fun setupAdapter() {
        quickSortAdapter = TokofoodQuickSortAdapter(this@TokofoodQuickSortBottomSheet)
    }

    private fun setupRecyclerView() {
        binding?.rvTokofoodSearchQuickSort?.run {
            adapter = quickSortAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun applyAdapter() {
        arguments?.getParcelableArrayList<TokofoodQuickSortUiModel>(KEY_UI_MODELS)?.let { items ->
            quickSortAdapter?.setSortItems(items)
        }
    }

    interface Listener {
        fun onApplySort(uiModel: TokofoodQuickSortUiModel)
    }

    companion object {

        private const val KEY_UI_MODELS = "key_ui_models"

        private const val TAG = "TokofoodQuickSortBottomSheet"

        @JvmStatic
        fun createInstance(quickSortItems: ArrayList<TokofoodQuickSortUiModel>,
                           listener: Listener): TokofoodQuickSortBottomSheet {
            return TokofoodQuickSortBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_UI_MODELS, quickSortItems)
                }
                this.listener = listener
            }
        }

    }

}