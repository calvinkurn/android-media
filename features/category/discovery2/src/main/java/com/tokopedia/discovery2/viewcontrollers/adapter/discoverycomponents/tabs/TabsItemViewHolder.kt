package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show

class TabsItemViewHolder(itemView: View, fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val tabImageView: ImageView = itemView.findViewById(R.id.tab_image)
    private val selectedView: View = itemView.findViewById(R.id.selected_view)
    private val tabTextView: TextView = itemView.findViewById(R.id.tab_text)
    private lateinit var tabsItemViewModel: TabsItemViewModel
    private var positionForParentAdapter: Int = -1

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsItemViewModel = discoveryBaseViewModel as TabsItemViewModel
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            tabsItemViewModel.getComponentLiveData().observe(lifecycleOwner, Observer {
                val itemData = it.data?.get(0)
                positionForParentAdapter = itemData?.positionForParentItem ?: -1
                itemData?.let { item ->
                    tabImageView.loadImage(item.backgroundImage ?: "")
                    item.name?.let { name ->
                        setTabText(name)
                    }
                    item.fontColor?.let { fontColor ->
                        setFontColor(fontColor)
                    }
                    showSelectedView(item.isSelected)
                }
            })
            tabsItemViewModel.getSelectionChangeLiveData().observe(lifecycleOwner, Observer {
                showSelectedView(it)
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            tabsItemViewModel.getComponentLiveData().removeObservers(lifecycleOwner)
        }
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


    private fun showSelectedView(isSelected: Boolean) {
        if (isSelected) {
            selectedView.show()
        } else {
            selectedView.hide()
        }
    }
}


