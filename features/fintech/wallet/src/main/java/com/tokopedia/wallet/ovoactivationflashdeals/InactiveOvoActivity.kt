package com.tokopedia.wallet.ovoactivationflashdeals

import androidx.fragment.app.Fragment
import com.tokopedia.wallet.R
import com.tokopedia.wallet.ovoactivation.view.BaseOvoActivationActivity

class InactiveOvoActivity : BaseOvoActivationActivity() {

    override fun getNewFragment(): Fragment? {
        var registerApplink = ""
        var helpApplink = ""
        var tncApplink = ""

        intent.data?.let {
            registerApplink = it.getQueryParameter(REGISTER_APPLINK) ?: ""
            helpApplink = it.getQueryParameter(HELP_APPLINK) ?: ""
            tncApplink = it.getQueryParameter(TNC_APPLINK) ?: ""
        }
        if (registerApplink.isNullOrBlank() && helpApplink.isNullOrBlank() && tncApplink.isNullOrBlank()) {
            intent.extras?.let {
                registerApplink = it.getString(REGISTER_APPLINK) ?: ""
                helpApplink = it.getString(HELP_APPLINK) ?: ""
                tncApplink = it.getString(TNC_APPLINK) ?: ""
            }
        }

        return InactiveOvoFragment.newInstance(registerApplink, helpApplink, tncApplink)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_inactive_ovo
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_ovo_activation
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    companion object {
        val REGISTER_APPLINK = "applink_register"
        val HELP_APPLINK = "applink_help"
        val TNC_APPLINK = "applink_tnc"
    }
}