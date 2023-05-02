package com.tokopedia.topads.common.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsToolTipBottomSheet : BottomSheetUnify() {

    private var descriptionTypography: Typography? = null
    private var description: String = ""

    companion object {
        fun newInstance(): TopAdsToolTipBottomSheet {
            return TopAdsToolTipBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView =
            View.inflate(context, R.layout.topads_dash_sheet_info, null)
        setChild(contentView)
        showCloseIcon = true
        descriptionTypography = contentView?.findViewById(R.id.toolTipDescription)
        descriptionTypography?.text = description
    }



    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, "")
    }

    fun setDescription(description: String) {
        this.description = description
    }
}
