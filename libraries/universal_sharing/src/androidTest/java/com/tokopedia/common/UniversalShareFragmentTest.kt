package com.tokopedia.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput

abstract class UniversalShareFragmentTest : Fragment() {

    lateinit var bottomSheet: UniversalShareBottomSheet

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_universal_share_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUniversalShare(getAffiliateInput())

    }

    companion object {
        private val TAG = "UniversalShareTestActivity"
    }

    private fun initializeBottomsheet() {
        bottomSheet = UniversalShareBottomSheet.createInstance()
    }

    abstract fun showUniversalShare(affiliatePDPInput: AffiliatePDPInput)

    abstract fun getAffiliateInput(): AffiliatePDPInput
}
