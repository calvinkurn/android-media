package com.tokopedia.feedcomponent.onboarding.view.bottomsheet.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class BaseFeedUserOnboardingBottomSheet : BottomSheetUnify() {

    protected var mListener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showCloseIcon = false
        showKnob = true
        showHeader = false

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onSuccess()
    }
}