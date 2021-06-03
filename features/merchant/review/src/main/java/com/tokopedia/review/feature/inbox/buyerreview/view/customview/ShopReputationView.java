package com.tokopedia.review.feature.inbox.buyerreview.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.review.R;
import com.tokopedia.unifyprinciples.Typography;

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
    private int medalWidth;

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

    public Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return context.getResources().getDrawable(resId, context.getApplicationContext().getTheme());
        else
            return AppCompatResources.getDrawable(context, resId);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ShopReputationView);
        try {
            showTooltip = styledAttributes.getBoolean(R.styleable.ShopReputationView_srv_show_tooltip, false);
            medalWidth = (int) styledAttributes.getDimension(R.styleable.ShopReputationView_srv_medal_width,
                    getContext().getResources().getDimensionPixelSize(R.dimen.dp_15));
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
        int medalMargin = getContext().getResources().getDimensionPixelSize(R.dimen.dp_3);
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
                dialog.setContentView(com.tokopedia.design.R.layout.seller_reputation_bottom_sheet_dialog);
                Typography point = dialog.findViewById(com.tokopedia.design.R.id.reputation_point);

                String pointText = TextUtils.isEmpty(pointValue) || pointValue.equals("0") ?
                        getContext().getString(com.tokopedia.design.R.string.no_reputation_yet) :
                        String.valueOf(pointValue) + " " + getContext().getString(R.string.point);

                if (point != null) {
                    point.setText(pointText);
                }
                LinearLayout sellerReputation = dialog.findViewById(com.tokopedia.design.R.id.seller_reputation);
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
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(medalWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(param);
        imageView.setImageDrawable(getDrawable(imageView.getContext(),imageResource));
        return imageView;
    }

    private int getIconResource(int type) {
        switch (type) {
            case MEDAL_TYPE_1:
                return com.tokopedia.design.R.drawable.ic_badge_bronze;
            case MEDAL_TYPE_2:
                return com.tokopedia.design.R.drawable.ic_badge_silver;
            case MEDAL_TYPE_3:
                return com.tokopedia.design.R.drawable.ic_badge_gold;
            case MEDAL_TYPE_4:
                return com.tokopedia.design.R.drawable.ic_badge_diamond;
            default:
                return com.tokopedia.design.R.drawable.ic_badge_none;
        }
    }
}