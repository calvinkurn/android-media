package com.tokopedia.tokopoints.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;

public class ServerErrorView extends NestedScrollView {

    private AppCompatImageView imageError;
    private AppCompatTextView tvTitleError;
    private AppCompatTextView tvLabelError;
    private RoundButton btnError;
    private CharSequence errorTitle;
    private CharSequence errorSubTitle;

    public ServerErrorView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public ServerErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public ServerErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    private void init(AttributeSet attrs) {
        readAttributes(attrs);
        inflateLayout();
        setDefaultValues();
    }

    private void readAttributes(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray array = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.ServerErrorView, 0, 0);
            errorTitle = array.getString(R.styleable.ServerErrorView_errorTitle);
            errorSubTitle = array.getString(R.styleable.ServerErrorView_errorSubtitle);
            array.recycle();
        }
    }

    private void setDefaultValues() {
        if (TextUtils.isEmpty(errorTitle)) {
            errorTitle = getResources().getText(R.string.tp_label_server_error);
        }

        if (TextUtils.isEmpty(errorSubTitle)) {
            errorSubTitle = getResources().getText(R.string.tp_label_try_again);
        }
    }

    private void inflateLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tp_server_error, this, true);
        imageError = findViewById(R.id.img_error);
        tvTitleError = findViewById(R.id.text_title_error);
        tvLabelError = findViewById(R.id.text_label_error);
        btnError = findViewById(R.id.text_failed_action);
    }

    public void showErrorUi(boolean hasInternet) {

        int noConnectionImageId = R.drawable.ic_tp_toped_sorry;
        int buttonFontSize = getResources().getInteger(R.integer.tp_error_btn_medium);
        int buttonColor = MethodChecker.getColor(getContext(), com.tokopedia.design.R.color.transparent);
        int buttonFontColor = MethodChecker.getColor(getContext(), com.tokopedia.design.R.color.tkpd_main_green);

        if (!hasInternet) {

            noConnectionImageId = R.drawable.ic_tp_no_connection;
            buttonFontSize = getResources().getInteger(R.integer.tp_error_btn_large);

            buttonColor = MethodChecker.getColor(getContext(), com.tokopedia.design.R.color.bg_button_green_border_outline);

            buttonFontColor = MethodChecker.getColor(getContext(), com.tokopedia.design.R.color.white);
            errorTitle = getResources().getText(R.string.tp_no_internet_title);
            errorSubTitle = getResources().getText(R.string.tp_no_internet_label);
        }

        imageError.setImageResource(noConnectionImageId);

        btnError.setTextColor(buttonFontColor);
        btnError.setButtonColor(buttonColor);
        btnError.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonFontSize);

        tvTitleError.setText(errorTitle);
        tvLabelError.setText(errorSubTitle);
    }
}
