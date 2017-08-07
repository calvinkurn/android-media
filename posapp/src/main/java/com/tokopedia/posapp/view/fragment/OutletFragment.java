package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.Shop;
import com.tokopedia.posapp.view.adapter.OutletAdapter;
import com.tokopedia.posapp.di.component.DaggerOutletComponent;
import com.tokopedia.posapp.view.presenter.OutletPresenter;
import com.tokopedia.posapp.view.presenter.ShopPresenter;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;
import com.tokopedia.posapp.view.viewmodel.shop.ShopViewModel;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFragment extends BaseDaggerFragment implements Outlet.View, Shop.View {
    RecyclerView recyclerOutlet;
    TextView textShopName;
    EditText editSearchOutlet;
    OutletAdapter adapter;

    @Inject
    OutletPresenter outletPresenter;

    @Inject
    ShopPresenter shopPresenter;

    public static OutletFragment createInstance(Bundle bundle) {
        OutletFragment fragment = new OutletFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_outlet, container, false);
        preparePresenter();
        prepareView(parentView);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerOutletComponent daggerOutletComponent =
                (DaggerOutletComponent) DaggerOutletComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerOutletComponent.inject(this);
    }

    @Override
    public void clearOutletData() {
        adapter.clearData();
    }

    @Override
    public void onOutletClicked(String outletId) {

    }

    @Override
    public void onSuccessGetOutlet(OutletViewModel outlet) {
        adapter.setData(outlet);
        outletPresenter.setHasNextPage(outlet.getNextUri());
    }

    @Override
    public void onErrorGetOutlet(String errorMessage) {

    }

    @Override
    public void onSuccessGetShop(ShopViewModel shop) {
        if(shop != null && shop.getShopInfo() != null && shop.getShopInfo().getShopName() != null) {
            textShopName.setText(shop.getShopInfo().getShopName());
        }
    }

    @Override
    public void onErrorGetShop(String errorMessage) {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void finishLoading() {

    }

    private void fetchData() {
        outletPresenter.getOutlet("");
        shopPresenter.getUserShop();
    }

    private void preparePresenter() {
        outletPresenter.attachView(this);
        shopPresenter.attachView(this);
    }

    private void prepareView(View parentView) {
        recyclerOutlet = parentView.findViewById(R.id.recycler_outlet);
        textShopName = parentView.findViewById(R.id.text_shop_name);
        editSearchOutlet = parentView.findViewById(R.id.edit_search_outlet);

        adapter = new OutletAdapter(getContext(), this);
        recyclerOutlet.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerOutlet.setAdapter(adapter);

        editSearchOutlet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submitQuery(editSearchOutlet.getText());
                return true;
            }
        });
    }

    private void submitQuery(CharSequence query) {
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            outletPresenter.getOutlet(query.toString());
        }
    }
}
