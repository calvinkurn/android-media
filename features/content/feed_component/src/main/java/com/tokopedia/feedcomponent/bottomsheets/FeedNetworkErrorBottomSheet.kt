package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_network_error.*

class FeedNetworkErrorBottomSheet : BottomSheetUnify() {

    private var dismissedByClosing = false

    companion object {
        fun newInstance(): FeedNetworkErrorBottomSheet {
            return FeedNetworkErrorBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_network_error, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCloseIcon = true
        isDragable = false
        globalError.errorAction.visibility = View.GONE

        setCloseClickListener {
            dismissedByClosing = true
            dismiss()
        }

    }
}