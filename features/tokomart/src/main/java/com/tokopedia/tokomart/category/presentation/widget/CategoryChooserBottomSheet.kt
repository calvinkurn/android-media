package com.tokopedia.tokomart.category.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.listener.RadioButtonListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.item_tokomart_category_chooser_subcategory.view.*

class CategoryChooserBottomSheet(
        private val subCategoryList: List<String> = listOf()
): BottomSheetUnify(), RadioButtonListener {

    private lateinit var mAdapter: CategoryChooserAdapter
    private lateinit var rvSubcategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    private fun initBottomSheet() {
        val dummySubCategory = listOf("Semua", "Susu Bubuk", "Susu UHT", "Susu Segar", "Kental Manis")
        showCloseIcon = true
        setTitle("Kategory Dummy")
        setCloseClickListener { dismiss() }

        val childView = View.inflate(context, R.layout.bottom_sheet_tokomart_category_chooser, null)
        rvSubcategory = childView.findViewById(R.id.rv_category_chooser)
        mAdapter = CategoryChooserAdapter(dummySubCategory, this)

        rvSubcategory.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        setChild(childView)
    }

    override fun onChecked(position: Int) {
        // TODO : Check the consistency of this function
        for (i in 0 until mAdapter.itemCount) {
            if (i != position) {
                val vh = rvSubcategory.findViewHolderForLayoutPosition(i)
                vh?.itemView?.radio_button_subcategory?.isChecked = false
            }
        }
    }

    inner class CategoryChooserAdapter(
            var subCategoryList: List<String> = listOf(),
            var radioButtonListener: RadioButtonListener
    ): RecyclerView.Adapter<CategoryChooserAdapter.CategoryChooserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChooserViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tokomart_category_chooser_subcategory, parent, false)
            return CategoryChooserViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryChooserViewHolder, position: Int) {
            holder.bind(subCategoryList[position])
        }

        override fun getItemCount(): Int = subCategoryList.size

        inner class CategoryChooserViewHolder(view: View): RecyclerView.ViewHolder(view) {
            fun bind(item: String) {
                itemView.tv_subcategory.text = item
                itemView.radio_button_subcategory.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        radioButtonListener.onChecked(layoutPosition)
                        println("Category : $item")
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(): CategoryChooserBottomSheet {
            val fragment = CategoryChooserBottomSheet()
            return fragment
        }
    }
}