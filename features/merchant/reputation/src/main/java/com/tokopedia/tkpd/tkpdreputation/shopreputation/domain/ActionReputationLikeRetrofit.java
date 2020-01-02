package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain;

import android.content.Context;
import androidx.collection.ArrayMap;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewActService;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/11/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ActionReputationLikeRetrofit {

    public interface OnLikeDislikeReviewListener {
        void onSuccess();

        void onFailure(String message);
    }

    private Context context;
    private String shopId;
    private String shopDomain;

    @Inject
    ReviewActService actionReview;

    private OnLikeDislikeReviewListener onLikeDislikeReviewListener;

    private Subscription onLikeDislikeReviewSubs;

    public ActionReputationLikeRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
    }

    private Observer<Response<TokopediaWsV4Response>> onActionLikeDislike() {
        return new Observer<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful())
                    onLikeDislikeReviewListener.onSuccess();
                else
                    onLikeDislikeReviewListener.onFailure(tkpdResponseResponse.body().getErrorMessages().get(0));
            }
        };
    }

    private Map<String, String> paramShopTalk(String prodId, String reviewId, String likeStatus) {
        ArrayMap<String, String> param = new ArrayMap<>();
        param.put("shop_domain", shopDomain);
        param.put("shop_id", shopId);
        param.put("product_id", prodId);
        param.put("review_id", reviewId);
        param.put("like_status", likeStatus);
        return param;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void actionReportReview(String reviewId, String message) {
        Observable<Response<TokopediaWsV4Response>> observable = actionReview.getApi().reportReview(AuthUtil.generateParams(context, paramReportReview(reviewId, message)));
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionLikeDislike());
    }

    private Map<String, String> paramReportReview(String reviewId, String message) {
        ArrayMap<String, String> param = new ArrayMap<>();
        param.put("shop_domain", shopDomain);
        param.put("shop_id", shopId);
        param.put("review_id", reviewId);
        param.put("text_message", message);
        return param;
    }
}
