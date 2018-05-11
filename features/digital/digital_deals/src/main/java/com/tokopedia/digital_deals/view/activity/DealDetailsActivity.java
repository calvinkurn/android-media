package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;

public class DealDetailsActivity extends BaseSimpleActivity implements DealFragmentCallbacks{


    @Override
    protected Fragment getNewFragment() {
        CategoryItemsViewModel itemsViewModel = getIntent().getParcelableExtra(DealDetailsPresenter.HOME_DATA);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DealDetailsPresenter.HOME_DATA, itemsViewModel);
        return DealDetailsFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);

    }

    @Override
    public void replaceFragment(int flag) {
        if(flag == 1){
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////            transaction.replace(R.id.parent_view, vewerve);
//            transaction.addToBack
// Stack("rgrgere");
//            transaction.commit();
        }
    }
}