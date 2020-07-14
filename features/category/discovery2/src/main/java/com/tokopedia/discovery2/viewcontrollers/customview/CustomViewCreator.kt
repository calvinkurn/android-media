package com.tokopedia.discovery2.viewcontrollers.customview

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class CustomViewCreator(context: Context, val component: ComponentsList, val componentItem:ComponentsItem,fragment:Fragment) : FrameLayout(context) {
    val viewHolder:AbstractViewHolder
    val viewModel:DiscoveryBaseViewModel
    init
    {
        viewHolder =  DiscoveryHomeFactory.createViewHolder(View.inflate(context,component.id, this), component.ordinal, fragment) as AbstractViewHolder
            viewModel = DiscoveryHomeFactory.createViewModel(component.ordinal)(context.applicationContext as Application,componentItem,0)
            viewHolder.bindView(viewModel)
            viewModel.onAttachToViewHolder()
            viewHolder.onViewAttachedToWindow()
            viewHolder.setUpObservers(fragment.viewLifecycleOwner)

    }


    companion object {
        fun getCustomViewObject(context: Context,  component: ComponentsList, componentItem:ComponentsItem,fragment:Fragment):CustomViewCreator{
            return CustomViewCreator(context,component,componentItem,fragment)
        }
    }
}