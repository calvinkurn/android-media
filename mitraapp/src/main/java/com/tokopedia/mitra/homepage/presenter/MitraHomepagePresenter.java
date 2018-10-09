package com.tokopedia.mitra.homepage.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;
import com.tokopedia.mitra.homepage.domain.MitraHomepageCategoriesUseCase;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MitraHomepagePresenter extends BaseDaggerPresenter<MitraHomepageContract.View> implements MitraHomepageContract.Presenter {

    private ApplinkRouter applinkRouter;
    private UserSession userSession;
    private MitraHomepageCategoriesUseCase mitraHomepageCategoriesUseCase;

    @Inject
    public MitraHomepagePresenter(ApplinkRouter applinkRouter, UserSession userSession, MitraHomepageCategoriesUseCase mitraHomepageCategoriesUseCase) {
        this.applinkRouter = applinkRouter;
        this.userSession = userSession;
        this.mitraHomepageCategoriesUseCase = mitraHomepageCategoriesUseCase;
    }

    @Override
    public void onViewCreated() {
        if (userSession.isLoggedIn()) {
            getView().hideLoginContainer();
        } else {
            getView().showLoginContainer();
        }
        getView().showCategoriesLoading();
        getView().hideCategories();

        mitraHomepageCategoriesUseCase
                .createObservable(mitraHomepageCategoriesUseCase.create())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CategoryRow>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().showMessageInRedSnackBar(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(List<CategoryRow> categoryRows) {
                        getView().hideCategoriesLoading();
                        getView().showCategories();
                        getView().renderCategories(categoryRows);
                    }
                });
    }

    @Override
    public void onLoginResultReceived() {
        if (userSession.isLoggedIn()) {
            getView().hideLoginContainer();
        } else {
            getView().showMessageInRedSnackBar(R.string.mitra_homepage_login_failed_error_message);
        }
    }

    @Override
    public void onLoginBtnClicked() {
        getView().navigateToLoginPage();
    }

    @Override
    public void onApplinkReceive(String applink) {
        if (applinkRouter.isSupportApplink(applink)) {
            getView().navigateToNextPage(applinkRouter.getApplinkIntent(getView().getActivity(), applink));
        } else {
            getView().showMessageInRedSnackBar(R.string.mitra_homepage_category_not_available);
        }
    }
}
