package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.databinding.WidgetCreateReviewBadRatingCategoriesBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewBadRatingCategoryAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewBadRatingCategoriesTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewItemAnimator
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoriesUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewBadRatingCategoryViewHolder

class CreateReviewBadRatingCategories @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewBadRatingCategoriesBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val SPAN_COUNT = 2
    }

    private val badRatingCategoryListener = BadRatingCategoryListener()
    private val layoutManager = GridLayoutManager(context, SPAN_COUNT, RecyclerView.VERTICAL, false)
    private val itemAnimator = CreateReviewItemAnimator()
    private val typeFactory = CreateReviewBadRatingCategoriesTypeFactory(badRatingCategoryListener)
    private val adapter = CreateReviewBadRatingCategoryAdapter(typeFactory)

    override val binding = WidgetCreateReviewBadRatingCategoriesBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.rvBadRatingCategories.layoutManager = layoutManager
        binding.rvBadRatingCategories.itemAnimator = itemAnimator
        binding.rvBadRatingCategories.adapter = adapter
    }

    private fun showBadRatingCategories(badRatingCategories: List<CreateReviewBadRatingCategoryUiModel>) {
        adapter.updateItems(badRatingCategories)
    }

    fun updateUi(uiState: CreateReviewBadRatingCategoriesUiState) {
        when (uiState) {
            is CreateReviewBadRatingCategoriesUiState.Loading -> {
                // noop
            }
            is CreateReviewBadRatingCategoriesUiState.Showing -> {
                showBadRatingCategories(uiState.badRatingCategories)
                animateShow()
            }
            is CreateReviewBadRatingCategoriesUiState.Hidden -> {
                animateHide()
            }
        }
    }

    fun setListener(newListener: CreateReviewBadRatingCategoryViewHolder.Listener) {
        badRatingCategoryListener.listener = newListener
    }

    private inner class BadRatingCategoryListener : CreateReviewBadRatingCategoryViewHolder.Listener {
        var listener: CreateReviewBadRatingCategoryViewHolder.Listener? = null

        override fun onImpressBadRatingCategory(description: String) {
            listener?.onImpressBadRatingCategory(description)
        }

        override fun onBadRatingCategoryClicked(
            description: String,
            checked: Boolean,
            id: String,
            shouldRequestFocus: Boolean
        ) {
            listener?.onBadRatingCategoryClicked(description, checked, id, shouldRequestFocus)
        }
    }
}