package com.tokopedia.analytics.debugger.ui.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analytics.debugger.domain.DeleteFpmLogUseCase;
import com.tokopedia.analytics.debugger.domain.DeleteGtmLogUseCase;
import com.tokopedia.analytics.debugger.domain.GetFpmLogUseCase;
import com.tokopedia.analytics.debugger.domain.GetGtmLogUseCase;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Subscriber;

public class FpmDebuggerPresenter implements AnalyticsDebugger.Presenter {
    private GetFpmLogUseCase getFpmLogUseCase;
    private DeleteFpmLogUseCase deleteFpmLogUseCase;
    private AnalyticsDebugger.View view;

    private String keyword = "";
    private int page = 0;
    private RequestParams requestParams;

    public FpmDebuggerPresenter(GetFpmLogUseCase getFpmLogUseCase,
                                DeleteFpmLogUseCase deleteFpmLogUseCase) {
        this.getFpmLogUseCase = getFpmLogUseCase;
        this.deleteFpmLogUseCase = deleteFpmLogUseCase;
        requestParams = RequestParams.create();
    }

    @Override
    public void attachView(AnalyticsDebugger.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getFpmLogUseCase.unsubscribe();
        deleteFpmLogUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void loadMore() {
        setRequestParams(++page, keyword);
        getFpmLogUseCase.execute(requestParams, loadMoreSubscriber());
    }

    @Override
    public void search(String text) {
        setRequestParams(page = 0, keyword = text);
        getFpmLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void reloadData() {
        setRequestParams(page = 0, keyword = "");
        getFpmLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void deleteAll() {
        deleteFpmLogUseCase.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                view.onDeleteCompleted();
            }
        });
    }

    private void setRequestParams(int page, String keyword) {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword);
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page);
    }

    private Subscriber<List<Visitable>> loadMoreSubscriber() {
        return new Subscriber<List<Visitable>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Visitable> visitables) {
                view.onLoadMoreCompleted(visitables);
            }
        };
    }

    private Subscriber<List<Visitable>> reloadSubscriber() {
        return new Subscriber<List<Visitable>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Visitable> visitables) {
                view.onReloadCompleted(visitables);
            }
        };
    }
}
