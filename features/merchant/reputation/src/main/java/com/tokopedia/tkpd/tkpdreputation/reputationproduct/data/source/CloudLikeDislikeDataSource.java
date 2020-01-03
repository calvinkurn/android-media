package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpd.tkpdreputation.network.shop.ShopService;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudLikeDislikeDataSource {

    private ShopService shopService;
    private LikeDislikeDomainMapper likeDislikeDomainMapper;
    private UserSessionInterface userSessionInterface;

    public CloudLikeDislikeDataSource(ShopService shopService,
                                      LikeDislikeDomainMapper likeDislikeDomainMapper,
                                      UserSessionInterface userSessionInterface) {
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
        this.userSessionInterface = userSessionInterface;
    }

    public Observable<LikeDislikeDomain> getLikeDislikeReviewCloudSource(Map<String, String> parameters) {
        return shopService.getApi().getLikeReview(AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(),
                userSessionInterface.getDeviceId(),
                parameters
        )).map(likeDislikeDomainMapper);
    }
}
