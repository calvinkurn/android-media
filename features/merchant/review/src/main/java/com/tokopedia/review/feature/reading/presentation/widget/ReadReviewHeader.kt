package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetReadReviewHeaderBinding
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryHeaderListener
import com.tokopedia.review.feature.reading.data.AvailableFilters
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHighlightedTopicListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx
import timber.log.Timber

class ReadReviewHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        const val RATING_STAR_WIDTH = 24
        const val RATING_STAR_HEIGHT = 24
        const val SHOULD_SHOW_TOPIC_COUNT = 2
    }

    private val binding = WidgetReadReviewHeaderBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var isProductReview: Boolean = true
    private var topicFilterChipIndex: Int = 0

    init {
        setupViews()
    }

    fun setIsProductReview(isProductReview: Boolean){
        this.isProductReview = isProductReview
    }

    fun showShopPageReviewHeader() {
        binding.readReviewHeaderRating.gone()
        binding.readReviewHeaderChevronRight.gone()
        binding.readReviewHighlightedTopicLeft.gone()
        binding.readReviewHighlightedTopicRight.gone()
        binding.readReviewFilterDivider.gone()

        val dimen8dp =
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
        val dimen12dp =
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
        val dimen16dp =
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        binding.containerReviewRating.setMargin(dimen16dp, dimen12dp, dimen16dp, dimen12dp)
        binding.containerReviewRating.setPadding(dimen12dp, dimen8dp, dimen12dp, dimen8dp)
        binding.readReviewSatisfactionRate.let {
            it.setMargin(Int.ZERO, Int.ZERO, it.right, it.bottom)
        }
        binding.readReviewRatingAndReviewCount?.let {
            it.setMargin(Int.ZERO, it.top, it.right, Int.ZERO)
        }
        binding.readReviewShopChevron.visible()

        showRatingContainerBorderLine()
    }

    fun hideRatingContainer() {
        binding.containerReviewRating.gone()
        binding.readReviewFilterDivider.gone()
    }

    fun showRatingContainer() {
        binding.containerReviewRating.visible()
        binding.readReviewFilterDivider.visible()
    }

    private fun showRatingContainerBorderLine() {
        try {
            binding.containerReviewRating.setBackgroundResource(R.drawable.bg_review_header_bordered)
        }catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun setupViews() {
        binding.readReviewSortFilter.sortFilterPrefix.viewTreeObserver?.addOnGlobalLayoutListener {
            val sortFilterPrefixVisibility = binding.readReviewSortFilter.sortFilterPrefix.visibility
            if (sortFilterPrefixVisibility == View.GONE) {
                configPaddingForGoneSortFilterPrefix()
            } else {
                configPaddingForVisibleSortFilterPrefix()
            }
        }
    }

    private fun configPaddingForVisibleSortFilterPrefix() {
        val paddingTop = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        binding.readReviewSortFilter.setPadding(paddingTop, 0, 0, 0)
        binding.readReviewSortFilter.sortFilterItems.setPadding(0, 0, 0, 0)
    }

    private fun configPaddingForGoneSortFilterPrefix() {
        val paddingTop = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        binding.readReviewSortFilter.setPadding(0, 0, 0, 0)
        binding.readReviewSortFilter.sortFilterItems.setPadding(paddingTop, 0, 0, 0)
    }

    private fun mapAvailableFiltersToSortFilter(topics: List<ProductTopic>, availableFilters: AvailableFilters, listener: ReadReviewFilterChipsListener): ArrayList<SortFilterItem> {
        val filter = arrayListOf<SortFilterItem>()
        if (availableFilters.withAttachment) {
            val withAttachmentFilterOption = SortFilterItem(context.getString(R.string.review_reading_filter_with_attachment), ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_SMALL)
            withAttachmentFilterOption.apply {
                this.listener = { listener.onFilterWithAttachmentClicked(this.type == ChipsUnify.TYPE_SELECTED) }
            }
            filter.add(withAttachmentFilterOption)
        }
        if (availableFilters.rating) {
            val ratingFilter = getSortFilterItem(context.getString(R.string.review_reading_filter_all_ratings))
            setListenerAndChevronListener(ratingFilter) {
                listener.onFilterWithRatingClicked(getIndexOfSortFilter(ratingFilter), isChipsActive(ratingFilter.type))
            }
            filter.add(ratingFilter)
        }
        if (availableFilters.topics) {
            val topicFilter = getSortFilterItem(context.getString(R.string.review_reading_filter_all_topics))
            setListenerAndChevronListener(topicFilter) { listener.onFilterWithTopicClicked(topics, getIndexOfSortFilter(topicFilter), isChipsActive(topicFilter.type)) }
            filter.add(topicFilter)
            topicFilterChipIndex = filter.indexOf(topicFilter)
        }
        val sortOption = getSortFilterItem(getDefaultSortTitle())
        setListenerAndChevronListener(sortOption) { listener.onSortClicked(mapSortTitleToBottomSheetInput(sortOption)) }
        filter.add(sortOption)
        return filter
    }

    private fun getSortFilterItem(text: String): SortFilterItem {
        return SortFilterItem(text, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_SMALL)
    }

    private fun setListenerAndChevronListener(sortFilterItem: SortFilterItem, action: () -> Unit) {
        sortFilterItem.apply {
            this.listener = { action.invoke() }
            this.chevronListener = { action.invoke() }
        }
    }

    private fun updateFilterChip(sortFilterItem: SortFilterItem?, isEmpty: Boolean, title: String, drawable: Drawable? = null) {
        sortFilterItem?.apply {
            this.title = title
            drawable?.let {
                refChipUnify.chip_image_icon.apply {
                    layoutParams.apply {
                        width = RATING_STAR_WIDTH.toPx()
                        height = RATING_STAR_HEIGHT.toPx()
                    }
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }
                iconDrawable = it
            }
            type = if (isEmpty) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
        }
    }

    private fun getIndexOfSortFilter(sortFilterItem: SortFilterItem): Int {
        return binding.readReviewSortFilter.chipItems?.indexOf(sortFilterItem) ?: 0
    }

    private fun isChipsActive(chipType: String): Boolean {
        return chipType == ChipsUnify.TYPE_SELECTED
    }

    private fun getTopicFilterTitleBasedOnCount(selectedFilter: Set<ListItemUnify>): String {
        return if (selectedFilter.size > 1) {
            context.getString(R.string.review_reading_filter_multiple_topic_selected, selectedFilter.size)
        } else {
            selectedFilter.firstOrNull()?.listTitleText
                    ?: context.getString(R.string.review_reading_filter_all_topics)
        }
    }

    private fun getRatingFilterTitleAndDrawableBasedOnCount(selectedFilter: Set<ListItemUnify>): Pair<String, Drawable?> {
        return when {
            selectedFilter.size > 1 -> {
                Pair(context.getString(R.string.review_reading_filter_multiple_rating_selected, selectedFilter.size), null)
            }
            selectedFilter.isEmpty() -> {
                Pair(context.getString(R.string.review_reading_filter_all_ratings), null)
            }
            else -> {
                Pair(selectedFilter.firstOrNull()?.listTitleText
                        ?: context.getString(R.string.review_reading_filter_all_ratings), getIconUnifyDrawable(context, IconUnify.STAR_FILLED, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300)))
            }
        }
    }

    private fun mapSortTitleToBottomSheetInput(sortOption: SortFilterItem): String {
        return if (sortOption.title == getDefaultSortTitle()) {
            getDefaultSort()
        } else {
            sortOption.title.toString()
        }
    }

    private fun getDefaultSort(): String {
        return if (isProductReview)
            SortTypeConstants.MOST_HELPFUL_COPY
        else
            SortTypeConstants.LATEST_COPY
    }

    private fun getDefaultSortTitle(): String {
        return context.getString(R.string.review_reading_sort_default)
    }

    fun setRatingData(productRating: ProductRating) {
        binding.readReviewHeaderRating.setRating(productRating.ratingScore)
        binding.readReviewSatisfactionRate.text = productRating.satisfactionRate
        binding.readReviewRatingAndReviewCount.text = context.getString(R.string.review_reading_rating_and_review_count, productRating.totalRatingFmt, productRating.totalRatingTextAndImageFmt)
    }

    fun setListener(readReviewHeaderListener: ReadReviewHeaderListener) {
        binding.readReviewSatisfactionRate.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
        binding.readReviewHeaderChevronRight.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
        binding.containerReviewRating.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
        binding.readReviewShopChevron.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
    }

    fun setAvailableFilters(topics: List<ProductTopic>, availableFilters: AvailableFilters, listener: ReadReviewFilterChipsListener) {
        binding.readReviewSortFilter.apply {
            sortFilterItems.removeAllViews()
            sortFilterPrefix.setOnClickListener {
                resetAllFilters()
            }
            addItem(mapAvailableFiltersToSortFilter(topics, availableFilters, listener))
            dismissListener = {
                listener.onClearFiltersClicked()
            }
        }
    }

    fun updateFilter(selectedFilter: Set<ListItemUnify>, sortFilterBottomSheetType: SortFilterBottomSheetType, index: Int) {
        if (sortFilterBottomSheetType is SortFilterBottomSheetType.RatingFilterBottomSheet) {
            val titleAndDrawable = getRatingFilterTitleAndDrawableBasedOnCount(selectedFilter)
            updateFilterChip(binding.readReviewSortFilter.chipItems?.get(index), selectedFilter.isEmpty(), titleAndDrawable.first, titleAndDrawable.second)
        } else {
            updateFilterChip(binding.readReviewSortFilter.chipItems?.get(index), selectedFilter.isEmpty(), getTopicFilterTitleBasedOnCount(selectedFilter))
        }
    }

    fun updateFilterWithMedia() {
        binding.readReviewSortFilter.chipItems?.firstOrNull()?.toggleSelected()
    }

    fun updateSelectedSort(selectedSort: String) {
        val defaultSelectedSort = getDefaultSort()
        binding.readReviewSortFilter.chipItems?.lastOrNull()?.apply {
            if (selectedSort == defaultSelectedSort) {
                title = getDefaultSortTitle()
                type = ChipsUnify.TYPE_NORMAL
            } else {
                title = selectedSort
                type = ChipsUnify.TYPE_SELECTED
            }
        }
    }

    fun setSeeAll(show: Boolean, listener: ReviewGalleryHeaderListener? = null) {
        binding.readReviewSeeAll.apply {
            showWithCondition(show)
            setOnClickListener {
                listener?.onSeeAllClicked()
            }
        }
    }

    fun setHighlightedTopics(topics: List<ProductTopic>, listener: ReadReviewHighlightedTopicListener) {
        val highlightedTopic = listOf(binding.readReviewHighlightedTopicLeft, binding.readReviewHighlightedTopicRight)
        topics.filter { it.shouldShow }.take(SHOULD_SHOW_TOPIC_COUNT).mapIndexed { index, productTopic ->
            highlightedTopic.getOrNull(index)?.apply {
                setHighlightedTopic(productTopic)
                this.setListener(listener, index)
                show()
            }
        }
    }

    fun updateFilterFromHighlightedTopic(title: String) {
        binding.readReviewSortFilter.chipItems?.getOrNull(topicFilterChipIndex)?.apply {
            this.title = title
            type = ChipsUnify.TYPE_SELECTED
        }
    }

    fun isSortFilterActive(): Boolean {
        return binding.readReviewSortFilter.chipItems?.lastOrNull()?.title != getDefaultSortTitle()
    }

    fun getReviewRatingContainer(): ConstraintLayout {
        return binding.containerReviewRating
    }
}