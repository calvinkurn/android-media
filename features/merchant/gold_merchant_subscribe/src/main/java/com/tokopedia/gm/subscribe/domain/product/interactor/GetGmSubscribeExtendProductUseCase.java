package com.tokopedia.gm.subscribe.domain.product.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/2/17.
 */
public class GetGmSubscribeExtendProductUseCase extends UseCase<List<GmProductDomainModel>> {
    private final GmSubscribeProductRepository GmSubscribeProductRepository;

    @Inject
    public GetGmSubscribeExtendProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository GmSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.GmSubscribeProductRepository = GmSubscribeProductRepository;
    }

    @Override
    public Observable<List<GmProductDomainModel>> createObservable(RequestParams requestParams) {
        return GmSubscribeProductRepository.getExtendProductSelection();
    }
}
