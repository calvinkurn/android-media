package com.tokopedia.core.facade;

import android.util.Log;

import com.tokopedia.core.fragment.shopstatistic.ShopStatisticResponse;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticReview;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticSatisfaction;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacadeShopStatistic {

    private JSONObject shopInfo;
    private static final String TAG = FacadeShopStatistic.class.getSimpleName();
    public static FacadeShopStatistic createInstance(String shopInfo){
        FacadeShopStatistic facade = new FacadeShopStatistic();
        try {
            facade.shopInfo = new JSONObject(shopInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facade;
    }

    public ShopStatisticTransaction.Model getTransactionModel(){
        ShopStatisticTransaction.Model model = new ShopStatisticTransaction.Model();
        getTransactionFromJSON(model);
        return model;
    }

    public ShopStatisticResponse.Model getResponseModel(){
        ShopStatisticResponse.Model model = new ShopStatisticResponse.Model();
        getResponseFromJSON(model);
        return model;
    }

    public ShopStatisticSatisfaction.Model getSatisfactionModel(){
        ShopStatisticSatisfaction.Model model = new ShopStatisticSatisfaction.Model();
        getSatisfactionFromJSON(model);
        return model;
    }

    private void getTransactionFromJSON(ShopStatisticTransaction.Model model){
        JSONObject transaction = shopInfo.optJSONObject("shop_tx_stats");
        Log.d(TAG, "getTransactionFromJSON "+transaction.toString());
        model.hasTransaction = transaction.optInt("shop_tx_has_transaction", 0);
        model.oneMonth.successPercentage = Float.valueOf(transaction.optString("shop_tx_success_rate_1_month"));
        model.threeMonth.successPercentage = Float.valueOf(transaction.optString("shop_tx_success_rate_3_month"));
        model.twelveMonth.successPercentage = Float.valueOf(transaction.optString("shop_tx_success_rate_1_year"));
        model.oneMonth.totalTransaction = transaction.optString("shop_tx_success_1_month_fmt");
        model.threeMonth.totalTransaction = transaction.optString("shop_tx_success_3_month_fmt");
        model.twelveMonth.totalTransaction = transaction.optString("shop_tx_success_1_year_fmt");
        model.oneMonth.showPercentage = transaction.optInt("shop_tx_show_percentage_1_month", 0);
        model.threeMonth.showPercentage = transaction.optInt("shop_tx_show_percentage_3_month", 0);
        model.twelveMonth.showPercentage = transaction.optInt("shop_tx_show_percentage_1_year",0);
        model.oneMonth.hasTransaction = transaction.optInt("shop_tx_has_transaction_1_month", 0);
        model.threeMonth.hasTransaction = transaction.optInt("shop_tx_has_transaction_3_month", 0);
        model.twelveMonth.hasTransaction = transaction.optInt("shop_tx_has_transaction_1_year", 0);
    }

    private void getResponseFromJSON(ShopStatisticResponse.Model model){
        JSONObject response = shopInfo.optJSONObject("speed");
        JSONObject threeDay = response.optJSONObject("three_days");
        JSONObject twoDay = response.optJSONObject("two_days");
        JSONObject oneDay = response.optJSONObject("one_day");

        try {
            model.responseFast = oneDay.optInt("count");
            model.responseMedium = twoDay.optInt("count");
            model.responseSlow = threeDay.optInt("count");
            model.barFast = Float.valueOf(oneDay.optString("width"));
            model.barMedium = Float.valueOf(twoDay.optString("width"));
            model.barSlow = Float.valueOf(threeDay.optString("width"));
        } catch (Exception e) {
            model.responseFast = 0;
            model.responseMedium = 0;
            model.responseSlow = 0;
        }
        model.mainResponse = response.optString("badge");
        model.responseSpeed = response.optString("speed_level");
    }

    @SuppressWarnings("unused")
    private void getSatisfactionFromJSON(ShopStatisticSatisfaction.Model model){
        JSONObject shopStats = shopInfo.optJSONObject("stats");
        JSONObject oneMonth = shopStats.optJSONObject("shop_last_one_month");
        JSONObject sixMonth = shopStats.optJSONObject("shop_last_six_months");
        JSONObject twelveMonth = shopStats.optJSONObject("shop_last_twelve_months");
//        JSONObject ownerInfo = shopInfo.optJSONObject("owner");
//        JSONObject userRep = ownerInfo.optJSONObject("user_reputation");

        model.month1 = getMonthlySatisfactionFromJSON(oneMonth);
        model.month6 = getMonthlySatisfactionFromJSON(sixMonth);
        model.month12 = getMonthlySatisfactionFromJSON(twelveMonth);
    }

    private ShopStatisticSatisfaction.Model.MonthlySatisfaction getMonthlySatisfactionFromJSON(JSONObject month){
        String positif = month.optString("count_score_good");
        String netral = month.optString("count_score_neutral");
        String negatif = month.optString("count_score_bad");
        return new ShopStatisticSatisfaction.Model.MonthlySatisfaction(positif, netral, negatif);
    }

    public ShopStatisticReview.Model getReviewModel() {
        ShopStatisticReview.Model model = new ShopStatisticReview.Model();
        getSatisfactionFromJSON(model);
        return model;
    }

    private void getSatisfactionFromJSON(ShopStatisticReview.Model model) {
        JSONObject ratings  = shopInfo.optJSONObject("ratings");
        JSONObject quality = ratings.optJSONObject("quality");
        JSONObject accuracy = ratings.optJSONObject("accuracy");
        JSONObject qualityWidth = ratings.optJSONObject("quality_width");
        JSONObject accuracyWidth = ratings.optJSONObject("accuracy_width");

        model.listCounterStar = new ArrayList<>();
        model.listPercentageStar = new ArrayList<>();
        model.counter = new ShopStatisticReview.Model.typeCounter();
        model.percentage = new ShopStatisticReview.Model.typePercentage();

        try {
            model.accuracyMean = accuracy.getString("average");
            model.qualityMean = quality.getString("average");
            model.counterAccuracyReview = accuracy.getString("count_total");
            model.counterQualityReview = quality.getString("count_total");

            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.percentage.accuracy = Float.valueOf(accuracyWidth.getString("one_star_rank"))/100f;
            model.percentage.quality = Float.valueOf(qualityWidth.getString("one_star_rank"))/100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.percentage.accuracy = Float.valueOf(accuracyWidth.getString("two_star_rank"))/100f;
            model.percentage.quality = Float.valueOf(qualityWidth.getString("two_star_rank"))/100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.percentage.accuracy = Float.valueOf(accuracyWidth.getString("three_star_rank"))/100f;
            model.percentage.quality = Float.valueOf(qualityWidth.getString("three_star_rank"))/100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.percentage.accuracy = Float.valueOf(accuracyWidth.getString("four_star_rank"))/100f;
            model.percentage.quality = Float.valueOf(qualityWidth.getString("four_star_rank"))/100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.percentage.accuracy = Float.valueOf(accuracyWidth.getString("five_star_rank"))/100f;
            model.percentage.quality = Float.valueOf(qualityWidth.getString("five_star_rank"))/100f;
            model.listPercentageStar.add(model.percentage);

            model.percentage = new ShopStatisticReview.Model.typePercentage();
            model.counter.accuracy = accuracy.getString("one_star_rank");
            model.counter.quality = quality.getString("one_star_rank");
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
            model.counter.accuracy = accuracy.getString("two_star_rank");
            model.counter.quality = quality.getString("two_star_rank");
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
            model.counter.accuracy = accuracy.getString("three_star_rank");
            model.counter.quality = quality.getString("three_star_rank");
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
            model.counter.accuracy = accuracy.getString("four_star_rank");
            model.counter.quality = quality.getString("four_star_rank");
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
            model.counter.accuracy = accuracy.getString("five_star_rank");
            model.counter.quality = quality.getString("five_star_rank");
            model.listCounterStar.add(model.counter);
        } catch (Exception e) {
            e.printStackTrace();
            model.accuracyMean = "0";
            model.qualityMean = "0";
            model.counterAccuracyReview = "0";
            model.counterQualityReview = "0";

            model.percentage.accuracy = 0.0f;
            model.percentage.quality = 0.0f;
            model.counter.accuracy = "0";
            model.counter.quality = "0";
            for(int i = 0; i<5; i++) {
                model.listPercentageStar.add(model.percentage);
                model.listCounterStar.add(model.counter);
            }
        }
    }

}
