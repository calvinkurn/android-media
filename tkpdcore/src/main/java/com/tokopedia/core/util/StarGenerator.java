package com.tokopedia.core.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.core2.R;

/**
 * Created by Steven on 11/01/2016.
 */
public class StarGenerator {
    private static final int STAR_NONE = R.drawable.ic_icon_star_grey_24dp;
    private static final int STAR = R.drawable.ic_icon_star_yellow_24dp;
    private static final int SIZE = R.dimen.star_size;


    public static void setReputationStars(Context context, LinearLayout layout, int levelStar) {
        layout.removeAllViews();
        layout.setOrientation(LinearLayout.HORIZONTAL);
        createStar(context, layout, levelStar);
    }

    private static void createStar(Context context, LinearLayout layout, int levelStar) {
        for( int i = 1 ; i <= levelStar ; i++){
            View star = createStar(context, STAR);
            layout.addView(star);
        }for( int i = 5 ; i > levelStar ; i--){
            View star = createStar(context, STAR_NONE);
            layout.addView(star);
        }
    }

    private static ImageView createStar(Context context, int starType){
        ImageView star = new ImageView(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(SIZE),
                context.getResources().getDimensionPixelSize(SIZE)
        );
        star.setPadding(2,0,2,0);
        star.setLayoutParams(param);
        star.setImageResource(starType);
        return star;
    }
}
