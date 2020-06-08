package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created by jegul on 08/06/20
 */
class PlayPrivacyPolicyBottomSheet @Inject constructor() : BottomSheetUnify() {

    init {
        isFullpage = false
        isHideable = true
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView() {
        setTitle(getString(R.string.play_privacy_policy))
    }

    companion object {
        private const val TAG = "PlayPrivacyPolicyBottomSheet"
    }
}