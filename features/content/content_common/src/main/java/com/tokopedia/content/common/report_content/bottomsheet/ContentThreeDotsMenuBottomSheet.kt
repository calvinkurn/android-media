package com.tokopedia.content.common.report_content.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.BottomSheetFeedThreeDotsMenuBinding
import com.tokopedia.content.common.report_content.adapter.ContentReportAdapter
import com.tokopedia.content.common.report_content.adapter.ContentReportViewHolder
import com.tokopedia.content.common.report_content.adapter.FeedMenuAdapter
import com.tokopedia.content.common.report_content.model.*
import com.tokopedia.content.common.report_content.viewholder.FeedMenuViewHolder
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlin.math.roundToInt

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class ContentThreeDotsMenuBottomSheet : BottomSheetUnify(), ContentReportViewHolder.Listener {

    private var _binding: BottomSheetFeedThreeDotsMenuBinding? = null
    private val binding: BottomSheetFeedThreeDotsMenuBinding
        get() = _binding!!

    private var reasonType: String = ""
    private var reasonDesc: String = ""
    private var contentId: String = ""

    private val adapter: FeedMenuAdapter by lazy {
        FeedMenuAdapter(object : FeedMenuViewHolder.Listener {
            override fun onClick(item: FeedMenuItem) {
                if (item.type != FeedMenuIdentifier.Report) {
                    dismiss()
                } else {
                    showHeader = true
                    bottomSheetHeader.visible()
                }

                mListener?.onMenuItemClick(item, contentId)
            }
        })
    }

    private val reportAdapter: ContentReportAdapter = ContentReportAdapter(this)

    private val mFeedMenuItemList = mutableListOf<FeedMenuItem>()
    private var mListener: Listener? = null
    private var mAnalytic: FeedAccountTypeAnalytic? = null

    private val maxSheetHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENTAGE).roundToInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    private fun setupBottomSheet() {
        setTitle(String.EMPTY)
        _binding = BottomSheetFeedThreeDotsMenuBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        showCloseIcon = true
        setChild(binding.root)

        setCloseClickListener {
            mListener?.onMenuBottomSheetCloseClick(contentId)
            dismiss()
        }
    }

    private val reportSheet by lazyThreadSafetyNone {
        ContentReportBottomSheet.getFragment(childFragmentManager, requireActivity().classLoader)
            .apply {
                setListener(object : ContentReportBottomSheet.Listener {
                    override fun onCloseButtonClicked() {
                        dismiss()
                    }

                    override fun onItemReportClick(item: PlayUserReportReasoningUiModel.Reasoning) {
                        submissionReportSheet.apply {
                            setData(item)
                        }.show(childFragmentManager)
                        mListener?.onReportClicked(item)
                    }

                    override fun onFooterClicked() {
                        mListener?.onFooterClicked()
                    }
                })
            }
    }

    private val submissionReportSheet by lazyThreadSafetyNone {
        ContentSubmitReportBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        ).apply {
            setListener(object : ContentSubmitReportBottomSheet.Listener {
                override fun onBackButtonListener() {
                    dismiss()
                }

                override fun onFooterClicked() {
                    mListener?.onFooterClicked()
                }

                override fun onSubmitReport(desc: String) {
                    mListener?.onSubmitReport(desc)
                }
            })
        }
    }

    fun showReportLayoutWhenLaporkanClicked(
        data: Result<List<PlayUserReportReasoningUiModel>> = Success(
            emptyList()
        )
    ) {
        if (data is Success && data.data.isNotEmpty()) {
            reportSheet.apply {
                updateList(data)
            }.show(childFragmentManager)
        } else {
            setTitle(getString(R.string.content_common_report_comment))
            binding.let {
                it.root.layoutParams.height = maxSheetHeight
                it.viewReportGroup.visible()
                it.rvFeedMenu.gone()
            }
        }
    }

    fun sendReport() {
        mListener?.onReportPost(
            FeedComplaintSubmitReportUseCase.Param(
                reportType = FeedComplaintSubmitReportUseCase.VALUE_REPORT_TYPE_POST,
                contentId = contentId,
                reason = reasonType,
                reasonDetails = reasonDesc
            )
        )
    }

    fun setFinalView() {
        binding.viewReportGroup.gone()
        binding.reportFinalLayout.root.visible()
    }

    private fun setupView() {
        binding.rvFeedMenu.adapter = adapter
        adapter.updateData(mFeedMenuItemList)
        bottomSheetClose.visible()

        binding.rvReportItem.adapter = reportAdapter
        reportAdapter.updateList(listOfCommentReport)

        binding.rvFeedMenu.isNestedScrollingEnabled = false
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    fun setData(
        feedMenuItemList: List<FeedMenuItem>,
        contentId: String
    ): ContentThreeDotsMenuBottomSheet {
        this.contentId = contentId
        mFeedMenuItemList.clear()
        mFeedMenuItemList.addAll(feedMenuItemList)

        if (isAdded) adapter.updateData(mFeedMenuItemList)

        return this
    }

    @Deprecated("Analytic is not valid")
    fun setAnalytic(analytic: FeedAccountTypeAnalytic) {
        mAnalytic = analytic
    }

    override fun onClicked(item: ContentReportItemUiModel) {
        reasonType = item.type.value
        reasonDesc = getString(item.title)
        sendReport()
    }

    companion object {
        private const val TAG = "FeedAccountTypeBottomSheet"

        private const val HEIGHT_PERCENTAGE = 0.4

        fun get(fragmentManager: FragmentManager): ContentThreeDotsMenuBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? ContentThreeDotsMenuBottomSheet
        }

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentThreeDotsMenuBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentThreeDotsMenuBottomSheet::class.java.name
            ) as ContentThreeDotsMenuBottomSheet
        }
    }

    interface Listener {
        fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String)
        fun onReportPost(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param)
        fun onMenuBottomSheetCloseClick(contentId: String)

        fun onFooterClicked()
        fun onReportClicked(item: PlayUserReportReasoningUiModel.Reasoning)
        fun onSubmitReport(desc: String)
    }
}
