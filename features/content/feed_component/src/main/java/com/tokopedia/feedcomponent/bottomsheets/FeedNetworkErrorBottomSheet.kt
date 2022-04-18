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
    var onRetry: (() -> Unit)? = null


    companion object {
        private var shouldShowCobaLagiBtn = false
        fun newInstance(shouldShowRetryBtn: Boolean = false): FeedNetworkErrorBottomSheet {
            this.shouldShowCobaLagiBtn = shouldShowRetryBtn
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
        if (shouldShowCobaLagiBtn)
        globalError.errorAction.visibility = View.VISIBLE
        else
        globalError.errorAction.visibility = View.GONE


        globalError.errorAction.setOnClickListener {
            onRetry?.invoke()
            dismiss()
        }

        setCloseClickListener {
            dismissedByClosing = true
            dismiss()
        }

    }
}