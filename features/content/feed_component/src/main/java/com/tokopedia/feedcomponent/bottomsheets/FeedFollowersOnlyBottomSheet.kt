package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class FeedFollowersOnlyBottomSheet : BottomSheetUnify() {

    private var dismissedByClosing = false
    private lateinit var followBtn: UnifyButton
    private var listener: Listener? = null
    private var mPositionInFeed: Int = 0


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
        showCloseIcon = true
        isDragable = false

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

    fun show(
        fragmentManager: FragmentManager,
        listener: Listener?,
        position:Int = 0
    ) {
        this.listener = listener
        this.mPositionInFeed = position
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