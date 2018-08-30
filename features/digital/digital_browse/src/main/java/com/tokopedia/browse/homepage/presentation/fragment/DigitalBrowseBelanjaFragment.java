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
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseBelanjaContract;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseBelanjaFragment extends BaseDaggerFragment implements DigitalBrowseBelanjaContract.View {

    public static Fragment getFragmentInstance() {
        return new DigitalBrowseBelanjaFragment();
    }

    public DigitalBrowseBelanjaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_browse_belanja, container, false);
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
