package com.tokopedia.checkout.view.feature.addressoptions.addressadapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by fajarnuha on 27/02/19.
 */
public class AddressAdapterTypeFactory extends BaseAdapterTypeFactory implements AddressTypeFactory{


    @Override
    public int type(RecipientAddressModel viewHolder) {
        return RecipientAddressViewHolder.TYPE;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        return super.createViewHolder(parent, type);
    }
}
