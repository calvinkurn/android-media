package com.tokopedia.deals.brand_detail.ui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.deals.databinding.BottomSheetDealsBrandDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class DealsBrandDetailBottomSheet {

    fun showBottomSheet(context: Context,
                        title: String,
                        desc: String,
                        fragmentManager: FragmentManager){
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomSheetDealsBrandDetailBinding.inflate(LayoutInflater.from(context))
        with(binding){
            tgDescDetailBrand.text = desc
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(title)
        bottomSheet.show(fragmentManager, "")
    }
}