package com.tokopedia.topads.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography

class TopadsAutoBidSwitchPartialLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var switchBidEditKeyword: SwitchUnify? = null
        private set
    var txtBidTitleEditKeyword: Typography? = null
        private set
    var ivBidInfoEditKeyword: ImageUnify? = null
        private set

    var tracker: TrackerListener? = null

    var onInfoClicked: (() -> Unit)? = null
    var onCheckBoxStateChanged: ((Boolean) -> Unit)? = null     //true for automatic
    val isBidAutomatic get() = switchBidEditKeyword?.isChecked ?: false

    init {
        inflate(context, R.layout.topads_common_auto_bid_switch_partial_layout, this)
        initView()
        setClick()
    }

    fun switchToAutomatic() {
        switchBidEditKeyword?.isChecked = true
    }

    fun switchToManual() {
        switchBidEditKeyword?.isChecked = false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClick() {
        ivBidInfoEditKeyword?.setOnClickListener {
            onInfoClicked?.invoke()
        }

        switchBidEditKeyword?.let {
            it.setOnTouchListener { view, motionEvent ->
                tracker?.autoBidSwitchClicked(it.isChecked)
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    context.showBidStateChangeConfirmationDialog(isBidAutomatic, {
                        if(isBidAutomatic)
                            tracker?.bidChangeToManualLanjuktanClicked()
                        else
                            tracker?.bidChangeConfirmationDialogPositiveClick()
                        it.isChecked = !it.isChecked
                        onCheckBoxStateChanged?.invoke(it.isChecked)
                    }, {
                        if(isBidAutomatic)
                            tracker?.bidChangeToManualDismissed()
                        else
                            tracker?.bidChangeConfirmationDialogNegativeClick()
                    })
                }
                false
            }
        }
    }

    private fun initView() {
        switchBidEditKeyword = findViewById(R.id.switchBidEditKeyword) //checked for otomatis
        txtBidTitleEditKeyword = findViewById(R.id.txtBidTitleEditKeyword)
        ivBidInfoEditKeyword = findViewById(R.id.ivBidInfoEditKeyword)
        switchBidEditKeyword?.isClickable = false
    }

    interface TrackerListener {
        fun autoBidSwitchClicked(on: Boolean)
        fun bidChangeConfirmationDialogPositiveClick()
        fun bidChangeConfirmationDialogNegativeClick()
        fun bidChangeToManualLanjuktanClicked()
        fun bidChangeToManualDismissed()
    }
}
