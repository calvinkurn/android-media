package com.tokopedia.talk.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilter.Companion.RELATIONSHIP_AND
import com.tokopedia.sortfilter.SortFilter.Companion.TYPE_QUICK
import com.tokopedia.talk.databinding.WidgetTalkReadingHeaderBinding
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper.SORT_LATEST
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify

class TalkReadingHeader : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val talkReadingHeaderBinding = WidgetTalkReadingHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    fun bind(talkReadingHeaderModel: TalkReadingHeaderModel, onCategoryModifiedListener: OnCategoryModifiedListener, showBottomSheet: () -> Unit) {
        talkReadingHeaderBinding.readingHeaderProductImage.loadImage(talkReadingHeaderModel.productImageUrl)
        talkReadingHeaderBinding.readingHeaderProductName.text = talkReadingHeaderModel.productName
        initSortFilter(talkReadingHeaderModel, onCategoryModifiedListener, showBottomSheet)
    }

    fun updateSelectedSort(chipText: String) {
        val sortChip = talkReadingHeaderBinding.readingHeaderChips.chipItems?.firstOrNull()
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
        talkReadingHeaderBinding.readingHeaderChips.apply {
            sortFilterItems.removeAllViews()
            addItem(talkReadingHeaderModel.categories)
            chipItems?.firstOrNull()?.refChipUnify?.setChevronClickListener {
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