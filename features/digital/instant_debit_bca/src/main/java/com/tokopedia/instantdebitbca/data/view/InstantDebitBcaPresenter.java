package com.tokopedia.instantdebitbca.data.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.instantdebitbca.data.domain.GetAccessTokenUseCase;
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaPresenter extends BaseDaggerPresenter<InstantDebitBcaContract.View>
        implements InstantDebitBcaContract.Presenter {

    private CompositeSubscription compositeSubscription;
    private GetAccessTokenUseCase getAccessTokenUseCase;

    @Inject
    public InstantDebitBcaPresenter(GetAccessTokenUseCase getAccessTokenUseCase) {
        this.getAccessTokenUseCase = getAccessTokenUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getAccessTokenBca() {
        compositeSubscription.add(
                getAccessTokenUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TokenInstantDebitBca>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().showErrorMessage(e);
                                }
                            }

                            @Override
                            public void onNext(TokenInstantDebitBca tokenInstantDebitBca) {
                                getView().openWidgetBca(tokenInstantDebitBca.getAccessToken());
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        detachView();
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }
}
