package com.tokopedia.content.common.report_content.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.ViewPlayUserReportSheetBinding
import com.tokopedia.content.common.report_content.adapter.UserReportReasoningAdapter
import com.tokopedia.content.common.report_content.adapter.itemdecoration.ReasoningListItemDecoration
import com.tokopedia.content.common.report_content.adapter.viewholder.UserReportReasoningViewHolder
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.PlayUserReportSection
import com.tokopedia.content.common.report_content.model.PlayUserReportSectionType
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.content.common.R as commonR

/**
 * @author by astidhiyaa on 31/05/23
 */
class ContentReportBottomSheet : BottomSheetUnify() {

    private var _binding: ViewPlayUserReportSheetBinding? = null
    private val binding: ViewPlayUserReportSheetBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private val tvHeader = PlayUserReportSection(
        type = PlayUserReportSectionType.Header,
        title = commonR.string.play_user_report_header,
        isUrl = false
    )

    private val tvFooter = PlayUserReportSection(
        type = PlayUserReportSectionType.Footer,
        title = commonR.string.content_user_report_footer,
        isUrl = true,
        onClick = { mListener?.onFooterClicked() }
    )

    private val categoryAdapter by lazyThreadSafetyNone {
        UserReportReasoningAdapter(object : UserReportReasoningViewHolder.Listener {
            override fun onItemCategoryClicked(item: PlayUserReportReasoningUiModel.Reasoning) {
                mListener?.onItemReportClick(item)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun initBottomSheet() {
        clearContentPadding = false
        showHeader = false

        _binding = ViewPlayUserReportSheetBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        setOnDismissListener {
            mListener?.onCloseButtonClicked()
        }
    }

    private fun setupView() {
        binding.rvCategory.apply {
            adapter = categoryAdapter
            addItemDecoration(ReasoningListItemDecoration(this.context))
        }

        binding.headerContentReport.title = getString(commonR.string.content_user_report_header)

        binding.headerContentReport.closeListener = View.OnClickListener {
            mListener?.onCloseButtonClicked()
        }
    }

    fun updateList(result: Result<List<PlayUserReportReasoningUiModel>>) {
        when (result) {
            is Success -> {
                val data = buildList {
                    add(tvHeader)
                    addAll(result.data)
                    add(tvFooter)
                }
                categoryAdapter.setItemsAndAnimateChanges(data)
            }

            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        mListener = null
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onCloseButtonClicked()
        fun onItemReportClick(item: PlayUserReportReasoningUiModel.Reasoning)
        fun onFooterClicked()
    }

    companion object {
        const val TAG = "ContentReportBottomSheet"

        fun get(fragmentManager: FragmentManager): ContentReportBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? ContentReportBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentReportBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentReportBottomSheet::class.java.name
            ) as ContentReportBottomSheet
        }
    }
}
