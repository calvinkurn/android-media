package com.tokopedia.notifcenter.view.adapter

import android.support.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactoryImpl

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterAdapter(typeFactory: NotifCenterTypeFactoryImpl)
    : BaseAdapter<NotifCenterTypeFactoryImpl>(typeFactory) {

    private val emptyModel: EmptyResultViewModel = EmptyResultViewModel()

    fun addEmpty(title: String, description: String, @DrawableRes icon: Int) {
        emptyModel.title = title
        emptyModel.content = description
        emptyModel.iconRes = icon

        val position = visitables.size
        visitables.add(emptyModel)
        notifyItemInserted(position)
    }

}