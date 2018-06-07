package com.tokopedia.digital.product.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.digital.R;

/**
 * @author by furqan on 07/06/18.
 */

public class DigitalPromoFragment extends Fragment {

    public static DigitalPromoFragment createInstance() {
        return new DigitalPromoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_promo, container, false);

        return view;
    }
}
