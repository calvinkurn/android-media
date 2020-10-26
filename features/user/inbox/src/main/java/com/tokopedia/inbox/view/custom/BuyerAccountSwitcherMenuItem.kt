package com.tokopedia.inbox.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.inbox.R
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide

class BuyerAccountSwitcherMenuItem : AccountSwitcherMenuItem {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override val role: Int = RoleType.BUYER

    override fun onCreateView(view: View) {
        initSmallIcon()
        initNameMargin()
    }

    private fun initSmallIcon() {
        smallIcon?.hide()
    }

    private fun initNameMargin() {
        val startMargin = context.resources.getDimension(R.dimen.dp_inbox_12).toInt()
        val nameLayoutParam = name?.layoutParams as? MarginLayoutParams
        nameLayoutParam?.marginStart = startMargin
        nameLayoutParam?.setMargins(
                startMargin,
                nameLayoutParam.topMargin,
                nameLayoutParam.rightMargin,
                nameLayoutParam.bottomMargin
        )
        name?.layoutParams = nameLayoutParam
    }
}