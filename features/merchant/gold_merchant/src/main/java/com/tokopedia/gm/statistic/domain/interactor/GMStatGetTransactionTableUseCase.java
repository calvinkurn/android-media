package com.tokopedia.gm.statistic.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.domain.GMStatRepository;
import com.tokopedia.gm.statistic.domain.model.transaction.table.GetTransactionTableModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatGetTransactionTableUseCase extends UseCase<GetTransactionTableModel> {
    public static final String START_DATE = "sdt";
    public static final String END_DATE = "edt";
    public static final String PAGE = "p";
    public static final String PAGE_SIZE = "ps";

    private GMStatRepository gmStatRepository;

    @Inject
    public GMStatGetTransactionTableUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatRepository gmStatRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatRepository = gmStatRepository;
    }

    public static RequestParams createRequestParam(long startDate, long endDate,
                                                   int page, int pageSize,
                                                   @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        RequestParams params = RequestParams.create();
        params.putLong(START_DATE, startDate);
        params.putLong(END_DATE, endDate);
        params.putInt(PAGE, page);
        params.putInt(PAGE_SIZE, pageSize);
        params.putInt(GMTransactionTableSortType.param, sortType);
        params.putInt(GMTransactionTableSortBy.param, sortBy);
        return params;
    }

    @Override
    public Observable<GetTransactionTableModel> createObservable(RequestParams requestParams) {
        final long startDate = requestParams.getLong(START_DATE, -1);
        final long endDate = requestParams.getLong(END_DATE, -1);
        final int sortType = requestParams.getInt(GMTransactionTableSortType.param, -1);
        final int sortBy = requestParams.getInt(GMTransactionTableSortBy.param, -1);
        final int page = requestParams.getInt(PAGE, -1);
        final int pageSize = requestParams.getInt(PAGE_SIZE, -1);
        return gmStatRepository.getTransactionTable(startDate, endDate,page, pageSize, sortType, sortBy);
    }
}
