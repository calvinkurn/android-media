package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.databinding.BottomsheetFollowRestrictionFeedBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

@Suppress("LateinitUsage")
class FeedFollowersOnlyBottomSheet : BottomSheetUnify() {

    private var listener: Listener? = null
    private var mPositionInFeed: Int = 0
    private var campaignStatus: String = ""
    private var _viewBinding: BottomsheetFollowRestrictionFeedBinding? = null
    private fun getBindingView() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = BottomsheetFollowRestrictionFeedBinding.inflate(LayoutInflater.from(context))
        setChild(getBindingView().root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCloseIcon = true
        isDragable = false

        setSubtitleText()

        getBindingView().buttonFollow.setOnClickListener {
            mPositionInFeed.let { it1 ->
                getBindingView().buttonFollow.isLoading = true
                listener?.onFollowClickedFromFollowBottomSheet(
                    it1
                )
            }
        }

        setCloseClickListener {
            dismiss()
        }
    }

    private fun setSubtitleText() {
        getBindingView().subMessageDescription.let {
            when (campaignStatus) {
                FeedXCampaign.UPCOMING -> it.text =
                    getString(com.tokopedia.content.common.R.string.feed_follow_bottom_sheet_desc_upcoming_text)
                FeedXCampaign.ONGOING -> it.text =
                    getString(com.tokopedia.content.common.R.string.feed_follow_bottom_sheet_desc_ongoing_text)
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        listener: Listener?,
        position: Int = 0,
        status: String
    ) {
        this.listener = listener
        this.mPositionInFeed = position
        this.campaignStatus = status
        show(fragmentManager, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    interface Listener {
        fun onFollowClickedFromFollowBottomSheet(position: Int)
    }

    companion object {
        private const val TAG = "FeedFollowersOnlyBottomSheet"

        fun getOrCreate(fragmentManager: FragmentManager): FeedFollowersOnlyBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? FeedFollowersOnlyBottomSheet
            return oldInstance ?: FeedFollowersOnlyBottomSheet()
        }
    }

}
