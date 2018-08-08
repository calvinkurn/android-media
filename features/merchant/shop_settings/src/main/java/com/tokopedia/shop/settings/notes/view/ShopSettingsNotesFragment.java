package com.tokopedia.shop.settings.notes.view;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.domain.usecase.GetShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.GetShopNotesUseCase;
import com.tokopedia.shop.common.graphql.model.shopetalase.ShopEtalase;
import com.tokopedia.shop.common.graphql.model.shopnote.ShopNotes;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopSettingsNotesFragment extends BaseDaggerFragment {

    public static ShopSettingsNotesFragment newInstance() {
        return new ShopSettingsNotesFragment();
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
        GetShopNotesUseCase useCase = new GetShopNotesUseCase(getContext());
        useCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopNotes>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Test", "test");
            }

            @Override
            public void onNext(ArrayList<ShopNotes> shopNotes) {
                Log.i("Test", "test");
            }
        });
    }

}
