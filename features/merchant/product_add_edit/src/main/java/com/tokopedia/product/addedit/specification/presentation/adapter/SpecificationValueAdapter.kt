package com.tokopedia.product.addedit.specification.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.presentation.adapter.viewholder.SpecificationValueViewHolder
import com.tokopedia.product.addedit.specification.presentation.dialog.SpecificationDataBottomSheet
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel

class SpecificationValueAdapter(private val fragmentManager: FragmentManager?) :
        RecyclerView.Adapter<SpecificationValueViewHolder>(),
        SpecificationValueViewHolder.OnSpecificationValueViewHolderClickListener,
        SpecificationDataBottomSheet.SpecificationDataListener {

    private var items: MutableList<AnnotationCategoryData> = mutableListOf()
    private var itemsSelected: MutableList<SpecificationInputModel> = mutableListOf()
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
        val itemValue = getDataSelected(position).data
        holder.bindData(item.variant, itemValue)
    }

    override fun onSpecificationValueTextClicked(position: Int) {
        items.getOrNull(position)?.let { annotationData ->
            val bottomSheet = SpecificationDataBottomSheet(
                    title = annotationData.variant,
                    ids = annotationData.data.map { it.id.toString() },
                    data = annotationData.data.map { it.name },
                    selectedId = getDataSelected(position).id,
                    specificationDataListener = this)

            editedPosition = position
            bottomSheet.show(fragmentManager)
        }
    }

    override fun onSpecificationValueTextCleared(position: Int) {
        itemsSelected.getOrNull(position)?.let {
            it.id = ""
            it.data = ""
        }
    }

    override fun onSpecificationDataSelected(specificationId: String, specificationData: String) {
        editedPosition?.let {
            setDataSelected(it, specificationId, specificationData)
        }
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
}