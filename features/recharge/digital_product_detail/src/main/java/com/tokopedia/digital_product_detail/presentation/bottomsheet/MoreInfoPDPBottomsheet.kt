package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.BottomSheetMoreInfoBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalMoreInfoAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MoreInfoPDPBottomsheet(
    private val listInfo: List<String>
): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
        clearContentPadding = true
    }

    private var binding by autoClearedNullable<BottomSheetMoreInfoBinding>()

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
        binding = BottomSheetMoreInfoBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            rvMoreInfo.run {
                val adapterMoreInfo = DigitalMoreInfoAdapter()
                val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                layoutManager = linearLayoutManager
                adapter = adapterMoreInfo
                adapterMoreInfo.setListInfo(listInfo)
            }
        }
        setTitle(getString(R.string.bottom_sheet_more_info))
        setChild(binding?.root)
    }
}