package com.tokopedia.talk.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.sortfilter.SortFilter.Companion.RELATIONSHIP_AND
import com.tokopedia.sortfilter.SortFilter.Companion.TYPE_QUICK
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper.SORT_CATEGORY
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_reading_header.view.*

class TalkReadingHeader : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_talk_reading_header, this)
    }

    fun bind(talkReadingHeaderModel: TalkReadingHeaderModel) {
        this.readingHeaderProductImage.loadImage(talkReadingHeaderModel.productImageUrl)
        this.readingHeaderProductName.text = talkReadingHeaderModel.productName
        talkReadingHeaderModel.categories.forEach {
            this.readingHeaderChips.chipItems.add(it)
            if(it.title.contains(SORT_CATEGORY)) {
                it.refChipUnify.setChevronClickListener {  }
            }
        }
        initSortFilter()
    }

    fun updateSelectedSort(itemUnify: SortFilterItem) {
        this.readingHeaderChips.chipItems.removeAt(0)
        this.readingHeaderChips.chipItems.add(0, itemUnify)
        this.readingHeaderChips.chipItems.first().refChipUnify.setChevronClickListener {  }
    }

    private fun initSortFilter() {
        this.readingHeaderChips.apply {
            filterRelationship = RELATIONSHIP_AND
            filterType = TYPE_QUICK
            visibility = View.VISIBLE
        }
    }

}