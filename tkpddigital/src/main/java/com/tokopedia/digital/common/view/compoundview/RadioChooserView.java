package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalRadioChooserView;
import com.tokopedia.digital.product.view.model.Operator;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Rizky on 31/01/18.
 */

public class RadioChooserView extends BaseDigitalRadioChooserView<Operator> {

    @BindView(R2.id.radio_group_container)
    LinearLayout radioGroupContainer;

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
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_widget_radio_choser;
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
        radioGroup = new RadioGroup(getContext());
        radioGroupContainer.addView(radioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        this.operators = data;

        for (int i = 0; i < data.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(data.get(i).getName());
            radioButton.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_600));
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());
        Operator operatorModel = data.get(radioGroup.getChildAt(0).getId());
        actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(operatorModel);
        initCheckRadioButtonBasedOnLastOrder(radioGroup, defaultOperatorId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(data.get(i));
                actionListener.tracking();
            }
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
