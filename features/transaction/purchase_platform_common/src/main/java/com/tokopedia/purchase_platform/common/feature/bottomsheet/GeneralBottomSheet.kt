package com.tokopedia.purchase_platform.common.feature.bottomsheet

import android.content.Context
import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class GeneralBottomSheet {

    private var title: String = ""
    private var desc: String = ""
    private var buttonText: String = ""
    private var iconRes: Int = 0
    private var buttonOnClickListener: View.OnClickListener = View.OnClickListener {  }

    var bottomSheet: BottomSheetUnify? = null

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun setButtonText(buttonText: String) {
        this.buttonText = buttonText
    }

    fun setButtonOnClickListener(onClickListener: View.OnClickListener) {
        this.buttonOnClickListener = onClickListener
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        this.iconRes = iconRes
    }

    fun show(context: Context, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            showHeader = false
            showKnob = true
            isDragable = true
            overlayClickDismiss = true
            clearContentPadding = true
            val childView = View.inflate(context, R.layout.layout_general_bottom_sheet, null)
            setupChildView(childView)
            setChild(childView)
        }
        bottomSheet?.show(fragmentManager, "")
    }

    private fun setupChildView(childView: View) {
        childView.findViewById<Typography>(R.id.tv_title).text = title
        childView.findViewById<Typography>(R.id.tv_desc).text = desc
        childView.findViewById<ImageUnify>(R.id.iv_icon).loadImageDrawable(iconRes)
        val button = childView.findViewById<UnifyButton>(R.id.btn_action)
        button.text = buttonText
        button.setOnClickListener(buttonOnClickListener)
    }
}