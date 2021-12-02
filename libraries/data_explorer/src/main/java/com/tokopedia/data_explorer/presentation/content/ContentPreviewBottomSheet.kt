package com.tokopedia.data_explorer.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.data_explorer.R
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.data_explorer_content_preview_layout.*

class ContentPreviewBottomSheet: BottomSheetUnify() {

    private val childLayoutRes = R.layout.data_explorer_content_preview_layout
    private var cellText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            cellText = it.getString(CELL_TEXT) ?: ""
        }
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight().toDp()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCellContent.text = cellText
    }

    companion object {
        const val TITLE = "Cell Info"
        const val TAG = "ContentPreview"
        private const val CELL_TEXT = "cell text"
        fun show(cellText: String, childFragmentManager: FragmentManager) {
            val fragment = ContentPreviewBottomSheet()
            fragment.arguments = Bundle().apply { putString(CELL_TEXT, cellText) }
            fragment.show(childFragmentManager, TAG)
        }
    }

}