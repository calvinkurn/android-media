package com.tokopedia.tkpd.tkpdreputation.review.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class RatingBarReview extends BaseCustomView {

    private static final int DEFAULT_INPUT_VALUE_NUM_STARS = 5;
    public static final int DEF_VALUE_EMPTY = 0;
    private int numstars;
    private int rating;
    private float percentageProgress;
    private int totalReview;

    private RatingBar ratingBar;
    private RoundCornerProgressBar roundCornerProgressBar;
    private TextView counterReview;

    public RatingBarReview(@NonNull Context context) {
        super(context);
        init();
    }

    public RatingBarReview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatingBarReview(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.RatingBarReview);
        try {
            numstars = styledAttributes.getInt(R.styleable.RatingBarReview_numstars, DEFAULT_INPUT_VALUE_NUM_STARS);
            rating = styledAttributes.getInt(R.styleable.RatingBarReview_rating, DEF_VALUE_EMPTY);
            percentageProgress = styledAttributes.getFloat(R.styleable.RatingBarReview_progress_bar_percentage, DEF_VALUE_EMPTY);
            totalReview = styledAttributes.getInt(R.styleable.RatingBarReview_total_review, DEF_VALUE_EMPTY);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setNumstars(numstars);
        setRating(rating);
        setPercentageProgress(percentageProgress);
        setTotalReview(totalReview);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_rating_bar_review, this);
        ratingBar = view.findViewById(R.id.product_rating);
        roundCornerProgressBar = view.findViewById(R.id.progress_value_review);
        counterReview = view.findViewById(R.id.counter_review);
    }

    public int getNumstars() {
        return numstars;
    }

    public void setNumstars(int numstars) {
        this.numstars = numstars;
        ratingBar.setNumStars(numstars);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        ratingBar.setRating(rating);
    }

    public float getPercentageProgress() {
        return percentageProgress;
    }

    public void setPercentageProgress(float percentageProgress) {
        this.percentageProgress = percentageProgress;
        roundCornerProgressBar.setProgress(percentageProgress);
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
        counterReview.setText(String.valueOf(totalReview));
    }
}
