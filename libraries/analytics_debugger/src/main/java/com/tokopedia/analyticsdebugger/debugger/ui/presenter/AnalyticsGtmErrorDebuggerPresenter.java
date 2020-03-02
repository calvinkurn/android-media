package com.tokopedia.analyticsdebugger.debugger.ui.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analyticsdebugger.debugger.domain.DeleteGtmErrorLogUseCase;
import com.tokopedia.analyticsdebugger.debugger.domain.GetGtmErrorLogUseCase;
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Subscriber;

public class AnalyticsGtmErrorDebuggerPresenter implements AnalyticsDebugger.Presenter {
    private GetGtmErrorLogUseCase getGtmErrorLogUseCase;
    private DeleteGtmErrorLogUseCase deleteGtmErrorLogUseCase;
    private AnalyticsDebugger.View view;

    private String keyword = "";
    private int page = 0;
    private RequestParams requestParams;

    public AnalyticsGtmErrorDebuggerPresenter(GetGtmErrorLogUseCase getGtmErrorLogUseCase,
                                              DeleteGtmErrorLogUseCase deleteGtmErrorLogUseCase) {
        this.getGtmErrorLogUseCase = getGtmErrorLogUseCase;
        this.deleteGtmErrorLogUseCase = deleteGtmErrorLogUseCase;
        requestParams = RequestParams.create();
    }

    @Override
    public void attachView(AnalyticsDebugger.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getGtmErrorLogUseCase.unsubscribe();
        deleteGtmErrorLogUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void loadMore() {
        setRequestParams(page++, keyword);
        getGtmErrorLogUseCase.execute(requestParams, loadMoreSubscriber());
    }

    @Override
    public void search(String text) {
        setRequestParams(page = 0, keyword = text);
        getGtmErrorLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void reloadData() {
        setRequestParams(page = 0, keyword = "");
        getGtmErrorLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void deleteAll() {
        deleteGtmErrorLogUseCase.execute(new Subscriber<Boolean>() {
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
