package com.tokopedia.core.people.datamanager;

import android.content.Context;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.core.people.retrofit.RetrofitInteractor;
import com.tokopedia.core.people.retrofit.RetrofitInteractorImpl;

/**
 * Created on 5/31/16.
 */
public class DataManagerImpl implements DataManager {

    private final PeopleInfoFragmentPresenter presenter;
    private final RetrofitInteractor retrofit;

    public DataManagerImpl(PeopleInfoFragmentPresenter presenter) {
        this.presenter = presenter;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void requestPeopleInfo(final Context context, final String userID) {
        retrofit
                .getPeopleInfo(
                        context,
                        AuthUtil.generateParams(context, NetworkParam.paramGetPeopleInfo(userID)),
                        new RetrofitInteractor.GetPeopleInfoListener() {
                            @Override
                            public void onSuccess(InputOutputData data) {
                                presenter.setOnRequestSuccess(data);
                            }

                            @Override
                            public void onTimeout(NetworkErrorHelper.RetryClickedListener clickedListener) {
                                presenter.setOnRequestTimeOut(clickedListener);
                            }

                            @Override
                            public void onError(String message) {
                                presenter.setOnRequestError(message);
                            }

                            @Override
                            public void onNullData() {
                                presenter.setOnRequestError();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    @Override
    public void unSubscribe() {
        retrofit.unsubscribe();
    }

}
