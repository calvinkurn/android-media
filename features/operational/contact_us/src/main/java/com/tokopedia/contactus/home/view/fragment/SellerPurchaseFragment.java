package com.tokopedia.contactus.home.view.fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.home.di.ContactUsComponent;
import com.tokopedia.contactus.home.di.DaggerContactUsComponent;
import com.tokopedia.contactus.home.view.presenter.PurchaseListContract;
import com.tokopedia.contactus.home.view.presenter.SellerPurchasePresenter;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 14/05/18.
 */
/*Extending BuyerPurchaseFragment as ui is same but usecase diff so presenter change implementaiton*/
public class SellerPurchaseFragment extends BuyerPurchaseFragment {
    @Inject
    SellerPurchasePresenter presenter;

    ContactUsComponent contactUsComponent;
    public static SellerPurchaseFragment newInstance() {
        return new SellerPurchaseFragment();

    }

    protected void initInjector() {
        contactUsComponent = DaggerContactUsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        contactUsComponent.inject(this);
    }

    protected String getType() {
        return getString(R.string.penjualan);
    }

    @Override
    public PurchaseListContract.Presenter getPresenter() {
        return presenter;
    }
}
