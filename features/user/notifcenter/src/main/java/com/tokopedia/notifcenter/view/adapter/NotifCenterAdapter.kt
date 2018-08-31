package com.tokopedia.notifcenter.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactory
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactoryImpl

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterAdapter (typeFactory: NotifCenterTypeFactoryImpl)
    : BaseAdapter<NotifCenterTypeFactoryImpl>(typeFactory)  {
    private val list : ArrayList<Visitable<*>> = ArrayList()
    private val loadingMoreModel: LoadingMoreModel = LoadingMoreModel()

    lateinit var typeFactory : NotifCenterTypeFactory

    fun addItem(visitable : Visitable<*>) {
        val position = this.list.size
        if (this.list.add(visitable)) {
            notifyItemInserted(position)
        }
    }

    fun removeItem(visitable : Visitable<*>) {
        val position = this.list.indexOf(visitable)
        if (this.list.remove(visitable)) {
            notifyItemRemoved(position)
        }
    }

    fun showError(message: String, onRetryListener: ErrorNetworkModel.OnRetryListener) {
        errorNetworkModel.errorMessage = message
        errorNetworkModel.onRetryListener = onRetryListener
        addItem(errorNetworkModel)
    }
}
