package com.tokopedia.logisticaddaddress.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.databinding.ChipsItemBinding

/**
 * Created by fwidjaja on 2019-05-29.
 */
class ZipCodeChipsAdapter(private var actionListener: ActionListener) :
    RecyclerView.Adapter<ZipCodeChipsAdapter.ViewHolder>() {
    var zipCodes = mutableListOf<String>()

    interface ActionListener {
        fun onZipCodeClicked(zipCode: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChipsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return zipCodes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        zipCodes.getOrNull(position)?.apply {
            holder.bind(this)
        }
    }

    inner class ViewHolder(
        private val binding: ChipsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(zipCode: String) {
            val res = binding.root.context.resources
            binding.tvChipsItem.apply {
                text = zipCode
                setTextColor(res.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                setOnClickListener {
                    setTextColor(res.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G300))
                    actionListener.onZipCodeClicked(zipCode)
                }
            }
        }
    }
}
