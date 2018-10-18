package com.tokopedia.logisticaddaddress.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.logisticaddaddress.di.AddressScope;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class ManageAddressAdapter extends BaseAdapter<AddressTypeFactory> {

    private EmptyModel emptyModel = new EmptyModel();

    public ManageAddressAdapter(AddressTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
    public void showEmptyState() {
        visitables.clear();
        visitables.add(emptyModel);
        notifyDataSetChanged();
    }

    public void hideEmptyState() {
        visitables.clear();
        notifyDataSetChanged();
    }

}
