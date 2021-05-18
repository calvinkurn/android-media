package com.tokopedia.tokomart.category.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.listener.RadioButtonListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_tokomart_category_chooser.*
import kotlinx.android.synthetic.main.item_tokomart_category_chooser_subcategory.view.*

class CategoryChooserBottomSheet(
        private val subCategoryList: List<Pair<String, Int>>
): BottomSheetUnify(), RadioButtonListener {

    private lateinit var mAdapter: CategoryChooserAdapter
    private lateinit var rvSubcategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setTitle(getString(R.string.tokomart_category_bottom_sheet_title))
        setCloseClickListener { dismiss() }

        val childView = View.inflate(context, R.layout.bottom_sheet_tokomart_category_chooser, null)
        val itemDivider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                .apply { context?.let { ContextCompat.getDrawable(it, R.drawable.divider_category_chooser) } }

        rvSubcategory = childView.findViewById(R.id.rv_category_chooser)
        mAdapter = CategoryChooserAdapter(subCategoryList.map { it.first }, this)
        rvSubcategory.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDivider)
            runWhenReady {
                initBottomSheetState()
            }
        }
        setChild(childView)
    }

    private fun initBottomSheetState() {
        val categorySemua = rvSubcategory.findViewHolderForLayoutPosition(0)
        categorySemua?.itemView?.radio_button_subcategory?.isChecked = true
    }

    private fun updateProductAmount(amount: String) {
        button_select_item.text = getString(
                R.string.tokomart_category_bottom_sheet_button, amount)
    }

    override fun onChecked(position: Int) {
        updateProductAmount(subCategoryList[position].second.toString())
        for (i in 0 until mAdapter.itemCount) {
            if (i != position) {
                val vh = rvSubcategory.findViewHolderForLayoutPosition(i)
                vh?.itemView?.radio_button_subcategory?.isChecked = false
            }
        }
    }

    private inline fun RecyclerView.runWhenReady(crossinline action: () -> Unit) {
        val globalLayoutListener = object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                action()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
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
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(
            subCategoryList: List<Pair<String, Int>> = listOf()
        ): CategoryChooserBottomSheet {
            val fragment = CategoryChooserBottomSheet(subCategoryList)
            return fragment
        }
    }
}