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
        private var shouldShowRetryBtn = false
        val EXTRA_SHOULD_SHOW_RETRY_BUTTON = "ShouldShowRetryButton"
        fun newInstance(bundle: Bundle): FeedNetworkErrorBottomSheet {
            val fragment = FeedNetworkErrorBottomSheet()
            fragment.arguments = bundle
            return fragment
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
        shouldShowRetryBtn = arguments?.getBoolean(EXTRA_SHOULD_SHOW_RETRY_BUTTON, false) ?: false
        if (shouldShowRetryBtn)
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