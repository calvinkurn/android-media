package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.CartRepository;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public class GetAllCartUseCase extends UseCase<List<CartDomain>> {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private CartRepository cartRepository;

    public GetAllCartUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             CartRepository cartRepository) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<List<CartDomain>> createObservable(RequestParams requestParams) {
        return cartRepository.getAllCartProducts();
    }
}
