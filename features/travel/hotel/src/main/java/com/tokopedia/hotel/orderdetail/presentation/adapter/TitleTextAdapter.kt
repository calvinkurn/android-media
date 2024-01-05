package com.tokopedia.hotel.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tokopedia.hotel.databinding.ItemHotelDetailTitleTextBinding
import com.tokopedia.hotel.databinding.ItemHotelDetailTitleTextHorizontalLeftBinding
import com.tokopedia.hotel.databinding.ItemHotelDetailTitleTextHorizontalOrangeBinding
import com.tokopedia.hotel.databinding.ItemHotelDetailTitleTextVerticalBinding
import com.tokopedia.hotel.orderdetail.data.model.TitleContent

/**
 * @author by jessica on 13/05/19
 */

class TitleTextAdapter(val type: Int): RecyclerView.Adapter<TitleTextAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ViewBinding = when (type) {
            HORIZONTAL_LAYOUT -> ItemHotelDetailTitleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HORIZONTAL_LEFT_LAYOUT -> ItemHotelDetailTitleTextHorizontalLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VERTICAL_LAYOUT -> ItemHotelDetailTitleTextVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HORIZONTAL_LAYOUT_ORANGE -> ItemHotelDetailTitleTextHorizontalOrangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ItemHotelDetailTitleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ViewHolder(binding)
    }

    var titleTextList: MutableList<TitleContent> = arrayListOf()

    fun addData(list: MutableList<TitleContent>) {
        titleTextList = list
        notifyDataSetChanged()
    }

    fun addData(titleText: TitleContent) {
        titleTextList.add(titleText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(titleTextList.get(position), position)
    }

    override fun getItemCount(): Int {
        return titleTextList.size
    }

    inner class ViewHolder(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TitleContent, position: Int) {
            when(binding) {
                is ItemHotelDetailTitleTextBinding -> {
                    binding.title.text = data.title
                    binding.text.text = data.content
                }
                is ItemHotelDetailTitleTextHorizontalLeftBinding -> {
                    binding.title.text = data.title
                    binding.text.text = data.content
                }
                is ItemHotelDetailTitleTextVerticalBinding -> {
                    binding.title.text = data.title
                    binding.text.text = data.content
                }
                is ItemHotelDetailTitleTextHorizontalOrangeBinding -> {
                    binding.title.text = data.title
                    binding.text.text = data.content
                }
            }
        }
    }

    companion object {
        const val HORIZONTAL_LAYOUT = 1
        const val HORIZONTAL_LEFT_LAYOUT = 3
        const val VERTICAL_LAYOUT = 2
        const val HORIZONTAL_LAYOUT_ORANGE = 11
    }
}

