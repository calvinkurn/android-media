package com.tokopedia.sellerfeedback.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerfeedback.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class SellerFeedbackPageChooserBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_seller_feedback_page_chooser
        private val TAG: String? = SellerFeedbackPageChooserBottomSheet::class.java.canonicalName

        fun createInstance(): SellerFeedbackPageChooserBottomSheet {
            return SellerFeedbackPageChooserBottomSheet().apply {
                clearContentPadding = true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, TAG)
    }

    fun dismiss(manager: FragmentManager) {
        (manager.findFragmentByTag(TAG) as? BottomSheetUnify)
            ?.dismissAllowingStateLoss()
    }

    private fun initView() {

    }

    private fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(LAYOUT, container)
        val menuTitle = itemView.context.getString(R.string.seller_feedback_choose_page)
        setTitle(menuTitle)
        setChild(itemView)
    }
}