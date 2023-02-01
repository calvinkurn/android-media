package com.tokopedia.review.feature.bulk_write_review.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.BottomsheetBulkReviewExpandedTextAreaBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BulkReviewExpandedTextAreaBottomSheet : BottomSheetUnify(), CoroutineScope {

    companion object {
        const val TAG = "BulkReviewExpandedTextAreaBottomSheet"
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private var toasterQueueCollectorJob: Job? = null

    private var binding by viewBinding(BottomsheetBulkReviewExpandedTextAreaBinding::bind)

    init {
        clearContentPadding = true
        overlayClickDismiss = false
        showCloseIcon = false
        isFullpage = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetBulkReviewExpandedTextAreaBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContainer()
        setupTextArea()
    }

    fun show(manager: FragmentManager, tag: String?, onShow: () -> Unit) {
        setShowListener {
            dialog?.setCancelable(false)
            onShow()
        }
        super.show(manager, tag)
    }

    fun setData(hint: StringRes, text: String) {
        binding?.textAreaBulkReviewExpanded?.editText?.hint = context?.let { hint.getStringValueWithDefaultParam(it) }
        binding?.textAreaBulkReviewExpanded?.editText?.setText(text)
        binding?.textAreaBulkReviewExpanded?.requestFocus()
    }

    fun setListener(listener: Listener) {
        setAction(MethodChecker.getDrawable(context, R.drawable.ic_collapse)) {
            listener.onDismiss(binding?.textAreaBulkReviewExpanded?.editText?.text?.toString().orEmpty())
        }
    }

    fun setToasterQueue(expandedTextAreaToasterQueue: Flow<CreateReviewToasterUiModel<Any>>) {
        toasterQueueCollectorJob?.cancel()
        toasterQueueCollectorJob = launchCatchError(block = {
            collectWhenResumed(expandedTextAreaToasterQueue.cancellable()) {
                suspendCoroutine<Unit> { cont ->
                    binding?.containerBulkReviewExpandedTextArea?.let { view ->
                        Toaster.build(
                            view,
                            it.message.getStringValueWithDefaultParam(view.context),
                            it.duration,
                            it.type,
                            it.actionText.getStringValue(view.context)
                        ) {}.run {
                            addCallback(object : Snackbar.Callback() {
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    removeCallback(this)
                                    cont.resume(Unit)
                                }
                            })
                            show()
                        }
                    }
                }
            }
        }, onError = {/* noop */})
    }

    private fun setupContainer() {
        binding?.containerBulkReviewExpandedTextArea?.setOnClickListener {
            binding?.textAreaBulkReviewExpanded?.run {
                requestFocus()
                showSoftKeyboard(editText)
            }
        }
    }

    private fun setupTextArea() {
        removeBorder()
        setMaxLines()
    }

    private fun removeBorder() {
        binding?.textAreaBulkReviewExpanded?.textInputLayout?.boxStrokeWidth = Int.ZERO
        binding?.textAreaBulkReviewExpanded?.textInputLayout?.boxStrokeWidthFocused = Int.ZERO
    }

    private fun setMaxLines() {
        binding?.textAreaBulkReviewExpanded?.maxLine = Int.MAX_VALUE
    }

    private fun getInputMethodManager(): InputMethodManager? {
        return activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    }

    private fun showSoftKeyboard(view: View) {
        getInputMethodManager()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    interface Listener {
        fun onDismiss(text: String)
    }
}
