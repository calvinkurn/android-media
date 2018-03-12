package com.tokopedia.posapp.outlet.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.outlet.view.Outlet;
import com.tokopedia.posapp.shop.view.Shop;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;
import com.tokopedia.posapp.outlet.view.adapter.OutletAdapter;
import com.tokopedia.posapp.di.component.DaggerOutletComponent;
import com.tokopedia.posapp.outlet.view.presenter.OutletPresenter;
import com.tokopedia.posapp.shop.view.presenter.ShopPresenter;
import com.tokopedia.posapp.outlet.view.viewmodel.OutletItemViewModel;
import com.tokopedia.posapp.outlet.view.viewmodel.OutletViewModel;
import com.tokopedia.posapp.shop.view.viewmodel.ShopViewModel;
import com.tokopedia.posapp.base.widget.PosAlertDialog;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFragment extends BaseDaggerFragment implements Outlet.View, Shop.View {
    private RecyclerView recyclerOutlet;
    private TextView textShopName;
    private EditText editSearchOutlet;
    private OutletAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Inject
    OutletPresenter outletPresenter;

    @Inject
    ShopPresenter shopPresenter;

    private EndlessRecyclerviewListener scrollListener;

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
    public void onOutletClicked(final OutletItemViewModel outlet) {
        new PosAlertDialog(getContext())
                .setTitle(getString(R.string.outlet_chooser_dialog_title))
                .setMessage(getString(R.string.outlet_chooser_dialog_message))
                .setPositiveButton(getString(R.string.yes), new PosAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface) {
                        setChosenOutlet(outlet);
                        getActivity().startActivity(new Intent(getContext(), ProductListActivity.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton(getString(R.string.No), new PosAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void setChosenOutlet(OutletItemViewModel outlet) {
        PosSessionHandler.setOutletId(getContext(), outlet.getOutletId());
        PosSessionHandler.setOutletName(getContext(), outlet.getOutletName());
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
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerOutlet.setLayoutManager(gridLayoutManager);
        recyclerOutlet.setAdapter(adapter);

        scrollListener = new EndlessRecyclerviewListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                outletPresenter.getNextOutlet(editSearchOutlet.getText().toString());
            }
        };

        recyclerOutlet.addOnScrollListener(scrollListener);

        editSearchOutlet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                } else if (i == EditorInfo.IME_ACTION_SEARCH
                        || keyEvent == null
                        || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    outletPresenter.getOutlet(editSearchOutlet.getText().toString().trim());
                    scrollListener.resetState();
                }
                return true;
            }
        });
    }
}
