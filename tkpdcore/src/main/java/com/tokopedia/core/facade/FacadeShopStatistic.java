package com.tokopedia.core.facade;

import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticReview;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticSatisfaction;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;
import com.tokopedia.core.shopinfo.models.shopmodel.Accuracy;
import com.tokopedia.core.shopinfo.models.shopmodel.AccuracyWidth;
import com.tokopedia.core.shopinfo.models.shopmodel.Quality;
import com.tokopedia.core.shopinfo.models.shopmodel.QualityWidth;
import com.tokopedia.core.shopinfo.models.shopmodel.Ratings;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastOneMonth;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastSixMonths;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastTwelveMonths;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;

import java.util.ArrayList;

/**
 * modified by mnormansyah 10 april 2017
 */
public class FacadeShopStatistic {

    private static final String TAG = "FacadeShopStatistic";
    private ShopModel shopModel;

    public static FacadeShopStatistic createInstance(String shopInfo){
        FacadeShopStatistic facade = new FacadeShopStatistic();

        facade.shopModel = CacheUtil.convertStringToModel(shopInfo, ShopModel.class);
        return facade;
    }

    public ShopStatisticTransaction.Model getTransactionModel(){
        ShopStatisticTransaction.Model model = new ShopStatisticTransaction.Model();
        getTransactionFromJSON(model);
        return model;
    }

    public ShopStatisticSatisfaction.Model getSatisfactionModel(){
        ShopStatisticSatisfaction.Model model = new ShopStatisticSatisfaction.Model();
        getSatisfactionFromJSON(model);
        return model;
    }

    private void getTransactionFromJSON(ShopStatisticTransaction.Model model){
        ShopTxStats transaction = shopModel.shopTxStats;
        model.hasTransaction = transaction.shopTxHasTransaction;
        model.oneMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate1Month);
        model.threeMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate3Month);
        model.twelveMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate1Year);
        model.oneMonth.totalTransaction = transaction.shopTxSuccess1MonthFmt;
        model.threeMonth.totalTransaction = transaction.shopTxSuccess3MonthFmt;
        model.twelveMonth.totalTransaction = transaction.shopTxSuccess1YearFmt;
        model.oneMonth.showPercentage = transaction.shopTxShowPercentage1Month;
        model.threeMonth.showPercentage = transaction.shopTxShowPercentage3Month;
        model.twelveMonth.showPercentage = transaction.shopTxShowPercentage1Year;
        model.oneMonth.hasTransaction = transaction.shopTxHasTransaction1Month;
        model.threeMonth.hasTransaction = transaction.shopTxHasTransaction3Month;
        model.twelveMonth.hasTransaction = transaction.shopTxHasTransaction1Year;
    }

    private void getSatisfactionFromJSON(ShopStatisticSatisfaction.Model model) {
        model.month1 = getMonthlySatisfactionFromJSON(shopModel.stats.shopLastOneMonth);
        model.month6 = getMonthlySatisfactionFromJSON(shopModel.stats.shopLastSixMonths);
        model.month12 = getMonthlySatisfactionFromJSON(shopModel.stats.shopLastTwelveMonths);
    }

    private ShopStatisticSatisfaction.Model.MonthlySatisfaction getMonthlySatisfactionFromJSON(ShopLastOneMonth month) {
        String positif = month.countScoreGood;
        String netral = month.countScoreNeutral;
        String negatif = month.countScoreBad;
        return new ShopStatisticSatisfaction.Model.MonthlySatisfaction(positif, netral, negatif);
    }

    private ShopStatisticSatisfaction.Model.MonthlySatisfaction getMonthlySatisfactionFromJSON(ShopLastSixMonths month) {
        String positif = month.countScoreGood;
        String netral = month.countScoreNeutral;
        String negatif = month.countScoreBad;
        return new ShopStatisticSatisfaction.Model.MonthlySatisfaction(positif, netral, negatif);
    }

    private ShopStatisticSatisfaction.Model.MonthlySatisfaction getMonthlySatisfactionFromJSON(ShopLastTwelveMonths month) {
        String positif = month.countScoreGood;
        String netral = month.countScoreNeutral;
        String negatif = month.countScoreBad;
        return new ShopStatisticSatisfaction.Model.MonthlySatisfaction(positif, netral, negatif);
    }


    public ShopStatisticReview.Model getReviewModel() {
        ShopStatisticReview.Model model = new ShopStatisticReview.Model();
        getSatisfactionFromJSON(model);
        return model;
    }

    private void getSatisfactionFromJSON(ShopStatisticReview.Model model) {
        Ratings ratings = shopModel.ratings;
        Quality quality = ratings.quality;
        Accuracy accuracy = ratings.accuracy;
        QualityWidth qualityWidth = ratings.qualityWidth;
        AccuracyWidth accuracyWidth = ratings.accuracyWidth;

        model.listCounterStar = new ArrayList<>();
        model.listPercentageStar = new ArrayList<>();
        model.counter = new ShopStatisticReview.Model.typeCounter();
        model.percentage = new ShopStatisticReview.Model.typePercentage();

        model.accuracyMean = accuracy.average;
        model.qualityMean = quality.average;
        model.counterAccuracyReview = accuracy.countTotal;
        model.counterQualityReview = quality.countTotal;

            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = Float.valueOf(accuracyWidth.oneStarRank) / 100f;
        model.percentage.quality = Float.valueOf(qualityWidth.oneStarRank) / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = Float.valueOf(accuracyWidth.twoStarRank) / 100f;
        model.percentage.quality = Float.valueOf(qualityWidth.twoStarRank) / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = Float.valueOf(accuracyWidth.threeStarRank) / 100f;
        model.percentage.quality = Float.valueOf(qualityWidth.threeStarRank) / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = Float.valueOf(accuracyWidth.fourStarRank) / 100f;
        model.percentage.quality = Float.valueOf(qualityWidth.fourStarRank) / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = Float.valueOf(accuracyWidth.fiveStarRank) / 100f;
        model.percentage.quality = Float.valueOf(qualityWidth.fiveStarRank) / 100f;
            model.listPercentageStar.add(model.percentage);

            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.counter.accuracy = accuracy.oneStarRank;
        model.counter.quality = quality.oneStarRank;
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = accuracy.twoStarRank;
        model.counter.quality = quality.twoStarRank;
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = accuracy.threeStarRank;
        model.counter.quality = quality.threeStarRank;
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = accuracy.fourStarRank;
        model.counter.quality = quality.fourStarRank;
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = accuracy.fiveStarRank;
        model.counter.quality = quality.fiveStarRank;
            model.listCounterStar.add(model.counter);
    }

}
