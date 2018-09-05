package com.tokopedia.notifcenter.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactoryImpl

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterAdapter(typeFactory: NotifCenterTypeFactoryImpl)
    : BaseAdapter<NotifCenterTypeFactoryImpl>(typeFactory) {

    fun getList() = visitables

}