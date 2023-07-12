package com.tokopedia.shop.score.penalty.presentation.old.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetFilterPenaltyBinding
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.old.adapter.filter.FilterPenaltyAdapterOld
import com.tokopedia.shop.score.penalty.presentation.old.model.FilterTypePenaltyUiModelWrapperOld
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld
import com.tokopedia.shop.score.penalty.presentation.viewmodel.old.ShopPenaltyViewModelOld
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PenaltyFilterBottomSheetOld : BaseBottomSheetShopScore<BottomsheetFilterPenaltyBinding>(),
    FilterPenaltyBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelShopPenalty by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPenaltyViewModelOld::class.java)
    }

    private var isApplyFilter = false

    private val filterPenaltyAdapterTypeFactory by lazy { FilterPenaltyAdapterFactory(this) }
    private val filterPenaltyAdapter by lazy { FilterPenaltyAdapterOld(filterPenaltyAdapterTypeFactory) }

    private var penaltyFilterFinishListener: PenaltyFilterFinishListener? = null

    override fun bind(view: View) = BottomsheetFilterPenaltyBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottomsheet_filter_penalty

    override fun getTitleBottomSheet(): String =
        getString(R.string.title_penalty_filter_bottom_sheet)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PENALTY_FILTER_BOTTOM_SHEET_TAG)
            }
        }
    }

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java)?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showKnob = true
        isDragable = true
        isHideable = true
        showCloseIcon = false
        clearContentPadding = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PenaltyFilterDialogStyle)
        customPeekHeight = (getScreenHeight() / 2).toDp()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataCacheFromManager()
        setupRecyclerView()
        observePenaltyFilter()
        observeUpdateFilterSelected()
        observeResetFilter()
        clickBtnApplied()
        clickBtnReset()
    }

    override fun onDestroy() {
        removeObservers(viewModelShopPenalty.penaltyPageData)
        removeObservers(viewModelShopPenalty.filterPenaltyData)
        removeObservers(viewModelShopPenalty.updateSortSelectedPeriod)
        removeObservers(viewModelShopPenalty.resetFilterResult)
        removeObservers(viewModelShopPenalty.updateFilterSelected)
        super.onDestroy()
    }

    override fun onChipsFilterItemClick(
        nameFilter: String,
        chipType: String,
        chipTitle: String,
        position: Int
    ) {
        when (nameFilter) {
            ShopScoreConstant.TITLE_SORT -> {
                viewModelShopPenalty.updateFilterSelected(nameFilter, chipType, position)
            }
            ShopScoreConstant.TITLE_TYPE_PENALTY -> {
                viewModelShopPenalty.updateFilterSelected(nameFilter, chipType, position)
            }
        }
    }

    override fun onSeeAllButtonClicked(uiModel: PenaltyFilterUiModel) {
        // No-op
    }

    private fun getDataCacheFromManager() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID_PENALTY_FILTER)
            )
        }
        val filterTypePenalty =
            cacheManager?.get(KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapperOld::class.java)
                ?: FilterTypePenaltyUiModelWrapperOld()
        viewModelShopPenalty.getFilterPenalty(filterTypePenalty.penaltyFilterList)
    }

    private fun clickBtnApplied() {
        binding?.btnShowPenalty?.setOnClickListener {
            isApplyFilter = true
            penaltyFilterFinishListener?.onClickFilterApplied(viewModelShopPenalty.getPenaltyFilterUiModelList())
            dismiss()
        }
    }

    private fun observePenaltyFilter() {
        observe(viewModelShopPenalty.filterPenaltyData) {
            when (it) {
                is Success -> {
                    filterPenaltyAdapter.updateData(it.data)
                    showHideBottomSheetReset()
                }
                else -> {
                }
            }
        }
    }

    private fun observeUpdateFilterSelected() = observe(viewModelShopPenalty.updateFilterSelected) {
        when (it) {
            is Success -> {
                filterPenaltyAdapter.updateFilterSelected(it.data.first, it.data.second)
                showHideBottomSheetReset()
            }
            is Fail -> {
            }
        }
    }

    private fun observeResetFilter() = observe(viewModelShopPenalty.resetFilterResult) {
        when (it) {
            is Success -> {
                filterPenaltyAdapter.resetFilterSelected(it.data)
                showHideBottomSheetReset()
            }
            else -> {
            }
        }
    }

    private fun clickBtnReset() {
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.reset_filter_penalty)
        }
        bottomSheetAction.setOnClickListener {
            viewModelShopPenalty.resetFilterSelected()
        }
    }

    private fun showHideBottomSheetReset() {
        if (checkIsSelected()) {
            bottomSheetAction.show()
        } else {
            bottomSheetAction.hide()
        }
    }

    private fun checkIsSelected(): Boolean {
        viewModelShopPenalty.getPenaltyFilterUiModelList().forEach {
            it.chipsFilterList.forEach { chips ->
                if (chips.isSelected) {
                    return true
                }
            }
        }
        return false
    }

    private fun setupRecyclerView() {
        binding?.rvPenaltyFilterBottomSheet?.run {
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = filterPenaltyAdapter
        }
    }

    fun setPenaltyFilterFinishListener(penaltyFilterFinishListener: PenaltyFilterFinishListener) {
        this.penaltyFilterFinishListener = penaltyFilterFinishListener
    }

    interface PenaltyFilterFinishListener {
        fun onClickFilterApplied(penaltyFilterUiModelOldList: List<PenaltyFilterUiModelOld>)
    }

    companion object {
        const val PENALTY_FILTER_BOTTOM_SHEET_TAG = "PenaltyFilterBottomSheetTag"
        const val KEY_FILTER_TYPE_PENALTY = "key_filter_type_penalty"
        const val KEY_CACHE_MANAGER_ID_PENALTY_FILTER = "key_cache_manager_id_penalty_filter"

        fun newInstance(cacheManagerId: String): PenaltyFilterBottomSheetOld {
            val penaltyFilterBottomSheetOld = PenaltyFilterBottomSheetOld()
            val args = Bundle()
            args.putString(KEY_CACHE_MANAGER_ID_PENALTY_FILTER, cacheManagerId)
            penaltyFilterBottomSheetOld.arguments = args
            return penaltyFilterBottomSheetOld
        }
    }
}
