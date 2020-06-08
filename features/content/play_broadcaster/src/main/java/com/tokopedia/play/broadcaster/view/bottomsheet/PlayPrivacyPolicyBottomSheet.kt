package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
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

    private lateinit var clPrivacyPolicy: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        clPrivacyPolicy.layoutParams = clPrivacyPolicy.layoutParams.apply {
            height = (getScreenHeight() * 0.6f).toInt()
        }
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.play_privacy_policy))
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_privacy_policy, null)
        clPrivacyPolicy = view.findViewById(R.id.cl_privacy_policy)
        return view
    }

    companion object {
        private const val TAG = "PlayPrivacyPolicyBottomSheet"
    }
}