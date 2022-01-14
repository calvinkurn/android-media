package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

const val AUTO_BID = "Otomatis"
const val MANUAL_BID = "Manual"
private const val AUTO_BID_STATE = "auto_bid"
private const val CLICK_BID_TYPE_SELECT = "click - mode pengaturan"
private const val MANUAL_LAYOUT_EVENT_LABEL = "mode pengaturan atur manual"
private const val OTOMATIS_LAYOUT_EVENT_LABEL = "mode pengaturan atur otomatis"

class AutoBidSelectionSheet : BottomSheetUnify() {

    private lateinit var nextBtn: UnifyButton
    private lateinit var otomatisLayout: ConstraintLayout
    private lateinit var manualLayout: ConstraintLayout
    private lateinit var otomatisRadioBtn: RadioButtonUnify
    private lateinit var manualRadioBtn: RadioButtonUnify
    var selectedId = 0
    var onItemClick: ((autoBidState: String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(
            context,
            com.tokopedia.topads.common.R.layout.auto_bid_selection_layout,
            null
        )
        setChild(contentView)
        setTitle(getString(com.tokopedia.topads.common.R.string.autobid_list_step))

        contentView.findViewById<Typography>(com.tokopedia.topads.common.R.id.manual_learn_more)
            .hide()
        contentView.findViewById<Typography>(com.tokopedia.topads.common.R.id.otomatis_learn_more)
            .hide()
        contentView.findViewById<Typography>(com.tokopedia.topads.common.R.id.otomatis_tag).hide()
        nextBtn = contentView.findViewById(com.tokopedia.topads.common.R.id.save_autobid)
        otomatisRadioBtn =
            contentView.findViewById(com.tokopedia.topads.common.R.id.radiobtn_otomatis)
        manualRadioBtn = contentView.findViewById(com.tokopedia.topads.common.R.id.radiobtn_manual)
        otomatisLayout = contentView.findViewById(com.tokopedia.topads.common.R.id.atur_otomatis)
        manualLayout = contentView.findViewById(com.tokopedia.topads.common.R.id.atur_manual)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (selectedId == 0) {
            otomatisRadioBtn.isChecked = true
            manualRadioBtn.isChecked = false
        } else {
            manualRadioBtn.isChecked = true
            otomatisRadioBtn.isChecked = false
        }
        handleClick()
    }

    private fun handleClick() {
        manualLayout.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT,
                MANUAL_LAYOUT_EVENT_LABEL
            )
            manualRadioBtn.isChecked = true
            otomatisRadioBtn.isChecked = false
            onItemClick?.invoke("")
            dismiss()
        }

        otomatisLayout.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT,
                OTOMATIS_LAYOUT_EVENT_LABEL
            )
            otomatisRadioBtn.isChecked = true
            manualRadioBtn.isChecked = false
            onItemClick?.invoke(AUTO_BID_STATE)
            dismiss()
        }
    }

    fun setChecked(current: String) {
        selectedId = if (current == AUTO_BID) {
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