package com.tokopedia.developer_options.remote_config.adapters

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout

import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.remote_config.KeyValueListener
import com.tokopedia.remoteconfig.RemoteConfig

class KeyValueListAdapter(val listener: KeyValueListener) : Adapter<KeyValueListAdapter.RemoteConfigItemViewHolder>(), Filterable {

    private var configListData = arrayListOf<Pair<String, String>>()
    private var configFilterListData = arrayListOf<Pair<String, String>>()

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RemoteConfigItemViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.remote_config_list_item, viewGroup, false)

        return RemoteConfigItemViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return configListData.size
    }

    override fun onBindViewHolder(viewHolder: RemoteConfigItemViewHolder, position: Int) {
        viewHolder.bindView(configListData[position])
    }

    fun setConfigData(remoteConfig: RemoteConfig?, listData: Set<String>) {
        if (listData.isNotEmpty()) {
            configListData.clear()

            setConfigList(remoteConfig, listData)

            notifyDataSetChanged()
        }
    }

    private fun setConfigList(remoteConfig: RemoteConfig?, listData: Set<String>) {
        for (configKey: String in listData) {
            val configValue = remoteConfig?.getString(configKey) ?: ""
            val remoteConfigData = Pair(configKey, configValue)
            configListData.add(remoteConfigData)
            configFilterListData.add(remoteConfigData)
        }
    }

    fun setData(listData: List<Pair<String, String>>) {
        if (listData.isNotEmpty()) {
            configListData.clear()
            configListData.addAll(listData)

            notifyDataSetChanged()
        }
    }

    class RemoteConfigItemViewHolder(
            itemView: View,
            listener: KeyValueListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val keyTextView: AppCompatTextView = itemView.findViewById(R.id.config_key)
        private val valueTextView: AppCompatTextView = itemView.findViewById(R.id.config_value)

        init {
            val itemContainer: LinearLayout = itemView.findViewById(R.id.config_container)
            itemContainer.setOnClickListener {
                listener.onListItemClick(keyTextView.text.toString().trim())
            }
        }

        fun bindView(itemData: Pair<String, String>) {
            keyTextView.text = itemData.first
            valueTextView.text = itemData.second
        }
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Pair<String, String>> = mutableListOf()
            if (constraint.isNullOrBlank()) {
                filteredList.addAll(configFilterListData)
            } else {
                val searchedWords = constraint.toString().trim()
                for ((key, value) in configFilterListData) {
                    val isContainsKey = key.contains(searchedWords, true)
                    val isContainsValue = value.contains(searchedWords, true)
                    if (isContainsKey || isContainsValue) {
                        filteredList.add(Pair(key, value))
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            configListData.clear()
            (results?.values as? MutableList<Pair<String, String>>)?.let {
                configListData.addAll(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter(): Filter = searchFilter
}
