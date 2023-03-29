package com.tokopedia.review.feature.bulk_write_review.presentation.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.BottomsheetBulkReviewBadRatingCategoryBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.BulkReviewBadRatingCategoryAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.reviewcommon.extension.intersectWith
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

class BulkReviewBadRatingCategoryBottomSheet :
    BottomSheetUnify(),
    BulkReviewBadRatingCategoryViewHolder.Listener,
    CoroutineScope {

    companion object {
        const val TAG = "BulkReviewBadRatingCategoryBottomSheet"
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private var binding by viewBinding(BottomsheetBulkReviewBadRatingCategoryBinding::bind)
    private var listener: Listener? = null
    private var toasterQueueCollectorJob: Job? = null

    private val adapter = BulkReviewBadRatingCategoryAdapter(this)

    init {
        clearContentPadding = true
        overlayClickDismiss = false
        showCloseIcon = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetBulkReviewBadRatingCategoryBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.bulk_review_bad_rating_category_bottom_sheet_title))
        setupRecyclerView()
    }

    override fun onBadRatingCategoryChecked(position: Int, badRatingCategoryID: String, reason: String) {
        listener?.onBadRatingCategorySelected(position, badRatingCategoryID, reason)
    }

    override fun onBadRatingCategoryUnchecked(position: Int, badRatingCategoryID: String, reason: String) {
        listener?.onBadRatingCategoryUnselected(position, badRatingCategoryID, reason)
    }

    override fun onBadRatingCategoryImpressed(position: Int, reason: String) {
        listener?.onBadRatingCategoryImpressed(position, reason)
    }

    fun show(manager: FragmentManager, tag: String?, onShow: () -> Unit) {
        setShowListener {
            dialog?.setCancelable(false)
            handleCancellation()
            onShow()
        }
        super.show(manager, tag)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleCancellation() {
        dialog?.window?.decorView?.findViewById<View>(
            com.google.android.material.R.id.touch_outside
        )?.setOnTouchListener { _, event ->
            if (event.action == KeyEvent.ACTION_DOWN && !event.isAboveBottomSheet()) {
                listener?.onClickOutsideBottomSheet()
            }
            false
        }
    }

    fun setData(badRatingCategories: List<BulkReviewBadRatingCategoryUiModel>) {
        binding?.rvBulkReviewBadRatingCategory?.post {
            adapter.setBadRatingCategories(badRatingCategories)
        }
        binding?.btnBulkReviewBadRatingCategoryCta?.isEnabled = badRatingCategories.any {
            it.selected
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
        setupListener()
    }

    private fun setupRecyclerView() {
        binding?.rvBulkReviewBadRatingCategory?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.rvBulkReviewBadRatingCategory?.adapter = adapter
    }

    private fun setupListener() {
        binding?.btnBulkReviewBadRatingCategoryCta?.setOnClickListener {
            listener?.onApplyBadRatingCategory()
        }
    }

    private fun MotionEvent.isAboveBottomSheet(): Boolean {
        return intersectWith(bottomSheetWrapper, 0L)
    }

    fun setToasterQueue(badRatingCategoryBottomSheetToasterQueue: Flow<CreateReviewToasterUiModel<Any>>) {
        toasterQueueCollectorJob?.cancel()
        toasterQueueCollectorJob = launchCatchError(block = {
            collectWhenResumed(badRatingCategoryBottomSheetToasterQueue.cancellable()) {
                suspendCoroutine<Unit> { cont ->
                    binding?.root?.let { view ->
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
        }, onError = { /* noop */ })
    }

    interface Listener {
        fun onApplyBadRatingCategory()
        fun onBadRatingCategorySelected(position: Int, badRatingCategoryID: String, reason: String)
        fun onBadRatingCategoryUnselected(position: Int, badRatingCategoryID: String, reason: String)
        fun onBadRatingCategoryImpressed(position: Int, reason: String)
        fun onClickOutsideBottomSheet()
    }
}
