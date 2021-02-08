package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalRadioChooserView;

import java.util.List;

/**
 * Created by Rizky on 31/01/18.
 */

public class RadioChooserView extends BaseDigitalRadioChooserView<Operator> {

    private static final int EMPTY_MARGIN_VALUE = 0;
    private static final int RADIO_DIVIDER_MARGIN_VALUE = 40;

    private LinearLayout radioGroupContainer;

    private List<Operator> operators;

    private RadioGroup radioGroup;

    public RadioChooserView(Context context) {
        super(context);
    }

    public RadioChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        radioGroupContainer = findViewById(R.id.radio_group_container);
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_digital_radio_choser;
    }

    @Override
    public void enableLabelText(String labelText) {

    }

    @Override
    public void disableLabelText() {

    }

    @Override
    public void enableError(String errorMessage) {

    }

    @Override
    public void disableError() {

    }

    @Override
    public void renderInitDataList(final List<Operator> data, String defaultOperatorId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewRadioGroup = inflater.inflate(R.layout.view_digital_radio_group_button,null, false);
        radioGroup  = viewRadioGroup.findViewById(R.id.radio_group_button);
        radioGroupContainer.addView(radioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        this.operators = data;
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                EMPTY_MARGIN_VALUE,
                EMPTY_MARGIN_VALUE,
                RADIO_DIVIDER_MARGIN_VALUE,
                EMPTY_MARGIN_VALUE
        );
        for (int i = 0; i < data.size(); i++) {
            View radioView = inflater.inflate(R.layout.view_digital_radio_button,null, false);
            RadioButton radioButton  = radioView.findViewById(R.id.radio_button);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setId(i);
            radioButton.setText(data.get(i).getName());
            radioButton.setTextSize(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_font_12) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N400));
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());
        Operator operatorModel = data.get(radioGroup.getChildAt(0).getId());
        actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(operatorModel);
        initCheckRadioButtonBasedOnLastOrder(radioGroup, defaultOperatorId);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(data.get(i));
            actionListener.tracking();
        });
    }

    private void initCheckRadioButtonBasedOnLastOrder(RadioGroup radioGroup,
                                                      String defaultOperatorId) {
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getOperatorId().equals(defaultOperatorId)) {
                radioGroup.check(radioGroup.getChildAt(i).getId());
                actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(operators.get(radioGroup.getChildAt(i).getId()));
                actionListener.tracking();
            }
        }
    }

    @Override
    public void renderUpdateDataSelected(Operator data) {
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getOperatorId().equals(data.getOperatorId())) {
                radioGroup.check(radioGroup.getChildAt(i).getId());
                actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(operators.get(radioGroup.getChildAt(i).getId()));
                actionListener.tracking();
            }
        }
    }

}
