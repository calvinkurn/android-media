package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.databinding.ItemServerLoggerChannelBinding
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerConstants
import com.tokopedia.unifycomponents.Label

class ServerChannelAdapter :
    RecyclerView.Adapter<ServerChannelAdapter.ItemServerChannelViewHolder>() {

    private val serverChannelList = mutableListOf<String>()

    fun setServerChannelList(items: List<String>) {
        if (items.isNullOrEmpty()) return
        this.serverChannelList.clear()
        this.serverChannelList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemServerChannelViewHolder {
        return ItemServerChannelViewHolder(
            ItemServerLoggerChannelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemServerChannelViewHolder, position: Int) {
        holder.bind(serverChannelList[position])
    }

    override fun getItemCount(): Int = serverChannelList.size

    inner class ItemServerChannelViewHolder(private val binding: ItemServerLoggerChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            with(binding) {
                labelServerLogger.text = data
                when (data) {
                    ServerLoggerConstants.SCALYR -> {
                        labelServerLogger.setLabelType(Label.HIGHLIGHT_DARK_RED)
                    }
                    ServerLoggerConstants.NEW_RELIC -> {
                        labelServerLogger.setLabelType(Label.HIGHLIGHT_DARK_GREEN)
                    }
                    ServerLoggerConstants.EMBRACE -> {
                        labelServerLogger.setLabelType(Label.HIGHLIGHT_DARK_BLUE)
                    }
                }
            }
        }
    }
}