package com.tokopedia.tkpd.reputationproduct.facade;

import android.content.Context;
import android.text.Html;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.Logger;
import com.tokopedia.tkpd.reputationproduct.ReputationProduct;
import com.tokopedia.tkpd.reputationproduct.adapter.ListViewReputationAdapter;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.review.model.product_review.ReviewProductModel;
import com.tokopedia.tkpd.util.RatingStatsUtils;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 7/4/15.
 */
public class FacadeReputation {

    private static String URL;
    private static Context context;

    public static FacadeReputation createInstance(Context context) {
        FacadeReputation facade = new FacadeReputation();
        facade.context = context;
        facade.URL = TkpdUrl.GET_REVIEW;
        return facade;
    }

    public static List<ListViewReputationAdapter.Model> getListReputationFromJSON(JSONObject result){
        return getListReputation(result);
    }

    private static List<ListViewReputationAdapter.Model> getListReputation(JSONObject Result) {
        List<ListViewReputationAdapter.Model> list = new ArrayList<>();
        try {
            JSONArray ReviewList =  new JSONArray(Result.getString("list"));
            for (int i = 0; i < ReviewList.length(); i++) {
                JSONObject ReviewContent = new JSONObject(ReviewList.getString(i));
                ListViewReputationAdapter.Model model = new ListViewReputationAdapter.Model();
                model.isGetLikeDislike = false;
                model.statusLikeDislike = 3;
                model.date = ReviewContent.getString("create_time_fmt");
                model.comment = ReviewContent.getString("message");
                model.username = ReviewContent.getString("full_name");
                model.avatarUrl = ReviewContent.getString("user_img");
                model.starQuality = ReviewContent.getInt("rate_product");
                model.starAccuracy = ReviewContent.getInt("rate_accuracy");
                model.userId = ReviewContent.getString("user_id");
                model.reviewId = ReviewContent.getString("review_id");
                model.reputationId = ReviewContent.optString("reputation_id", "");
                model.userLabel = ReviewContent.getString("user_label");
                model.shopReputation = getScoreMedal(ReviewContent);
                model.typeMedal = getMedalType(ReviewContent);
                model.levelMedal = getMedalLevel(ReviewContent);
                model.shopName = Html.fromHtml(ReviewContent.getString("shop_name")).toString();
                model.shopAvatarUrl = ReviewContent.getString("shop_img_uri");
                model.shopId = ReviewContent.getString("shop_id");
                model.productId = ReviewContent.optString("product_id", "");
                model.productName = ReviewContent.optString("product_name", "");
                model.productAvatar = ReviewContent.optString("product_img", "");

                JSONObject ownerJsonObj = new JSONObject(ReviewContent.getString("owner"));
                model.userIdResponder = ownerJsonObj.getString("user_id");
                model.avatarUrlResponder = ownerJsonObj.getString("user_img");
                model.labelIdResponder = ownerJsonObj.getString("user_label_id");
                model.userLabelResponder = ownerJsonObj.getString("user_label");
                model.userNameResponder = ownerJsonObj.getString("full_name");

                JSONObject userReputation = new JSONObject(ReviewContent.getString("user_reputation"));
                model.counterSmiley = Float.valueOf(userReputation.getString("positive_percentage").replace(",",".")) + "%";
                model.noReputationUserScore = userReputation.optInt("no_reputation", 1);
                model.positive = userReputation.optInt("positive", 0);
                model.negative = userReputation.optInt("negative", 0);
                model.netral = userReputation.optInt("neutral", 0);
                if(!userReputation.isNull("summary")) {
                    model.smiley = userReputation.getInt("summary");
                }
                model.counterLike = 0;
                model.counterDislike = 0;


                if (!ReviewContent.isNull("response")) {
                    JSONObject Response = new JSONObject(ReviewContent.getString("response"));
                    model.responseMessage = Html.fromHtml(Response.getString("response_msg")).toString();
                    model.responseDate = Response.getString("response_time_fmt");
                    model.counterResponse = 1;
                } else {
                    model.counterResponse = 0;
                }

                list.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static int getMedalType(JSONObject jsonObject) throws JSONException {
        JSONObject badges = new JSONObject(jsonObject.getString("shop_badge_set"));
        return badges.getInt("set");
    }

    private static int getMedalLevel(JSONObject jsonObject) throws  JSONException {
        JSONObject badges = new JSONObject(jsonObject.getString("shop_badge_set"));
        return badges.getInt("level");
    }

    private static String getScoreMedal(JSONObject json) throws JSONException {
        return json.getString("shop_reputation");
    }

    private NetworkHandler.OnRetryRequestListener onRetryRequestListener;

    public interface FacadeReputationListenerV2 {
        void OnNoResult(RatingStatsUtils.ModelRatingStats model);
        void OnSuccessGetListReview(RatingStatsUtils.ModelRatingStats model, List<ListViewReputationAdapter.Model> list, JSONObject result);
        void OnError(ArrayList<String> messageError);
        void OnSuccessGetCounterLikeDislike(List<ListViewReputationAdapter.Model> list);
        void OnErrorGetCounterLikeDislike(List<ListViewReputationAdapter.Model> list);
        void OnTimeOut();
    }

    public interface FacadeGetLikeDislike{
        void OnSuccessGetCounterLikeDislike(ReviewProductModel model);
        void OnError(JSONObject Result);
        void OnTimeOut();
    }

    public static class Param {
        public String act;
        public String shopId;
        public String productId;
        public String NAV;
        public int page;
        public int filterValue;
        public RatingStatsUtils.RatingType filter;
    }

    public void getReputationV2(Param param, final FacadeReputationListenerV2 listener) {
        NetworkHandler network = new NetworkHandler(context, URL);
        network.AddParam("act", "get_product_review");
        network.AddParam("shop_id", param.shopId);
        network.AddParam("product_id", param.productId);
        network.AddParam("per_page", "10");
        network.AddParam("page", param.page);
        network.setTimeOutListener(new NetworkHandler.OnTimeOutListener() {
            @Override
            public void onNetworkTimeOut() {
                listener.OnTimeOut();
            }
        });
        onRetryRequestListener = network.getOnRetryRequestInterface();
        if(param.NAV.equals(ReputationProduct.SIX_MONTH_REPUTATION)){
            network.AddParam("month_range", 6);
        }
        switch (param.filter) {
            case ByAccuracy:
                network.AddParam("rate_accuracy", param.filterValue);
                break;
            case ByQuality:
                network.AddParam("rating", param.filterValue);
                break;
            default:
                break;
        }
        network.Commit(OnGetReputationConnectionV2(listener));
    }

    private NetworkHandler.NetworkHandlerListener OnGetReputationConnectionV2(final FacadeReputationListenerV2 listener) {
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                Logger.i("hangman", Result.toString());
                if(getListReputation(Result).size() == 0) {
                    listener.OnNoResult(getModelRatingStats(Result));
                } else {
                    listener.OnSuccessGetListReview(getModelRatingStats(Result), getListReputation(Result), Result);
                    getCounterLikeDislike(listener, getListReputation(Result));
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                listener.OnError(MessageError);
            }
        };
    }

    public void getCounterLikeDislike(final FacadeReputationListenerV2 listener, List<ListViewReputationAdapter.Model> list) {
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.PRODUCT_ADD);
        network.AddParam("act", "get_like_dislike_review");
        network.AddParam("review_ids", getReviewIDS(list));
        network.setTimeOutListener(new NetworkHandler.OnTimeOutListener() {
            @Override
            public void onNetworkTimeOut() {
                listener.OnTimeOut();
            }
        });
        onRetryRequestListener = network.getOnRetryRequestInterface();
        network.Commit(OnGetCounterLikeDislikeConnection(listener, list));
    }

    public void getCounterLikeDislikeNew(final FacadeGetLikeDislike listener, final ReviewProductModel model) {
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.PRODUCT_ADD);
        network.AddParam("act", "get_like_dislike_review");
        network.AddParam("review_ids", model.getReviewId());
        network.setTimeOutListener(new NetworkHandler.OnTimeOutListener() {
            @Override
            public void onNetworkTimeOut() {
                listener.OnTimeOut();
            }
        });
        onRetryRequestListener = network.getOnRetryRequestInterface();
        network.Commit(new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject result) {
                try {
                    JSONArray array = new JSONArray(result.getString("like_dislike_review"));
                    JSONObject object = new JSONObject(array.getString(0));
                    JSONObject totalLikeDislike = new JSONObject(object.getString("total_like_dislike"));
                    model.detail.isGetLikeDislike = true;
                    model.detail.statusLikeDislike = object.optInt("like_status", 0);
                    model.detail.counterDislike = totalLikeDislike.getInt("total_dislike");
                    model.detail.counterLike = totalLikeDislike.getInt("total_like");

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                listener.OnSuccessGetCounterLikeDislike(model);
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        });
    }

    public static String getReviewIDS(List<ListViewReputationAdapter.Model> list) {
        String reviewIDS = "";
        for(int i=0; i<list.size(); i++) {
            if(i == list.size()-1) {
                reviewIDS = reviewIDS+""+list.get(i).reviewId;
            } else {
                reviewIDS = reviewIDS+""+list.get(i).reviewId+"~";
            }
        }
        return reviewIDS;
    }

    private NetworkHandler.NetworkHandlerListener OnGetCounterLikeDislikeConnection(final FacadeReputationListenerV2 listener, final List<ListViewReputationAdapter.Model> list) {
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                CommonUtils.dumper("hangman like-dislike"+Result.toString());
                try {
                    listener.OnSuccessGetCounterLikeDislike(UpdateList(list, Result));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                listener.OnErrorGetCounterLikeDislike(showDefaultList(list));
                listener.OnError(MessageError);
            }
        };
    }

    private static List<ListViewReputationAdapter.Model> showDefaultList(List<ListViewReputationAdapter.Model> list) {
        for(int i = 0; i<list.size(); i++) {
            ListViewReputationAdapter.Model model = list.get(i);
            model.isGetLikeDislike = true;
            list.set(i, model);
        }
        return list;
    }

    public static List<ListViewReputationAdapter.Model> UpdateList(List<ListViewReputationAdapter.Model> list, JSONObject result) throws Exception {
        JSONArray array = new JSONArray(result.getString("like_dislike_review"));
        for(int j=0; j<array.length(); j++) {
            JSONObject object = new JSONObject(array.getString(j));
            JSONObject totalLikeDislike = new JSONObject(object.getString("total_like_dislike"));

            for(int i = 0; i<list.size(); i++) {
                if(list.get(i).reviewId.equals(object.getString("review_id"))) {
                    ListViewReputationAdapter.Model model = list.get(i);
                    model.isGetLikeDislike = true;

                    model.statusLikeDislike = object.optInt("like_status", 3);

                    model.counterDislike = totalLikeDislike.getInt("total_dislike");
                    model.counterLike = totalLikeDislike.getInt("total_like");

                    list.set(i, model);
                }
            }

        }

        return list;
    }

    public void retryConnection() {
        onRetryRequestListener.onRetryRequest();
    }

    private static RatingStatsUtils.ModelRatingStats getModelRatingStats(JSONObject Result) {
        RatingStatsUtils.ModelRatingStats model = new RatingStatsUtils.ModelRatingStats();
        setDefaultListValue(model);
        try {
            model.isProductOwner = Result.getInt("is_owner");
            JSONObject rating_list = new JSONObject(Result.getString("rating_list"));
            model.accuracyMean = rating_list.getString("rate_accuracy_point");
            model.qualityMean = rating_list.getString("rating_point");
            model.counterAccuracyReview = rating_list.getString("count_review_fmt");
            model.counterQualityReview = rating_list.getString("count_review_fmt");

            JSONArray rating_list_arr = rating_list.getJSONArray("rating_list_arr");
            for(int i=0;i<rating_list_arr.length();i++){
                JSONObject starObj = new JSONObject(rating_list_arr.getString(i));
                model.counter = new RatingStatsUtils.ModelRatingStats.typeCounter();
                model.percentage = new RatingStatsUtils.ModelRatingStats.typePercentage();

                int star = starObj.getInt("rating_star_point");
                model.counter.quality = starObj.getInt("rating");
                model.counter.accuracy = starObj.getInt("rate_accuracy");
                model.listCounterStar.set(star - 1, model.counter);
                model.percentage.quality = Float.valueOf(starObj.getString("total_rating_persen"))/100f;
                model.percentage.accuracy = Float.valueOf(starObj.getString("total_rate_accuracy_persen"))/100f;
                model.listPercentageStar.set(star - 1, model.percentage);
            }
            return model;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private static void setDefaultListValue(RatingStatsUtils.ModelRatingStats model) {
        model.accuracyMean = "0";
        model.qualityMean = "0";
        model.counterAccuracyReview = "0";
        model.counterQualityReview = "0";
        for(int j = 0 ; j<5; j++) {
            model.counter = new RatingStatsUtils.ModelRatingStats.typeCounter();
            model.percentage = new RatingStatsUtils.ModelRatingStats.typePercentage();
            model.counter.accuracy = 0;
            model.counter.quality = 0;
            model.percentage.accuracy = 0.0f;
            model.percentage.quality = 0.0f;
            model.listCounterStar.add(model.counter);
            model.listPercentageStar.add(model.percentage);
        }
    }

    public static class UpdateLikeDislikeParam {
        public String reviewID;
        public int statusLikeDislike;
        public String productID;
        public String shopID;
    }

    public interface OnUpdateLikeDislikeListener {
        void onSuccess();
        void onError(String Message);
    }

    public void updateLikeDislike(UpdateLikeDislikeParam param, OnUpdateLikeDislikeListener listener) {
        NetworkHandler networkHandler = new NetworkHandler(context, URL);
        networkHandler.AddParam("act", "like_dislike_review");
        networkHandler.AddParam("review_id", param.reviewID);
        if (param.statusLikeDislike == 0) {
            networkHandler.AddParam("like_status", 3);
        } else {
            networkHandler.AddParam("like_status", param.statusLikeDislike);
        }
        networkHandler.AddParam("product_id", param.productID);
        networkHandler.AddParam("shop_id", param.shopID);
        networkHandler.Commit(OnUpdateLikeDislikeConnection(listener));
    }

    private NetworkHandler.NetworkHandlerListener OnUpdateLikeDislikeConnection(final OnUpdateLikeDislikeListener listener) {
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                CommonUtils.dumper("hangman result update like dislike "+Result.toString());
                try {
                    if(Result.getInt("success") == 1) {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                listener.onError(MessageError.get(0));
            }
        };
    }
}
