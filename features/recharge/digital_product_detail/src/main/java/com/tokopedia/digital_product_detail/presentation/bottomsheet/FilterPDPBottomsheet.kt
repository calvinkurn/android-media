package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalFilterAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class FilterPDPBottomsheet(private val title: String, private val action:String, private val filterTagComponents: List<TelcoFilterTagComponent>): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
    }

    private var binding by autoClearedNullable<BottomSheetFilterBinding>()

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
        binding = BottomSheetFilterBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            val adapterFilter = DigitalFilterAdapter()
            adapterFilter.setChipList(filterTagComponents)
            rvPdpFilter.adapter = adapterFilter
            rvPdpFilter.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        setTitle(title)
        setAction(action, {

        })
        setChild(binding?.root)
    }
}