package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.mitra.R;

import java.util.List;

/**
 * Created by Rizky on 04/09/18.
 */
public class DigitalOperatorDropdownInputView extends LinearLayout {

    private TextView tvLabelChooser;
    private TextView tvOperatorProductName;
    private ImageView ivArrow;
    private TextView tvErrorChooser;
    private RelativeLayout dropdownLayout;

    private ActionListener actionListener;

    public interface ActionListener {

        void onClickOperatorDropdown();

    }

    public DigitalOperatorDropdownInputView(Context context) {
        super(context);
        init(context);
    }

    public DigitalOperatorDropdownInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalOperatorDropdownInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalOperatorDropdownInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_operator_dropdown,
                this, true);
        tvLabelChooser = view.findViewById(R.id.tv_label_chooser);
        tvOperatorProductName = view.findViewById(R.id.tv_operator_product_name);
        ivArrow = view.findViewById(R.id.iv_arrow);
        tvErrorChooser = view.findViewById(R.id.tv_error_chooser);
        dropdownLayout = view.findViewById(R.id.dropdown_layout);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderOperatorList(List<RenderProductModel> renderProductModels, String defaultOperatorId) {
        Operator selectedOperator = null;
        if (!renderProductModels.isEmpty()) {
            selectedOperator = findOperatorById(renderProductModels, defaultOperatorId);
            if (selectedOperator == null) {
                selectedOperator = renderProductModels.get(0).getOperator();
            }
        }
        tvOperatorProductName.setText(selectedOperator.getName());
//        invalidateContentView();
//        if (actionListener != null) {
//            actionListener.onUpdateDataDigitalChooserSelectedRendered(selectedOperator);
//            actionListener.tracking();
//        }
        dropdownLayout.setOnClickListener(v -> actionListener.onClickOperatorDropdown());
    }

    private Operator findOperatorById(List<RenderProductModel> renderProductModels, String defaultOperatorId) {
        for (int i = 0, operatorsSize = renderProductModels.size(); i < operatorsSize; i++) {
            Operator operator = renderProductModels.get(i).getOperator();
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(defaultOperatorId)) {
                return renderProductModels.get(i).getOperator();
            }
        }
        return null;
    }

}
