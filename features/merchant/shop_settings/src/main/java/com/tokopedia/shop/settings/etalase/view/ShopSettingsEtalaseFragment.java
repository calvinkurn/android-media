package com.tokopedia.shop.settings.etalase.view;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.DeleteShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.ReorderShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.UpdateShopEtalaseUseCase;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopSettingsEtalaseFragment extends BaseDaggerFragment {

    public static ShopSettingsEtalaseFragment newInstance() {
        return new ShopSettingsEtalaseFragment();
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
//        GetShopEtalaseUseCase useCase = new GetShopEtalaseUseCase(getContext());
//        useCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopEtalase>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("Test", "test");
//            }
//
//            @Override
//            public void onNext(ArrayList<ShopEtalase> shopEtalaseList) {
//                Log.i("Test", "test");
//            }
//        });

//        AddShopEtalaseUseCase addShopEtalaseUseCase = new AddShopEtalaseUseCase(getContext());
//        addShopEtalaseUseCase.execute(AddShopEtalaseUseCase.createRequestParams("Etalase X")
//                , new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Test", "test");
//                    }
//
//                    @Override
//                    public void onNext(String successMessage) {
//                        Log.i("Test", successMessage);
//                    }
//                });

        UpdateShopEtalaseUseCase updateShopEtalaseUseCase = new UpdateShopEtalaseUseCase(getContext());
        updateShopEtalaseUseCase.execute(UpdateShopEtalaseUseCase.createRequestParams("123", "Etalase X")
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

        DeleteShopEtalaseUseCase deleteShopEtalaseUseCase = new DeleteShopEtalaseUseCase(getContext());
        deleteShopEtalaseUseCase.execute(DeleteShopEtalaseUseCase.createRequestParams("123")
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

        ReorderShopEtalaseUseCase reorderShopEtalaseUseCase = new ReorderShopEtalaseUseCase(getContext());
        ArrayList<String> etalaseIdList = new ArrayList<>();
        etalaseIdList.add("123");
        etalaseIdList.add("456");
        etalaseIdList.add("789");
        etalaseIdList.add("012");
        reorderShopEtalaseUseCase.execute(ReorderShopEtalaseUseCase.createRequestParams(etalaseIdList)
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
