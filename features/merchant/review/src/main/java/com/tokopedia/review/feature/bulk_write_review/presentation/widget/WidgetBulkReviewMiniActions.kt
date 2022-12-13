package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetBulkReviewMiniActionsBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.BulkReviewMiniActionAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewMiniActionViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView

class WidgetBulkReviewMiniActions @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetBulkReviewMiniActionsBinding>(context, attrs, defStyleAttr),
    BulkReviewMiniActionViewHolder.Listener {
    override val binding = WidgetBulkReviewMiniActionsBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val adapter: BulkReviewMiniActionAdapter = BulkReviewMiniActionAdapter(this)
    private val itemAnimator by lazy(LazyThreadSafetyMode.NONE) { DefaultItemAnimator() }
    private var listener: Listener? = null

    init {
        initRecyclerView()
    }

    override fun onClickMiniAction(id: Int) {
        when (id) {
            IconUnify.EDIT -> listener?.onClickTestimonyMiniAction()
            IconUnify.CAMERA -> listener?.onClickAddAttachmentMiniAction()
        }
    }

    fun updateUiState(uiState: BulkReviewMiniActionsUiState, animate: Boolean) {
        when (uiState) {
            is BulkReviewMiniActionsUiState.Hidden -> {
                animateHide(animate = animate, onAnimationEnd = { gone() })
            }
            is BulkReviewMiniActionsUiState.Showing -> {
                adapter.setMiniActions(uiState.miniActions)
                if (animate) {
                    binding.rvBulkReviewMiniAction.itemAnimator = itemAnimator
                } else {
                    binding.rvBulkReviewMiniAction.itemAnimator = null
                }
                animateShow(animate = animate, onAnimationStart = { show() })
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private fun initRecyclerView() {
        binding.rvBulkReviewMiniAction.layoutManager = FlexboxLayoutManager(context).apply {
            alignItems = AlignItems.FLEX_START
        }
        binding.rvBulkReviewMiniAction.adapter = adapter
    }

    interface Listener {
        fun onClickTestimonyMiniAction()
        fun onClickAddAttachmentMiniAction()
    }
}
