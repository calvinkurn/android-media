package com.tokopedia.review.feature.bulk_write_review.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.review.R
import com.tokopedia.review.databinding.BottomsheetBulkReviewBadRatingCategoryBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.BulkReviewBadRatingCategoryAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class BulkReviewBadRatingCategoryBottomSheet :
    BottomSheetUnify(),
    BulkReviewBadRatingCategoryViewHolder.Listener {

    companion object {
        const val TAG = "BulkReviewBadRatingCategoryBottomSheet"
    }

    private var binding by viewBinding(BottomsheetBulkReviewBadRatingCategoryBinding::bind)
    private var listener: Listener? = null

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
            onShow()
        }
        super.show(manager, tag)
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
        setOnDismissListener { listener?.onDismiss() }
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

    interface Listener {
        fun onDismiss()
        fun onApplyBadRatingCategory()
        fun onBadRatingCategorySelected(position: Int, badRatingCategoryID: String, reason: String)
        fun onBadRatingCategoryUnselected(position: Int, badRatingCategoryID: String, reason: String)
        fun onBadRatingCategoryImpressed(position: Int, reason: String)
    }
}
