package com.tokopedia.tkpd.tkpdcontactus.home.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.di.ContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.di.DaggerContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.view.adapter.PurchaseListAdpater;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.PurchaseListContract;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.PurchaseListPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuyerPurchaseFragment extends BaseDaggerFragment implements PurchaseListContract.View, HasComponent<ContactUsComponent> {

    @Inject
    PurchaseListPresenter presenter;
    @BindView(R2.id.order_list_full)
    VerticalRecyclerView orderListFull;
    @BindView(R2.id.empty_layout)
    LinearLayout emptyLayout;
    private ContactUsComponent contactUsComponent;
    PurchaseListAdpater adapter;

    public BuyerPurchaseFragment() {
        // Required empty public constructor
    }


    public static BuyerPurchaseFragment newInstance() {
        BuyerPurchaseFragment fragment = new BuyerPurchaseFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PurchaseListAdpater(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_full_order_list, container, false);
        getPresenter().attachView(this);

        initInjector();
        ButterKnife.bind(this, view);
        orderListFull.setAdapter(adapter);
        return view;
    }

    public PurchaseListContract.Presenter getPresenter() {
        return presenter;
    }

    protected void initInjector() {
        contactUsComponent = DaggerContactUsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        contactUsComponent.inject(this);
    }

    @Override
    public ContactUsComponent getComponent() {
        if (contactUsComponent == null) initInjector();
        return contactUsComponent;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void setPurchaseList(List<BuyerPurchaseList> buyerPurchaseLists) {
        if(buyerPurchaseLists.size()<=0) {
            emptyLayout.setVisibility(View.VISIBLE);
        }else {
            adapter.setBuyerPurchaseLists(buyerPurchaseLists);
        }
    }

    @Override
    public void setEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
    }


}
