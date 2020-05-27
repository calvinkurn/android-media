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
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show

class TabsItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val tabImageView: ImageView = itemView.findViewById(R.id.tab_image)
    private val selectedView: View = itemView.findViewById(R.id.selected_view)
    private val tabTextView: TextView = itemView.findViewById(R.id.tab_text)
    private lateinit var tabsItemViewModel: TabsItemViewModel
    private var positionForParentAdapter: Int = -1

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsItemViewModel = discoveryBaseViewModel as TabsItemViewModel
        tabsItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
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
//                if (adapterPosition == 0) {
//                    item.isSelected = true
//                }
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
            if(!data.isSelected) {
                (it as ImageView).apply {
                    data.isSelected = !data.isSelected
                    showSelectedView(data.isSelected)
                }
                changeDataInTabsViewModel()
            }
        }
    }

    //temp code
    private fun changeDataInTabsViewModel() {
        (fragment as? DiscoveryFragment)?.let { discoveryFragment ->
            (discoveryFragment.getDiscoveryRecyclerViewAdapter().getChildHolderViewModel(positionForParentAdapter) as? TabsViewModel)?.let { tabsViewModel ->
                val tabsListData = tabsViewModel.getListDataLiveData().value
                tabsListData?.let { listData ->
                    listData.forEach { item ->
                        if (item.data?.get(0)?.isSelected == true
                                && item.data?.get(0)?.name != tabsItemViewModel.getComponentLiveData().value?.data?.get(0)?.name) {
                            item.data?.get(0)?.isSelected = false
                        }
                    }
                    tabsViewModel.getListDataLiveData().value = listData
                }
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