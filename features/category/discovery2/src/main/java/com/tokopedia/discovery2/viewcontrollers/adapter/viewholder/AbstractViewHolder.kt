package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.di.UIWidgetComponent
import com.tokopedia.discovery2.discoveryext.UIWidgetUninitializedException
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var lifecycleOwner: LifecycleOwner? = null

    constructor(itemView: View, lifecycleOwner: LifecycleOwner) : this(itemView) {
        this.lifecycleOwner = lifecycleOwner
    }

    protected var parentAbstractViewHolder: AbstractViewHolder? = null
    var discoveryBaseViewModel: DiscoveryBaseViewModel? = null

    abstract fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel)
    lateinit var uiWidgetComponent: UIWidgetComponent

    fun bindView(
        discoveryBaseViewModel: DiscoveryBaseViewModel,
        parentViewHolder: AbstractViewHolder?
    ) {
        this.parentAbstractViewHolder = parentViewHolder
        if (::uiWidgetComponent.isInitialized) {
            val prevViewModel = this.discoveryBaseViewModel
            if (prevViewModel != null) {
                if (prevViewModel != discoveryBaseViewModel) {
                    val needToRemoveObserver = prevViewModel.detachedBindingAdapterPosition != -1
                    if (needToRemoveObserver) {
                        removeObservers(lifecycleOwner)
                        prevViewModel.onDetachToViewHolder()
                        lifecycleOwner?.lifecycle?.removeObserver(prevViewModel)
                    }
                    this.discoveryBaseViewModel = discoveryBaseViewModel
                }
            } else {
                this.discoveryBaseViewModel = discoveryBaseViewModel
            }
            this.bindView(discoveryBaseViewModel)
            this.discoveryBaseViewModel?.onAttachToViewHolder()
            setUpObservers(lifecycleOwner)
        } else {
            throw UIWidgetUninitializedException()
        }
    }

    open fun getInnerRecycleView(): RecyclerView? {
        return null
    }

    open fun onViewAttachedToWindow() {

    }

    open fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        discoveryBaseViewModel?.let { lifecycleOwner?.lifecycle?.addObserver(it) }
    }

    open fun removeObservers(lifecycleOwner: LifecycleOwner?) {

    }

    open fun onViewDetachedToWindow() {
        val dbvm = discoveryBaseViewModel
        dbvm?.detachedBindingAdapterPosition = bindingAdapterPosition
    }
}
