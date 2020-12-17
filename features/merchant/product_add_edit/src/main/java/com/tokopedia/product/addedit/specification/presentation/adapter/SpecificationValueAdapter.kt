package com.tokopedia.product.addedit.specification.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.presentation.adapter.viewholder.SpecificationValueViewHolder
import com.tokopedia.product.addedit.specification.presentation.dialog.SpecificationDataBottomSheet

class SpecificationValueAdapter(private val fragmentManager: FragmentManager?) :
        RecyclerView.Adapter<SpecificationValueViewHolder>(),
        SpecificationValueViewHolder.OnSpecificationValueViewHolderClickListener,
        SpecificationDataBottomSheet.SpecificationDataListener {

    private var items: MutableList<AnnotationCategoryData> = mutableListOf()
    private var itemsSelected: MutableList<String> = mutableListOf()
    private var editedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_specification_value, parent, false)
        return SpecificationValueViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecificationValueViewHolder, position: Int) {
        val item = getData(position)
        val itemValue = getDataSelected(position)
        holder.bindData(item.variant, itemValue)
    }

    fun setData(items: List<AnnotationCategoryData>) {
        this.items = items.toMutableList()
        if (itemsSelected.isEmpty()) itemsSelected = items.map { "" }.toMutableList() // generate item to collect input data
        notifyDataSetChanged()
    }

    fun setData(items: List<AnnotationCategoryData>, itemsSelected: List<String>) {
        this.itemsSelected = itemsSelected.toMutableList()
        setData(items)
    }

    fun setDataSelected(position: Int, itemData: String) {
        if (position in 0 until itemCount) {
            itemsSelected[position] = itemData
            notifyItemChanged(position)
        }
    }

    fun getData(position: Int): AnnotationCategoryData {
        return items.getOrNull(position) ?: AnnotationCategoryData()
    }

    fun getDataSelected(position: Int): String {
        return itemsSelected.getOrNull(position).orEmpty()
    }

    override fun onSpecificationValueTextClicked(position: Int) {
        items.getOrNull(position)?.let { annotationData ->
            val bottomSheet = SpecificationDataBottomSheet(
                    title = annotationData.variant,
                    data = annotationData.data.map { it.name },
                    selectedData = getDataSelected(position),
                    specificationDataListener = this)

            editedPosition = position
            bottomSheet.show(fragmentManager)
        }
    }

    override fun onSpecificationDataSelected(specificationData: String) {
        editedPosition?.let {
            setDataSelected(it, specificationData)
        }
    }
}