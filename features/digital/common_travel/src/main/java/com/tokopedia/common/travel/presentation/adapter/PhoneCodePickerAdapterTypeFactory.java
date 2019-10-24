package com.tokopedia.common.travel.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

/**
 * Created by alvarisi on 12/19/17.
 */

public class PhoneCodePickerAdapterTypeFactory extends BaseAdapterTypeFactory {
    public PhoneCodePickerAdapterTypeFactory() {
    }

    public int type(CountryPhoneCode viewModel) {
        return PhoneCodePickerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (PhoneCodePickerViewHolder.LAYOUT == type) {
            return new PhoneCodePickerViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
