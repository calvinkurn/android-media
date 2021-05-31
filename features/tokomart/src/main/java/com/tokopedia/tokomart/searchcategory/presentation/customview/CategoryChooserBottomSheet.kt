package com.tokopedia.tokomart.searchcategory.presentation.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.utils.copyParcelable
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

class CategoryChooserBottomSheet: BottomSheetUnify(), OptionRadioListener {

    private var filter: Filter? = null
    private var callback: Callback? = null
    private var initialSelectedOptionIndex = 0
    private var selectedOption: IndexedValue<Option>? = null

    private var categoryChooserView: View? = null
    private var adapter: CategoryChooserAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var buttonApply: UnifyButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()

        findViews()
        configureViews()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setTitle(filter?.title ?: "")
        setCloseClickListener { dismiss() }

        categoryChooserView = View.inflate(context, R.layout.bottom_sheet_tokomart_category_chooser, null)
        setChild(categoryChooserView)
    }

    private fun findViews() {
        val categoryChooserView = categoryChooserView ?: return

        recyclerView = categoryChooserView.findViewById(R.id.tokomartCategoryChooserRecyclerView)
        buttonApply = categoryChooserView.findViewById(R.id.tokomartCategoryChooserButtonApply)
    }

    private fun configureViews() {
        val context = context ?: return
        val filter = filter ?: return
        val selectedOption = selectedOption ?: return

        val drawable = ContextCompat.getDrawable(context, R.drawable.tokomart_divider_category_chooser) ?: return
        val itemDivider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDivider.setDrawable(drawable)

        adapter = CategoryChooserAdapter(filter.options, this)
        recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            if(it.itemDecorationCount == 0)
                it.addItemDecoration(itemDivider)
        }

        configureButtonApplyVisibility(selectedOption.index)

        buttonApply?.setOnClickListener {
            callback?.onApplyCategory(selectedOption.value)
        }
    }

    fun setResultCountText(resultCount: String) {
        buttonApply?.text = resultCount
    }

    override fun onChecked(position: Int, option: Option, isChecked: Boolean) {
        if (isChecked) return
        if (selectedOption?.value == option) return

        val previousPosition = selectedOption?.index ?: -1

        updateSelectedOption(position, option)
        notifyAdapterSelection(previousPosition, position)
        configureButtonApplyVisibility(position)

        callback?.getResultCount(option)
    }

    private fun updateSelectedOption(position: Int, option: Option) {
        selectedOption = IndexedValue(position, option)
    }

    private fun notifyAdapterSelection(oldPosition: Int, newPosition: Int) {
        recyclerView?.post {
            val adapter = adapter ?: return@post

            if (oldPosition >= 0 && oldPosition < adapter.itemCount)
                adapter.notifyItemChanged(oldPosition)

            if (newPosition >= 0 && newPosition < adapter.itemCount)
                adapter.notifyItemChanged(newPosition)
        }
    }

    private fun configureButtonApplyVisibility(position: Int) {
        val shouldShowButtonApply = initialSelectedOptionIndex != position
        buttonApply?.showWithCondition(shouldShowButtonApply)
    }

    override fun getCheckedOption() = selectedOption

    private class CategoryChooserAdapter(
            private val optionList: List<Option> = listOf(),
            private val optionRadioListener: OptionRadioListener,
    ): RecyclerView.Adapter<CategoryChooserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChooserViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tokomart_category_chooser, parent, false)

            return CategoryChooserViewHolder(view, optionRadioListener)
        }

        override fun onBindViewHolder(holder: CategoryChooserViewHolder, position: Int) {
            holder.bind(optionList[position])
        }

        override fun getItemCount(): Int = optionList.size
    }

    private class CategoryChooserViewHolder(
            view: View,
            private val optionRadioListener: OptionRadioListener,
    ): RecyclerView.ViewHolder(view) {

        private val container: ConstraintLayout? = itemView.findViewById(R.id.tokomartCategoryChooserItemContainer)
        private val title: TextView? = itemView.findViewById(R.id.tokomartCategoryChooserItemTitle)
        private val radio: RadioButtonUnify? = itemView.findViewById(R.id.tokomartCategoryChooserItemRadio)

        fun bind(item: Option) {
            val checkedOption = optionRadioListener.getCheckedOption()
            val isChecked = adapterPosition == (checkedOption?.index ?: -1)

            container?.setOnClickListener {
                optionRadioListener.onChecked(adapterPosition, item, isChecked)
            }

            title?.text = item.name

            radio?.isChecked = isChecked
            radio?.setOnCheckedChangeListener { _, _ ->
                optionRadioListener.onChecked(adapterPosition, item, isChecked)
            }
        }
    }

    fun show(
            fragmentManager: FragmentManager,
            mapParameter: Map<String, String>,
            filter: Filter,
            callback: Callback,
    ) {
        this.filter = filter.copyParcelable()
        this.callback = callback

        determineSelectedOption(mapParameter, filter)

        show(fragmentManager, CATEGORY_CHOOSER_BOTTOM_SHEET_TAG)
    }

    private fun determineSelectedOption(mapParameter: Map<String, String>, filter: Filter) {
        val firstOption = filter.options.firstOrNull() ?: Option()
        val key = firstOption.key
        val selectedValue = mapParameter[key] ?: ""

        val selectedOption = filter.options.withIndex().find {
            val opt = it.value
            opt.key == key && opt.value == selectedValue
        } ?: IndexedValue(0, firstOption)

        this.selectedOption = selectedOption
        this.initialSelectedOptionIndex = selectedOption.index
    }

    interface Callback {

        fun onApplyCategory(selectedOption: Option)

        fun getResultCount(selectedOption: Option)
    }

    companion object {
        private const val CATEGORY_CHOOSER_BOTTOM_SHEET_TAG = "CATEGORY_CHOOSER_BOTTOM_SHEET_TAG"
    }
}

private interface OptionRadioListener {

    fun onChecked(position: Int, option: Option, isChecked: Boolean)

    fun getCheckedOption(): IndexedValue<Option>?
}