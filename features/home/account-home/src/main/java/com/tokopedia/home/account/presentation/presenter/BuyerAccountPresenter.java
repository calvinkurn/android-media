package com.tokopedia.home.account.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.subscriber.GetBuyerAccountSubscriber;
import com.tokopedia.home.account.presentation.viewmodel.AccountRecommendationTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountPresenter implements BuyerAccount.Presenter {

    private GetBuyerAccountUseCase getBuyerAccountUseCase;
    private BuyerAccount.View view;

    private final GetRecommendationUseCase getRecommendationUseCase;

    public static final String X_SOURCE_RECOM_WIDGET = "recom_widget";
    public static final String AKUN_PAGE = "akun";

    public BuyerAccountPresenter(GetBuyerAccountUseCase getBuyerAccountUseCase, GetRecommendationUseCase getRecommendationUseCase) {
        this.getBuyerAccountUseCase = getBuyerAccountUseCase;
        this.getRecommendationUseCase = getRecommendationUseCase;
    }

    @Override
    public void getFirstRecomData(){
        if (this.view == null)
            return;
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(0,
                X_SOURCE_RECOM_WIDGET,
                AKUN_PAGE,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
                    @Override
                    public void onStart() {
                        view.showLoadMoreLoading();
                    }
                    @Override
                    public void onCompleted() {
                        view.hideLoadMoreLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoadMoreLoading();
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        List<Visitable> visitables = new ArrayList<>();
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        visitables.add(new AccountRecommendationTitleViewModel(recommendationWidget.getTitle()));
                        visitables.addAll(getRecommendationVisitables(recommendationWidget));
                        view.hideLoadMoreLoading();
                        view.onRenderRecomAccountBuyer(visitables);
                    }
                });
    }

    @Override
    public void getRecomData(int page) {
        if (this.view == null)
            return;
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                page,
                X_SOURCE_RECOM_WIDGET,
                AKUN_PAGE,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
                    @Override
                    public void onStart() {
                        view.showLoadMoreLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoadMoreLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoadMoreLoading();
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        view.hideLoadMoreLoading();
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        view.onRenderRecomAccountBuyer(getRecommendationVisitables(recommendationWidget));
                    }
                });
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recomendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recomendationList.add(new RecommendationProductViewModel(item));
        }
        return recomendationList;
    }

    @Override
    public void getBuyerData(String query, String saldoQuery) {
        view.showLoading();
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(AccountConstants.QUERY, query);
        requestParams.putString(AccountConstants.SALDO_QUERY, saldoQuery);
        requestParams.putObject(AccountConstants.VARIABLES, new HashMap<>());

        getBuyerAccountUseCase.execute(requestParams, new GetBuyerAccountSubscriber(view));
    }

    @Override
    public void attachView(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getBuyerAccountUseCase.unsubscribe();
        view = null;
    }
}
