package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.PostFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.model.PostFilterUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.shc_bottom_sheet_content.view.*

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class PostFilterBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "PostFilterBottomSheet"
        fun newInstance(): PostFilterBottomSheet {
            return PostFilterBottomSheet().apply {
                showCloseIcon = false
                isDragable = true
                showKnob = true
                isHideable = true
            }
        }
    }

    private var postFilterAdapter: PostFilterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    fun init(
            context: Context,
            postFilters: List<PostFilterUiModel>,
            onItemClick: (PostFilterUiModel) -> Unit
    ): PostFilterBottomSheet {
        if (postFilterAdapter == null) {
            postFilterAdapter = PostFilterAdapter(onItemClick)
        }
        postFilterAdapter?.setItems(postFilters)

        setTitle(context.getString(R.string.shc_select_category))
        val inflater = LayoutInflater.from(context)
        val child = inflater.inflate(R.layout.shc_bottom_sheet_content, LinearLayout(context), false)
        with(child) {
            rvBottomSheetContent.layoutManager = LinearLayoutManager(context)
            rvBottomSheetContent.adapter = postFilterAdapter
        }
        setChild(child)
        child.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        customPeekHeight = child.measuredHeight
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return this
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}