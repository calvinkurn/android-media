package com.tokopedia.core.fragment.shopstatistic;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.animation.FlipAnimation;
import com.tokopedia.core2.R;

import java.util.List;

/**
 * Created by Tkpd_Eka on 7/10/2015.
 */
public class ShopStatisticReview {

    private ViewHolder holder;
    private Context context;
    private View view;
    private Model model;

    public static class Model {
        public enum RatingType {
            ByQuality,
            ByAccuracy
        }
        public static class typePercentage {
            public float accuracy;
            public float quality;
        }
        public static class typeCounter {
            public String accuracy;
            public String quality;
        }
        public typeCounter counter;
        public typePercentage percentage;
        public List<typeCounter> listCounterStar;
        public List<typePercentage> listPercentageStar;
        public RatingType ratingType;
        public String qualityMean;
        public String accuracyMean;
        public String counterQualityReview;
        public String counterAccuracyReview;
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

    public ShopStatisticReview(Context context, View view) {
        this.context = context;
        this.view = view;
        holder = new ViewHolder();
        initView();
    }

    private void initView() {
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
    }

    public void setModel(Model model) {
        this.model = model;
        setModelToView();
    }

    private void setModelToView() {
        holder.accuracyFilterMeanTV.setText(model.accuracyMean);
        holder.qualityFilterMeanTV.setText(model.qualityMean);
        holder.accuracyCountReviewTV.setText(model.counterAccuracyReview);
        holder.qualityCountReviewTV.setText(model.counterQualityReview);

        if(getCurrentRatingType() == model.ratingType.ByQuality){
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

    private void changeStatisticText(final TextView textView, String paramCounter) {
        textView.setText(paramCounter);
    }

    private void changeStatisticBar(View view, float paramFloat, boolean reset) {
        if(reset){
            view.setScaleX(0f);
        }
        view.setPivotX(0f);
        view.animate().setDuration(300L).scaleX(paramFloat).start();
    }

    public void setListener() {
        holder.qualityFilterLayout.setOnClickListener(onChangeRatingClickListener());
        holder.accuracyFilterLayout.setOnClickListener(onChangeRatingClickListener());
    }

    private View.OnClickListener onChangeRatingClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getCurrentRatingType()) {
                    case ByAccuracy: changeRatingType(Model.RatingType.ByQuality); break;
                    case ByQuality: changeRatingType(Model.RatingType.ByAccuracy); break;
                }
            }
        };
    }

    private void changeRatingType(Model.RatingType paramRatingType) {
        setCurrentRatingType(paramRatingType);
        flipCard();
        setModelToView();
    }

    private void flipCard() {
        FlipAnimation flipAnimation = new FlipAnimation(holder.topFace, holder.bottomFace);
        if (holder.topFace.getVisibility() == View.GONE){
            flipAnimation.reverse();
        }
        holder.rootLayout.startAnimation(flipAnimation);
    }

    private void setCurrentRatingType(Model.RatingType param) {
        model.ratingType = param;
    }

    public Model.RatingType getCurrentRatingType() {
        if(model.ratingType == null) {
            model.ratingType = Model.RatingType.ByQuality;
        }
        return model.ratingType;
    }
}
