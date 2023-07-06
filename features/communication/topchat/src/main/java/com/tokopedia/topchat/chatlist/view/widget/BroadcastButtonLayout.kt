package com.tokopedia.topchat.chatlist.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.databinding.TopchatPartialBroadcastFabBinding

class BroadcastButtonLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)
    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) :
        super(context, attrSet, defStyleAttr)

    private var binding: TopchatPartialBroadcastFabBinding? = null

    init {
        binding = TopchatPartialBroadcastFabBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }

    fun toggleBroadcastButton(
        shouldShow: Boolean,
        shouldShowLabel: Boolean
    ) {
        if (shouldShow) {
            toggleBroadcastLabel(shouldShowLabel)
            show()
        } else {
            hide()
        }
    }

    fun toggleBroadcastLabel(shouldShowLabel: Boolean) {
        if (shouldShowLabel) {
            binding?.labelNewFabBroadcast?.show()
            binding?.backgroundShadowLabelBroadcast?.show()
        } else {
            binding?.labelNewFabBroadcast?.hide()
            binding?.backgroundShadowLabelBroadcast?.hide()
        }
    }

    companion object {
        const val BROADCAST_FAB_LABEL_PREF_NAME = "topchat_seller_should_show_broadcast_new_label"
        const val BROADCAST_FAB_LABEL_ROLLENCE_KEY = "broadcast_label"
    }
}
