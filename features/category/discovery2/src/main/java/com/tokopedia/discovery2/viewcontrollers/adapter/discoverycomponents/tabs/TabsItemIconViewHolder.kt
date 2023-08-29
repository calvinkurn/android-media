package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.Constant.TAB_BACKGROUND
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as RUnify

class TabsItemIconViewHolder(itemView: View, fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val tabIconImageView: ImageView = itemView.findViewById(R.id.tab_icon_image)
    private val tabTextView: TextView = itemView.findViewById(R.id.tab_text)
    private var tabsItemIconViewModel: TabsItemIconViewModel? = null
    private var positionForParentAdapter: Int = -1
    private var itemData : DataItem? =  null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsItemIconViewModel = discoveryBaseViewModel as TabsItemIconViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            tabsItemIconViewModel?.getComponentLiveData()?.observe(
                lifecycleOwner
            ) { componentsItem ->
                itemData = componentsItem.data?.firstOrNull()
                positionForParentAdapter = itemData?.positionForParentItem ?: -1
                itemData?.let { item ->
                    if(item.isSelected){
                        tabIconImageView.loadImage(
                            item.iconImageUrl
                        )
                    }else{
                        tabIconImageView.loadImage(
                            item.inactiveIconImageUrl
                        )
                    }
                    item.name?.let { name ->
                        setTabText(name)
                    }
                    setFontColor(item.fontColor)
                }
            }
            tabsItemIconViewModel?.getSelectionChangeLiveData()?.observe(
                lifecycleOwner
            ) {
                if(it){
                    tabIconImageView.loadImage(
                        itemData?.iconImageUrl
                    )
                }else{
                    tabIconImageView.loadImage(
                        itemData?.inactiveIconImageUrl
                    )
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            tabsItemIconViewModel?.getComponentLiveData()?.removeObservers(lifecycleOwner)
        }
    }

    private fun setTabText(name: String) {
        tabTextView.text = name
    }

    private fun setFontColor(fontColor: String?) {
        try {
            if (fontColor.isNullOrEmpty()) {
                tabTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        RUnify.color.Unify_G500
                    )
                )
            } else {
                tabTextView.setTextColor(Color.parseColor(fontColor))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
