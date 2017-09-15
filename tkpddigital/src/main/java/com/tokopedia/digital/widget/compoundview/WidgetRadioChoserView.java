package com.tokopedia.digital.widget.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 7/18/17.
 */

public class WidgetRadioChoserView extends LinearLayout {

    @BindView(R2.id.radio_group_container)
    LinearLayout radioGroupContainer;

    private RadioGroup radioGroup;
    private RadioChoserListener listener;

    public WidgetRadioChoserView(Context context) {
        super(context);
        init();
    }

    public WidgetRadioChoserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetRadioChoserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(RadioChoserListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_radio_choser, this);
        ButterKnife.bind(this);
    }

    public void renderDataView(final List<RechargeOperatorModel> operators, final LastOrder lastOrder,
                               String lastOperatorSelected) {
        radioGroup = new RadioGroup(getContext());
        radioGroupContainer.addView(radioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < operators.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(operators.get(i).name);
            radioButton.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                    getResources().getDisplayMetrics().density);
            radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_600));
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());
        RechargeOperatorModel modelll = operators.get(radioGroup.getChildAt(0).getId());
        listener.onCheckChange(modelll);
        checkRadioButtonBasedOnLastOrder(operators, radioGroup, lastOrder, lastOperatorSelected);
        radioGroup.setOnTouchListener(getOnTouchListener());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                listener.onResetClientNumber();
                listener.onCheckChange(operators.get(i));
            }
        });
    }

    private void checkRadioButtonBasedOnLastOrder(List<RechargeOperatorModel> operators,
                                                  RadioGroup radioGroup, LastOrder lastOrder,
                                                  String lastOperatorSelected) {
        if (lastOrder != null && lastOrder.getData() != null
                && lastOrder.getData().getAttributes() != null && radioGroup != null) {
            for (int i = 0; i < operators.size(); i++) {
                if (operators.get(i).operatorId == lastOrder.getData().getAttributes().getOperator_id()) {
                    radioGroup.check(radioGroup.getChildAt(i).getId());
                    listener.onCheckChange(operators.get(radioGroup.getChildAt(i).getId()));
                }
            }
        } else {
            if (radioGroup != null)
                for (int i = 0; i < operators.size(); i++) {
                    if (String.valueOf(operators.get(i).operatorId)
                            .equalsIgnoreCase(lastOperatorSelected)) {
                        radioGroup.check(radioGroup.getChildAt(i).getId());
                        listener.onCheckChange(operators.get(radioGroup.getChildAt(i).getId()));
                    }
                }
        }
    }

    private View.OnTouchListener getOnTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    listener.onTrackingOperator();
                }
                return false;
            }
        };
    }

    public interface RadioChoserListener {
        void onCheckChange(RechargeOperatorModel selectedOperator);
        void onResetClientNumber();
        void onTrackingOperator();
    }
}