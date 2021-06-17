package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.AvailableFilters
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewHeader : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var rating: Typography? = null
    private var satisfactionRate: Typography? = null
    private var ratingAndReviewCount: Typography? = null
    private var chevron: IconUnify? = null
    private var sortFilter: SortFilter? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_header, this)
        bindViews()
    }

    private fun bindViews() {
        rating = findViewById(R.id.read_review_overall_rating)
        satisfactionRate = findViewById(R.id.read_review_satisfaction_rate)
        ratingAndReviewCount = findViewById(R.id.read_review_rating_and_review_count)
        chevron = findViewById(R.id.read_review_header_chevron_right)
        sortFilter = findViewById(R.id.read_review_sort_filter)
    }

    private fun mapAvailableFiltersToSortFilter(topics: List<ProductTopic>, availableFilters: AvailableFilters, listener: ReadReviewFilterChipsListener): ArrayList<SortFilterItem> {
        val filter = arrayListOf<SortFilterItem>()
        if (availableFilters.withAttachment) {
            filter.add(SortFilterItem(context.getString(R.string.review_reading_filter_with_attachment), ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_SMALL).apply {
                this.listener = {
                    listener.onFilterWithAttachmentClicked()
                    toggleSelected()
                }
            })
        }
        if (availableFilters.rating) {
            filter.add(getSortFilterItem(context.getString(R.string.review_reading_filter_all_ratings)) { listener.onFilterWithRatingClicked() })
        }
        if (availableFilters.topics) {
            filter.add(getSortFilterItem(context.getString(R.string.review_reading_filter_all_topics)) { listener.onFilterWithTopicClicked(topics) })
        }
        val sortOption = getSortFilterItem(context.getString(R.string.review_reading_sort_most_helpful))
        sortOption.listener = { listener.onSortClicked(sortOption.title.toString()) }
        filter.add(sortOption)
        return filter
    }

    private fun getSortFilterItem(text: String, action: () -> Unit = {}): SortFilterItem {
        return SortFilterItem(text, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_SMALL).apply {
            listener = { action.invoke() }
            chevronListener = { action.invoke() }
        }
    }

    fun setRatingData(productRating: ProductRating) {
        rating?.text = productRating.ratingScore
        this.satisfactionRate?.text = productRating.satisfactionRate
        this.ratingAndReviewCount?.text = context.getString(R.string.review_reading_rating_and_review_count, productRating.totalRating, productRating.totalRatingTextAndImage)
    }

    fun setListener(readReviewHeaderListener: ReadReviewHeaderListener) {
        chevron?.setOnClickListener {
            readReviewHeaderListener.onChevronClicked()
        }
    }

    fun setAvailableFilters(topics: List<ProductTopic>, availableFilters: AvailableFilters, listener: ReadReviewFilterChipsListener) {
        sortFilter?.addItem(mapAvailableFiltersToSortFilter(topics, availableFilters, listener))
    }

    fun updateFilter() {

    }

    fun updateSelectedSort(selectedSort: String) {
        sortFilter?.chipItems?.lastOrNull()?.apply {
            title = selectedSort
            type = if(selectedSort == SortTypeConstants.MOST_HELPFUL_COPY) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
        }
    }
}