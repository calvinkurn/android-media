package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx

class ToolTipInfoBottomSheet : BottomSheetUnify() {

    private lateinit var infoTitle: String
    private lateinit var infoDescription: String

    private fun initInjector() {
        activity?.let {
            DaggerThankYouPageComponent.builder()
                .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        } ?: run {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        initInjector()
        initUI()
    }

    private fun getData() {
        arguments?.let {
            if (it.containsKey(TOOLTIP_INFO_TITLE)) {
                infoTitle = it.getString(TOOLTIP_INFO_TITLE) ?: ""
            }
            if (it.containsKey(TOOLTIP_INFO_DESCRIPTION)) {
                infoDescription = it.getString(TOOLTIP_INFO_DESCRIPTION) ?: ""
            }
        }
    }

    private fun initUI() {
        val childView = LayoutInflater.from(context).inflate(
            R.layout.thank_bottomsheet_tooltip_info,
            null, false
        )
        setChild(childView)
        setTitle(infoTitle)
        childView?.apply {
            findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tvDescription).text =
                infoDescription
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetWrapper.setPadding(
            MARGIN_0,
            bottomSheetWrapper.paddingTop,
            MARGIN_0,
            bottomSheetWrapper.paddingBottom
        )
        bottomSheetAction.setMargin(marginRight = MARGIN_16.toPx())
        bottomSheetClose.setMargin(
            marginLeft = MARGIN_16.toPx(),
            marginTop = MARGIN_4.toPx(),
            marginRight = MARGIN_12.toPx()
        )
        isDragable = true
        isHideable = true
        customPeekHeight = getScreenHeight().toDp()
    }

    companion object {
        private const val TOOLTIP_INFO_TITLE = "infoTitle"
        private const val TOOLTIP_INFO_DESCRIPTION = "infoDescription"
        private const val TAG_TOOLTIP_INFO_BOTTOM_SHEET = "tag_tooltip_info_bottom_sheet"

        private const val MARGIN_16 = 16
        private const val MARGIN_4 = 16
        private const val MARGIN_12 = 16
        private const val MARGIN_0 = 0

        fun openTooltipInfoBottomSheet(
            activity: FragmentActivity?,
            infoTitle: String?,
            infoDescription: String?
        ) {
            activity?.apply {
                val invoiceBottomSheet = getToolTipInfoFragment(infoTitle, infoDescription)
                invoiceBottomSheet.show(supportFragmentManager, TAG_TOOLTIP_INFO_BOTTOM_SHEET)
            }
        }

        private fun getToolTipInfoFragment(infoTitle: String?, infoDescription: String?)
                : ToolTipInfoBottomSheet = ToolTipInfoBottomSheet().apply {
            val bundle = Bundle()
            bundle.putString(TOOLTIP_INFO_TITLE, infoTitle)
            bundle.putString(TOOLTIP_INFO_DESCRIPTION, infoDescription)
            arguments = bundle
        }
    }
}
