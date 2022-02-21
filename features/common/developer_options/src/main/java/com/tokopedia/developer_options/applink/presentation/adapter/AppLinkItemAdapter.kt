package com.tokopedia.developer_options.applink.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import com.tokopedia.unifyprinciples.Typography

class AppLinkItemAdapter(private val appLinkItemListener: AppLinkItemListener):
    RecyclerView.Adapter<AppLinkItemAdapter.AppLinkItemViewHolder>(), Filterable {

    private val appLinkListData = mutableListOf<AppLinkUiModel>()
    private val appLinkListFilterData = mutableListOf<AppLinkUiModel>()

    fun setAppLinkList(appLinkList: List<AppLinkUiModel>) {
        if (appLinkList.isNotEmpty()) return
        appLinkListData.clear()
        appLinkListFilterData.clear()
        appLinkListData.addAll(appLinkList)
        appLinkListFilterData.addAll(appLinkList)
        notifyDataSetChanged()
    }

    fun clearAppLinkList() {
        appLinkListData.clear()
        appLinkListFilterData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppLinkItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_applink_list, parent, false)
        return AppLinkItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppLinkItemViewHolder, position: Int) {
        if (appLinkListData.isNotEmpty()) {
            holder.bind(appLinkListData[position])
        }
    }

    override fun getItemCount(): Int = appLinkListData.size

    inner class AppLinkItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvAppLinTitle: Typography = itemView.findViewById(R.id.tvApplinkTitle)
        private val tvAppLinkVariable: Typography = itemView.findViewById(R.id.tvApplinkVariable)
        private val tvDestActivity: Typography = itemView.findViewById(R.id.tvDestActivity)

        fun bind(item: AppLinkUiModel) {
            with(item) {
                tvAppLinTitle.text = appLink
                tvAppLinkVariable.text = appLinkVariable
                tvDestActivity.text = destActivity

                itemView.setOnClickListener {
                    appLinkItemListener.onAppLinkItemClicked(appLink)
                }
            }
        }
    }

    interface AppLinkItemListener {
        fun onAppLinkItemClicked(appLink: String)
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<AppLinkUiModel> = mutableListOf()
            if (constraint.isNullOrBlank()) {
                filteredList.addAll(appLinkListFilterData)
            } else {
                val searchedWords = constraint.toString().trim()
                for (item in appLinkListFilterData) {
                    val isContainsAppLink = item.appLink.contains(searchedWords, true)
                    val isContainsAppLinkVariable = item.appLinkVariable.contains(searchedWords, true)
                    val isContainsDestActivity = item.destActivity.contains(searchedWords, true)
                    if (isContainsAppLink || isContainsAppLinkVariable || isContainsDestActivity) {
                        filteredList.add(AppLinkUiModel(item.appLink, item.appLinkVariable, item.destActivity))
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            (results?.values as? MutableList<AppLinkUiModel>)?.let {
                appLinkListData.clear()
                appLinkListData.addAll(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter(): Filter = searchFilter
}