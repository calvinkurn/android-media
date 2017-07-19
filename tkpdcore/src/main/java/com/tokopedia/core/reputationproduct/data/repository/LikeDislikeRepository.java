package com.tokopedia.core.reputationproduct.data.repository;

import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public interface LikeDislikeRepository {
    Observable<LikeDislikeDomain> getLikeDislikeRepository(Map<String, String> requestParams);
}
