package com.tokopedia.content.common.report_content.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.BottomSheetFeedThreeDotsMenuBinding
import com.tokopedia.content.common.databinding.BottomsheetFeedPostReportBinding
import com.tokopedia.content.common.report_content.adapter.FeedMenuAdapter
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.report_content.viewholder.FeedMenuViewHolder
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class ContentThreeDotsMenuBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedThreeDotsMenuBinding? = null
    private val binding: BottomSheetFeedThreeDotsMenuBinding
        get() = _binding!!

    private var isClicked = 0
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

                mListener?.onMenuItemClick(item, "")
            }
        })
    }

    private val mFeedMenuItemList = mutableListOf<FeedMenuItem>()
    private var mListener: Listener? = null
    private var mAnalytic: FeedAccountTypeAnalytic? = null

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
            setUpReportLayoutView(it.reportLayout)
            it.reportLayout.root.visible()
            it.rvFeedMenu.gone()
        }
    }
    private fun setUpReportLayoutView(binding: BottomsheetFeedPostReportBinding) {
        binding.let {
            it.reportSubtext1.setOnClickListener {
                setSpamCase()
            }
            it.reportSubtext1Icon.setOnClickListener {
                setSpamCase()
            }
            it.reportSubtext2.setOnClickListener {
                setInappropriateCase()
            }
            it.reportSubtext2Icon.setOnClickListener {
                setInappropriateCase()
            }
            it.reportSubtext3.setOnClickListener {
                setInappropriateCase()
            }
            it.reportSubtext3Icon.setOnClickListener {
                setInappropriateCase()
            }
            it.reportSubtext4.setOnClickListener {
                setInappropriateSpeech()
            }
            it.reportSubtext4Icon.setOnClickListener {
                setInappropriateSpeech()
            }
            it.reportSubtext5.setOnClickListener {
                setFraudCase()
            }
            it.reportSubtext5Icon.setOnClickListener {
                setFraudCase()
            }
            it.reportSubtext6.setOnClickListener {
                setIlleagalGoodSaleCase()
            }
            it.reportSubtext6Icon.setOnClickListener {
                setIlleagalGoodSaleCase()
            }
        }
    }

    private fun setSpamCase() {
        isClicked = SPAM
        sendReport()
    }

    private fun setAbuseCase() {
        isClicked = ABUSE
        sendReport()
    }

    private fun setInappropriateCase() {
        isClicked = INAPPROPRIATE
        sendReport()
    }
    private fun setInappropriateSpeech() {
        isClicked = INAPPROPRIATE_LANGUAGE
        sendReport()
    }
    private fun setFraudCase() {
        isClicked = FRAUD
        sendReport()
    }
    private fun setIlleagalGoodSaleCase() {
        isClicked = ILLEGAL_GOODS
        sendReport()
    }
    fun sendReport() {
        getReason()
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
        binding.reportLayout.layout1.gone()
        binding.reportLayout.reportFinalLayout.root.visible()
    }

    private fun getReason() {
        when (isClicked) {
            TYPE1 -> {
                reasonType = getString(R.string.feed_common_reason_type_spam)
                reasonDesc = getString(R.string.feed_common_reason_desc_spam)
            }
            TYPE2 -> {
                reasonType = getString(R.string.feed_common_reason_type_abuse)
                reasonDesc = getString(R.string.feed_common_reason_desc_abuse)
            }
            TYPE3 -> {
                reasonType = getString(R.string.feed_common_reason_type_inappropriate)
                reasonDesc = getString(R.string.feed_common_reason_desc_inappropriate)
            }
            TYPE4 -> {
                reasonType = getString(R.string.feed_common_reason_type_others)
                reasonDesc = getString(R.string.feed_component_report_option_text2)
            }
            TYPE5 -> {
                reasonType = getString(contentCommonR.string.feed_common_reason_type_fraud)
                reasonDesc = getString(R.string.feed_component_report_option_text3)
            }
            TYPE6 -> {
                reasonType = getString(R.string.feed_common_reason_type_others)
                reasonDesc = getString(R.string.feed_component_report_option_text4)
            }
        }
    }

    private fun setupView() {
        binding.rvFeedMenu.adapter = adapter
        adapter.updateData(mFeedMenuItemList)
        bottomSheetClose.visible()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
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
                    resource.getDimensionPixelSize(R.dimen.feed_bottomsheet_toaster_margin_bottom)
            }
            if (actionText?.isEmpty() == false) {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, actionText)
                    .show()
            } else {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type).show()
            }
        }
    }

    fun setAnalytic(analytic: FeedAccountTypeAnalytic) {
        mAnalytic = analytic
    }

    companion object {
        private const val SPAM = 1
        private const val ABUSE = 2
        private const val INAPPROPRIATE = 3
        private const val INAPPROPRIATE_LANGUAGE = 4
        private const val FRAUD = 5
        private const val ILLEGAL_GOODS = 6
        private const val TYPE1 = 1
        private const val TYPE2 = 2
        private const val TYPE3 = 3
        private const val TYPE4 = 4
        private const val TYPE5 = 5
        private const val TYPE6 = 6

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            tag: String
        ): ContentThreeDotsMenuBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(tag) as? ContentThreeDotsMenuBottomSheet
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
