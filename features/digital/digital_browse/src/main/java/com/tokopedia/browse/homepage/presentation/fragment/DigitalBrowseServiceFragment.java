package com.tokopedia.browse.homepage.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseLayananContract;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseServiceFragment extends BaseDaggerFragment implements DigitalBrowseLayananContract.View {

    private static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";

    public static Fragment getFragmentInstance() {
        return new DigitalBrowseServiceFragment();
    }

    public static Fragment getFragmentInstance(int categoryId) {
        DigitalBrowseServiceFragment fragment = new DigitalBrowseServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);

        return fragment;
    }

    public DigitalBrowseServiceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_digital_browser_service_shimmering_loading, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(DigitalBrowseHomeComponent.class).inject(this);
    }
}
