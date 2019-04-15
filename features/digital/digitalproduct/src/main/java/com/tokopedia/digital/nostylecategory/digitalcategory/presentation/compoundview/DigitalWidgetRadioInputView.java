package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * Created by Rizky on 04/09/18.
 */
public class DigitalWidgetRadioInputView extends LinearLayout {

    private static final int EMPTY_MARGIN_VALUE = 0;
    private static final int RADIO_DIVIDER_MARGIN_VALUE = 40;

    private LinearLayout radioGroupContainer;

    private ActionListener actionListener;

    public interface ActionListener {

        void onItemSelected(BaseWidgetItem item);

    }

    public DigitalWidgetRadioInputView(Context context) {
        super(context);
        init(context);
    }

    public DigitalWidgetRadioInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalWidgetRadioInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalWidgetRadioInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_widget_radio_input,
                this, true);
        radioGroupContainer = view.findViewById(R.id.radio_group_container);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderInitDataList(final List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewRadioGroup = inflater.inflate(R.layout.view_digital_radio_group_button, null, false);
        RadioGroup radioGroup = viewRadioGroup.findViewById(R.id.radio_group_button);
        radioGroupContainer.addView(radioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                EMPTY_MARGIN_VALUE,
                EMPTY_MARGIN_VALUE,
                RADIO_DIVIDER_MARGIN_VALUE,
                EMPTY_MARGIN_VALUE
        );

        if (inputFieldModel.getName().equals(InputFieldModel.Companion.getNAME_OPERATOR_ID())) {
            for (int i = 0; i < items.size(); i++) {
                Operator operator = ((Operator) items.get(i));
                View radioView = inflater.inflate(R.layout.view_digital_radio_button,null, false);
                RadioButton radioButton  = radioView.findViewById(R.id.radio_button);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setId(i);
                radioButton.setText(operator.getName());
                radioButton.setTextSize(getResources().getDimension(R.dimen.sp_12) /
                        getResources().getDisplayMetrics().density);
                radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_600));
                radioGroup.addView(radioButton);
            }
            radioGroup.check(radioGroup.getChildAt(0).getId());
            Operator operator = ((Operator) items.get(radioGroup.getChildAt(0)
                    .getId()));
            actionListener.onItemSelected(operator);
            selectDefaultId(radioGroup, items, inputFieldModel, defaultId);
//            initCheckRadioButtonBasedOnLastOrder(radioGroup, defaultId);
        } else if (inputFieldModel.getName().equals(InputFieldModel.Companion.getNAME_PRODUCT_ID())) {

        }

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            actionListener.onItemSelected(items.get(i));
        });
    }

//    private void initCheckRadioButtonBasedOnLastOrder(RadioGroup radioGroup,
//                                                      String defaultOperatorId) {
//        for (int i = 0; i < operators.size(); i++) {
//            if (operators.get(i).getOperatorId().equals(defaultOperatorId)) {
//                radioGroup.check(radioGroup.getChildAt(i).getId());
//                actionListener.onUpdateDataDigitalRadioChooserSelectedRendered(operators.get(radioGroup.getChildAt(i).getId()));
//                actionListener.tracking();
//            }
//        }
//    }

    private void selectDefaultId(RadioGroup radioGroup, List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultOperatorId) {
        if (inputFieldModel.getName().equals("operator_id")) {
            for (int i = 0, operatorsSize = items.size(); i < operatorsSize; i++) {
                Operator operator = ((Operator) items.get(i));
                if (String.valueOf(operator.getOperatorId())
                        .equalsIgnoreCase(defaultOperatorId)) {
                    radioGroup.check(radioGroup.getChildAt(i).getId());
                }
            }
        } else if (inputFieldModel.getName().equals("product_id")){
            for (int i = 0, productSize = items.size(); i < productSize; i++) {
                Product product = ((Product) items.get(i));
                if (String.valueOf(product.getProductId())
                        .equalsIgnoreCase(defaultOperatorId)) {
                    radioGroup.check(radioGroup.getChildAt(i).getId());
                }
            }
        }
    }

}
