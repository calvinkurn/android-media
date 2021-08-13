package com.tokopedia.product.addedit.specification.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.presentation.adapter.viewholder.SpecificationValueViewHolder
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel

class SpecificationValueAdapter(private val fragmentManager: FragmentManager?) :
        RecyclerView.Adapter<SpecificationValueViewHolder>(),
        SpecificationValueViewHolder.OnSpecificationValueViewHolderClickListener {

    private var items: MutableList<AnnotationCategoryData> = mutableListOf()
    private var itemsSelected: MutableList<SpecificationInputModel> = mutableListOf()
    private var mContext: Context? = null
    private var onSpecificationChanged: (itemsSelected: List<SpecificationInputModel>) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_specification_value, parent, false)
        mContext = parent.context
        return SpecificationValueViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecificationValueViewHolder, position: Int) {
        val item = getData(position)
        val itemValue = getDataSelected(position).data
        holder.bindData(item.variant, itemValue)
    }

    override fun onSpecificationValueTextClicked(position: Int) {
        items.getOrNull(position)?.let { annotationData ->
            showSpecificationOption(annotationData.variant, annotationData.data, getDataSelected(position).id, position)
        }
    }

    override fun onSpecificationValueTextCleared(position: Int) {
        itemsSelected.getOrNull(position)?.let {
            it.id = ""
            it.data = ""
        }
        onSpecificationChanged(itemsSelected)
    }

    fun setData(items: List<AnnotationCategoryData>) {
        this.items = items.toMutableList()
        // generate item to collect input data
        if (itemsSelected.isEmpty()) {
            itemsSelected = MutableList(itemCount) {
                SpecificationInputModel()
            }
        }
        notifyDataSetChanged()
    }

    fun setData(items: List<AnnotationCategoryData>, itemsSelected: List<SpecificationInputModel>) {
        this.itemsSelected = itemsSelected.toMutableList()
        setData(items)
    }

    fun setDataSelected(position: Int, itemId: String, itemData: String) {
        itemsSelected.getOrNull(position)?.apply {
            id = itemId
            data = itemData
            notifyItemChanged(position)
        }
        onSpecificationChanged(itemsSelected)
    }

    fun getData(position: Int): AnnotationCategoryData {
        return items.getOrNull(position) ?: AnnotationCategoryData()
    }

    fun getDataSelected(position: Int): SpecificationInputModel {
        return itemsSelected.getOrNull(position) ?: SpecificationInputModel()
    }

    fun getDataSelectedList(): List<SpecificationInputModel> {
        return itemsSelected
    }

    fun showOnSpecificationChanged(onChanged: (itemsSelected: List<SpecificationInputModel>) -> Unit) {
        onSpecificationChanged = onChanged
    }

    private fun showSpecificationOption(title: String, annotationData: List<Values>, selectedId: String, adapterPosition: Int) {
        val optionTitle = mContext?.getString(R.string.title_specification_bottom_sheet) + " " + title
        var selectedPosition: Int? = null
        val options = annotationData.mapIndexed { index, value ->
            if (value.id.toString() == selectedId) selectedPosition = index
            value.name
        }
        val optionPicker = OptionPicker().apply {
            selectedPosition?.let { setSelectedPosition(it) }
            setDividerVisible(true)
            setSearchable(true)
            setTitle(optionTitle)
            setItemMenuList(options)
            setOnItemClickListener { selectedText, _ ->
                annotationData.firstOrNull {
                    it.name == selectedText
                }?.let {
                    setDataSelected(adapterPosition, it.id.toString(), it.name)
                }
            }
        }

        fragmentManager?.let { optionPicker.show(it, null) }
    }
}