package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    private lateinit var initialFilterTagComponents: List<TelcoFilterTagComponent>
    private lateinit var listener: FilterBottomSheetListener

    fun setTitleAndAction(titleBottomSheet: String, action: String) {
        this.titleBottomSheet = titleBottomSheet
        this.action = action
    }

    fun setFilterTagDataComponent(initialFilterTagComponents: List<TelcoFilterTagComponent>) {
        this.initialFilterTagComponents = initialFilterTagComponents
    }

    fun setListener(listener: FilterBottomSheetListener) {
        this.listener = listener
    }

    var filterTagComponents = mutableListOf<TelcoFilterTagComponent>()

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
                filterTagComponents = manager.get(SAVED_FILTER_TAG_COMPONENT, type)
                    ?: mutableListOf()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFilterTagData()
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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is AllFilterPDPBottomsheet) {
            childFragment.setListener(this)
        }
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false

        binding = BottomSheetFilterBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            adapterFilter.setChipList(filterTagComponents)
            rvPdpFilter.adapter = adapterFilter
            rvPdpFilter.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            btnPdpFilter.setOnClickListener {
                dismiss()
            }
        }
        if (::titleBottomSheet.isInitialized) setTitle(titleBottomSheet)
        if (::action.isInitialized) {
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
        if (::listener.isInitialized) {
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
        filterTagComponents.filter {
            it.paramName.equals(tagComponent.paramName)
        }.first().filterTagDataCollections.get(position).run {
            isSelected = !isSelected
        }
    }

    override fun onChipActiveTracked(element: FilterTagDataCollection) {
        if (::listener.isInitialized) {
            listener.onChipClicked(element.value)
        }
    }

    override fun onCheckBoxAllFilterClicked(tagComponent: TelcoFilterTagComponent, position: Int) {
        val filterComponentChanged = filterTagComponents.toMutableList()
        filterComponentChanged[position] = tagComponent
        filterTagComponents = filterComponentChanged
        adapterFilter.setChipList(filterTagComponents)
    }

    override fun onCheckBoxAllFilterActiveClicked(element: FilterTagDataCollection) {
        if (::listener.isInitialized) {
            listener.onChipClicked(element.value)
        }
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

    private fun setFilterTagData() {
        if (::initialFilterTagComponents.isInitialized) {
            initialFilterTagComponents.forEach {
                val dataCollections = mutableListOf<FilterTagDataCollection>()
                it.filterTagDataCollections.forEach {
                    dataCollections.add(FilterTagDataCollection(it.key, it.value, it.isSelected))
                }
                val filterTagComponent =
                    TelcoFilterTagComponent(it.name, it.text, it.paramName, dataCollections)
                filterTagComponents.add(filterTagComponent)
            }
        }
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