package com.tkpd.atc_variant.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tkpd.atc_variant.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantBottomSheet : BottomSheetUnify() {

    private var listener: AtcVariantBottomSheetListener? = null

    fun show(fragmentManager: FragmentManager, tag: String, listener: AtcVariantBottomSheetListener) {
        this.listener = listener
        show(fragmentManager, tag)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        isDragable = true
        isHideable = true
        clearContentPadding = true
        setTitle(context?.getString(R.string.title_bottomsheet_atc_variant) ?: "")

        setOnDismissListener {
            listener?.onBottomSheetDismiss()
        }

        val view = View.inflate(context, R.layout.bottomsheet_atc_variant, null)
        setChild(view)
    }

}

interface AtcVariantBottomSheetListener {
    fun onBottomSheetDismiss()
}