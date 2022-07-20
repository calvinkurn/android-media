package com.tokopedia.vouchercreation.product.list.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcShowcaseFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.ShowCaseListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ShowCaseSelection

class ShowCaseBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplyShowCaseFilter(selectedShowCases: List<ShowCaseSelection>)
    }

    companion object {

        private const val SHOWCASE_SELECTIONS = "SHOWCASE_SELECTIONS"

        @JvmStatic
        fun createInstance(showCaseSelections: List<ShowCaseSelection>,
                           clickListener: OnApplyButtonClickListener): ShowCaseBottomSheet {
            return ShowCaseBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(SHOWCASE_SELECTIONS, ArrayList(showCaseSelections))
                }
            }
        }
    }

    private var listAdapter: ShowCaseListAdapter? = ShowCaseListAdapter()

    private var binding: BottomsheetMvcShowcaseFilterLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val showCaseSelections: ArrayList<ShowCaseSelection> by lazy {
        arguments?.getParcelableArrayList(SHOWCASE_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetMvcShowcaseFilterLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_showcase))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        listAdapter?.setShowCaseSelections(showCaseSelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetMvcShowcaseFilterLayoutBinding?) {
        binding?.rvShowcaseList?.let {
            it.adapter = listAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.applyButton?.setOnClickListener {
            val selectedShowCases = listAdapter?.getSelectedShowCases() ?: listOf()
            clickListener?.onApplyShowCaseFilter(selectedShowCases)
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}