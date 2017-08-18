package com.tokopedia.posapp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.Shop;
import com.tokopedia.posapp.view.activity.ProductDetailActivity;
import com.tokopedia.posapp.view.adapter.OutletAdapter;
import com.tokopedia.posapp.di.component.DaggerOutletComponent;
import com.tokopedia.posapp.view.presenter.OutletPresenter;
import com.tokopedia.posapp.view.presenter.ShopPresenter;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;
import com.tokopedia.posapp.view.viewmodel.shop.ShopViewModel;

import javax.inject.Inject;

import static com.tokopedia.posapp.view.fragment.ProductDetailFragment.PRODUCT_PASS;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFragment extends BaseDaggerFragment implements Outlet.View, Shop.View {
    private RecyclerView recyclerOutlet;
    private TextView textShopName;
    private EditText editSearchOutlet;
    private OutletAdapter adapter;

    // TODO: 8/8/17 temporary
    Button buttonPdp;

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
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.outlet_chooser_dialog_title)
                .setMessage(R.string.outlet_chooser_dialog_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 8/18/17 navigate to product list screen
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
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
        recyclerOutlet.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerOutlet.setAdapter(adapter);

        editSearchOutlet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submitQuery(editSearchOutlet.getText());
                return true;
            }
        });

        buttonPdp = parentView.findViewById(R.id.button_pdp);
        buttonPdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                ProductPass productPass = ProductPass.Builder.aProductPass()
                        .setProductId(163209073)
                        .build();
                intent.putExtra(PRODUCT_PASS, productPass);
                startActivity(intent);
            }
        });
    }

    private void submitQuery(CharSequence query) {
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            outletPresenter.getOutlet(query.toString());
        }
    }
}
