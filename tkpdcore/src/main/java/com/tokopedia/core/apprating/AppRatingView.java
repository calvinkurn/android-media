package com.tokopedia.core.apprating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;

/**
 * Created by okasurya on 1/10/18.
 */

public class AppRatingView extends FrameLayout {
    private ImageView imageRating;
    private TextView textDescription;
    private RatingBar ratingBar;

    public AppRatingView(@NonNull Context context) {
        this(context, null, 0);
    }

    public AppRatingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppRatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_app_rating, this, true);

        imageRating = rootView.findViewById(R.id.image_rating);
        ratingBar = rootView.findViewById(R.id.rating_bar);
        textDescription = rootView.findViewById(R.id.text_description);
        ratingBar.setOnRatingBarChangeListener(getRatingBarListener());
    }

    private RatingBar.OnRatingBarChangeListener getRatingBarListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                onRatingChange(rating);
            }
        };
    }

    private void onRatingChange(float rating) {
        AppRatingEnum appRating = ratingFactory(rating);
        if(appRating != AppRatingEnum.EMPTY) {
            ImageHandler.loadImageWithId(imageRating, appRating.getDrawableId(), appRating.getDrawableId());
            textDescription.setText(appRating.getStringId());
            textDescription.setTextColor(
                    AppCompatResources.getColorStateList(getContext(), appRating.getColorId())
            );
        }
    }

    private AppRatingEnum ratingFactory(float rating) {
        if(rating == 1) {
            return AppRatingEnum.WORST;
        } else if(rating == 2) {
            return AppRatingEnum.BAD;
        } else if(rating == 3) {
            return AppRatingEnum.ORDINARY;
        } else if(rating == 4) {
            return AppRatingEnum.GOOD;
        } else if(rating == 5) {
            return AppRatingEnum.BEST;
        }

        return AppRatingEnum.EMPTY;
    }

    public float getRating() {
        return ratingBar.getRating();
    }
}
