package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.affiliate_toko.R

class AffiliateHelpFragment : Fragment() {

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliateHelpFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_help_fragment_layout, container, false)
    }

    private fun setObservers() {

    }
}