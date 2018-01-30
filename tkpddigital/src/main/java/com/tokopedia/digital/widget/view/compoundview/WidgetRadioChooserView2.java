package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Operator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rizky on 29/01/18.
 */

public class WidgetRadioChooserView2 extends LinearLayout {

    @BindView(R2.id.radio_group_container)
    LinearLayout radioGroupContainer;

    private List<Operator> operators;

    private RadioGroup radioGroup;
    private WidgetRadioChooserView2.RadioChoserListener listener;

    public WidgetRadioChooserView2(Context context) {
        super(context);
        init();
    }

    public WidgetRadioChooserView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetRadioChooserView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(WidgetRadioChooserView2.RadioChoserListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_radio_choser, this);
        ButterKnife.bind(this);
    }

    public void renderDataView(final List<Operator> operators, String defaultOperatorId) {
        radioGroup = new RadioGroup(getContext());
        radioGroupContainer.addView(radioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        this.operators = operators;

        for (int i = 0; i < operators.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(operators.get(i).getName());
            radioButton.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_600));
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());
        Operator operatorModel = operators.get(radioGroup.getChildAt(0).getId());
        listener.onCheckChange(operatorModel);
        initCheckRadioButtonBasedOnLastOrder(radioGroup, defaultOperatorId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                listener.onResetClientNumber();
                listener.onCheckChange(operators.get(i));
                listener.onTrackingOperator();
            }
        });
    }

    private void initCheckRadioButtonBasedOnLastOrder(RadioGroup radioGroup,
                                                      String defaultOperatorId) {
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getOperatorId().equals(defaultOperatorId)) {
                radioGroup.check(radioGroup.getChildAt(i).getId());
                listener.onCheckChange(operators.get(radioGroup.getChildAt(i).getId()));
            }
        }
    }

    public void updateOperator(String operatorId) {
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getOperatorId().equals(operatorId)) {
                radioGroup.check(radioGroup.getChildAt(i).getId());
                listener.onCheckChange(operators.get(radioGroup.getChildAt(i).getId()));
            }
        }
    }

    public interface RadioChoserListener {
        void onCheckChange(Operator selectedOperator);

        void onResetClientNumber();

        void onTrackingOperator();
    }
}
