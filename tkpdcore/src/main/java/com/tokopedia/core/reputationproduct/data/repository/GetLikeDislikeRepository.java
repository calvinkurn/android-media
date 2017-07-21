package com.tokopedia.core.reputationproduct.data.repository;

import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public interface GetLikeDislikeRepository {
    Observable<LikeDislikeDomain> getGetLikeDislikeRepository(Map<String, String> requestParams);
}
