package com.tokopedia.design.reputation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * @author by stevenfredian on 8/16/17.
 */

public class ShopReputationView extends BaseCustomView {

    public static final int MEDAL_TYPE_0 = 0;
    public static final int MEDAL_TYPE_1 = 1;
    public static final int MEDAL_TYPE_2 = 2;
    public static final int MEDAL_TYPE_3 = 3;
    public static final int MEDAL_TYPE_4 = 4;

    private LinearLayout reputationLayout;
    private BottomSheetDialog dialog;

    private boolean showTooltip;
    private int medalSize;

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
            medalSize = (int) styledAttributes.getDimension(R.styleable.ShopReputationView_srv_medal_size,
                    getContext().getResources().getDimensionPixelSize(R.dimen.image_medal_size));
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_reputation_shop, this);
        reputationLayout = view.findViewById(R.id.layout_reputation_view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setValue(int medalType, int level, String point) {
        reputationLayout.removeAllViews();
        int imageResource = getIconResource(medalType);
        if (medalType == MEDAL_TYPE_0) {
            level = 1;
            point = "0";
        }
        updateMedalView(reputationLayout, imageResource, level);
        if (showTooltip) {
            setToolTip(point, medalType, level);
        }
    }

    private void updateMedalView(LinearLayout reputationLayout, @DrawableRes int imageResource, int levelMedal) {
        int medalMargin = getContext().getResources().getDimensionPixelSize(R.dimen.margin_vvs);
        for (int i = 0; i < levelMedal; i++) {
            View medal = getGeneratedMedalImage(imageResource);
            if (i < levelMedal) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) medal.getLayoutParams();
                params.rightMargin = medalMargin;
                medal.setLayoutParams(params);
            }
            reputationLayout.addView(medal);
        }
    }

    private void setToolTip(final String pointValue, final int medalType, final int level) {
        reputationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(R.layout.seller_reputation_bottom_sheet_dialog);
                TextView point = dialog.findViewById(R.id.reputation_point);

                String pointText = TextUtils.isEmpty(pointValue) || pointValue.equals("0") ?
                        getContext().getString(R.string.no_reputation_yet) :
                        String.valueOf(pointValue) + " " + getContext().getString(R.string.point);

                if (point != null) {
                    point.setText(pointText);
                }
                LinearLayout sellerReputation = dialog.findViewById(R.id.seller_reputation);
                updateMedalView(sellerReputation, getIconResource(medalType), level);
                Button closeButton = dialog.findViewById(R.id.close_button);

                if (closeButton != null)
                    closeButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                dialog.show();
            }
        });
    }

    private ImageView getGeneratedMedalImage(@DrawableRes int imageResource) {
        ImageView imageView = new ImageView(getContext());
        imageView.setAdjustViewBounds(true);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, medalSize);
        imageView.setLayoutParams(param);
        imageView.setImageResource(imageResource);
        return imageView;
    }

    private int getIconResource(int type) {
        switch (type) {
            case MEDAL_TYPE_1:
                return R.drawable.ic_badge_bronze;
            case MEDAL_TYPE_2:
                return R.drawable.ic_badge_silver;
            case MEDAL_TYPE_3:
                return R.drawable.ic_badge_gold;
            case MEDAL_TYPE_4:
                return R.drawable.ic_badge_diamond;
            default:
                return R.drawable.ic_badge_none;
        }
    }
}