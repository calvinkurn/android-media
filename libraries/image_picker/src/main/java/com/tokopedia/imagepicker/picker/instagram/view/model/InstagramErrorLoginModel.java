package com.tokopedia.imagepicker.picker.instagram.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 5/7/18.
 */

public class InstagramErrorLoginModel extends ErrorNetworkModel{

    private ListenerLoginInstagram listenerLoginInstagram;

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        if(adapterTypeFactory instanceof ImageInstagramAdapterTypeFactory) {
            return ((ImageInstagramAdapterTypeFactory)adapterTypeFactory).type(this);
        }
        return super.type(adapterTypeFactory);
    }

    public ListenerLoginInstagram getListenerLoginInstagram() {
        return listenerLoginInstagram;
    }

    public void setListenerLoginInstagram(ListenerLoginInstagram listenerLoginInstagram) {
        this.listenerLoginInstagram = listenerLoginInstagram;
    }

    public interface ListenerLoginInstagram{
        void onClickLoginInstagram();
    }
}
