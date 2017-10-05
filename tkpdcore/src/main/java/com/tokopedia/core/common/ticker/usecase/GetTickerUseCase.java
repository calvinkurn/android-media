package com.tokopedia.core.common.ticker.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.common.ticker.repository.TickerRepository;

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
