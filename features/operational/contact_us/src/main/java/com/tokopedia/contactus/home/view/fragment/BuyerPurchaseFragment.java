package com.tokopedia.contactus.home.view.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.di.ContactUsComponent;
import com.tokopedia.contactus.home.di.DaggerContactUsComponent;
import com.tokopedia.contactus.home.view.adapter.PurchaseListAdpater;
import com.tokopedia.contactus.home.view.presenter.PurchaseListContract;
import com.tokopedia.contactus.home.view.presenter.PurchaseListPresenter;

import java.util.List;

import javax.inject.Inject;

public class BuyerPurchaseFragment extends BaseDaggerFragment implements PurchaseListContract.View, HasComponent<ContactUsComponent> {

    @Inject
    PurchaseListPresenter presenter;

    private RecyclerView orderListFull;
    private LinearLayout emptyLayout;
    private ContactUsComponent contactUsComponent;
    private PurchaseListAdpater adapter;

    public BuyerPurchaseFragment() {
        // Required empty public constructor
    }


    public static BuyerPurchaseFragment newInstance() {
        return new BuyerPurchaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PurchaseListAdpater(getContext(), getType());
        initInjector();
    }

    protected String getType() {
        return getString(R.string.pembelian);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_full_order_list, container, false);
        orderListFull = view.findViewById(R.id.order_list_full);
        emptyLayout = view.findViewById(R.id.empty_layout);
        getPresenter().attachView(this);
        orderListFull.setAdapter(adapter);
        orderListFull.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
        if (buyerPurchaseLists.size() <= 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            adapter.setBuyerPurchaseLists(buyerPurchaseLists);
        }
    }

    @Override
    public void setEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
    }


}
