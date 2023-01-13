package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.buyerorderdetail.databinding.PartialOrderFulfillmentBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PartialOrderFulfillmentBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<PartialOrderFulfillmentBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PartialOrderFulfillmentBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(
            getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_bottomsheet_title)
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
