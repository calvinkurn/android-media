package com.tokopedia.design.reputation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.design.R;

/**
 * Created by stevenfredian on 8/18/17.
 */

public class ReputationBadgeUtils {

    private static final int MEDAL_NONE = R.drawable.ic_icon_repsis_medal;
    private static final int MEDAL_BRONZE = R.drawable.ic_icon_repsis_medal_bronze;
    private static final int MEDAL_SILVER = R.drawable.ic_icon_repsis_medal_silver;
    private static final int MEDAL_GOLD = R.drawable.ic_icon_repsis_medal_gold;
    private static final int MEDAL_DIAMOND = R.drawable.ic_icon_repsis_medal_diamond;
    private static final int SIZE = R.dimen.medal_size;
    private static final int MARGIN = R.integer.medal_margin;

    public static final int MEDAL_TYPE_0 = 0;
    public static final int MEDAL_TYPE_1 = 1;
    public static final int MEDAL_TYPE_2 = 2;
    public static final int MEDAL_TYPE_3 = 3;
    public static final int MEDAL_TYPE_4 = 4;

    private static boolean isDialogHidden = false;

    public static void setReputationMedals(Context context, LinearLayout layout, int typeMedal,
                                           int levelMedal,
                                           @Nullable View.OnClickListener onClickListener) {
        int medalType = getTypeMedal(typeMedal);

        layout.removeAllViews();
        layout.setOrientation(LinearLayout.HORIZONTAL);
        if (typeMedal == MEDAL_TYPE_0) {
            createMedalBasedOnType(context, layout, medalType, 1, onClickListener);
        } else {
            createMedalBasedOnType(context, layout, medalType, levelMedal, onClickListener);
        }
    }

    private static void createMedalBasedOnType(Context context, LinearLayout layout, int medalType,
                                               int levelMedal,
                                               @Nullable View.OnClickListener onClickListener) {
        for (int i = 1; i <= levelMedal; i++) {
            View medal = createMedal(context, medalType);
            layout.addView(medal);
            if (i == levelMedal && onClickListener != null) {
                layout.setOnClickListener(onClickListener);
            }
        }
    }

    private static int getTypeMedal(int type) {
        if (type == MEDAL_TYPE_1) return MEDAL_BRONZE;
        if (type == MEDAL_TYPE_2) return MEDAL_SILVER;
        if (type == MEDAL_TYPE_3) return MEDAL_GOLD;
        if (type == MEDAL_TYPE_4) return MEDAL_DIAMOND;
        return MEDAL_NONE;
    }

    private static ImageView createMedal(Context context, int medalType) {
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
