package com.tokopedia.profile.view.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.profile.R

/**
 * @author by milhamj on 26/03/19.
 */
class OnboardingBottomSheet : BottomSheetDialogFragment() {

    private val profileRouter: ProfileModuleRouter by lazy {
        context!!.applicationContext as ProfileModuleRouter
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rounded_closeable_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}