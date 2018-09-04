package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokopedia.mitra.R;

/**
 * Created by Rizky on 04/09/18.
 */
public class DigitalOperatorRadioInputView extends LinearLayout {

    private TextView tvLabelChooser;
    private RadioGroup rgChooserOperator;
    private TextView tvErrorChooser;

    public DigitalOperatorRadioInputView(Context context) {
        super(context);
        init(context);
    }

    public DigitalOperatorRadioInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalOperatorRadioInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalOperatorRadioInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_operator_radio_input,
                this, true);
        tvLabelChooser = view.findViewById(R.id.tv_label_chooser);
        rgChooserOperator = view.findViewById(R.id.rg_chooser_operator);
        tvErrorChooser = view.findViewById(R.id.tv_error_chooser);
    }

}
