package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tradein.R
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp

class ShoSessionIdBs(val vm: TradeInHomeViewModel) : BottomSheetUnify() {
    private var contentView: View? = null
    private var etWrapper: TextAreaUnify? = null

    companion object {
        @JvmStatic
        fun newInstance(vm: TradeInHomeViewModel): ShoSessionIdBs {
            return ShoSessionIdBs(vm)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        setTitle(getString(R.string.tradein_imei_title))
        isDragable = true
        isHideable = true
        isKeyboardOverlap = false
        customPeekHeight = (getScreenHeight()).toDp()
        showCloseIcon = false
        showKnob = true
        contentView = View.inflate(context,
                R.layout.tradein_bs_show_session_id, null)

        val btnCopy = contentView?.findViewById<UnifyButton>(R.id.btn_sid_copy)

        btnCopy?.setOnClickListener {
            //TODO show snackbar to confirming the copy code
        }

        setChild(contentView)
    }
}