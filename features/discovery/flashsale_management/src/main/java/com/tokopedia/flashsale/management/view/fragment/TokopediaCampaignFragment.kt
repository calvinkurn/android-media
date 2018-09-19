package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

class TokopediaCampaignFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {
        fun createInstance() = TokopediaCampaignFragment()
    }
}