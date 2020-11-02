package com.tokopedia.tkpd.tkpdreputation.domain.model;

import java.util.List;

/**
 * @author by nisie on 9/29/17.
 */

public class GetLikeDislikeReviewDomain {

    private List<LikeDislikeListDomain> list = null;

    public GetLikeDislikeReviewDomain(List<LikeDislikeListDomain> list) {
        this.list = list;
    }

    public List<LikeDislikeListDomain> getList() {
        return list;
    }
}
