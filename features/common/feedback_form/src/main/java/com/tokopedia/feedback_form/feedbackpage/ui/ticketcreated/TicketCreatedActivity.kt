package com.tokopedia.developer_options.presentation.feedbackpage.ui.tickercreated

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TicketCreatedActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var fragment: TicketCreatedFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = TicketCreatedFragment.newInstance(bundle?: Bundle())
        }
        return fragment
    }

}