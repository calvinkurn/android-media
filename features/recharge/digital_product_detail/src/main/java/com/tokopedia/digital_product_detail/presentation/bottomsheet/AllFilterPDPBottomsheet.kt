package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetAllFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalAllFilterAdapter
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPFilterAllViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AllFilterPDPBottomsheet(
    private val title: String,
    private val adapterPosition: Int,
    private var filterTagComponent: TelcoFilterTagComponent,
    private var checkBoxFilterListener: OnCheckBoxAllFilterListener
) : BottomSheetUnify(), DigitalPDPFilterAllViewHolder.CheckBoxListener {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
    }

    private var binding by autoClearedNullable<BottomSheetAllFilterBinding>()
    private var adapterAllFilter = DigitalAllFilterAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
    }

    override fun onCheckBoxClicked(
        tagComponent: TelcoFilterTagComponent,
        element: FilterTagDataCollection,
        position: Int
    ) {
        filterTagComponent.filterTagDataCollections.get(position).run {
            isSelected = !isSelected
        }
        checkBoxFilterListener.onCheckBoxAllFilterClicked(filterTagComponent, adapterPosition)
    }

    override fun onCheckBoxClickedActive(element: FilterTagDataCollection) {
        checkBoxFilterListener.onCheckBoxAllFilterActiveClicked(element)
    }

    private fun initView() {
        binding = BottomSheetAllFilterBinding.inflate(LayoutInflater.from(context))
        binding?.let {
            adapterAllFilter.setCheckBoxList(filterTagComponent)
            it.rvPdpFilterAll.run {
                setHasFixedSize(true)
                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                val dividerItemDecoration =
                    DividerItemDecoration(context, linearLayoutManager.orientation).apply {
                        setUsePaddingLeft(false)
                    }
                layoutManager = linearLayoutManager
                adapter = adapterAllFilter
                addItemDecoration(dividerItemDecoration)
            }
        }

        setTitle(title)
        setChild(binding?.root)
    }

    interface OnCheckBoxAllFilterListener {
        fun onCheckBoxAllFilterClicked(
            tagComponent: TelcoFilterTagComponent,
            position: Int
        )

        fun onCheckBoxAllFilterActiveClicked(
            element: FilterTagDataCollection
        )
    }
}