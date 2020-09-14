package com.tokopedia.topads.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.getResDrawable

import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_bottom_sheet_layout_confirmation_manual_ads.view.*

class ManualAdsConfirmationSheet : BottomSheetUnify() {


    var manualClick: (() -> Unit)? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_create_bottom_sheet_layout_confirmation_manual_ads, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.ic_ilustration.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_manual_confirmation))
        setupView(view)
    }

    private fun setupView(view: View) {
        view?.cancel_btn_start_manual_ads.setOnClickListener {
            dismiss()
        }
        view?.btn_start_manual_ads.setOnClickListener {
            manualClick?.invoke()
            dismiss()
        }
    }

    companion object {
        fun newInstance(): ManualAdsConfirmationSheet {
            return ManualAdsConfirmationSheet()
        }
    }
}
