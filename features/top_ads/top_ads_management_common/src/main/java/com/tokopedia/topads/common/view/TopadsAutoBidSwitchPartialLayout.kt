package com.tokopedia.topads.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.dialog.DialogUnify
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
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    showChangeBidTypeConfirmationDialog(isBidAutomatic) {
                        it.isChecked = !it.isChecked
                        onCheckBoxStateChanged?.invoke(it.isChecked)
                    }
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

    private fun showChangeBidTypeConfirmationDialog(
        isSwitchedToAutomatic: Boolean, positiveClick: () -> Unit,
    ) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            if (isSwitchedToAutomatic) {
                setTitle(context.resources.getString(R.string.topads_common_manual_dialog_title))
                setDescription(context.resources.getString(R.string.topads_common_manual_dialog_description))
            } else {
                setTitle(context.resources.getString(R.string.topads_common_automatic_dialog_title))
                setDescription(context.resources.getString(R.string.topads_common_automatic_dialog_description))
            }
            setPrimaryCTAText(context.resources.getString(R.string.topads_common_dialog_cta_text))
            setSecondaryCTAText(context.resources.getString(R.string.topads_common_batal))
            setSecondaryCTAClickListener {
                dismiss()
            }
            setPrimaryCTAClickListener {
                positiveClick()
                dismiss()
            }
        }.show()
    }
}