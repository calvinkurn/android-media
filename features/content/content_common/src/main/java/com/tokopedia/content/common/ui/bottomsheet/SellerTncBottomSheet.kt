package com.tokopedia.content.common.ui.bottomsheet

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.content.common.R
import com.tokopedia.content.common.ui.custom.PlayTermsAndConditionView
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel

/**
 * Created by fachrizalmrsln at 20/09/2022
 */
class SellerTncBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null
    private var view: PlayTermsAndConditionView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }
    private fun setupView() {
        view = PlayTermsAndConditionView(requireContext())
            .apply {
                tag = TAG
                setListener(object : PlayTermsAndConditionView.Listener {
                    override fun onOkButtonClicked(view: PlayTermsAndConditionView) {
                        dismiss()
                        mListener?.clickCloseIcon()
                    }
                })
            }

        setChild(view)
        bottomSheet to view

        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false
        clearContentPadding = true
        setTitle(getString(R.string.play_bro_tnc_title))
    }

    fun initViews(tncList: List<TermsAndConditionUiModel>) {
        view?.setTermsAndConditions(tncList)
    }

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        const val TAG = "TNC_BOTTOM_SHEET"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): SellerTncBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? SellerTncBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                SellerTncBottomSheet::class.java.name
            ) as SellerTncBottomSheet
        }
    }

    interface Listener {
        fun clickCloseIcon()
    }

}