package com.tokopedia.developer_options.presentation.feedbackpage.ui

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TicketCreatedActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return TicketCreatedFragment()
    }

}