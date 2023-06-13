package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.databinding.BottomsheetNetworkErrorBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify

class FeedNetworkErrorBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetNetworkErrorBinding? = null
    private val binding: BottomsheetNetworkErrorBinding
        get() = _binding!!

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
    ): View {
        _binding = BottomsheetNetworkErrorBinding.inflate(layoutInflater)
        setChild(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        showCloseIcon = true
        isDragable = false
        val shouldShowRetryBtn = arguments?.getBoolean(EXTRA_SHOULD_SHOW_RETRY_BUTTON, false) ?: false
        binding.globalError.errorAction.showWithCondition(shouldShowRetryBtn)

        binding.globalError.errorAction.setOnClickListener {
            onRetry?.invoke()
            dismiss()
        }

        setCloseClickListener {
            dismissedByClosing = true
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
