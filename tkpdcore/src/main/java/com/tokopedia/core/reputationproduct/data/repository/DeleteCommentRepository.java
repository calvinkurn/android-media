package com.tokopedia.core.reputationproduct.data.repository;

import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public interface DeleteCommentRepository {
    Observable<ActResultDomain> deleteCommentRepository(Map<String, String> requestParams);
}
