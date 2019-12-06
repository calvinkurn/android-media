package com.tokopedia.sellerapp.facade;

import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticReview;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticSatisfaction;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastOneMonth;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastSixMonths;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopLastTwelveMonths;
import com.tokopedia.sellerapp.dashboard.model.ShopInfoDashboardModel;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfoTxStats;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopRatingStats;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopSatisfaction;

import java.util.ArrayList;

public class FacadeShopStatistic {

    private static final String TAG = "FacadeShopStatistic";
    private ShopInfoDashboardModel shopInfoDashboardModel;

    public static FacadeShopStatistic createInstance(String shopInfo){
        FacadeShopStatistic facade = new FacadeShopStatistic();

        facade.shopInfoDashboardModel = CacheUtil.convertStringToModel(shopInfo, ShopInfoDashboardModel.class);
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
        ShopInfoTxStats transaction = shopInfoDashboardModel.getShopInfoTxStats();
        model.hasTransaction = transaction.getShopTxHasTransaction();
        model.oneMonth.successPercentage = Float.valueOf(transaction.getShopTxSuccessRate1Month());
        model.threeMonth.successPercentage = Float.valueOf(transaction.getShopTxSuccessRate3Month());
        model.sixMonth.successPercentage = Float.valueOf(transaction.getShopTxSuccessRate6Month());
        model.oneMonth.totalTransaction = transaction.getShopTxSuccess1MonthFmt();
        model.threeMonth.totalTransaction = transaction.getShopTxSuccess3MonthFmt();
        model.sixMonth.totalTransaction = transaction.getShopTxSuccess6MonthFmt();
        model.oneMonth.showPercentage = transaction.getShopTxShowPercentage1Month();
        model.threeMonth.showPercentage = transaction.getShopTxShowPercentage3Month();
        model.sixMonth.showPercentage = transaction.getShopTxShowPercentage6Month();
        model.oneMonth.hasTransaction = transaction.getShopTxHasTransaction1Month();
        model.threeMonth.hasTransaction = transaction.getShopTxHasTransaction3Month();
        model.sixMonth.hasTransaction = transaction.getShopTxHasTransaction6Month();
    }

    private void getSatisfactionFromJSON(ShopStatisticSatisfaction.Model model) {
        model.month1 = getMonthlySatisfactionFromJSON(shopInfoDashboardModel.getShopSatisfaction().getRecentOneMonth());
        model.month6 = getMonthlySatisfactionFromJSON(shopInfoDashboardModel.getShopSatisfaction().getRecentSixMonth());
        model.month12 = getMonthlySatisfactionFromJSON(shopInfoDashboardModel.getShopSatisfaction().getRecentOneYear());
    }

    private ShopStatisticSatisfaction.Model.MonthlySatisfaction getMonthlySatisfactionFromJSON(ShopSatisfaction.SatisfactionItem satisfactionItem) {
        String positif = String.valueOf(satisfactionItem.getGood());
        String netral = String.valueOf(satisfactionItem.getNeutral());
        String negatif = String.valueOf(satisfactionItem.getBad());
        return new ShopStatisticSatisfaction.Model.MonthlySatisfaction(positif, netral, negatif);
    }

    public ShopStatisticReview.Model getReviewModel() {
        ShopStatisticReview.Model model = new ShopStatisticReview.Model();
        getReviewFromJSON(model);
        return model;
    }

    private void getReviewFromJSON(ShopStatisticReview.Model model) {
//        Ratings ratings = shopInfoDashboardModel.getShopRatingStats().ratings;
//        Quality quality = ratings.quality;
//        Accuracy accuracy = ratings.accuracy;
//        QualityWidth qualityWidth = ratings.qualityWidth;
//        AccuracyWidth accuracyWidth = ratings.accuracyWidth;
        ShopRatingStats shopRatingStats = shopInfoDashboardModel.getShopRatingStats();
        model.listCounterStar = new ArrayList<>();
        model.listPercentageStar = new ArrayList<>();
        model.counter = new ShopStatisticReview.Model.typeCounter();
        model.percentage = new ShopStatisticReview.Model.typePercentage();

        model.accuracyMean = String.valueOf(shopRatingStats.getRatingScore());
        model.qualityMean = String.valueOf(shopRatingStats.getRatingScore());
        model.counterAccuracyReview = String.valueOf(shopRatingStats.getTotalReview());
        model.counterQualityReview = String.valueOf(shopRatingStats.getTotalReview());

        ShopRatingStats.Detail shopRatingStatsStarDetail = shopRatingStats.getDetail();
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = (float) (shopRatingStatsStarDetail.getOneStar().getPercentage() / 100f);
        model.percentage.quality = (float) shopRatingStatsStarDetail.getOneStar().getPercentage() / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = (float) shopRatingStatsStarDetail.getTwoStar().getPercentage() / 100f;
        model.percentage.quality = (float) shopRatingStatsStarDetail.getTwoStar().getPercentage() / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = (float) shopRatingStatsStarDetail.getThreeStar().getPercentage() / 100f;
        model.percentage.quality = (float) shopRatingStatsStarDetail.getThreeStar().getPercentage() / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = (float) shopRatingStatsStarDetail.getFourStar().getPercentage() / 100f;
        model.percentage.quality = (float) shopRatingStatsStarDetail.getFourStar().getPercentage() / 100f;
            model.listPercentageStar.add(model.percentage);
            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.percentage.accuracy = (float) shopRatingStatsStarDetail.getFiveStar().getPercentage() / 100f;
        model.percentage.quality = (float) shopRatingStatsStarDetail.getFiveStar().getPercentage() / 100f;
            model.listPercentageStar.add(model.percentage);

            model.percentage = new ShopStatisticReview.Model.typePercentage();
        model.counter.accuracy = String.valueOf(shopRatingStatsStarDetail.getOneStar().getTotalReview());
        model.counter.quality = String.valueOf(shopRatingStatsStarDetail.getOneStar().getTotalReview());
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = String.valueOf(shopRatingStatsStarDetail.getTwoStar().getTotalReview());
        model.counter.quality = String.valueOf(shopRatingStatsStarDetail.getTwoStar().getTotalReview());
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = String.valueOf(shopRatingStatsStarDetail.getThreeStar().getTotalReview());
        model.counter.quality = String.valueOf(shopRatingStatsStarDetail.getThreeStar().getTotalReview());
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = String.valueOf(shopRatingStatsStarDetail.getFourStar().getTotalReview());
        model.counter.quality = String.valueOf(shopRatingStatsStarDetail.getFourStar().getTotalReview());
            model.listCounterStar.add(model.counter);
            model.counter = new ShopStatisticReview.Model.typeCounter();
        model.counter.accuracy = String.valueOf(shopRatingStatsStarDetail.getFiveStar().getTotalReview());
        model.counter.quality = String.valueOf(shopRatingStatsStarDetail.getFiveStar().getTotalReview());
            model.listCounterStar.add(model.counter);
    }

}
