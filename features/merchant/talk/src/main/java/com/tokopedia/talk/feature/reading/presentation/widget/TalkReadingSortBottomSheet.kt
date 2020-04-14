package com.tokopedia.talk.feature.reading.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_talk_sort_bottomsheet.*

class TalkReadingSortBottomSheet(
        private val onFinishedListener: OnFinishedListener,
        private val sortOptions: List<SortOption>
) : BottomSheetUnify() {

    companion object {

        fun createInstance(context: Context, sortOptions: List<SortOption>, onFinishedListener: OnFinishedListener) : TalkReadingSortBottomSheet {
            return TalkReadingSortBottomSheet(onFinishedListener, sortOptions).apply{
                val view = View.inflate(context, R.layout.widget_talk_sort_bottomsheet,null)
                setChild(view)
                setTitle(context.getString(R.string.reading_sort_bottom_sheet_title))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initView() {
        val data = TalkReadingMapper.mapSortOptionsToListUnifyItems(sortOptions)
        talkReadingSortOptions.setData(data)
        talkReadingSortOptions.setOnItemClickListener { parent, view, position, id ->
            val chosenSortOption = sortOptions[position]
            onFinishedListener.onFinishChooseSort(chosenSortOption)
            this.dismiss()
        }
        talkReadingSortOptions.onLoadFinish {
            data.forEachIndexed { index, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    val chosenSortOption = getSortOptionFromListUnify(listItemUnify.listTitleText)
                    onFinishedListener.onFinishChooseSort(chosenSortOption)
                    this.dismiss()
                }
                listItemUnify.listRightRadiobtn?.isChecked = sortOptions[index].isSelected
            }
        }
    }

    private fun getSortOptionFromListUnify(title: String): SortOption {
        return when(title) {
            SortOption.INFORMATIVENESS_DISPLAY_NAME -> {
                SortOption.SortByInformativeness(isSelected = true)
            }
            SortOption.TIME_DISPLAY_NAME -> {
                SortOption.SortByTime(isSelected = true)
            }
            else -> {
                SortOption.SortByLike(isSelected = true)
            }
        }
    }
}