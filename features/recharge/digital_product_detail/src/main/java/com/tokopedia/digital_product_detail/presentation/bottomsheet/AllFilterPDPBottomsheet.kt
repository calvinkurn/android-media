package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetAllFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalAllFilterAdapter
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPFilterAllViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AllFilterPDPBottomsheet : BottomSheetUnify(), DigitalPDPFilterAllViewHolder.CheckBoxListener {

    private lateinit var titleBottomSheet: String
    private var adapterPosition: Int? = null
    private lateinit var filterTagComponent: TelcoFilterTagComponent
    private lateinit var checkBoxFilterListener: OnCheckBoxAllFilterListener

    private var binding by autoClearedNullable<BottomSheetAllFilterBinding>()
    private var adapterAllFilter = DigitalAllFilterAdapter(this)

    fun setTitleAndAdapterPosition(titleBottomSheet: String, adapterPosition: Int) {
        this.titleBottomSheet = titleBottomSheet
        this.adapterPosition = adapterPosition
    }

    fun setFilterTagComponent(filterTagComponent: TelcoFilterTagComponent) {
        this.filterTagComponent = filterTagComponent
    }

    fun setListener(checkBoxFilterListener: OnCheckBoxAllFilterListener) {
        this.checkBoxFilterListener = checkBoxFilterListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )
                titleBottomSheet = manager.getString(SAVED_FILTER_TITLE) ?: ""
                adapterPosition = manager.get(SAVED_FILTER_ADAPTER_POSITION, Int::class.java) ?: 0
                filterTagComponent =
                    manager.get(SAVED_FILTER_TAG_COMPONENT, TelcoFilterTagComponent::class.java)
                        ?: TelcoFilterTagComponent()
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

    override fun onCheckBoxClicked(
        tagComponent: TelcoFilterTagComponent,
        element: FilterTagDataCollection,
        position: Int
    ) {
        if (::filterTagComponent.isInitialized && adapterPosition != null) {
            filterTagComponent.filterTagDataCollections.get(position).run {
                isSelected = !isSelected
            }
            adapterPosition?.let {
                if (::checkBoxFilterListener.isInitialized) checkBoxFilterListener.onCheckBoxAllFilterClicked(filterTagComponent, it)
            }
        }
    }

    override fun onCheckBoxClickedActive(element: FilterTagDataCollection) {
        if (::checkBoxFilterListener.isInitialized) checkBoxFilterListener.onCheckBoxAllFilterActiveClicked(element)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_FILTER_TAG_COMPONENT, filterTagComponent)
                it.put(SAVED_FILTER_ADAPTER_POSITION, adapterPosition)
                it.put(SAVED_FILTER_TITLE, titleBottomSheet)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
        binding = BottomSheetAllFilterBinding.inflate(LayoutInflater.from(context))
        binding?.let {
            if (::filterTagComponent.isInitialized) {
                adapterAllFilter.setCheckBoxList(filterTagComponent)
                it.rvPdpFilterAll.run {
                    setHasFixedSize(true)
                    val linearLayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    val dividerItemDecoration =
                        DividerItemDecoration(context, linearLayoutManager.orientation).apply {
                            setUsePaddingLeft(false)
                        }
                    layoutManager = linearLayoutManager
                    adapter = adapterAllFilter
                    addItemDecoration(dividerItemDecoration)
                }
            }
        }

        if (::titleBottomSheet.isInitialized) setTitle(titleBottomSheet)
        setChild(binding?.root)
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_FILTER_TAG_COMPONENT = "SAVED_FILTER_TAG_COMPONENT"
        private const val SAVED_FILTER_TITLE = "SAVED_FILTER_TITLE"
        private const val SAVED_FILTER_ADAPTER_POSITION = "SAVED_FILTER_ADAPTER_POSITION"

        fun getInstance(): AllFilterPDPBottomsheet = AllFilterPDPBottomsheet()
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