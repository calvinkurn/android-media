package com.tokopedia.browse.homepage.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.browse.common.applink.ApplinkConstant;
import com.tokopedia.browse.common.di.utils.DigitalBrowseComponentUtils;
import com.tokopedia.browse.common.presentation.DigitalBrowseBaseActivity;
import com.tokopedia.browse.homepage.di.DaggerDigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseBelanjaFragment;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseLayananFragment;

public class DigitalBrowseHomeActivity extends DigitalBrowseBaseActivity implements HasComponent<DigitalBrowseHomeComponent> {

    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_TAB = "tab";
    private static final String EXTRA_TITLE = "title";

    private static final int TYPE_BELANJA = 1;
    private static final int TYPE_LAYANAN = 2;

    private static DigitalBrowseHomeComponent digitalBrowseHomeComponent;

    @DeepLink({ApplinkConstant.DIGITAL_BROWSE })
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, DigitalBrowseHomeActivity.class);
        return intent.setData(uri.build()).putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_TITLE)) {
            setupToolbar();
        }
    }

    @Override
    public DigitalBrowseHomeComponent getComponent() {
        if (digitalBrowseHomeComponent == null) {
            digitalBrowseHomeComponent = DaggerDigitalBrowseHomeComponent.builder()
                    .digitalBrowseComponent(DigitalBrowseComponentUtils.getDigitalBrowseComponent(getApplication()))
                    .build();
        }
        return digitalBrowseHomeComponent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = null;

        if (Integer.parseInt(getIntent().getStringExtra(EXTRA_TYPE)) == TYPE_BELANJA) {
            fragment = DigitalBrowseBelanjaFragment.getFragmentInstance();
        } else if (Integer.parseInt(getIntent().getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN) {
            if (getIntent().hasExtra(EXTRA_TAB)) {
                fragment = DigitalBrowseLayananFragment.getFragmentInstance(
                        Integer.parseInt(getIntent().getStringExtra(EXTRA_TAB)));
            } else {
                fragment = DigitalBrowseLayananFragment.getFragmentInstance();
            }
        }

        return fragment;
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        updateTitle(title);
    }
}

