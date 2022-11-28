package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VoucherThreeDotsBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var entryPoint: MVCBottomSheetType = MVCBottomSheetType.UpcomingEntryPoint

    private var binding by autoClearedNullable<SmvcBottomsheetThreeDotsMenuBinding>()

    init {
        isFullpage = false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetThreeDotsMenuBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.getString(R.string.voucher_three_bots_title) ?: "")
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = VoucherThreeBotsBottomSheetAdapter(context, entryPoint)
        when(entryPoint) {
            is MVCBottomSheetType.UpcomingEntryPoint -> adapter.setUpcomingOptionsList()
            is MVCBottomSheetType.OngoingEntryPoint -> adapter.setOngoingOptionsList()
            is MVCBottomSheetType.CancelledEntryPoint -> adapter.setCancelledOptionsList()
        }

        binding?.recyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            entryPoint: MVCBottomSheetType
        ): VoucherThreeDotsBottomSheet {
            return VoucherThreeDotsBottomSheet().apply {
                this.context = context
                this.entryPoint = entryPoint
            }
        }

    }

}

sealed class MVCBottomSheetType {
    object UpcomingEntryPoint : MVCBottomSheetType()
    object OngoingEntryPoint: MVCBottomSheetType()
    object CancelledEntryPoint: MVCBottomSheetType()
}
