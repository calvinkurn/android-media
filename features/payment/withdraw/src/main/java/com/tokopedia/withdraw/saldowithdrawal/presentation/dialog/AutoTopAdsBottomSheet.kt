package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.databinding.SwdDialogAutoTopadsBottomsheetBinding

class AutoTopAdsBottomSheet: BottomSheetUnify() {

    private var binding: SwdDialogAutoTopadsBottomsheetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SwdDialogAutoTopadsBottomsheetBinding.inflate(layoutInflater)
        setChild(binding?.root)

        setupViews()
    }

    private fun setupViews() {

    }

    companion object {
        const val TAG = "AutoTopAdsBottomSheet"
    }
}
