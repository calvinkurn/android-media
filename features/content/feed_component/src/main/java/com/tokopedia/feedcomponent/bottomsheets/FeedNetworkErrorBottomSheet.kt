package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_network_error.*

class FeedNetworkErrorBottomSheet : BottomSheetUnify() {

    private var dismissedByClosing = false
    var onRetry: (() -> Unit)? = null


    companion object {
        private const val EXTRA_SHOULD_SHOW_RETRY_BUTTON = "ShouldShowRetryButton"
        fun newInstance(shouldShowRetryButton: Boolean = false): FeedNetworkErrorBottomSheet {
            val bundle = Bundle().also {
                it.putBoolean(EXTRA_SHOULD_SHOW_RETRY_BUTTON, shouldShowRetryButton)
            }
            return FeedNetworkErrorBottomSheet().apply {
                arguments = bundle
            }
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
        val shouldShowRetryBtn = arguments?.getBoolean(EXTRA_SHOULD_SHOW_RETRY_BUTTON, false) ?: false
        globalError.errorAction.showWithCondition(shouldShowRetryBtn)

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