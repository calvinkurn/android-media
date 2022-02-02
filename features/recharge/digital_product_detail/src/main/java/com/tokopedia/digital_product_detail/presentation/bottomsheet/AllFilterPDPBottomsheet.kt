package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetAllFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalAllFilterAdapter
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPFilterAllViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AllFilterPDPBottomsheet(private val title:String,
                              private var filterTagComponent : TelcoFilterTagComponent,
                              private val checkBoxListener: DigitalPDPFilterAllViewHolder.CheckBoxListener) : BottomSheetUnify(){

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
    }

    private var binding by autoClearedNullable<BottomSheetAllFilterBinding>()
    private var adapterAllFilter = DigitalAllFilterAdapter(checkBoxListener)

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

    private fun initView(){
        binding = BottomSheetAllFilterBinding.inflate(LayoutInflater.from(context))
        binding?.let {
            adapterAllFilter.setCheckBoxList(filterTagComponent)
            it.rvPdpFilterAll.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = adapterAllFilter
            }
        }

        setTitle(title)
        setChild(binding?.root)
    }
}