package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.mitra.R;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class MitraDigitalCategoryView extends LinearLayout {

    private DigitalOperatorChooserView digitalOperatorChooserView;

    public MitraDigitalCategoryView(Context context) {
        super(context);
        init(context);
    }

    public MitraDigitalCategoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MitraDigitalCategoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MitraDigitalCategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_agent_digital_category,
                this, true);
        digitalOperatorChooserView = view.findViewById(R.id.view_digital_operator_chooser);
    }

    public void renderOperator(List<InputFieldModel> inputFieldModels, List<RenderProductModel> renderProductModels,
                               String defaultOperatorId) {
        digitalOperatorChooserView.showOperatorChooser(inputFieldModels, renderProductModels, defaultOperatorId);
    }

}