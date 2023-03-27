package com.tokopedia.content.common.report_content.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.BottomSheetFeedThreeDotsMenuBinding
import com.tokopedia.content.common.report_content.adapter.ContentReportAdapter
import com.tokopedia.content.common.report_content.adapter.ContentReportViewHolder
import com.tokopedia.content.common.report_content.adapter.FeedMenuAdapter
import com.tokopedia.content.common.report_content.model.*
import com.tokopedia.content.common.report_content.model.listOfCommentReport
import com.tokopedia.content.common.report_content.viewholder.FeedMenuViewHolder
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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
                if (item.type != FeedMenuIdentifier.LAPORKAN) {
                    dismiss()
                } else {
                    showHeader = true
                    bottomSheetHeader.visible()
                    setTitle(getString(R.string.feed_report_comment))
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
        }
    }
    fun showReportLayoutWhenLaporkanClicked() {
        binding.let {
            it.viewReportGroup.visible()
            it.rvFeedMenu.gone()
        }
    }
    fun sendReport() {
        mListener?.onReportPost(
            FeedReportRequestParamModel(
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

    override fun onResume() {
        super.onResume()

        binding.root.maxHeight = maxSheetHeight
    }

    private fun setupView() {
        binding.rvFeedMenu.adapter = adapter
        adapter.updateData(mFeedMenuItemList)
        bottomSheetClose.visible()

        binding.rvReportItem.adapter = reportAdapter
        reportAdapter.updateList(listOfCommentReport)

    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    fun setData(feedMenuItemList: List<FeedMenuItem>, contentId: String): ContentThreeDotsMenuBottomSheet {
        this.contentId = contentId
        mFeedMenuItemList.clear()
        mFeedMenuItemList.addAll(feedMenuItemList)

        if (isAdded) adapter.updateData(mFeedMenuItemList)

        return this
    }
    fun showToasterOnLoginSuccessFollow(
        message: String,
        type: Int,
        actionText: String? = null
    ) {
        view?.rootView?.let {
            context?.resources?.let { resource ->
                Toaster.toasterCustomBottomHeight =
                    resource.getDimensionPixelSize(com.tokopedia.feedcomponent.R.dimen.feed_bottomsheet_toaster_margin_bottom)
            }
            if (actionText?.isEmpty() == false) {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, actionText)
                    .show()
            } else {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type).show()
            }
        }
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

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentThreeDotsMenuBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ContentThreeDotsMenuBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentThreeDotsMenuBottomSheet::class.java.name
            ) as ContentThreeDotsMenuBottomSheet
        }
    }

    interface Listener {
        fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String)
        fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel)
        fun onMenuBottomSheetCloseClick(contentId: String)
    }
}
