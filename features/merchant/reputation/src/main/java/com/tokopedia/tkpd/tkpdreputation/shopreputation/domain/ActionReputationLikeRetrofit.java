package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;

import java.util.Map;

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

    private ActionService actionService;
    private ReviewActService actionReview;

    private OnLikeDislikeReviewListener onLikeDislikeReviewListener;

    private Subscription onLikeDislikeReviewSubs;

    public ActionReputationLikeRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        actionService = new ActionService();
        actionReview = new ReviewActService();
    }

    public void setOnLikeDislikeReviewListener(OnLikeDislikeReviewListener listener) {
        this.onLikeDislikeReviewListener = listener;
    }

    public void actionReputationLikeDislike(String prodId, String reviewId, String status) {
        Observable<Response<TkpdResponse>> observable = actionService.getApi().actionLikeDislikeReview(AuthUtil.generateParams(context, paramShopTalk(prodId, reviewId, status)));
        onLikeDislikeReviewSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionLikeDislike());
    }

    private Observer<Response<TkpdResponse>> onActionLikeDislike() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
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
        Observable<Response<TkpdResponse>> observable = actionReview.getApi().reportReview(AuthUtil.generateParams(context, paramReportReview(reviewId, message)));
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
