package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.databinding.TopadsCreatePotentialPerformanceSheetBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemState
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemTag
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupAdapter
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify

class CreatePotentialPerformanceSheet : BottomSheetUnify() {

    private var searchPrediction: Int = 0
    private var recomPrediction: Int = 0
    private var binding: TopadsCreatePotentialPerformanceSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsCreatePotentialPerformanceSheetBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = true
        setChild(viewBinding.root)
        setTitle("Potensi tampil")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val productListAdapter = CreateEditAdGroupAdapter(CreateEditAdGroupTypeFactory())
        binding?.potentialPerformanceRv?.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        productListAdapter.updateList(getPotentialData())
    }

    private fun getPotentialData(): List<Visitable<*>> {
        return mutableListOf(CreateEditAdGroupItemAdsPotentialUiModel(
            CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE,
            "",
            "Perkiraan seberapa sering iklanmu tampil berdasarkan produk yang dipilih, biaya grup iklan, dan anggaran harian.",
            "",
            mutableListOf(
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Di Pencarian", searchPrediction.toString()
                ),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Di Rekomendasi", recomPrediction.toString()
                ),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Total Tampil ", (searchPrediction + recomPrediction).toString()
                )),
            state = CreateEditAdGroupItemState.LOADED
        ))
    }

    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "PRODUCT_POTENTIAL_PERFORMANCE_BOTTOM_SHEET_TAG"
        fun newInstance(searchPrediction: Int, recomPrediction: Int): CreatePotentialPerformanceSheet =
            CreatePotentialPerformanceSheet().apply {
                this.searchPrediction = searchPrediction
                this.recomPrediction = recomPrediction
            }
    }
}
