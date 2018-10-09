package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ReputationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
public class GetShopReputationRetrofit {

    public interface OnGetShopReputationListener {
        void onSuccess(List<ReputationModel> modelList);

        void onFailure();
    }

    public interface OnUpdateLikeDislikeListener {
        void onSuccess();

        void onFailure();
    }

    private Context context;
    private ShopService shopService;
    private String shopId;
    private String shopDomain;
    private List<ReputationModel> modelToUpdateLike;

    private OnGetShopReputationListener onGetShopReputationListener;
    private OnUpdateLikeDislikeListener onUpdateLikeDislikeListener;

    private Subscription onGetShopReputationSubs;
    private Subscription onGetLikeDislikeSubs;

    public GetShopReputationRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        shopService = new ShopService();
    }

    public void setOnGetShopReputationListener(OnGetShopReputationListener listener) {
        this.onGetShopReputationListener = listener;
    }

    public void getShopReputation(String page) {
        Observable<Response<TkpdResponse>> observable = shopService.getApi().getShopReview(AuthUtil.generateParams(context, paramShopReview(page)));
        onGetShopReputationSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetShopReview());
    }

    public boolean isFetching() {
        if (onGetShopReputationSubs != null)
            return !onGetShopReputationSubs.isUnsubscribed();
        else return false;
    }

    private Observer<Response<TkpdResponse>> onGetShopReview() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onGetShopReputationListener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful())
                    parseListReputation(tkpdResponseResponse.body().getJsonData());
                else
                    onGetShopReputationListener.onFailure();
            }
        };
    }

    private void parseListReputation(JSONObject Result) {
        List<ReputationModel> list = new ArrayList<>();
        try {
            JSONArray reviewList = new JSONArray(Result.getString("list"));
            for (int i = 0; i < reviewList.length(); i++) {
                list.add(getReputationModel(reviewList.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onGetShopReputationListener.onFailure();
        }
        onGetShopReputationListener.onSuccess(list);
    }

    private ReputationModel getReputationModel(JSONObject review) throws JSONException {
        ReputationModel model = new ReputationModel();
        model.isGetLikeDislike = false;
        model.statusLikeDislike = 0;
        model.date = review.getString("review_create_time");
        model.comment = review.getString("review_message");
        model.username = review.getString("review_user_name");
        model.avatarUrl = review.getString("review_user_image");
        model.starQuality = review.getInt("review_rate_quality");
        model.starAccuracy = review.getInt("review_rate_accuracy");
        model.userId = review.getString("review_user_id");
        model.reviewId = review.getString("review_id");
        model.reputationId = review.optString("review_reputation_id", "");
        model.userLabel = review.getString("review_user_label");
//        model.shopReputation = getScoreMedal(review);
        model.shopAvatarUrl = review.getString("review_shop_reputation_set");
        model.typeMedal = getMedalType(review);
        model.levelMedal = getMedalLevel(review);
        model.shopReputation = getScoreMedal(review);
        model.shopName = MethodChecker.fromHtml(review.getString("review_shop_name")).toString();
//        model.shopAvatarUrl = review.getString("shop_img_uri");// TODO ???
        model.shopAvatarUrl = ""; // TODO ?????
        model.shopId = review.getString("review_shop_id");
        model.productId = review.optString("review_product_id", "");
        model.productName = review.optString("review_product_name", "");
        model.productName = MethodChecker.fromHtml(model.productName).toString();
        model.productAvatar = review.optString("review_product_image", "");

        JSONObject ownerJsonObj = new JSONObject(review.getString("review_product_owner"));
        model.userIdResponder = ownerJsonObj.getString("user_id");
        model.avatarUrlResponder = ownerJsonObj.getString("user_image");
        model.labelIdResponder = ownerJsonObj.getString("user_label_id");
        model.userLabelResponder = ownerJsonObj.getString("user_label");
        model.userNameResponder = ownerJsonObj.getString("user_name");

        JSONObject userReputation = new JSONObject(review.getString("review_user_reputation"));
        model.counterSmiley = Float.valueOf(userReputation.getString("positive_percentage").replace(",", ".")) + "%";
        model.noReputationUserScore = userReputation.optInt("no_reputation", 1);
        model.positive = userReputation.optInt("positive", 0);
        model.negative = userReputation.optInt("negative", 0);
        model.netral = userReputation.optInt("neutral", 0);
        if (!userReputation.isNull("summary")) {
            model.smiley = userReputation.getInt("summary");
        }
        model.counterLike = 0;
        model.counterDislike = 0;

        JSONObject Response = new JSONObject(review.getString("review_response"));
        if (!Response.getString("response_message").equals("0")) {
            model.responseMessage = MethodChecker.fromHtml(Response.getString("response_message")).toString();
            model.responseDate = Response.getString("response_create_time");
            model.counterResponse = 1;
        } else {
            model.counterResponse = 0;
        }

        try {
            model.reviewImageList = new ArrayList<>();
            JSONArray imageList = review.getJSONArray("review_image_attachment");
            for (int i = 0; i < imageList.length(); i++) {
                ImageUpload image = new ImageUpload();
                image.setPicSrc(imageList.getJSONObject(i).getString("uri_thumbnail"));
                image.setDescription(imageList.getJSONObject(i).getString("description"));
                image.setPicSrcLarge(imageList.getJSONObject(i).getString("uri_large"));
                image.setImageId(imageList.getJSONObject(i).getString("attachment_id"));
                model.reviewImageList.add(image);
            }
        } catch (JSONException e) {
            model.reviewImageList = new ArrayList<>();
        }
        return model;
    }

    private static int getMedalType(JSONObject jsonObject) throws JSONException {
        try {
            JSONObject shopReputation = new JSONObject(jsonObject.getString("review_shop_reputation_set"));
            JSONObject shopBadge = new JSONObject(shopReputation.getString("reputation_badge"));
            return shopBadge.optInt("set", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getMedalLevel(JSONObject jsonObject) throws JSONException {
        try {
            JSONObject shopReputation = new JSONObject(jsonObject.getString("review_shop_reputation_set"));
            JSONObject shopBadge = new JSONObject(shopReputation.getString("reputation_badge"));
            return shopBadge.optInt("level", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private static String getScoreMedal(JSONObject jsonObject) throws JSONException {
        try {
            JSONObject shopReputation = new JSONObject(jsonObject.getString("review_shop_reputation_set"));
            return shopReputation.optString("score", "0");
        } catch (Exception e) {
            return "0";
        }
    }

    //=============================================================================================================================

    public void setOnUpdateLikeDislikeListener(OnUpdateLikeDislikeListener listener, List<ReputationModel> modelList) {
        this.modelToUpdateLike = modelList;
        this.onUpdateLikeDislikeListener = listener;
    }

    public void getLikeDislike(String reviewIds) {
        Observable<Response<TkpdResponse>> observable = shopService.getApi().getLikeDislike(AuthUtil.generateParams(context, paramLikeDislike(reviewIds)));
        onGetLikeDislikeSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetLikeDislike());
    }

    private Observer<Response<TkpdResponse>> onGetLikeDislike() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                onGetLikeResponse(tkpdResponseResponse.body().getJsonData());
            }
        };
    }

    private void onGetLikeResponse(JSONObject response) {
        if (response == null) {
            onUpdateLikeDislikeListener.onFailure();
            return;
        }
        try {
            updateModelLikeDislike(response.getJSONArray("like_dislike_review"));
        } catch (JSONException e) {
            onUpdateLikeDislikeListener.onFailure();
        }
        onUpdateLikeDislikeListener.onSuccess();
    }

    private void updateModelLikeDislike(JSONArray JSONLikeDislike) throws JSONException {
        int total = JSONLikeDislike.length();
        for (int i = 0; i < total; i++) {
            JSONObject object = JSONLikeDislike.getJSONObject(i);
            JSONObject totalLikeDislike = object.getJSONObject("total_like_dislike");
            modelToUpdateLike.get(i).isGetLikeDislike = true;
            modelToUpdateLike.get(i).statusLikeDislike = object.optInt("like_status", 0);
            modelToUpdateLike.get(i).counterDislike = totalLikeDislike.getInt("total_dislike");
            modelToUpdateLike.get(i).counterLike = totalLikeDislike.getInt("total_like");
        }
        modelToUpdateLike = null;
    }

    private Map<String, String> paramShopReview(String page) {
        ArrayMap<String, String> param = new ArrayMap<>();
        param.put("shop_domain", shopDomain);
        param.put("shop_id", shopId);
        param.put("page", page);
        return param;
    }

    private Map<String, String> paramLikeDislike(String ids) {
        ArrayMap<String, String> param = new ArrayMap<>();
        param.put("shop_domain", shopDomain);
        param.put("shop_id", shopId);
        param.put("review_ids", ids);
        return param;
    }
}
