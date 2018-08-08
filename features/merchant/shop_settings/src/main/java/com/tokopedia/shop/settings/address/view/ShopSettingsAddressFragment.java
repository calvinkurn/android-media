package com.tokopedia.shop.settings.address.view;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.domain.usecase.GetShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.GetShopLocationUseCase;
import com.tokopedia.shop.common.graphql.model.shopetalase.ShopEtalase;
import com.tokopedia.shop.common.graphql.model.shoplocation.ShopLocation;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopSettingsAddressFragment extends BaseDaggerFragment {

    public static ShopSettingsAddressFragment newInstance() {
        return new ShopSettingsAddressFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(getContext());
        GetShopLocationUseCase useCase = new GetShopLocationUseCase(getContext());
        useCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopLocation>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Test", "test");
            }

            @Override
            public void onNext(ArrayList<ShopLocation> shopLocationArrayList) {
                Log.i("Test", "test");
            }
        });
    }

}
