package com.tokopedia.gopay.kyc.presentation.fragment.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

abstract class GoPayKycBaseFragment: BaseDaggerFragment() {

    abstract fun handleBackPressForGopay()
    abstract fun sendOpenScreenGopayEvent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOpenScreenGopayEvent()
        setUpOnBackPressed()
    }

    private fun setUpOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPressForGopay()
        }
    }

}