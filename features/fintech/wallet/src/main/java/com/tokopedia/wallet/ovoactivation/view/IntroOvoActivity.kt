package com.tokopedia.wallet.ovoactivation.view

import androidx.fragment.app.Fragment

/**
 * Created by nabillasabbaha on 20/09/18.
 */
class IntroOvoActivity : BaseOvoActivationActivity(), IntroOvoFragment.OvoFragmentListener {

    override fun getNewFragment(): Fragment? {
        return IntroOvoFragment.newInstance(isTokoCashActive())
    }

    private fun isTokoCashActive(): Boolean {
        val tokocashActive = intent.data.getQueryParameter(WALLET_PARAM_TOKOCASH_ACTIVE)
        return if (tokocashActive.isNotEmpty())
            tokocashActive.toBoolean()
        else false
    }

    override fun setTitleHeader(titleHeader: String) {
        updateTitle(titleHeader)
    }

    companion object {

        const val TOKOCASH_ACTIVE = "tokocash_active"
        const val WALLET_PARAM_TOKOCASH_ACTIVE = "tokocash"
    }
}
