package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var lifecycleOwner: LifecycleOwner? = null

    constructor(itemView: View, lifecycleOwner: LifecycleOwner) : this(itemView) {
        this.lifecycleOwner = lifecycleOwner
    }

    protected var parentAbstractViewHolder: AbstractViewHolder? = null
    protected var discoveryBaseViewModel: DiscoveryBaseViewModel? = null

    // TODO Remove this implementation
    abstract fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel)

    fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel, parentViewHolder: AbstractViewHolder?) {
        this.parentAbstractViewHolder = parentViewHolder

        if (this.discoveryBaseViewModel != null) {
            if (this.discoveryBaseViewModel !== discoveryBaseViewModel) {
                Log.e("AbstractViewHolder","removing observer old observer "+this.discoveryBaseViewModel +" adding observer "+discoveryBaseViewModel)
                removeObservers(lifecycleOwner)
                this.discoveryBaseViewModel?.onDetachToViewHolder()
                this.discoveryBaseViewModel = discoveryBaseViewModel

            }
        } else {
            this.discoveryBaseViewModel = discoveryBaseViewModel
        }
        this.bindView(discoveryBaseViewModel)
        this.discoveryBaseViewModel?.onAttachToViewHolder()
        setUpObservers(lifecycleOwner)
    }

    open fun getInnerRecycleView(): RecyclerView? {
        return null
    }

    open fun onViewAttachedToWindow() {


    }

    open fun setUpObservers(lifecycleOwner: LifecycleOwner?) {

    }

    open fun removeObservers(lifecycleOwner: LifecycleOwner?) {

    }

    open fun onViewDetachedToWindow() {

    }

}