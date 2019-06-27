package com.tokopedia.feedcomponent.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.feedcomponent.R;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class RatingBarReview extends BaseCustomView {

    private static final int DEFAULT_INPUT_VALUE_NUM_STARS = 5;
    public static final int DEF_VALUE_EMPTY = 0;
    private int numstars;
    private int rating;

    private RatingBar ratingBar;

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
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setNumstars(numstars);
        setRating(rating);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_rating_bar_review, this);
        ratingBar = view.findViewById(R.id.product_rating);
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
}
