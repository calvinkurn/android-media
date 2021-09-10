package com.tokopedia.review.feature.inbox.buyerreview.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.InboxReputationDetailPojo;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ProductData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReputationBadge;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ResponseCreateTime;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewCreateTime;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewImageUrl;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewInboxDatum;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewResponse;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewUpdateTime;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ShopData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ShopReputation;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.UserData;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.UserReputation;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ImageAttachmentDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ProductDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ResponseCreateTimeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewCreateTimeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewUpdateTimeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.UserDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.UserReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailMapper implements Func1<Response<TokopediaWsV4Response>, ReviewDomain> {
    @Override
    public ReviewDomain call(Response<TokopediaWsV4Response> response) {

        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                InboxReputationDetailPojo data = response.body()
                        .convertDataObj(InboxReputationDetailPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = "";
            if (response.body() != null) {
                messageError = response.body().getErrorMessageJoined();
            }
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private ReviewDomain mappingToDomain(InboxReputationDetailPojo data) {
        return new ReviewDomain(convertToListReview(data),
                data.getReputationId(),
                convertToUserDataDomain(data.getUserData()),
                convertToShopDataDomain(data.getShopData()),
                data.getInvoiceRefNum(),
                data.getInvoiceTime(),
                data.getOrderId());
    }

    private ShopDataDomain convertToShopDataDomain(ShopData shopData) {
        return new ShopDataDomain(shopData.getShopId(),
                shopData.getShopUserId(),
                shopData.getDomain(),
                shopData.getShopName(),
                shopData.getShopUrl(),
                shopData.getLogo(),
                convertToShopReputationDomain(shopData.getShopReputation()));
    }

    private ShopReputationDomain convertToShopReputationDomain(ShopReputation shopReputation) {
        return new ShopReputationDomain(
                shopReputation.getTooltip(),
                shopReputation.getReputationScore(),
                shopReputation.getScore(),
                shopReputation.getMinBadgeScore(),
                shopReputation.getReputationBadgeUrl(),
                convertToReputationBadgeDomain(shopReputation.getReputationBadge())
        );
    }

    private ReputationBadgeDomain convertToReputationBadgeDomain(ReputationBadge reputationBadge) {
        return new ReputationBadgeDomain(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    private UserDataDomain convertToUserDataDomain(UserData userData) {
        return new UserDataDomain(
                userData.getUserId(),
                userData.getFullName(),
                userData.getUserEmail(),
                userData.getUserStatus(),
                userData.getUserUrl(),
                userData.getUserLabel(),
                userData.getUserProfilePict(),
                convertToUserReputationDomain(userData.getUserReputation())
        );
    }

    private UserReputationDomain convertToUserReputationDomain(UserReputation userReputation) {
        return new UserReputationDomain(
                userReputation.getPositive(),
                userReputation.getNeutral(),
                userReputation.getNegative(),
                userReputation.getPositivePercentage(),
                userReputation.getNoReputation()
        );
    }

    private List<ReviewItemDomain> convertToListReview(InboxReputationDetailPojo data) {
        List<ReviewItemDomain> list = new ArrayList<>();
        for (ReviewInboxDatum pojo : data.getReviewInboxData()) {
            list.add(convertToReputationItem(pojo));
        }

        return list;
    }

    private ReviewItemDomain convertToReputationItem(ReviewInboxDatum pojo) {
        return new ReviewItemDomain(
                convertToProductDataDomain(pojo.getProductData()),
                pojo.getReviewInboxId(),
                pojo.getReviewId(),
                pojo.isReviewHasReviewed(),
                pojo.isReviewIsSkippable(),
                pojo.isReviewIsSkipped(),
                pojo.isReviewIsEditable(),
                convertToReviewDataDomain(pojo.getReviewData())
        );
    }

    private ReviewDataDomain convertToReviewDataDomain(ReviewData reviewData) {
        return new ReviewDataDomain(
                reviewData.getReviewId(),
                reviewData.getReputationId(),
                reviewData.getReviewTitle(),
                reviewData.getReviewMessage(),
                reviewData.getReviewRating(),
                convertToImageAttachmentDomain(reviewData.getReviewImageUrl()),
                convertToReviewCreateTime(reviewData.getReviewCreateTime()),
                convertToReviewUpdateTime(reviewData.getReviewUpdateTime()),
                reviewData.isReviewAnonymity(),
                convertToReviewResponseDomain(reviewData.getReviewResponse())
        );
    }

    private ReviewUpdateTimeDomain convertToReviewUpdateTime(ReviewUpdateTime reviewUpdateTime) {
        return new ReviewUpdateTimeDomain(reviewUpdateTime.getDateTimeFmt1(),
                reviewUpdateTime.getUnixTimestamp(),
                reviewUpdateTime.getDateTimeIos(),
                reviewUpdateTime.getDateTimeAndroid());
    }

    private ReviewCreateTimeDomain convertToReviewCreateTime(ReviewCreateTime reviewCreateTime) {
        return new ReviewCreateTimeDomain(reviewCreateTime.getDateTimeFmt1(),
                reviewCreateTime.getUnixTimestamp(),
                reviewCreateTime.getDateTimeIos(),
                reviewCreateTime.getDateTimeAndroid());
    }

    private List<ImageAttachmentDomain> convertToImageAttachmentDomain(List<ReviewImageUrl> reviewImageUrl) {
        List<ImageAttachmentDomain> list = new ArrayList<>();

        for (ReviewImageUrl pojo : reviewImageUrl) {
            list.add(new ImageAttachmentDomain(
                    pojo.getAttachmentId(),
                    pojo.getDescription(),
                    pojo.getUriThumbnail(),
                    pojo.getUriLarge()
            ));
        }
        return list;
    }

    private ReviewResponseDomain convertToReviewResponseDomain(ReviewResponse reviewResponse) {
        return new ReviewResponseDomain(
                reviewResponse.getResponseMessage(),
                convertToResponseCreateTime(reviewResponse.getResponseCreateTime()),
                reviewResponse.getResponseBy()
        );
    }

    private ResponseCreateTimeDomain convertToResponseCreateTime(ResponseCreateTime responseCreateTime) {
        return new ResponseCreateTimeDomain(responseCreateTime.getDateTimeFmt1(),
                responseCreateTime.getUnixTimestamp(),
                responseCreateTime.getDateTimeIos(),
                responseCreateTime.getDateTimeAndroid());
    }

    private ProductDataDomain convertToProductDataDomain(ProductData productData) {
        return new ProductDataDomain(
                productData.getProductId(),
                productData.getProductName(),
                productData.getProductImageUrl(),
                productData.getProductPageUrl(),
                productData.getShopId(),
                productData.getProductStatus()
        );
    }
}
