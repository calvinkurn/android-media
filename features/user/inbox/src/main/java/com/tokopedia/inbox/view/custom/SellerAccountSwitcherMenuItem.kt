package com.tokopedia.inbox.view.custom

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.inboxcommon.RoleType

class SellerAccountSwitcherMenuItem : AccountSwitcherMenuItem {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override val role: Int = RoleType.SELLER

}