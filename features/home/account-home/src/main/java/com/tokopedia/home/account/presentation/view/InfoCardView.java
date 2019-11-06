package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/18/18.
 */
public class InfoCardView extends BaseCustomView {
    private RelativeLayout layout;
    private ImageView icon;
    private TextView textMain;
    private TextView textSecondary;
    private TextView textNew;

    public InfoCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public InfoCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_info_card, this);
        layout = view.findViewById(R.id.layout_info);
        icon = view.findViewById(R.id.image_view);
        textMain = view.findViewById(R.id.text_main);
        textSecondary = view.findViewById(R.id.text_secondary);
        textNew = view.findViewById(R.id.txt_new);
    }

    public void setMainText(String text) {
        textMain.setText(text);
    }

    public void setMainText(@StringRes int textRes) {
        textMain.setText(textRes);
    }

    public void setSecondaryText(String text) {
        textSecondary.setText(text);
    }

    public void setSecondaryText(@StringRes int textRes) {
        textSecondary.setText(textRes);
    }

    public void setImage(@NonNull String url) {
        ImageHandler.loadImage(getContext(), icon, url, R.drawable.ic_big_notif_customerapp);
    }

    public void setImage(@DrawableRes int res) {
        icon.setImageResource(res);
    }

    public void setTextNewVisiblity(int visiblity) {
        textNew.setVisibility(visiblity);
    }
}
