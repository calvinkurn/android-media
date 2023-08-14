package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.databinding.ChipsUnifyItemBinding
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_NORMAL
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_SELECTED

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
        val binding = ChipsUnifyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val binding: ChipsUnifyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(zipCode: String) {
            val res = binding.root.context.resources
            binding.chipsItem.apply {
                chipText = zipCode
                chipType = TYPE_NORMAL
                setOnClickListener {
                    chipType = TYPE_SELECTED
                    actionListener.onZipCodeClicked(zipCode)
                }
            }
        }
    }
}
