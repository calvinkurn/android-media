package com.tokopedia.filter.newdynamicfilter.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.design.color.ColorSampleView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView

import java.util.ArrayList

class BottomSheetExpandableItemSelectedListAdapter(private val filterView: BottomSheetDynamicFilterView, private val filterTitle: String) : RecyclerView.Adapter<BottomSheetExpandableItemSelectedListAdapter.ViewHolder>() {

    private var selectedOptionsList: List<Option> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context).inflate(R.layout.bottom_sheet_selected_filter_item, parent, false)
        return ViewHolder(view, filterView, this, filterTitle)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(selectedOptionsList[position], position)
    }

    override fun getItemCount(): Int = selectedOptionsList.size

    fun setSelectedOptionsList(selectedOptionsList: List<Option>) {
        this.selectedOptionsList = selectedOptionsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View,
                              private val filterView: BottomSheetDynamicFilterView,
                              private val adapter: BottomSheetExpandableItemSelectedListAdapter,
                              private val filterTitle: String) : RecyclerView.ViewHolder(itemView) {

        private var itemText: TextView? = null
        private var colorIcon: ColorSampleView? = null
        private var itemContainer: View? = null
        private var ratingIcon: View? = null
        private var filterNewIcon: View? = null

        init {
            initViewHolderViews(itemView)
        }

        private fun initViewHolderViews(itemView: View) {
            itemText = itemView.findViewById(R.id.filter_item_text)
            ratingIcon = itemView.findViewById(R.id.rating_icon)
            colorIcon = itemView.findViewById(R.id.color_icon)
            itemContainer = itemView.findViewById(R.id.filter_item_container)
            filterNewIcon = itemView.findViewById(R.id.filter_new_icon)
        }

        fun bind(option: Option, position: Int) {
            bindFilterNewIcon(option)

            bindRatingOption(option)

            bindColorOption(option)

            itemText?.text = option.name

            if (option.isCategoryOption) {
                bindCategoryOption(option)
            } else {
                bindGeneralOption(option, position)
            }
        }

        private fun bindFilterNewIcon(option: Option) {
            if (option.isNew) {
                filterNewIcon?.visibility = View.VISIBLE
            } else {
                filterNewIcon?.visibility = View.GONE
            }
        }

        private fun bindRatingOption(option: Option) {
            if (Option.KEY_RATING == option.key) {
                ratingIcon?.visibility = View.VISIBLE
            } else {
                ratingIcon?.visibility = View.GONE
            }
        }

        private fun bindColorOption(option: Option) {
            if (!TextUtils.isEmpty(option.hexColor)) {
                colorIcon?.visibility = View.VISIBLE
                colorIcon?.setColor(Color.parseColor(option.hexColor))
            } else {
                colorIcon?.visibility = View.GONE
            }
        }

        private fun bindCategoryOption(option: Option) {
            val isOptionSelected = filterView.isSelectedCategory(option)
            setItemContainerBackgroundResource(isOptionSelected)

            itemContainer?.setOnClickListener {
                if (isOptionSelected) {
                    filterView.removeSelectedOption(option, filterTitle)
                } else {
                    filterView.selectCategory(option, filterTitle)
                }
                adapter.notifyDataSetChanged()
            }
        }

        private fun bindGeneralOption(option: Option, position: Int) {
            val isOptionSelected = filterView.getFilterViewState(option.uniqueId)
            setItemContainerBackgroundResource(isOptionSelected)

            itemContainer?.setOnClickListener {
                val newCheckedState = !isOptionSelected
                filterView.saveCheckedState(option, newCheckedState, filterTitle)
                adapter.notifyItemChanged(position)
            }
        }

        private fun setItemContainerBackgroundResource(isSelected: Boolean) {
            if (isSelected) {
                itemContainer?.setBackgroundResource(R.drawable.quick_filter_item_background_selected)
            } else {
                itemContainer?.setBackgroundResource(R.drawable.quick_filter_item_background_neutral)
            }
        }
    }
}
