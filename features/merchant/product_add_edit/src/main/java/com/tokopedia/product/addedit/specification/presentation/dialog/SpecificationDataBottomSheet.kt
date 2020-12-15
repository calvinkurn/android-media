package com.tokopedia.product.addedit.specification.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.add_edit_product_specification_data_bottom_sheet_content.*

class SpecificationDataBottomSheet(
        private val title: String,
        private val data: List<String>,
        private val specificationDataListener: SpecificationDataListener? = null
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Specification Data"
    }

    private var contentView: View? = null
    private var dataList: ArrayList<ListItemUnify> = arrayListOf()

    interface SpecificationDataListener {
        fun onSpecificationDataSelected(position: Int, specificationData: String)
    }

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addMarginCloseButton()
        setupList()
        setupSearch()
    }

    private fun setupSearch() {
        searchBarData.searchBarTextField.afterTextChanged { text ->
            val filteredList = dataList.filter { listItem ->
                listItem.listTitleText.startsWith(text)
            }
            listUnifyData.setData(ArrayList(filteredList))
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun addMarginCloseButton() {
        val topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(0, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        setTitle(title)
        overlayClickDismiss = false
        contentView = View.inflate(context,
                R.layout.add_edit_product_specification_data_bottom_sheet_content, null)
        setChild(contentView)
    }

    private fun setupList() {
        dataList = mapToListItems(data)
        listUnifyData.setData(dataList)
        listUnifyData.onLoadFinish {
            listUnifyData.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = dataList[position]
                selectedItem.listRightRadiobtn?.performClick()
            }

            dataList.forEachIndexed { position, listItemUnify ->
                var res = android.R.color.transparent
                if (position % 2 == 0) {
                    res = com.tokopedia.unifycomponents.R.drawable.ic_check_green
                }
                listItemUnify.listChevron?.setImageResource(res)

                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    dismiss()
                    specificationDataListener?.onSpecificationDataSelected(position, listItemUnify.listTitleText)
                }
            }
        }
    }

    private fun mapToListItems(selections: List<String>) =
            ArrayList(selections.map { createListItem(it) })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.CHEVRON, text)
        listItem.isBold = false
        return listItem
    }
}