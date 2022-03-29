package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryHeaderListener
import com.tokopedia.review.feature.reading.data.AvailableFilters
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHighlightedTopicListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ReadReviewHeader : BaseCustomView {

    companion object {
        const val RATING_STAR_WIDTH = 24
        const val RATING_STAR_HEIGHT = 24
        const val SHOULD_SHOW_TOPIC_COUNT = 2
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var rating: ReadReviewRating? = null
    private var satisfactionRate: Typography? = null
    private var ratingAndReviewCount: Typography? = null
    private var chevron: IconUnify? = null
    private var sortFilter: SortFilter? = null
    private var seeAll: Typography? = null
    private var topicLeft: ReadReviewHighlightedTopic? = null
    private var topicRight: ReadReviewHighlightedTopic? = null

    private var isProductReview: Boolean = true
    private var topicFilterChipIndex: Int = 0

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_header, this)
        bindViews()
    }

    fun setIsProductReview(isProductReview: Boolean){
        this.isProductReview = isProductReview
    }

    fun hideRatingView() {
        rating?.gone()
    }

    private fun bindViews() {
        rating = findViewById(R.id.read_review_header_rating)
        satisfactionRate = findViewById(R.id.read_review_satisfaction_rate)
        ratingAndReviewCount = findViewById(R.id.read_review_rating_and_review_count)
        chevron = findViewById(R.id.read_review_header_chevron_right)
        sortFilter = findViewById(R.id.read_review_sort_filter)
        sortFilter?.sortFilterPrefix?.viewTreeObserver?.addOnGlobalLayoutListener {
            val sortFilterPrefixVisibility = sortFilter?.sortFilterPrefix?.visibility
            if(sortFilterPrefixVisibility == View.GONE){
                configPaddingForGoneSortFilterPrefix()
            }else{
                configPaddingForVisibleSortFilterPrefix()
            }
        }
        seeAll = findViewById(R.id.read_review_see_all)
        topicLeft = findViewById(R.id.read_review_highlighted_topic_left)
        topicRight = findViewById(R.id.read_review_highlighted_topic_right)
    }

    private fun configPaddingForVisibleSortFilterPrefix() {
        val paddingTop = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        sortFilter?.setPadding(paddingTop, 0, 0, 0)
        sortFilter?.sortFilterItems?.setPadding(0, 0, 0, 0)
    }

    private fun configPaddingForGoneSortFilterPrefix() {
        val paddingTop = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        sortFilter?.setPadding(0, 0, 0, 0)
        sortFilter?.sortFilterItems?.setPadding(paddingTop, 0, 0, 0)
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
        val sortOption = getSortFilterItem(context.getString(R.string.review_reading_sort_default))
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
        return sortFilter?.chipItems?.indexOf(sortFilterItem) ?: 0
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
                        ?: context.getString(R.string.review_reading_filter_all_ratings), getIconUnifyDrawable(context, IconUnify.STAR_FILLED, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300)))
            }
        }
    }

    private fun mapSortTitleToBottomSheetInput(sortOption: SortFilterItem): String {
        return if (sortOption.title == context.getString(R.string.review_reading_sort_default)) {
            if(!isProductReview)
                SortTypeConstants.LATEST_COPY
            else
                SortTypeConstants.MOST_HELPFUL_COPY
        } else {
            sortOption.title.toString()
        }
    }

    fun setRatingData(productRating: ProductRating) {
        rating?.setRating(productRating.ratingScore)
        this.satisfactionRate?.text = productRating.satisfactionRate
        this.ratingAndReviewCount?.text = context.getString(R.string.review_reading_rating_and_review_count, productRating.totalRatingFmt, productRating.totalRatingTextAndImageFmt)
    }

    fun setListener(readReviewHeaderListener: ReadReviewHeaderListener) {
        satisfactionRate?.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
        chevron?.setOnClickListener {
            readReviewHeaderListener.openStatisticsBottomSheet()
        }
    }

    fun setAvailableFilters(topics: List<ProductTopic>, availableFilters: AvailableFilters, listener: ReadReviewFilterChipsListener) {
        sortFilter?.apply {
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
            updateFilterChip(sortFilter?.chipItems?.get(index), selectedFilter.isEmpty(), titleAndDrawable.first, titleAndDrawable.second)
        } else {
            updateFilterChip(sortFilter?.chipItems?.get(index), selectedFilter.isEmpty(), getTopicFilterTitleBasedOnCount(selectedFilter))
        }
    }

    fun updateFilterWithImage() {
        sortFilter?.chipItems?.firstOrNull()?.toggleSelected()
    }

    fun updateSelectedSort(selectedSort: String) {
        val defaultSelectedSort = if (isProductReview)
            SortTypeConstants.MOST_HELPFUL_COPY
        else
            SortTypeConstants.LATEST_COPY
        sortFilter?.chipItems?.lastOrNull()?.apply {
            if (selectedSort == defaultSelectedSort) {
                title = context.getString(R.string.review_reading_sort_default)
                type = ChipsUnify.TYPE_NORMAL
            } else {
                title = selectedSort
                type = ChipsUnify.TYPE_SELECTED
            }
        }
    }

    fun setSeeAll(listener: ReviewGalleryHeaderListener) {
        seeAll?.apply {
            show()
            setOnClickListener {
                listener.onSeeAllClicked()
            }
        }
    }

    fun setHighlightedTopics(topics: List<ProductTopic>, listener: ReadReviewHighlightedTopicListener) {
        val highlightedTopic = listOf(topicLeft, topicRight)
        topics.filter { it.shouldShow }.take(SHOULD_SHOW_TOPIC_COUNT).mapIndexed { index, productTopic ->
            highlightedTopic.getOrNull(index)?.apply {
                setHighlightedTopic(productTopic)
                this.setListener(listener, index)
                show()
            }
        }
    }

    fun updateFilterFromHighlightedTopic(title: String) {
        sortFilter?.chipItems?.getOrNull(topicFilterChipIndex)?.apply {
            this.title = title
            type = ChipsUnify.TYPE_SELECTED
        }
    }
}