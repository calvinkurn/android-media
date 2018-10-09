package com.tokopedia.gm.statistic.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.statistic.domain.GMStatRepository;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatGetTransactionGraphUseCase extends UseCase<GMTransactionGraphMergeModel> {
    public static final String START_DATE = "sdt";
    public static final String END_DATE = "edt";
    private static final String SHOP_ID = "shop_id";

    private GMStatRepository gmStatRepository;
    private final GMModuleRouter gmModuleRouter;

    @Inject
    public GMStatGetTransactionGraphUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatRepository gmStatRepository,
            GMModuleRouter gmModuleRouter) {
        super(threadExecutor, postExecutionThread);
        this.gmStatRepository = gmStatRepository;
        this.gmModuleRouter = gmModuleRouter;
    }

    public static RequestParams createRequestParam(long startDate, long endDate, String shopId) {
        RequestParams params = RequestParams.create();
        params.putLong(START_DATE, startDate);
        params.putLong(END_DATE, endDate);
        params.putString(SHOP_ID, shopId);
        return params;
    }

    @Override
    public Observable<GMTransactionGraphMergeModel> createObservable(final RequestParams requestParams) {
        final long startDate = requestParams.getLong(START_DATE, -1);
        final long endDate = requestParams.getLong(END_DATE, -1);
        final String shopId = requestParams.getString(SHOP_ID, "");

        if(shopId.isEmpty()){
            return gmStatRepository.getTransactionGraph(startDate, endDate);
        }else {
            return gmStatRepository.getTransactionGraph(startDate, endDate)
                    .zipWith(gmModuleRouter.getDataDeposit(shopId), new Func2<GMTransactionGraphMergeModel, DataDeposit, GMTransactionGraphMergeModel>() {
                        @Override
                        public GMTransactionGraphMergeModel call(GMTransactionGraphMergeModel gmTransactionGraphMergeModel, DataDeposit dataDeposit) {
                            gmTransactionGraphMergeModel.dataDeposit = dataDeposit;
                            return gmTransactionGraphMergeModel;
                        }
                    });
        }
    }
}
