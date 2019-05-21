package com.tokopedia.common.travel.presentation.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class PhoneCodePickerViewHolder extends AbstractViewHolder<CountryPhoneCode> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_phone_code_picker;

    private TextView countryPhonCode;
    private TextView countryName;

    public PhoneCodePickerViewHolder(View itemView) {
        super(itemView);
        countryPhonCode = (TextView) itemView.findViewById(R.id.country_phone_code);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
    }

    @Override
    public void bind(CountryPhoneCode flightBookingPhoneCodeViewModel) {
        countryPhonCode.setText(itemView.getContext().getString(R.string.phone_code_picker_label, flightBookingPhoneCodeViewModel.getCountryPhoneCode()));
        countryName.setText(flightBookingPhoneCodeViewModel.getCountryName());
    }
}
