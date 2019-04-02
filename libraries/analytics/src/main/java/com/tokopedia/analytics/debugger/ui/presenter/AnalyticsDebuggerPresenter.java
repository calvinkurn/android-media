package com.tokopedia.analytics.debugger.ui.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analytics.debugger.domain.DeleteGtmLogUseCase;
import com.tokopedia.analytics.debugger.domain.GetGtmLogUseCase;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerPresenter implements AnalyticsDebugger.Presenter {
    private GetGtmLogUseCase getGtmLogUseCase;
    private DeleteGtmLogUseCase deleteGtmLogUseCase;
    private AnalyticsDebugger.View view;

    private String keyword = "";
    private int page = 0;
    private RequestParams requestParams;

    @Inject
    public AnalyticsDebuggerPresenter(GetGtmLogUseCase getGtmLogUseCase,
                                      DeleteGtmLogUseCase deleteGtmLogUseCase) {
        this.getGtmLogUseCase = getGtmLogUseCase;
        this.deleteGtmLogUseCase = deleteGtmLogUseCase;
        requestParams = RequestParams.create();
    }

    @Override
    public void attachView(AnalyticsDebugger.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getGtmLogUseCase.unsubscribe();
        deleteGtmLogUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void loadMore() {
        setRequestParams(page++, keyword);
        getGtmLogUseCase.execute(requestParams, loadMoreSubscriber());
    }

    @Override
    public void search(String text) {
        setRequestParams(page = 0, keyword = text);
        getGtmLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void reloadData() {
        setRequestParams(page = 0, keyword = "");
        getGtmLogUseCase.execute(requestParams, reloadSubscriber());
    }

    @Override
    public void deleteAll() {
        deleteGtmLogUseCase.execute(new Subscriber<Boolean>() {
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
