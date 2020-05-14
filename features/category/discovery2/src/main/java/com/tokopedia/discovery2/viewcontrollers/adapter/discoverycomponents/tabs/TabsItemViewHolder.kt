package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class TabsItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val tabImageView: ImageView = itemView.findViewById(R.id.tab_image)
    private val selectedView: View = itemView.findViewById(R.id.selected_view)
    private val tabTextView: TextView = itemView.findViewById(R.id.tab_text)
    private lateinit var tabsItemViewModel: TabsItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsItemViewModel = discoveryBaseViewModel as TabsItemViewModel
        tabsItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            val itemData = it.data?.get(0)
            itemData?.let { item ->
                ImageHandler.LoadImage(tabImageView, item.backgroundImage)
                item.name?.let { name ->
                    setTabText(name)
                }
                item.fontColor?.let { fontColor ->
                    setFontColor(fontColor)
                }
                if (adapterPosition == 0) {
                    item.isSelected = true
                }
                showSelectedView(item.isSelected)
                setClick(item)
            }
        })
    }

    private fun setTabText(name: String) {
        tabTextView.text = name
    }

    private fun setFontColor(fontColor: String) {
        if (fontColor.length > 1) {
            tabTextView.setTextColor(Color.parseColor(fontColor))
            selectedView.setBackgroundColor(Color.parseColor(fontColor))
        }
    }

    private fun setClick(data: DataItem) {
        tabImageView.setOnClickListener {
            (it as ImageView).apply {
                data.isSelected = !data.isSelected
                showSelectedView(data.isSelected)
            }
        }
    }

    private fun showSelectedView(isSelected: Boolean) {
        if (isSelected) {
            selectedView.show()
        } else {
            selectedView.hide()
        }

    }

}