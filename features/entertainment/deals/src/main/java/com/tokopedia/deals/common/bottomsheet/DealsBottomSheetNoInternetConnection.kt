package com.tokopedia.deals.common.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.deals.databinding.BottomSheetDealsNoConnectionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class DealsBottomSheetNoInternetConnection {

    fun showErroNoConnection(
            context: Context,
            fragmentManager: FragmentManager,
            listener : DealsOnClickBottomSheetNoConnectionListener
    ){
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomSheetDealsNoConnectionBinding.inflate(LayoutInflater.from(context))
        with(binding){
            geDealsNoConnection.setActionClickListener {
                bottomSheet.dismiss()
            }
        }
        bottomSheet.setOnDismissListener {
            listener.onDismissBottomsheet()
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.show(fragmentManager, "")
    }

    interface DealsOnClickBottomSheetNoConnectionListener{
        fun onDismissBottomsheet()
    }
}