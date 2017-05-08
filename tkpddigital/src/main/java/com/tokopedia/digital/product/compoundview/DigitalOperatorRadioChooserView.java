package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.Operator;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalOperatorRadioChooserView extends BaseDigitalRadioChooserView<Operator> {
    @BindView(R2.id.tv_label_chooser)
    TextView tvLabel;
    @BindView(R2.id.rg_chooser_operator)
    RadioGroup radioGroupOparator;
    @BindView(R2.id.tv_error_chooser)
    TextView tvErrorOperator;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorRadioChooserView(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorRadioChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorRadioChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_radio_chooser_digital_module;
    }

    @Override
    public void enableLabelText(String labelText) {
        tvLabel.setText(labelText);
        tvLabel.setVisibility(VISIBLE);
    }

    @Override
    public void disableLabelText() {
        tvLabel.setText("");
        tvLabel.setVisibility(GONE);
    }

    @Override
    public void enableError(String errorMessage) {
        tvErrorOperator.setText(errorMessage);
        tvErrorOperator.setVisibility(VISIBLE);
    }

    @Override
    public void disableError() {
        tvErrorOperator.setText("");
        tvErrorOperator.setVisibility(GONE);
    }

    @Override
    public void renderInitDataList(List<Operator> data) {
        this.dataList = data;
        if (data.size() > 2) {
            radioGroupOparator.setOrientation(LinearLayout.VERTICAL);
        } else {
            radioGroupOparator.setOrientation(LinearLayout.HORIZONTAL);
        }
        for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
            Operator operator = dataList.get(i);
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(i);
            radioButton.setText(operator.getName());
            radioButton.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.grey_600));
            radioGroupOparator.addView(radioButton);
        }
        radioGroupOparator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(
                        dataList.get(checkedId)
                );
            }
        });
        radioGroupOparator.check(0);
    }

    @Override
    public void renderUpdateDataSelected(Operator data) {
        actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(data);
    }
}
