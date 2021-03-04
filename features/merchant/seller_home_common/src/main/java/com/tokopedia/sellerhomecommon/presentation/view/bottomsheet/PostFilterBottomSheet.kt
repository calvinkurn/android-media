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
import kotlinx.android.synthetic.main.shc_post_filter_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class PostFilterBottomSheet : BottomSheetUnify(), PostFilterAdapter.Listener {

    companion object {
        const val TAG = "PostFilterBottomSheet"
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

    override fun onItemClick(item: PostFilterUiModel) {
        postFilterAdapter?.getItems()?.forEach {
            it.isSelected = it == item
        }
        postFilterAdapter?.notifyDataSetChanged()
    }

    fun init(
            context: Context,
            postFilters: List<PostFilterUiModel>,
            onItemClick: (PostFilterUiModel) -> Unit
    ): PostFilterBottomSheet {
        if (postFilterAdapter == null) {
            postFilterAdapter = PostFilterAdapter(postFilters, this)
        }

        setTitle(context.getString(R.string.shc_select_category))
        val inflater = LayoutInflater.from(context)
        val child = inflater.inflate(R.layout.shc_post_filter_bottom_sheet, LinearLayout(context), false)
        with(child) {
            rvShcPostFilter.layoutManager = LinearLayoutManager(context)
            rvShcPostFilter.adapter = postFilterAdapter
            btnShcPostFilterApply.setOnClickListener {
                val selected = postFilters.find { it.isSelected }
                selected?.let {
                    onItemClick(it)
                }
                dismiss()
            }
        }
        setChild(child)
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return this
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}