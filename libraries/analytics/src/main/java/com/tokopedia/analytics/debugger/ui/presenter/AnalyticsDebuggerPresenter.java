package com.tokopedia.analytics.debugger.ui.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
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
    private AnalyticsDebugger.View view;

    private String keyword = "";
    private int page = 0;
    private RequestParams requestParams;

    @Inject
    public AnalyticsDebuggerPresenter(GetGtmLogUseCase getGtmLogUseCase) {
        this.getGtmLogUseCase = getGtmLogUseCase;
        requestParams = RequestParams.create();
    }

    @Override
    public void attachView(AnalyticsDebugger.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getGtmLogUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void loadMore() {
        page++;
        loadData();
    }

    @Override
    public void search(String text) {
        keyword = text;
        page = 0;
        loadData();
    }

    @Override
    public void reloadData() {
        keyword = "";
        page = 0;
        loadData();
    }

    private void loadData() {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword);
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page);
        getGtmLogUseCase.execute(requestParams, new Subscriber<List<Visitable>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Visitable> visitables) {
                view.onFetchCompleted(visitables);
            }
        });
    }
}
