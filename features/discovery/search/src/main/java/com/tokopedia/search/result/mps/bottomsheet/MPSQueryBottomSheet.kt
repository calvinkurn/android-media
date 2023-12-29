package com.tokopedia.search.result.mps.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchMpsQueryBottomsheetLayoutBinding
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx

class MPSQueryBottomSheet: BottomSheetUnify() {

    private var listener: Listener? = null
    private var binding: SearchMpsQueryBottomsheetLayoutBinding? = null
    private val adapter = Adapter(::onChipSelected)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SearchMpsQueryBottomsheetLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        setTitle(getString(R.string.search_mps_query_bottomsheet_title))
        setChild(binding?.root)
        setOnDismissListener {
            listener?.onDismiss()
        }

        initViews()
    }

    private fun initViews() {
        binding?.apply {
            mpsChooseQueryRecyclerView.layoutManager = chipsLayoutManager()
            mpsChooseQueryRecyclerView.adapter = adapter
            mpsChooseQueryRecyclerView.isNestedScrollingEnabled = false
            mpsChooseQueryRecyclerView.addItemDecoration(
                ChipSpacingItemDecoration(8.toPx(), 8.toPx())
            )

            mpsChooseQueryButton.setOnClickListener(::onFindProductClick)
        }

        adapter.submitList(getQueryList().map(::QueryChip))
    }

    private fun getQueryList() = arguments?.getStringArrayList(ARGS_QUERY_LIST) ?: listOf()

    private fun chipsLayoutManager() = ChipsLayoutManager
        .newBuilder(requireContext())
        .setOrientation(ChipsLayoutManager.HORIZONTAL)
        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
        .build()

    private fun onChipSelected(queryChip: QueryChip) {
        val currentList = adapter.currentList
        val newList = currentList.map {
            it.copy(isSelected = it.query == queryChip.query)
        }

        adapter.submitList(newList)
    }

    private fun onFindProductClick(ignored: View) {
        val context = context ?: return
        val selectedQueryChip = adapter.currentList.find { it.isSelected } ?: return
        val parameters = "${SearchApiConst.Q}=${selectedQueryChip.query}"

        RouteManager.route(
            context,
            "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?$parameters"
        )

        dismiss()
    }

    private class Adapter(
        private val onChipSelected: (QueryChip) -> Unit,
    ): ListAdapter<QueryChip, ViewHolder>(ChipQueryDiffUtilCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val chipsUnify = ChipsUnify(parent.context)
            return ViewHolder(chipsUnify, onChipSelected)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    private class ChipQueryDiffUtilCallback: DiffUtil.ItemCallback<QueryChip>() {
        override fun areItemsTheSame(oldItem: QueryChip, newItem: QueryChip): Boolean =
            oldItem.query == newItem.query

        override fun areContentsTheSame(oldItem: QueryChip, newItem: QueryChip): Boolean =
            oldItem.isSelected == newItem.isSelected
    }

    private class ViewHolder(
        private val chipsUnify: ChipsUnify,
        private val onChipSelected: (QueryChip) -> Unit,
    ): RecyclerView.ViewHolder(chipsUnify) {

        init {
            chipsUnify.chipSize = ChipsUnify.SIZE_MEDIUM
        }

        fun bind(queryChip: QueryChip) {
            chipsUnify.chipText = queryChip.query
            chipsUnify.chipType = queryChip.chipType()
            chipsUnify.setOnClickListener {
                onChipSelected(queryChip)
            }
        }

        private fun QueryChip.chipType() =
            if (isSelected) ChipsUnify.TYPE_SELECTED
            else ChipsUnify.TYPE_NORMAL
    }

    private data class QueryChip(val query: String, val isSelected: Boolean = false)

    companion object {
        const val TAG = "MPS_QUERY_BOTTOMSHEET"
        private const val ARGS_QUERY_LIST = "QUERY_LIST"

        fun create(queryList: List<String>, listener: Listener) = MPSQueryBottomSheet().also {
            it.arguments = mpsQueryBottomSheetArgs(queryList)
            it.listener = listener
        }

        private fun mpsQueryBottomSheetArgs(queryList: List<String>) =
            Bundle().apply {
                putStringArrayList(ARGS_QUERY_LIST, ArrayList(queryList))
            }
    }

    interface Listener {
        fun onDismiss()
    }
}
