package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalFilterAdapter
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipFilterViewHolder
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipListFilterViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.reflect.Type

class FilterPDPBottomsheet : BottomSheetUnify(),
    DigitalPDPChipFilterViewHolder.ChipListener,
    DigitalPDPChipListFilterViewHolder.ListFilterListener,
    AllFilterPDPBottomsheet.OnCheckBoxAllFilterListener {

    private lateinit var titleBottomSheet: String
    private lateinit var action: String
    private lateinit var filterTagComponents: List<TelcoFilterTagComponent>
    private lateinit var listener: FilterBottomSheetListener

    fun setTitleAndAction(titleBottomSheet: String, action: String){
        this.titleBottomSheet = titleBottomSheet
        this.action = action
    }

    fun setFilterTagData(filterTagComponents: List<TelcoFilterTagComponent>) {
        this.filterTagComponents = filterTagComponents
    }

    fun setListener(listener: FilterBottomSheetListener) {
        this.listener = listener
    }

    private var binding by autoClearedNullable<BottomSheetFilterBinding>()
    private val adapterFilter =
        DigitalFilterAdapter(this@FilterPDPBottomsheet, this@FilterPDPBottomsheet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )
                titleBottomSheet = manager.getString(SAVED_FILTER_TITLE) ?: ""
                action = manager.getString(SAVED_FILTER_ACTION) ?: ""

                val type: Type = object : TypeToken<List<TelcoFilterTagComponent>>() {}.type
                filterTagComponents = manager.get(SAVED_FILTER_TAG_COMPONENT, type) ?: emptyList<TelcoFilterTagComponent>()
            }
        }
    }

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_FILTER_TAG_COMPONENT, filterTagComponents)
                it.put(SAVED_FILTER_ACTION, action)
                it.put(SAVED_FILTER_TITLE, titleBottomSheet)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false

        binding = BottomSheetFilterBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            if (::filterTagComponents.isInitialized) {
                adapterFilter.setChipList(filterTagComponents)
                rvPdpFilter.adapter = adapterFilter
                rvPdpFilter.layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                btnPdpFilter.setOnClickListener {
                    dismiss()
                }
            }
        }
        if (::titleBottomSheet.isInitialized) setTitle(titleBottomSheet)
        if (::action.isInitialized){
            setAction(action, {
                resetFilter()
            })
        }
        setChild(binding?.root)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onSaveFilter()
        super.onDismiss(dialog)
    }

    private fun onSaveFilter() {
        if (::listener.isInitialized && ::filterTagComponents.isInitialized) {
            listener.onClickSaveFilter(
                filterTagComponents,
                initialSelectedCounter(filterTagComponents)
            )
        }
    }

    override fun onChipClicked(
        tagComponent: TelcoFilterTagComponent,
        element: FilterTagDataCollection,
        position: Int
    ) {
        if (::filterTagComponents.isInitialized) {
            filterTagComponents.filter {
                it.paramName.equals(tagComponent.paramName)
            }.first().filterTagDataCollections.get(position).run {
                isSelected = !isSelected
            }
        }
    }

    override fun onChipActiveTracked(element: FilterTagDataCollection) {
        if (::listener.isInitialized) { listener.onChipClicked(element.value) }
    }

    override fun onCheckBoxAllFilterClicked(tagComponent: TelcoFilterTagComponent, position: Int) {
        if (::filterTagComponents.isInitialized) {
            filterTagComponents.toMutableList()[position] = tagComponent
            filterTagComponents.toList()
            adapterFilter.notifyItemChanged(position)
        }
    }

    override fun onCheckBoxAllFilterActiveClicked(element: FilterTagDataCollection) {
        if (::listener.isInitialized) { listener.onChipClicked(element.value) }
    }

    override fun onSeeAllClicked(element: TelcoFilterTagComponent, position: Int) {
        childFragmentManager?.let {
            val allFilterPDPBottomsheet = AllFilterPDPBottomsheet.getInstance()
            allFilterPDPBottomsheet.setTitleAndAdapterPosition(element.text, position)
            allFilterPDPBottomsheet.setFilterTagComponent(element)
            allFilterPDPBottomsheet.setListener(this)
            allFilterPDPBottomsheet.show(it, "")
        }
    }

    private fun resetFilter() {
        if (::filterTagComponents.isInitialized) {
            filterTagComponents.forEach {
                it.filterTagDataCollections.forEach {
                    it.isSelected = false
                }
            }
            adapterFilter.notifyDataSetChanged()
        }
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

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_FILTER_TAG_COMPONENT = "SAVED_FILTER_TAG_COMPONENT"
        private const val SAVED_FILTER_TITLE = "SAVED_FILTER_TITLE"
        private const val SAVED_FILTER_ACTION = "SAVED_FILTER_ACTION"

        fun getInstance(): FilterPDPBottomsheet = FilterPDPBottomsheet()
    }

    interface FilterBottomSheetListener {
        fun onClickSaveFilter(
            filterTagComponents: List<TelcoFilterTagComponent>,
            initialSelectedCounter: Int
        )

        fun onChipClicked(chipName: String)
    }
}