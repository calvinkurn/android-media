package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PenaltyFilterBottomSheet: BaseBottomSheetShopScore(), FilterPenaltyBottomSheetListener {

    @Inject
    lateinit var shopPenaltyViewModel: ShopPenaltyViewModel

    private var isApplyFilter = false
    private var rvPenaltyFilter: RecyclerView? = null
    private var btnShowPenalty: UnifyButton? = null

    private val filterPenaltyAdapterTypeFactory by lazy { FilterPenaltyAdapterFactory(this) }
    private val filterPenaltyAdapter by lazy { FilterPenaltyAdapter(filterPenaltyAdapterTypeFactory) }

    private var penaltyFilterList = mutableListOf<PenaltyFilterUiModel>()
    private var penaltyFilterFinishListener: PenaltyFilterFinishListener? = null

    override fun getLayoutResId(): Int = R.layout.bottomsheet_filter_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_filter_bottom_sheet)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        initView(view)
        setupRecyclerView()
        observePenaltyFilter()
        observeUpdateFilterSelected()
        observeResetFilter()
        clickBtnApplied()
        clickBtnReset()
    }

    override fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int) {
        when (nameFilter) {
            ShopScoreConstant.TITLE_SORT -> {
                shopPenaltyViewModel.updateFilterSelected(nameFilter, chipType, position)
            }
            ShopScoreConstant.TITLE_TYPE_PENALTY -> {
                shopPenaltyViewModel.updateFilterManySelected(nameFilter, chipType, chipTitle)
            }
        }
    }

    private fun clickBtnApplied() {
        btnShowPenalty?.setOnClickListener {
            isApplyFilter = true
            penaltyFilterFinishListener?.onClickFilterApplied(shopPenaltyViewModel.getPenaltyFilterUiModelList())
            dismissAllowingStateLoss()
        }
    }

    private fun observePenaltyFilter() {
        observe(shopPenaltyViewModel.filterPenaltyData) {
            when (it) {
                is Success -> {
                    filterPenaltyAdapter.updateData(it.data.updateFilterPenaltyFromSortFilter())
                    showHideBottomSheetReset()
                }
                else -> {}
            }
        }
        shopPenaltyViewModel.getFilterPenalty(penaltyFilterList)
    }

    private fun List<BaseFilterPenaltyPage>.updateFilterPenaltyFromSortFilter(): List<BaseFilterPenaltyPage> {

        val chipsFilterUiModelList = mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>()

        shopPenaltyViewModel.getSortFilterWrapperList().map {
            chipsFilterUiModelList.add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(
                    title = it.sortFilterItem?.title.toString(),
                    isSelected = it.isSelected
            ))
        }

        this.filterIsInstance<PenaltyFilterUiModel>().find {
            it.title == ShopScoreConstant.TITLE_TYPE_PENALTY
        }?.chipsFilerList = chipsFilterUiModelList

        return this
    }

    private fun observeUpdateFilterSelected() = observe(shopPenaltyViewModel.updateFilterSelected) {
        when (it) {
            is Success -> {
                filterPenaltyAdapter.updateFilterSelected(it.data.first, it.data.second)
                showHideBottomSheetReset()
            }
            is Fail -> {}
        }
    }

    private fun observeResetFilter() = observe(shopPenaltyViewModel.resetFilterResult) {
        when (it) {
            is Success -> {
                filterPenaltyAdapter.resetFilterSelected(it.data)
                showHideBottomSheetReset()
            }
            else -> {}
        }
    }

    private fun clickBtnReset() {
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.reset_filter_penalty)
        }
        bottomSheetAction.setOnClickListener {
            shopPenaltyViewModel.resetFilterSelected()
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
        shopPenaltyViewModel.getPenaltyFilterUiModelList().forEach {
            it.chipsFilerList.forEach { chips ->
                if (chips.isSelected) {
                    return true
                }
            }
        }
        return false
    }

    private fun setupRecyclerView() {
        rvPenaltyFilter?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filterPenaltyAdapter
        }
    }

    private fun initView(view: View) {
        rvPenaltyFilter = view.findViewById(R.id.rvPenaltyFilterBottomSheet)
        btnShowPenalty = view.findViewById(R.id.btnShowPenalty)
    }

    fun setPenaltyFilterFinishListener(penaltyFilterFinishListener: PenaltyFilterFinishListener) {
        this.penaltyFilterFinishListener = penaltyFilterFinishListener
    }

    interface PenaltyFilterFinishListener {
        fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>)
    }

    companion object {
        const val PENALTY_FILTER_BOTTOM_SHEET_TAG = "PenaltyFilterBottomSheetTag"

        fun newInstance(): PenaltyFilterBottomSheet {
            return PenaltyFilterBottomSheet()
        }
    }
}