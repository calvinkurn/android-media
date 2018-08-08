package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.animation.FlipAnimation;
import com.tokopedia.core.R;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.AdvanceReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 7/9/15.
 */
public class RatingStatsUtils {

    private ViewHolder holder;
    private RatingType ratingType;
    private int rating;
    private ModelRatingStats model;
    private AdvanceReview advanceModel;
    private View[] filterLayout;
    private View[] barView;
    private TextView[] starCount;

    public RatingStatsUtils() {
        this.holder = new ViewHolder();
    }

    public enum RatingType {
        ByQuality,
        ByAccuracy
    }

    public static class ModelRatingStats {
        public static class typePercentage {
            public float accuracy;
            public float quality;
        }
        public static class typeCounter {
            public int accuracy;
            public int quality;
        }
        public int isProductOwner;
        public typeCounter counter;
        public typePercentage percentage;
        public List<typeCounter> listCounterStar = new ArrayList<>();
        public List<typePercentage> listPercentageStar = new ArrayList<>();
        public RatingType rating;
        public String qualityMean;
        public String accuracyMean;
        public String counterQualityReview;
        public String counterAccuracyReview;
    }

    public void setCurrentRatingType(RatingType param) {
        this.ratingType = param;
    }

    public RatingType getCurrentRatingType() {
        if(ratingType == null) {
            ratingType = RatingType.ByQuality;
        }
        return ratingType;
    }

    public void setCurrentRating(int args) {
        rating = args;
    }

    public int getCurrentRating() {
        return rating;
    }

    public static class ViewHolder {
        public TextView qualityCountReviewTV;
        public TextView accuracyCountReviewTV;
        public TextView qualityFilterMeanTV;
        public TextView accuracyFilterMeanTV;
        public TextView fiveStarCountTV;
        public TextView fourStarCountTV;
        public TextView threeStarCountTV;
        public TextView twoStarCountTV;
        public TextView oneStarCountTV;
        public LinearLayout clearFilterLayout;
        public LinearLayout qualityFilterLayout;
        public LinearLayout accuracyFilterLayout;
        public LinearLayout fiveStarFilterLayout;
        public LinearLayout fourStarFilterLayout;
        public LinearLayout threeStarFilterLayout;
        public LinearLayout twoStarFilterLayout;
        public LinearLayout oneStarFilterLayout;
        public View fiveRatingBarView;
        public View fourRatingBarView;
        public View threeRatingBarView;
        public View twoRatingBarView;
        public View oneRatingBarView;
        public View topFace;
        public View bottomFace;
        public View rootLayout;
    }

    private void setListener() {
        holder.qualityFilterLayout.setOnClickListener(onChangeRatingClickListener());
        holder.accuracyFilterLayout.setOnClickListener(onChangeRatingClickListener());
    }

    private View.OnClickListener onClearFilterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        };
    }

    private View.OnClickListener OnStarFilterClickListener(final int param) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByRatingStar(param, getCurrentRatingType());
                changeBackgroundLayout(view);
                holder.clearFilterLayout.setVisibility(View.VISIBLE);
            }
        };
    }

    private void sortByRatingStar(int star, RatingType rating) {
        setCurrentRating(star);
        listener.setOnSortByRatingStar(star, rating);
    }

    private void clearBackgroundLayout() {
        holder.fiveStarFilterLayout.setBackgroundColor(Color.TRANSPARENT);
        holder.fourStarFilterLayout.setBackgroundColor(Color.TRANSPARENT);
        holder.threeStarFilterLayout.setBackgroundColor(Color.TRANSPARENT);
        holder.twoStarFilterLayout.setBackgroundColor(Color.TRANSPARENT);
        holder.oneStarFilterLayout.setBackgroundColor(Color.TRANSPARENT);
    }
    public AdvanceReview getAdvanceReview() {
        return this.advanceModel;
    }
    private void changeBackgroundLayout(View view) {
        clearBackgroundLayout();
        view.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));
    }

    public void cacheSetBackground(int param){
        if(param>0){
            clearBackgroundLayout();
            filterLayout= new View[]{holder.oneStarFilterLayout, holder.twoStarFilterLayout
                    , holder.threeStarFilterLayout, holder.fourStarFilterLayout, holder.fiveStarFilterLayout};
            filterLayout[param-1].setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));
            holder.clearFilterLayout.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener onChangeRatingClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getCurrentRatingType()) {
                    case ByAccuracy: changeRatingType(RatingType.ByQuality); break;
                    case ByQuality: changeRatingType(RatingType.ByAccuracy); break;
                }
            }
        };
    }

    private void changeRatingType(RatingType param){
        setCurrentRatingType(param);
        flipCard();
        changeModelAdvance();
        if(getCurrentRating() != 0) {
            resetFilter();
        }
    }

    private void resetFilter() {
        sortByRatingStar(0, getCurrentRatingType());
        clearBackgroundLayout();
        holder.clearFilterLayout.setVisibility(View.GONE);
    }

    public void setModelToView(ModelRatingStats model) {
        this.model = model;
        changeModel();
    }

    private void changeModel() {
        holder.accuracyFilterMeanTV.setText(model.accuracyMean);
        holder.qualityFilterMeanTV.setText(model.qualityMean);
        holder.accuracyCountReviewTV.setText(model.counterAccuracyReview);
        holder.qualityCountReviewTV.setText(model.counterQualityReview);

        if(getCurrentRatingType() == ratingType.ByQuality){
            changeStatisticBar(holder.fiveRatingBarView, model.listPercentageStar.get(4).quality, false);
            changeStatisticBar(holder.fourRatingBarView, model.listPercentageStar.get(3).quality, false);
            changeStatisticBar(holder.threeRatingBarView, model.listPercentageStar.get(2).quality, false);
            changeStatisticBar(holder.twoRatingBarView, model.listPercentageStar.get(1).quality, false);
            changeStatisticBar(holder.oneRatingBarView, model.listPercentageStar.get(0).quality, false);
            changeStatisticText(holder.fiveStarCountTV, model.listCounterStar.get(4).quality);
            changeStatisticText(holder.fourStarCountTV, model.listCounterStar.get(3).quality);
            changeStatisticText(holder.threeStarCountTV, model.listCounterStar.get(2).quality);
            changeStatisticText(holder.twoStarCountTV, model.listCounterStar.get(1).quality);
            changeStatisticText(holder.oneStarCountTV, model.listCounterStar.get(0).quality);
        }else{
            changeStatisticBar(holder.fiveRatingBarView, model.listPercentageStar.get(4).accuracy, false);
            changeStatisticBar(holder.fourRatingBarView, model.listPercentageStar.get(3).accuracy, false);
            changeStatisticBar(holder.threeRatingBarView, model.listPercentageStar.get(2).accuracy, false);
            changeStatisticBar(holder.twoRatingBarView, model.listPercentageStar.get(1).accuracy, false);
            changeStatisticBar(holder.oneRatingBarView, model.listPercentageStar.get(0).accuracy, false);
            changeStatisticText(holder.fiveStarCountTV, model.listCounterStar.get(4).accuracy);
            changeStatisticText(holder.fourStarCountTV, model.listCounterStar.get(3).accuracy);
            changeStatisticText(holder.threeStarCountTV, model.listCounterStar.get(2).accuracy);
            changeStatisticText(holder.twoStarCountTV, model.listCounterStar.get(1).accuracy);
            changeStatisticText(holder.oneStarCountTV, model.listCounterStar.get(0).accuracy);
        }
    }

    public void setModelToView(AdvanceReview model) {
        this.advanceModel = model;
        changeModelAdvance();
    }

    public void changeModelAdvance() {
        holder.accuracyFilterMeanTV.setText(advanceModel.getProductRateAccuracyPoint());
        holder.qualityFilterMeanTV.setText(advanceModel.getProductRatingPoint());
        holder.accuracyCountReviewTV.setText(advanceModel.getProductReview());
        holder.qualityCountReviewTV.setText(advanceModel.getProductReview());
        if (advanceModel.getProductRatingList() != null) {
            if (advanceModel.getProductRatingList().size() > 0) {
                if (getCurrentRatingType() == ratingType.ByQuality) {
                    for (int i = 0; i < barView.length; i++) {
                        changeStatisticBar(barView[i], advanceModel.getProductRatingList().get(i).getRatingTotalRatingPersen(), false);
                        changeStatisticText(starCount[i], advanceModel.getProductRatingList().get(i).getRatingRating());
                    }
                } else {
                    for (int i = 0; i < barView.length; i++) {
                        changeStatisticBar(barView[i], advanceModel.getProductRatingList().get(i).getRatingTotalRateAccuracyPersen(), false);
                        changeStatisticText(starCount[i], advanceModel.getProductRatingList().get(i).getRatingRateAccuracy());
                    }
                }
            } else if (advanceModel.getProductRatingList().size() == 0) {
                for (int i = 0; i < barView.length; i++) {
                    changeStatisticBar(barView[i], "0", false);
                    changeStatisticText(starCount[i], 0);
                }
            }
        } else {
            for (int i = 0; i < barView.length; i++) {
                changeStatisticBar(barView[i], "0", false);
                changeStatisticText(starCount[i], 0);
            }
        }
    }

    // Function For Animation FLip in layout quality-accuracy - FW
    public void flipCard() {
        FlipAnimation flipAnimation = new FlipAnimation(holder.topFace, holder.bottomFace);
        if (holder.topFace.getVisibility() == View.GONE){
            flipAnimation.reverse();
        }
        holder.rootLayout.startAnimation(flipAnimation);
    }

    // Function For Animation Statistic Count Text View
    public void changeStatisticText(final TextView tv, final int mTextint){
        ValueAnimator animator = new ValueAnimator();
        int startCount = 0;
        startCount = Integer.valueOf(tv.getText().toString());
        animator.setObjectValues(startCount, mTextint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                tv.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    // Function For Animation View Statistic Bar - FW
    public void changeStatisticBar(final View mView, final float mPercentage, boolean reset){
        if(reset){
            mView.setScaleX(0f);
        }
        mView.setPivotX(0f);
        mView.animate().setDuration(300L).scaleX(mPercentage).start();
    }

    public void changeStatisticBar(final View mView, final String stringPercentage, boolean reset){
        float mPercentage = Float.valueOf(stringPercentage)/100f;
        if(reset){
            mView.setScaleX(0f);
        }
        mView.setPivotX(0f);
        mView.animate().setDuration(300L).scaleX(mPercentage).start();
    }

    private Context context;

    public static RatingStatsUtils createInstance(Context context) {
        RatingStatsUtils utils = new RatingStatsUtils();
        utils.context = context;
        return utils;
    }

    public void setView(View view) {
        holder.qualityCountReviewTV = (TextView) view.findViewById(R.id.quality_count_review_tv);
        holder.accuracyCountReviewTV= (TextView) view.findViewById(R.id.accuracy_count_review_tv);
        holder.qualityFilterMeanTV	 = (TextView) view.findViewById(R.id.quality_filter_mean_tv);
        holder.accuracyFilterMeanTV = (TextView) view.findViewById(R.id.accuracy_filter_mean_tv);
        holder.fiveStarCountTV = (TextView) view.findViewById(R.id.five_star_count_tv);
        holder.fourStarCountTV = (TextView) view.findViewById(R.id.four_star_count_tv);
        holder.threeStarCountTV	 = (TextView) view.findViewById(R.id.three_star_count_tv);
        holder.twoStarCountTV = (TextView) view.findViewById(R.id.two_star_count_tv);
        holder.oneStarCountTV = (TextView) view.findViewById(R.id.one_star_count_tv);
        holder.clearFilterLayout = (LinearLayout) view.findViewById(R.id.clear_filter_layout);
        holder.qualityFilterLayout  = (LinearLayout) view.findViewById(R.id.quality_filter_layout);
        holder.accuracyFilterLayout = (LinearLayout) view.findViewById(R.id.accuracy_filter_layout);
        holder.fiveStarFilterLayout = (LinearLayout) view.findViewById(R.id.five_star_filter_layout);
        holder.fourStarFilterLayout = (LinearLayout) view.findViewById(R.id.four_star_filter_layout);
        holder.threeStarFilterLayout= (LinearLayout) view.findViewById(R.id.three_star_filter_layout);
        holder.twoStarFilterLayout = (LinearLayout) view.findViewById(R.id.two_star_filter_layout);
        holder.oneStarFilterLayout = (LinearLayout) view.findViewById(R.id.one_star_filter_layout);
        holder.fiveRatingBarView = view.findViewById(R.id.five_rating_bar_view);
        holder.fourRatingBarView = view.findViewById(R.id.four_rating_bar_view);
        holder.threeRatingBarView = view.findViewById(R.id.three_rating_bar_view);
        holder.twoRatingBarView	= view.findViewById(R.id.two_rating_bar_view);
        holder.oneRatingBarView = view.findViewById(R.id.one_rating_bar_view);
        holder.topFace = view.findViewById(R.id.quality_filter_layout);
        holder.bottomFace = view.findViewById(R.id.accuracy_filter_layout);
        holder.rootLayout = view.findViewById(R.id.root_flip_animation);
        barView = new View[]{
                holder.fiveRatingBarView,
                holder.fourRatingBarView,
                holder.threeRatingBarView ,
                holder.twoRatingBarView ,
                holder.oneRatingBarView};
        starCount = new TextView[]{
                holder.fiveStarCountTV,
                holder.fourStarCountTV,
                holder.threeStarCountTV,
                holder.twoStarCountTV,
                holder.oneStarCountTV};
    }

    public interface RatingStatsClickListener {
        void setOnChangeRatingType(RatingType filter);
        void setOnSortByRatingStar(int star, RatingType rating);
    }

    private RatingStatsClickListener listener;

    public void setListener(RatingStatsClickListener mListener) {
        listener = mListener;
        setListener();
        holder.clearFilterLayout.setOnClickListener(onClearFilterClickListener());
        //-------- Separator --------//
        holder.fiveStarFilterLayout.setOnClickListener(OnStarFilterClickListener(5));
        holder.fourStarFilterLayout.setOnClickListener(OnStarFilterClickListener(4));
        holder.threeStarFilterLayout.setOnClickListener(OnStarFilterClickListener(3));
        holder.twoStarFilterLayout.setOnClickListener(OnStarFilterClickListener(2));
        holder.oneStarFilterLayout.setOnClickListener(OnStarFilterClickListener(1));
    }
}
