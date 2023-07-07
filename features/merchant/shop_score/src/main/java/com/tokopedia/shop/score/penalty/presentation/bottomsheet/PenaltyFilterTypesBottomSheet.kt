package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetFilterPenaltyTypesBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyTypesBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyTypesAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyTypesAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.filtertypes.ItemPenaltyFilterTypesChecklistViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesChecklistUiModel
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.PenaltyTypesUiModelWrapper

class PenaltyFilterTypesBottomSheet :
    BaseBottomSheetShopScore<BottomsheetFilterPenaltyTypesBinding>(),
    ItemPenaltyFilterTypesChecklistViewHolder.Listener {

    private val adapterTypeFactory by lazy {
        FilterPenaltyTypesAdapterFactory(this)
    }

    private val penaltyTypesAdapter by lazy {
        FilterPenaltyTypesAdapter(adapterTypeFactory)
    }

    private var selectedFilterList = mutableSetOf<Int>()
    private var isAnyFilterSelected = false
    private var listener: FilterPenaltyTypesBottomSheetListener? = null

    override fun getLayoutResId(): Int = R.layout.bottomsheet_filter_penalty_types

    override fun bind(view: View) = BottomsheetFilterPenaltyTypesBinding.bind(view)

    override fun getTitleBottomSheet(): String =
        getString(R.string.title_penalty_types)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PENALTY_FILTER_TYPES_TAG)
            }
        }
    }

    override fun onChecklistClicked(filterId: Int, isSelected: Boolean) {
        selectedFilterList = penaltyTypesAdapter.getSelectedFilter().toMutableSet()
        isAnyFilterSelected = selectedFilterList.size > Int.ZERO
        setBottomSheetAction()
        setApplyButton()
    }

    fun setListener(listener: FilterPenaltyTypesBottomSheetListener) {
        this.listener = listener
    }

    private fun setupView() {
        setupButton()
        setupRecyclerView()
        setAdapterData()
        setupAction()
    }

    private fun setupButton() {
        binding?.btnPenaltyFilterTypes?.setOnClickListener {
            listener?.onFilterSaved(selectedFilterList.toList())
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.rvPenaltyFilterTypes?.run {
            adapter = penaltyTypesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setAdapterData() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID_PENALTY_TYPES)
            )
        }
        val cacheResult =
            cacheManager?.get(KEY_FILTER_PENALTY_TYPES, PenaltyTypesUiModelWrapper::class.java)
                ?: PenaltyTypesUiModelWrapper()
        val penaltyTypes = mapToUiModels(cacheResult.chipsList)

        penaltyTypesAdapter.updateData(penaltyTypes)
    }

    private fun setupAction() {
        selectedFilterList = penaltyTypesAdapter.getSelectedFilter().toMutableSet()
        isAnyFilterSelected = selectedFilterList.size > Int.ZERO
        setBottomSheetAction()
    }

    private fun setApplyButton() {
        binding?.btnPenaltyFilterTypes?.show()
    }

    private fun setBottomSheetAction() {
        val resetText = getString(R.string.reset_filter_penalty)
        setAction(resetText) {
            resetAllFilters()
        }
    }

    private fun resetAllFilters() {
        isAnyFilterSelected = false
        selectedFilterList.clear()
        penaltyTypesAdapter.resetData()
        clearAction()
    }

    private fun mapToUiModels(filterList: List<ChipsFilterPenaltyUiModel>): List<ItemPenaltyFilterTypesChecklistUiModel> {
        return filterList.map {
            ItemPenaltyFilterTypesChecklistUiModel(
                isSelected = it.isSelected,
                title = it.title,
                filterId = it.value
            )
        }
    }

    companion object {
        const val PENALTY_FILTER_TYPES_TAG = "PenaltyFilterTypesBottomSheet"
        const val KEY_FILTER_PENALTY_TYPES = "key_filter_penalty_types"
        const val KEY_CACHE_MANAGER_ID_PENALTY_TYPES = "key_cache_manager_id_penalty_types"

        fun createInstance(cacheManagerId: String): PenaltyFilterTypesBottomSheet {
            val args = Bundle().apply {
                putString(KEY_CACHE_MANAGER_ID_PENALTY_TYPES, cacheManagerId)
            }
            return PenaltyFilterTypesBottomSheet().apply {
                arguments = args
            }
        }
    }

}
