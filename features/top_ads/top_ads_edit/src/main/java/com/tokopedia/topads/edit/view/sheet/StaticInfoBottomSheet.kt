package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_static_info_bs.*

class StaticInfoBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_static_info_bs, null)
        showKnob = true
        isHideable = true
        showCloseIcon = false
        setChild(contentView)
        setTitle(getString(R.string.topads_common_static_bs_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            image?.setImageDrawable(context?.getDrawable(R.drawable.topads_keyword_table))
        }
    }

    fun show(
            fragmentManager: FragmentManager) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): StaticInfoBottomSheet = StaticInfoBottomSheet()
    }
}