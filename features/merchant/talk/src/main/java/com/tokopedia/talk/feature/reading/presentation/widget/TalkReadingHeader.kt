package com.tokopedia.talk.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.sortfilter.SortFilter.Companion.RELATIONSHIP_AND
import com.tokopedia.sortfilter.SortFilter.Companion.TYPE_QUICK
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper.SORT_LATEST
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
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

    fun bind(talkReadingHeaderModel: TalkReadingHeaderModel, onCategoryModifiedListener: OnCategoryModifiedListener, showBottomSheet: () -> Unit) {
        this.readingHeaderProductImage.loadImage(talkReadingHeaderModel.productImageUrl)
        this.readingHeaderProductName.text = talkReadingHeaderModel.productName
        initSortFilter(talkReadingHeaderModel, onCategoryModifiedListener, showBottomSheet)
    }

    fun updateSelectedSort(chipText: String) {
        val sortChip = this.readingHeaderChips.chipItems?.first()
        if(chipText != SORT_LATEST) {
            sortChip?.type = ChipsUnify.TYPE_SELECTED
            sortChip?.selectedItem = arrayListOf(chipText)
        } else {
            sortChip?.type = ChipsUnify.TYPE_NORMAL
        }
    }

    fun clearAllSort(talkReadingHeaderModel: TalkReadingHeaderModel, onCategoryModifiedListener: OnCategoryModifiedListener, showBottomSheet: () -> Unit) {
        initSortFilter(talkReadingHeaderModel, onCategoryModifiedListener, showBottomSheet)
    }

    private fun initSortFilter(talkReadingHeaderModel: TalkReadingHeaderModel, onCategoryModifiedListener: OnCategoryModifiedListener, showBottomSheet: () -> Unit) {
        this.readingHeaderChips.apply {
            sortFilterItems.removeAllViews()
            addItem(talkReadingHeaderModel.categories)
            chipItems?.first()?.refChipUnify?.setChevronClickListener {
                showBottomSheet()
            }
            filterRelationship = RELATIONSHIP_AND
            filterType = TYPE_QUICK
            dismissListener = {
                onCategoryModifiedListener.onCategoriesCleared()
            }
            visibility = View.VISIBLE
        }
    }

}