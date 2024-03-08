package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recharge_pdp_emoney.databinding.BottomSheetsImageEmoneyListBinding
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPDPImagesListBottomSheetAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyImageModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.recharge_pdp_emoney.R as recharge_pdp_emoneyR

class EmoneyListImageBottomSheets: BottomSheetUnify() {

    init {
        clearContentPadding = true
    }

    fun updateData(listImages: List<EmoneyImageModel>) {
        adapter.renderList(listImages)
    }

    private val adapter = EmoneyPDPImagesListBottomSheetAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = BottomSheetsImageEmoneyListBinding.inflate(LayoutInflater.from(context))
        setChild(view.root)
        initView(view)
    }

    private fun initView(binding: BottomSheetsImageEmoneyListBinding) {
        with(binding) {
            setTitle(getString(recharge_pdp_emoneyR.string.recharge_pdp_emoney_list_title_bottomsheet))
            rvImageListBottomsheet.layoutManager = LinearLayoutManager(context)
            rvImageListBottomsheet.adapter = adapter
        }
    }

    companion object {
        fun newInstance(): EmoneyListImageBottomSheets {
            return EmoneyListImageBottomSheets()
        }
    }
}
