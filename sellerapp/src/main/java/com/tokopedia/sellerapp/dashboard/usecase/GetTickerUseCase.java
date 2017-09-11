package com.tokopedia.sellerapp.dashboard.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.sellerapp.dashboard.repository.TickerRepository;
import com.tokopedia.sellerapp.home.model.Ticker;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetTickerUseCase extends UseCase<Ticker.Tickers[]> {

    private TickerRepository tickerRepository;

    @Inject
    public GetTickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                            TickerRepository tickerRepository) {
        super(threadExecutor, postExecutionThread);
        this.tickerRepository = tickerRepository;
    }

    @Override
    public Observable<Ticker.Tickers[]> createObservable(RequestParams requestParams) {
        return tickerRepository.getTicker();
    }
}
