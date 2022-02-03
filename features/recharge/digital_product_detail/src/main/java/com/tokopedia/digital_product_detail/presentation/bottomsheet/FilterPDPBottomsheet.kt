package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalFilterAdapter
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipFilterViewHolder
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipListFilterViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class FilterPDPBottomsheet(
    private val title: String, private val action: String,
    private val filterTagComponents: List<TelcoFilterTagComponent>,
    private val listener: FilterBottomSheetListener
) : BottomSheetUnify(),
    DigitalPDPChipFilterViewHolder.ChipListener,
    DigitalPDPChipListFilterViewHolder.ListFilterListener,
    AllFilterPDPBottomsheet.OnCheckBoxAllFilterListener {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
    }

    private var binding by autoClearedNullable<BottomSheetFilterBinding>()
    private val adapterFilter =
        DigitalFilterAdapter(this@FilterPDPBottomsheet, this@FilterPDPBottomsheet)

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

    private fun initView() {
        binding = BottomSheetFilterBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            adapterFilter.setChipList(filterTagComponents)
            rvPdpFilter.adapter = adapterFilter
            rvPdpFilter.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            btnPdpFilter.setOnClickListener {
                dismiss()
            }
        }

        setTitle(title)
        setAction(action, {
            resetFilter()
        })
        setChild(binding?.root)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onSaveFilter()
        super.onDismiss(dialog)
    }

    private fun onSaveFilter() {
        listener.onClickSaveFilter(filterTagComponents, initialSelectedCounter(filterTagComponents))
    }

    override fun onChipClicked(
        tagComponent: TelcoFilterTagComponent,
        element: FilterTagDataCollection,
        position: Int
    ) {
        filterTagComponents.filter {
            it.paramName.equals(tagComponent.paramName)
        }.first().filterTagDataCollections.get(position).run {
            isSelected = !isSelected
            listener.onChipClicked(value)
        }
    }

    override fun onCheckBoxAllFilterClicked(tagComponent: TelcoFilterTagComponent, position: Int) {
        filterTagComponents.toMutableList()[position] = tagComponent
        filterTagComponents.toList()
        adapterFilter.notifyItemChanged(position)
    }

    override fun onSeeAllClicked(element: TelcoFilterTagComponent, position: Int) {
        fragmentManager?.let {
            AllFilterPDPBottomsheet(element.text, position, element, this).show(it, "")
        }
    }

    private fun resetFilter() {
        filterTagComponents.forEach {
            it.filterTagDataCollections.forEach {
                it.isSelected = false
            }
        }
        adapterFilter.notifyDataSetChanged()
    }

    private fun initialSelectedCounter(filterTagComponents: List<TelcoFilterTagComponent>): Int {
        var initialSelectedCounter = 0
        if (!filterTagComponents.isNullOrEmpty()) {
            filterTagComponents.forEachIndexed { index, tagComponent ->
                if (!index.isZero()) {
                    tagComponent.filterTagDataCollections.forEach {
                        if (it.isSelected) {
                            initialSelectedCounter++
                        }
                    }
                }
            }
        }

        return initialSelectedCounter
    }

    interface FilterBottomSheetListener {
        fun onClickSaveFilter(
            filterTagComponents: List<TelcoFilterTagComponent>,
            initialSelectedCounter: Int
        )

        fun onChipClicked(chipName: String)
    }
}