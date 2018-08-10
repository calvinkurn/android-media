package com.tokopedia.shop.settings.basicinfo.view.fragment;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.basicinfo.view.presenter.ShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;

import javax.inject.Inject;

public class ShopSettingsInfoFragment extends BaseDaggerFragment implements ShopSettingsInfoPresenter.View {

    @Inject
    ShopSettingsInfoPresenter shopSettingsInfoPresenter;

    public static ShopSettingsInfoFragment newInstance() {
        return new ShopSettingsInfoFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(getContext());
        super.onCreate(savedInstanceState);

        shopSettingsInfoPresenter.getShopBasicData();

        //        GetShopBasicDataUseCase getShopBasicDataUseCase = new GetShopBasicDataUseCase(getContext());
//        getShopBasicDataUseCase.execute(RequestParams.EMPTY, new Subscriber<ShopBasicData>() {
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
//            public void onNext(ShopBasicData shopBasicData) {
//                Log.i("Test", "test");
//            }
//        });
//
//        UpdateShopBasicDataUseCase mutateShopBasicDataUseCase = new UpdateShopBasicDataUseCase(getContext());
//        mutateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams("tagline",
//                "description", "logoCode", "filePath", "fileName")
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

    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        shopSettingsInfoPresenter.attachView(this);
    }

    @Override
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        Log.i("Test", "test");
    }

    @Override
    public void onErrorGetShopBasicData(Throwable throwable) {
        Log.i("Test", "test");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopSettingsInfoPresenter!= null) {
            shopSettingsInfoPresenter.detachView();
        }
    }

}
