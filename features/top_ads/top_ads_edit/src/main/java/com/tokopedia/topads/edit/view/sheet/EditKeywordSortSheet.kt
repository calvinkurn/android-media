package com.tokopedia.topads.edit.view.sheet


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.view.sheet.StaticInfoBottomSheet
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import kotlinx.android.synthetic.main.topads_edit_keyword_edit_sort_sheet.view.*
import java.util.*

class EditKeywordSortSheet : BottomSheetUnify() {

    var onItemClick: ((sortId: String) -> Unit)? = null
    var selectedSortText = 0
    var positionSort = 0
    var selected: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_edit_keyword_edit_sort_sheet, null)
        setChild(contentView)
        setTitle(getString(R.string.topads_edit_info_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sortListItemUnify = mapToItemUnifyList()

        view.sortList.setData(sortListItemUnify)

        view.goToStaticSheet?.text = MethodChecker.fromHtml(getString(com.tokopedia.topads.common.R.string.topads_common_choose_type_bs_extra))

        view.goToStaticSheet?.setOnClickListener {
            val sheet = StaticInfoBottomSheet.newInstance()
            sheet.show(childFragmentManager)
        }

        view.sortList?.let {
            it.onLoadFinish {

                it.setSelectedFilterOrSort(sortListItemUnify, positionSort.orZero())


                it.setOnItemClickListener { adapterView, view, index, l ->
                    onItemSortClickedBottomSheet(index, sortListItemUnify, it)
                }
                sortListItemUnify.forEachIndexed { index, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemSortClickedBottomSheet(index, sortListItemUnify, it)
                    }
                }
            }
        }

    }


    private fun onItemSortClickedBottomSheet(position: Int, sortListItemUnify: ArrayList<ListItemUnify>, sortListUnify: ListUnify) {
        try {
            positionSort = position
            selectedSortText = position
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            onItemClick?.invoke(sortListItemUnify[position].listTitleText)
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ListUnify.setSelectedFilterOrSort(items: List<ListItemUnify>, position: Int) {
        val clickedItem = this.getItemAtPosition(position) as ListItemUnify
        when (choiceMode) {
            ListView.CHOICE_MODE_SINGLE -> {
                items.filter {
                    it.listRightRadiobtn?.isChecked ?: false
                }.filterNot { it == clickedItem }.onEach { it.listRightRadiobtn?.isChecked = false }

                clickedItem.listRightRadiobtn?.isChecked = true
            }
        }
    }

    fun setChecked(current: String) {
        positionSort = if (current == TITLE_1) {
            0
        } else {
            1
        }
    }

    private fun mapToItemUnifyList(): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        var item1 = ListItemUnify(getString(R.string.topads_edit_keyword_edit_info_sheet_title_1), getString(R.string.topads_edit_keyword_edit_info_sheet_desc_1))
        var item2 = ListItemUnify(getString(R.string.topads_edit_keyword_edit_info_sheet_title_2), getString(R.string.topads_edit_keyword_edit_info_sheet_desc_2))

        item1.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
        item2.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
        itemUnifyList.add(0, item1)
        itemUnifyList.add(1, item2)
        return itemUnifyList
    }

    fun getSelectedSortId(): String {
        return when (selectedSortText) {
            0 -> TITLE_1
            1 -> TITLE_2
            else -> TITLE_2
        }
    }

    companion object {

        const val TITLE_1 = "Luas"
        const val TITLE_2 = "Spesifik"

        fun newInstance(): EditKeywordSortSheet {
            return EditKeywordSortSheet()
        }
    }
}
