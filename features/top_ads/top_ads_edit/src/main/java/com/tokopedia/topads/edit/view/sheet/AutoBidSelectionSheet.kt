package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

const val AUTO_BID = "Otomatis"
const val MANUAL_BID = "Manual"
class AutoBidSelectionSheet : BottomSheetUnify() {

    private lateinit var nextBtn: UnifyButton
    private lateinit var otomatisLayout: ConstraintLayout
    private lateinit var manualLayout: ConstraintLayout
    private lateinit var otomatisRadioBtn: RadioButtonUnify
    private lateinit var manualRadioBtn: RadioButtonUnify
    var selectedId = 0
    var onItemClick: ((autoBidState: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, com.tokopedia.topads.common.R.layout.auto_bid_selection_layout, null)
        setChild(contentView)
        setTitle(getString(com.tokopedia.topads.common.R.string.autobid_list_step))

        nextBtn = contentView.findViewById(com.tokopedia.topads.common.R.id.save_autobid)
        otomatisRadioBtn = contentView.findViewById(com.tokopedia.topads.common.R.id.radiobtn_otomatis)
        manualRadioBtn = contentView.findViewById(com.tokopedia.topads.common.R.id.radiobtn_manual)
        otomatisLayout = contentView.findViewById(com.tokopedia.topads.common.R.id.atur_otomatis)
        manualLayout = contentView.findViewById(com.tokopedia.topads.common.R.id.atur_manual)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(selectedId == 0) {
            otomatisRadioBtn.isChecked = true
            manualRadioBtn.isChecked = false
        } else {
            manualRadioBtn.isChecked = true
            otomatisRadioBtn.isChecked = false
        }
        handleClick()
    }

    private fun handleClick() {
        manualLayout?.setOnClickListener {
            manualRadioBtn?.isChecked = true
            otomatisRadioBtn?.isChecked = false
            onItemClick?.invoke("")
            dismiss()
        }

        otomatisLayout?.setOnClickListener {
            otomatisRadioBtn?.isChecked = true
            manualRadioBtn?.isChecked = false
            onItemClick?.invoke("auto_bid")
            dismiss()
        }

        manualRadioBtn?.setOnCheckedChangeListener { _, isChecked ->
            manualRadioBtn?.isChecked = true
            otomatisRadioBtn?.isChecked = false
            onItemClick?.invoke("")
            dismiss()
        }
        otomatisRadioBtn?.setOnCheckedChangeListener { _, isChecked ->
            otomatisRadioBtn?.isChecked = true
            manualRadioBtn?.isChecked = false
            onItemClick?.invoke("auto_bid")
            dismiss()
        }
    }

    fun setChecked(current: String) {
        selectedId = if(current == AUTO_BID) {
            0
        } else {
            1
        }
    }


    companion object {

        fun newInstance(): AutoBidSelectionSheet {
            return AutoBidSelectionSheet()
        }
    }
}