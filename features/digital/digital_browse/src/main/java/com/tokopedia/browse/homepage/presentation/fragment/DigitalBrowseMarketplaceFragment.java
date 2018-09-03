package com.tokopedia.browse.homepage.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseBelanjaContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseMarketplaceFragment extends BaseListFragment<DigitalBrowseMarketplaceViewModel, DigitalBrowseMarketplaceAdapterTypeFactory>
        implements DigitalBrowseBelanjaContract.View {

    public static Fragment getFragmentInstance() {
        return new DigitalBrowseMarketplaceFragment();
    }

    public DigitalBrowseMarketplaceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_browse_marketplace, container, false);
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected DigitalBrowseMarketplaceAdapterTypeFactory getAdapterTypeFactory() {
        return new DigitalBrowseMarketplaceAdapterTypeFactory();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(DigitalBrowseHomeComponent.class).inject(this);
    }

    @Override
    public void onItemClicked(DigitalBrowseMarketplaceViewModel viewModel) {

    }
}
