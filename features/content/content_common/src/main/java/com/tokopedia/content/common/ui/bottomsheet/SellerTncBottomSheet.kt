package com.tokopedia.content.common.ui.bottomsheet

import android.os.Bundle
import android.view.View
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
    private val mDataTnc = mutableListOf<TermsAndConditionUiModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
        mDataTnc.clear()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun initViews() {
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

        setCloseClickListener {
            dismiss()
            mListener?.clickCloseIcon()
        }

        setChild(view)
        bottomSheet to view

        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false
        clearContentPadding = true
    }

    private fun initData() {
        setTitle(getString(R.string.play_bro_tnc_title))
        view?.setTermsAndConditions(mDataTnc)
    }

    fun setData(tncList: List<TermsAndConditionUiModel>): SellerTncBottomSheet {
        if (mDataTnc.isNotEmpty()) mDataTnc.clear()
        mDataTnc.addAll(tncList)
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "SellerTncBottomSheet"

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
