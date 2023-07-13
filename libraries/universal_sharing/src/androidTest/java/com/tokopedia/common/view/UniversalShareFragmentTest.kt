package com.tokopedia.common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.universal_sharing.test.R
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

class UniversalShareFragmentTest : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_universal_share_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showUniversalBottomSheet(universalShareBottomSheet: UniversalShareBottomSheet) {
        universalShareBottomSheet.show(requireFragmentManager(), TAG)
    }

    companion object {
        private val TAG = "UniversalShareTestActivity"
    }
}
