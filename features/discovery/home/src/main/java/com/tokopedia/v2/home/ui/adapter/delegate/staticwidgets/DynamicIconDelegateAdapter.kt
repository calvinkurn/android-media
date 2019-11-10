package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CarouselDecoration
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.vo.DynamicIconDataModel
import kotlinx.android.synthetic.main.layout_dynamic_icon_section.view.*
import kotlinx.android.synthetic.main.layout_use_case_and_dynamic_icon.view.*

class DynamicIconDelegateAdapter : ViewTypeDelegateAdapter {
    override fun isForViewType(item: ModelViewType): Boolean {
        return item is DynamicIconDataModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return DynamicIconSectionViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as DynamicIconSectionViewHolder
        holder.bind(item as DynamicIconDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is DynamicIconSectionViewHolder){
            holder.bind(item as DynamicIconDataModel)
        }
    }

    inner class DynamicIconSectionViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.layout_dynamic_icon_section)) {
        private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START, true) }
        private val recyclerView = itemView.list

        fun bind(item: DynamicIconDataModel){
            startSnapHelper.attachToRecyclerView(recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            setDecoration()
            recyclerView.adapter = object : RecyclerView.Adapter<DynamicIconViewHolder>(){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconViewHolder {
                    return DynamicIconViewHolder(parent)
                }

                override fun getItemCount(): Int {
                    return item.dynamicIcons.size
                }

                override fun onBindViewHolder(holder: DynamicIconViewHolder, position: Int) {
                    holder.bind(item.dynamicIcons[position])
                }
            }
        }

        private fun setDecoration(){
            val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x

            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(CarouselDecoration(
                        itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16),
                        width,
                        5,
                        itemView.context.resources.getDimensionPixelOffset(
                                R.dimen.use_case_and_dynamic_icon_size
                        )
                ))
            }
        }


        inner class DynamicIconViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.layout_use_case_and_dynamic_icon)){
            val icon: AppCompatImageView = itemView.icon
            val title: TextView = itemView.title
            val container: LinearLayout = itemView.container

            fun bind(item: DynamicIconDataModel.IconDataModel){
                title.text = item.name
                ImageHandler.loadImageThumbs(itemView.context, icon, item.imageUrl)
                container.setOnClickListener { view ->

                }
            }
        }
    }
}