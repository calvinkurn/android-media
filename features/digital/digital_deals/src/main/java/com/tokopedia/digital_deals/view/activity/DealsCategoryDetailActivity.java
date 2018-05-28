package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;

public class DealsCategoryDetailActivity extends BaseSimpleActivity {



    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DealDetailsPresenter.HOME_DATA, getIntent().getParcelableExtra(DealDetailsPresenter.HOME_DATA));

        return CategoryDetailHomeFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        toolbar.setVisibility(View.GONE);

    }

}
