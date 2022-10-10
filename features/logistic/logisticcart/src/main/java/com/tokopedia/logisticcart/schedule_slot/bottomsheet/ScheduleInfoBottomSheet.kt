package com.tokopedia.logisticcart.schedule_slot.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticcart.databinding.BottomsheetScheduleShippingInfoBinding
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetInfoUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

class ScheduleInfoBottomSheet(private val data: BottomSheetInfoUiModel) : BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        clearContentPadding = true
        val view = BottomsheetScheduleShippingInfoBinding.inflate(layoutInflater).apply {
            tvScheduleShippingInfo.text = data.description
            // todo
            imgScheduleShippingInfo.loadImage("")
        }
        setTitle(data.title)
        setChild(view.root)
    }

    companion object {

        fun show(fm: FragmentManager, data: BottomSheetInfoUiModel): ScheduleInfoBottomSheet {
            val bottomsheet = ScheduleInfoBottomSheet(data)
            bottomsheet.show(fm, "")
            return bottomsheet
        }
    }
}
