package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.databinding.ChipsUnifyItemBinding
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by fwidjaja on 2019-05-29.
 */
class PopularCityAdapter(private var actionListener: ActionListener) :
    RecyclerView.Adapter<PopularCityAdapter.ViewHolder>() {
    var cityList = mutableListOf<String>()
    private var lastIndex = -1

    interface ActionListener {
        fun onCityChipClicked(city: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChipsUnifyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cityList.getOrNull(position)?.apply {
            holder.bind(this, position)
        }
    }

    inner class ViewHolder(
        private val binding: ChipsUnifyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            city: String,
            position: Int
        ) {
            binding.chipsItem.apply {
                chipText = city
                chipType = ChipsUnify.TYPE_NORMAL
                chipSize = ChipsUnify.SIZE_MEDIUM
                setOnClickListener {
                    notifyItemChanged(lastIndex)
                    lastIndex = position
                    chipType = ChipsUnify.TYPE_SELECTED
                    actionListener.onCityChipClicked(city)
                }
            }
        }
    }
}
