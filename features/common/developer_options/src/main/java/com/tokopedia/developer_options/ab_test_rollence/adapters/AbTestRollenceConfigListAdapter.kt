package com.tokopedia.developer_options.ab_test_rollence.adapters

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.ab_test_rollence.AbTestRollenceConfigListener
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

class AbTestRollenceConfigListAdapter(val listener: AbTestRollenceConfigListener) : Adapter<AbTestRollenceConfigListAdapter.AbTestRollenceItemViewHolder>() {

    private var configListData = arrayListOf<Pair<String, String>>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbTestRollenceItemViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.remote_config_list_item, viewGroup, false)

        return AbTestRollenceItemViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return configListData.size
    }

    override fun onBindViewHolder(viewHolder: AbTestRollenceItemViewHolder, position: Int) {
        viewHolder.bindView(configListData[position])
    }

    fun setConfigData(abTestPlatform: AbTestPlatform?, listData: Set<String>) {
        configListData.clear()
        if (listData.isNotEmpty()) {
            for (configKey: String in listData) {
                val configValue = abTestPlatform?.getString(configKey) ?: ""
                configListData.apply { add(Pair(configKey, configValue)) }
            }
        }
        notifyDataSetChanged()
    }

    class AbTestRollenceItemViewHolder(
            itemView: View,
            listener: AbTestRollenceConfigListener
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
