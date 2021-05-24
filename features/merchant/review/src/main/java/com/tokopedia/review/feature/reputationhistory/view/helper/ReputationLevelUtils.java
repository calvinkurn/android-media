package com.tokopedia.review.feature.reputationhistory.view.helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.tokopedia.review.R;

/**
 * Created by Tkpd_Eka on 7/8/2015.
 */
public class ReputationLevelUtils {

    private static final int MEDAL_NONE = R.drawable.ic_icon_repsis_medal;
    private static final int MEDAL_BRONZE = R.drawable.ic_icon_repsis_medal_bronze;
    private static final int MEDAL_SILVER = R.drawable.ic_icon_repsis_medal_silver;
    private static final int MEDAL_GOLD = R.drawable.ic_icon_repsis_medal_gold;
    private static final int MEDAL_DIAMOND = R.drawable.ic_icon_repsis_medal_diamond;
    private static final int SIZE = R.dimen.dp_20;

    public static final int MEDAL_TYPE_0 = 0;
    public static final int MEDAL_TYPE_1 = 1;
    public static final int MEDAL_TYPE_2 = 2;
    public static final int MEDAL_TYPE_3 = 3;
    public static final int MEDAL_TYPE_4 = 4;

    public static void setReputationMedalsWithoutDialog(Context context, LinearLayout layout, int typeMedal,
                                                        int levelMedal, String reputationPoints){
        int medalType = getTypeMedal(typeMedal);

        layout.removeAllViews();
        layout.setOrientation(LinearLayout.HORIZONTAL);
        if(typeMedal == MEDAL_TYPE_0) {
            createDefaultMedal(context, layout);
        } else {
            createMedalBasedOnTypeWithoutDialog(context, layout, medalType, levelMedal, reputationPoints);
        }
    }

    private static void createMedalBasedOnTypeWithoutDialog(Context context, LinearLayout layout, int medalType, int levelMedal, String reputationPoints) {
        for( int i = 1 ; i <= levelMedal ; i++){
            View medal = createMedal(context, medalType);
            layout.addView(medal);
        }
    }

    private static void createDefaultMedal(Context context, LinearLayout layout) {
        View medal = createMedal(context, MEDAL_NONE);
        layout.addView(medal);
    }

    private static int getTypeMedal(int type){
        if(type == MEDAL_TYPE_1) return MEDAL_BRONZE;
        if(type == MEDAL_TYPE_2) return MEDAL_SILVER;
        if(type == MEDAL_TYPE_3) return MEDAL_GOLD;
        if(type == MEDAL_TYPE_4) return MEDAL_DIAMOND;
        return MEDAL_NONE;
    }

    private static ImageView createMedal(Context context, int medalType){
        ImageView medal = new ImageView(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(SIZE),
                context.getResources().getDimensionPixelSize(SIZE)
        );
//        param.setMargins(MARGIN,0,MARGIN,0);
        medal.setLayoutParams(param);
        medal.setImageResource(medalType);
        return medal;
    }
}