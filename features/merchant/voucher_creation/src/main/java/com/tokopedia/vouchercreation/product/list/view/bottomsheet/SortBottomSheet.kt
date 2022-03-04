package com.tokopedia.vouchercreation.product.list.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcSortFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.SortListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.SortSelection

class SortBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplySortFilter(selectedSort: List<SortSelection>)
    }

    companion object {

        private const val SORT_SELECTIONS = "SORT_SELECTIONS"

        @JvmStatic
        fun createInstance(sortSelections: List<SortSelection>,
                           clickListener: OnApplyButtonClickListener): SortBottomSheet {
            return SortBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(SORT_SELECTIONS, ArrayList(sortSelections))
                }
            }
        }
    }

    private var listAdapter: SortListAdapter? = SortListAdapter()

    private var binding: BottomsheetMvcSortFilterLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val sortSelections: ArrayList<SortSelection> by lazy {
        arguments?.getParcelableArrayList(SORT_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetMvcSortFilterLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_sort))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        listAdapter?.setSortSelections(sortSelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetMvcSortFilterLayoutBinding?) {
        binding?.rvSortList?.let {
            it.adapter = listAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.applyButton?.setOnClickListener {
            val selectedSort = listAdapter?.getSelectedSort() ?: listOf()
            clickListener?.onApplySortFilter(selectedSort)
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}