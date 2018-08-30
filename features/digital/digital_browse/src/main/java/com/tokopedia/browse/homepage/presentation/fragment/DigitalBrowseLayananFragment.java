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

public class DigitalBrowseLayananFragment extends BaseDaggerFragment implements DigitalBrowseLayananContract.View {

    private static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";

    public static Fragment getFragmentInstance() {
        return new DigitalBrowseLayananFragment();
    }

    public static Fragment getFragmentInstance(int categoryId) {
        DigitalBrowseLayananFragment fragment = new DigitalBrowseLayananFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);

        return fragment;
    }

    public DigitalBrowseLayananFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_browse_layanan, container, false);
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
