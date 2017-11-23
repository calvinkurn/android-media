package com.tokopedia.core.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;

import java.util.List;

/**
 * Created by HenryPri on 20/06/17.
 */

public class IndicatorViewHelper {

    private static final String WHITE_COLOR = "#ffffff";

    public static void renderBadgesView(Context context,
                                        LinearLayout badgesContainer,
                                        List<Badge> badgeList) {

        badgesContainer.removeAllViews();
        if (badgeList != null) {
            for (Badge badge : badgeList) {
                LuckyShopImage.loadImage(context, badge.getImageUrl(), badgesContainer);
            }
        }
    }

    public static void renderLabelsView(Context context,
                                        FlowLayout labelContainer,
                                        List<Label> labelList) {

        labelContainer.removeAllViews();
        if (labelList != null) {
            for (Label label : labelList) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals(WHITE_COLOR)) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && labelText instanceof TintableBackgroundView) {
                        ((TintableBackgroundView) labelText).setSupportBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                labelContainer.addView(view);
            }
        }
    }
}
