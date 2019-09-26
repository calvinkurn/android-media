package com.tokopedia.tokopoints.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
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

    private void init(AttributeSet attributeSet) {
        inflateLayout();
    }

    private void inflateLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tp_server_error, this,true);
        imageError = findViewById(R.id.img_error);
        tvTitleError = findViewById(R.id.text_title_error);
        tvLabelError = findViewById(R.id.text_label_error);
        btnError = findViewById(R.id.text_failed_action);
    }

    public void showErrorUi(boolean hasInternet) {

        int noConnectionImageId = R.drawable.ic_tp_no_connection;

        int buttonFontSize = getResources().getInteger(R.integer.tp_error_btn_large);
        int buttonColor = MethodChecker.getColor(getContext(), R.color.bg_button_green_border_outline);
        int buttonFontColor = MethodChecker.getColor(getContext(), R.color.white);

        CharSequence titleText = getResources().getText(R.string.tp_no_internet_title);
        CharSequence labelText = getResources().getText(R.string.tp_no_internet_label);
        if (hasInternet) {

            noConnectionImageId = R.drawable.ic_tp_toped_sorry;

            buttonFontSize = getResources().getInteger(R.integer.tp_error_btn_medium);
            buttonColor = MethodChecker.getColor(getContext(), R.color.transparent);
            buttonFontColor = MethodChecker.getColor(getContext(), R.color.tkpd_main_green);

            titleText = getResources().getText(R.string.tp_label_server_error);
            labelText = getResources().getText(R.string.tp_label_try_again);
        }

        imageError.setImageResource(noConnectionImageId);

        btnError.setTextColor(buttonFontColor);
        btnError.setButtonColor(buttonColor);
        btnError.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonFontSize);

        tvTitleError.setText(titleText);
        tvLabelError.setText(labelText);
    }
}
