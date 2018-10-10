package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;


import java.util.List;

/**
 * Created by Rizky on 04/09/18.
 */
public class DigitalWidgetDropdownInputView extends LinearLayout {

    private TextView tvLabelChooser;
    private TextView tvItemName;
    private ImageView ivArrow;
    private TextView tvErrorChooser;
    private RelativeLayout dropdownLayout;

    private ActionListener actionListener;

    public interface ActionListener<T> {

        void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId);

        void onItemSelected(BaseWidgetItem item);

    }

    public DigitalWidgetDropdownInputView(Context context) {
        super(context);
        init(context);
    }

    public DigitalWidgetDropdownInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalWidgetDropdownInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalWidgetDropdownInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_widget_dropdown_input,
                this, true);
        tvLabelChooser = view.findViewById(R.id.tv_label_chooser);
        tvItemName = view.findViewById(R.id.tv_item_name);
        ivArrow = view.findViewById(R.id.iv_arrow);
        tvErrorChooser = view.findViewById(R.id.tv_error_chooser);
        dropdownLayout = view.findViewById(R.id.dropdown_layout);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderDropdownView(List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultId) {
        if (!TextUtils.isEmpty(inputFieldModel.getText())) {
            tvLabelChooser.setText(inputFieldModel.getText());
            tvLabelChooser.setVisibility(VISIBLE);
        } else {
            tvLabelChooser.setVisibility(GONE);
        }

        BaseWidgetItem item = null;
        String itemId = null;
        String itemName = null;
        if (!items.isEmpty()) {
            item = findItemById(items, inputFieldModel, defaultId);
            if (item == null) {
                item = items.get(0);
                if (inputFieldModel.getName().equals(InputFieldModel.NAME_OPERATOR_ID)) {
                    itemId = ((Operator) items.get(0)).getOperatorId();
                    itemName = ((Operator) items.get(0)).getName();
                } else if (inputFieldModel.getName().equals(InputFieldModel.NAME_PRODUCT_ID)) {
                    itemId = ((Product) items.get(0)).getProductId();
                    itemName = ((Product) items.get(0)).getDesc();
                }
            } else {
                if (inputFieldModel.getName().equals(InputFieldModel.NAME_OPERATOR_ID)) {
                    itemId = ((Operator) item).getOperatorId();
                    itemName = ((Operator) item).getName();
                } else if (inputFieldModel.getName().equals(InputFieldModel.NAME_PRODUCT_ID)) {
                    itemId = ((Product) item).getProductId();
                    itemName = ((Product) item).getDesc();
                }
            }
        }
        tvItemName.setText(itemName);
        actionListener.onItemSelected(item);
        String finalItemId = itemId;
        dropdownLayout.setOnClickListener(v -> actionListener.onClickDropdown(inputFieldModel, finalItemId));
    }

    private BaseWidgetItem findItemById(List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultId) {
        if (inputFieldModel.getName().equals(InputFieldModel.NAME_OPERATOR_ID)) {
            for (int i = 0, operatorsSize = items.size(); i < operatorsSize; i++) {
                Operator operator = ((Operator) items.get(i));
                if (String.valueOf(operator.getOperatorId())
                        .equalsIgnoreCase(defaultId)) {
                    return operator;
                }
            }
        } else if (inputFieldModel.getName().equals(InputFieldModel.NAME_PRODUCT_ID)){
            for (int i = 0, productSize = items.size(); i < productSize; i++) {
                Product product = ((Product) items.get(i));
                if (String.valueOf(product.getProductId())
                        .equalsIgnoreCase(defaultId)) {
                    return product;
                }
            }
        }
        return null;
    }

}
