package com.tokopedia.core.reputationproduct.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tokopedia.core.R;

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
    private static final int MARGIN = R.integer.medal_margin;

    public static final int MEDAL_TYPE_0 = 0;
    public static final int MEDAL_TYPE_1 = 1;
    public static final int MEDAL_TYPE_2 = 2;
    public static final int MEDAL_TYPE_3 = 3;
    public static final int MEDAL_TYPE_4 = 4;

    private static boolean isDialogHidden = false;

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

    public static void setReputationMedals(Context context, LinearLayout layout, int typeMedal, int levelMedal, String reputationPoints) {
        int medalType = getTypeMedal(typeMedal);

        layout.removeAllViews();
        layout.setOrientation(LinearLayout.HORIZONTAL);
        if(typeMedal == MEDAL_TYPE_0) {
            createDefaultMedal(context, layout);
        } else {
            createMedalBasedOnType(context, layout, medalType, levelMedal, reputationPoints);
        }
    }

    private static void createDefaultMedal(Context context, LinearLayout layout) {
        View medal = createMedal(context, MEDAL_NONE);
        layout.addView(medal);
    }

    private static void createMedalBasedOnType(Context context, LinearLayout layout, int medalType, int levelMedal, String reputationPoints) {
        for( int i = 1 ; i <= levelMedal ; i++){
            View medal = createMedal(context, medalType);
            layout.addView(medal);
            if(i == levelMedal) {
                layout.setOnClickListener(onReputationClick(context, reputationPoints, medal));
            }
        }
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

    private static View.OnClickListener onReputationClick(final Context context, final String reputationPoint, final View anchor){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                View popup = View.inflate(context, R.layout.popup_reputation, null);
                TextView point = (TextView)popup.findViewById(R.id.point);
                point.setText(reputationPoint + " Poin");
                final PopupWindow popWindow = new PopupWindow(popup, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popWindow.setOutsideTouchable(true);
                popWindow.setFocusable(false);
                popWindow.showAsDropDown(v);
                popWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popWindow.dismiss();
                        return true;
                    }
                });
            }
        };
    }
}