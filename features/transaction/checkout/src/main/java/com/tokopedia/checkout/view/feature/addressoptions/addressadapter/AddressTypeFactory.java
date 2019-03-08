package com.tokopedia.checkout.view.feature.addressoptions.addressadapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by fajarnuha on 27/02/19.
 */
public interface AddressTypeFactory extends AdapterTypeFactory {

    int type(RecipientAddressModel viewHolder);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
