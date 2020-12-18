package com.tokopedia.product.addedit.specification.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.add_edit_product_specification_data_bottom_sheet_content.*
import java.util.*
import kotlin.collections.ArrayList

class SpecificationDataBottomSheet(
        private val title: String,
        private val ids: List<String>,
        private val data: List<String>,
        private val selectedId: String?,
        private val specificationDataListener: SpecificationDataListener? = null
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Specification Data"
    }

    interface SpecificationDataListener {
        fun onSpecificationDataSelected(specificationId: String, specificationData: String)
    }

    private var contentView: View? = null
    private var dataList: ArrayList<ListItemUnify> = arrayListOf()
    private var filteredList: List<ListItemUnify> = listOf()

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
        setupList()
        setupSearch()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.title_specification_bottom_sheet) + " " + title)
        overlayClickDismiss = false
        contentView = View.inflate(context,
                R.layout.add_edit_product_specification_data_bottom_sheet_content, null)
        setChild(contentView)
    }

    private fun setupSearch() {
        val locale = Locale.getDefault()
        searchBarData.searchBarPlaceholder =
                getString(R.string.label_specification_search) + " " + title.toLowerCase(locale)
        searchBarData.searchBarTextField.afterTextChanged { text ->
            filteredList = dataList.filter { listItem ->
                listItem.listTitleText.toLowerCase(locale)
                        .startsWith(text.toLowerCase(locale))
            }
            listUnifyData.setData(ArrayList(filteredList))

        }
    }

    private fun setupList() {
        dataList = mapToListItems(data)
        filteredList = dataList
        listUnifyData.setData(dataList)
        listUnifyData.onLoadFinish {
            listUnifyData.setOnItemClickListener { _, _, position, _ ->
                filteredList.getOrNull(position)?.let { selectedItem ->
                    val selectedItemId = selectedItem.listActionText.orEmpty()
                    val selectedItemData = selectedItem.listTitleText
                    specificationDataListener?.onSpecificationDataSelected(selectedItemId, selectedItemData)
                }
                dismiss()
            }

            dataList.forEachIndexed { index, listItemUnify ->
                listItemUnify.listActionText = ids[index]
                listItemUnify.listChevron?.setImageResource(if (listItemUnify.listActionText == selectedId) {
                    com.tokopedia.unifycomponents.R.drawable.ic_check_green
                } else {
                    android.R.color.transparent
                })
            }
        }
    }

    private fun mapToListItems(datum: List<String>) =
            ArrayList(datum.map { createListItem(it) })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.CHEVRON, text)
        listItem.isBold = false
        return listItem
    }
}