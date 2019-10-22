package com.tokopedia.developer_options.remote_config.adapters

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.remote_config.RemoteConfigListener
import com.tokopedia.remoteconfig.RemoteConfig

class RemoteConfigListAdapter(val listener: RemoteConfigListener) : Adapter<RemoteConfigListAdapter.RemoteConfigItemViewHolder>() {

    private var configListData = arrayListOf<Pair<String, String>>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RemoteConfigItemViewHolder {
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

            for (configKey: String in listData) {
                val configValue = remoteConfig?.getString(configKey) ?: ""

                configListData.apply { add(Pair(configKey, configValue)) }
            }

            notifyDataSetChanged()
        }
    }

    class RemoteConfigItemViewHolder(
            itemView: View,
            listener: RemoteConfigListener
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
}
