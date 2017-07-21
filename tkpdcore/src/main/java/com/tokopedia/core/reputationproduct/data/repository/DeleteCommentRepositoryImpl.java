package com.tokopedia.core.reputationproduct.data.repository;

import com.tokopedia.core.reputationproduct.data.factory.ReputationProductDataFactory;
import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class DeleteCommentRepositoryImpl implements DeleteCommentRepository {
    private final ReputationProductDataFactory reputationProductDataFactory;

    public DeleteCommentRepositoryImpl(ReputationProductDataFactory reputationProductDataFactory) {
        this.reputationProductDataFactory = reputationProductDataFactory;
    }

    @Override
    public Observable<ActResultDomain> deleteCommentRepository(Map<String, String> requestParams) {

        return reputationProductDataFactory.getDeleteCommentDataSource()
                .getDeleteCommentDataSource(requestParams);
    }
}
