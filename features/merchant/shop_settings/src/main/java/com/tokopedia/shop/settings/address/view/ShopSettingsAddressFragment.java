package com.tokopedia.shop.settings.address.view;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.AddShopLocationUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.DeleteShopLocationUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.GetShopLocationUseCase;
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.ReorderShopLocationUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.UpdateShopLocationUseCase;
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
        useCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopLocationModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Test", "test");
            }

            @Override
            public void onNext(ArrayList<ShopLocationModel> shopLocationModelArrayList) {
                Log.i("Test", "test");
            }
        });

        AddShopLocationUseCase addShopLocationUseCase = new AddShopLocationUseCase(getContext());
        addShopLocationUseCase.execute(AddShopLocationUseCase.createRequestParams("Branch Ambassador",
                "Jalan Satrio",
                5467,
                564,
                56,
                32534,
                "gmail@tokopedia.com",
                "08898765456",
                "+1 323 555 1234")
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        UpdateShopLocationUseCase updateShopLocationUseCase = new UpdateShopLocationUseCase(getContext());
        updateShopLocationUseCase.execute(UpdateShopLocationUseCase.createRequestParams(
                "123",
                "Branch Ambassador",
                "Jalan Satrio",
                5467,
                564,
                56,
                32534,
                "gmail@tokopedia.com",
                "08898765456",
                "+1 323 555 1234")
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        DeleteShopLocationUseCase deleteShopLocationUseCase = new DeleteShopLocationUseCase(getContext());
        deleteShopLocationUseCase.execute(DeleteShopLocationUseCase.createRequestParams("123")
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        ReorderShopLocationUseCase reorderShopLocationUseCase = new ReorderShopLocationUseCase(getContext());
        ArrayList<String> idList = new ArrayList<>();
        idList.add("123");
        idList.add("456");
        idList.add("789");
        idList.add("012");
        reorderShopLocationUseCase.execute(ReorderShopLocationUseCase.createRequestParams(idList)
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });
    }

}
