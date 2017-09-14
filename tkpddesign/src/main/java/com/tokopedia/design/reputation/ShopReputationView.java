package com.tokopedia.design.reputation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by stevenfredian on 8/16/17.
 */

public class ShopReputationView extends BaseCustomView {

    public static final int MEDAL_TYPE_0 = 0;
    public static final int MEDAL_TYPE_1 = 1;
    public static final int MEDAL_TYPE_2 = 2;
    public static final int MEDAL_TYPE_3 = 3;
    public static final int MEDAL_TYPE_4 = 4;

    private LinearLayout reputationLayout;

    private boolean showTooltip;

    public ShopReputationView(Context context) {
        super(context);
        init();
    }

    public ShopReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShopReputationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ShopReputationView);
        try {
            showTooltip = styledAttributes.getBoolean(R.styleable.ShopReputationView_srv_show_tooltip, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_reputation_shop, this);
        reputationLayout = (LinearLayout) view.findViewById(R.id.layout_reputation_view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setValue(int medalType, int level, int point) {
        reputationLayout.removeAllViews();
        int imageResource = getIconResource(medalType);
        if (medalType == MEDAL_TYPE_0) {
            level = 1;
            point = 0;
        }
        updateMedalView(imageResource, level);
        if (showTooltip && medalType != MEDAL_TYPE_0) {
            setToolTip(point);
        }
    }

    private void updateMedalView(@DrawableRes int imageResource, int levelMedal) {
        for (int i = 0; i < levelMedal; i++) {
            View medal = getGeneratedMedalImage(imageResource);
            reputationLayout.addView(medal);
        }
    }

    private void setToolTip(final int point) {
        reputationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = View.inflate(getContext(), R.layout.item_tooltip_shop_reputation, null);
                TextView pointTextView = (TextView) popup.findViewById(R.id.text_view_point);
                pointTextView.setText(getContext().getString(R.string.reputation_shop_point, point));
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
        });
    }

    private ImageView getGeneratedMedalImage(@DrawableRes int imageResource) {
        ImageView imageView = new ImageView(getContext());
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.image_medal_size);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(size, size);
        imageView.setLayoutParams(param);
        imageView.setImageResource(imageResource);
        return imageView;
    }

    private int getIconResource(int type) {
        switch (type) {
            case MEDAL_TYPE_1:
                return R.drawable.ic_icon_repsis_medal_bronze;
            case MEDAL_TYPE_2:
                return R.drawable.ic_icon_repsis_medal_silver;
            case MEDAL_TYPE_3:
                return R.drawable.ic_icon_repsis_medal_gold;
            case MEDAL_TYPE_4:
                return R.drawable.ic_icon_repsis_medal_diamond;
            default:
                return R.drawable.ic_icon_repsis_medal;
        }
    }
}