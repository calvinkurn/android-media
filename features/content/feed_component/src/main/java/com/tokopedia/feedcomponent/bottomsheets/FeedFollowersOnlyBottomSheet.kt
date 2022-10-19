package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

@Suppress("LateinitUsage")
class FeedFollowersOnlyBottomSheet : BottomSheetUnify() {

    private var dismissedByClosing = false
    private lateinit var followBtn: UnifyButton
    private lateinit var subTitle: Typography
    private var listener: Listener? = null
    private var mPositionInFeed: Int = 0
    private var campaignStatus: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_follow_restriction_feed, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followBtn = view.findViewById(R.id.button_follow)
        subTitle = view.findViewById(R.id.sub_message_description)
        showCloseIcon = true
        isDragable = false

        setSubtitleText()

        followBtn.setOnClickListener { mPositionInFeed?.let { it1 ->
            followBtn.isLoading = true
            listener?.onFollowClickedFromFollowBottomSheet(
                it1
            )
        } }

        setCloseClickListener {
            dismissedByClosing = true
            dismiss()
        }
    }
    private fun setSubtitleText(){
        when(campaignStatus){
            FeedXCampaign.UPCOMING -> subTitle.text = getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_desc_upcoming_text)
            FeedXCampaign.ONGOING -> subTitle.text = getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_desc_ongoing_text)
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        listener: Listener?,
        position:Int = 0,
        status: String
    ) {
        this.listener = listener
        this.mPositionInFeed = position
        this.campaignStatus = status
        show(fragmentManager, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    interface Listener {
        fun onFollowClickedFromFollowBottomSheet(position: Int)
    }

}
