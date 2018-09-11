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
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.mitra.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class MitraDigitalCategoryView extends LinearLayout {

    private LinearLayout containerDigitalOperatorWidgetView;
    private LinearLayout containerDigitalProductWidgetView;

    private ActionListener actionListener;

    public interface ActionListener {

        void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId);

        void onProductSelected(Product item);

    }

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
        containerDigitalOperatorWidgetView = view.findViewById(R.id.container_digital_operator_widget_view);
        containerDigitalProductWidgetView = view.findViewById(R.id.container_digital_product_widget_view);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderWidgetViews(RenderOperatorModel renderOperatorModel, String defaultId) {
        if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
            containerDigitalOperatorWidgetView.removeAllViews();
        }
        if (containerDigitalProductWidgetView.getChildCount() > 0) {
            containerDigitalProductWidgetView.removeAllViews();
        }

        DigitalWidgetView<Operator> operatorWidgetView =
                new DigitalWidgetView<>(getContext());
        operatorWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
            @Override
            public void onItemSelected(Object operatorItem) {
                if (operatorItem instanceof Operator) {
                    // remove rendered products view
                    if (containerDigitalProductWidgetView.getChildCount() > 0) {
                        containerDigitalProductWidgetView.removeAllViews();
                    }

                    // re-render products for selected operator
                    String operatorId = ((Operator) operatorItem).getOperatorId();
                    RenderProductModel renderProductModel = findOperatorById(operatorId,
                            renderOperatorModel.getRenderProductModels());
                    for (int i=0; i<renderProductModel.getInputFieldModels().size(); i++ ) {
                        DigitalWidgetView<Product> digitalWidgetView =
                                new DigitalWidgetView<>(getContext());
                        digitalWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
                            @Override
                            public void onItemSelected(Object item) {
                                if (item instanceof Product) {
                                    actionListener.onProductSelected((Product) item);
                                }
                            }

                            @Override
                            public void onOperatorNotFound() {

                            }

                            @Override
                            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                                actionListener.onClickDropdown(inputFieldModel, selectedItemId);
                            }
                        });
                        digitalWidgetView.renderWidget(renderProductModel.getInputFieldModels().get(i),
                                renderProductModel.getOperator().getProductList(),
                                String.valueOf(renderProductModel.getOperator().getDefaultProductId()));
                        containerDigitalProductWidgetView.addView(digitalWidgetView);
                    }
                }
            }

            @Override
            public void onOperatorNotFound() {
                if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
                    containerDigitalProductWidgetView.removeAllViews();
                }
            }

            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickDropdown(inputFieldModel, selectedItemId);
            }
        });
        containerDigitalOperatorWidgetView.addView(operatorWidgetView);
        operatorWidgetView.renderWidget(renderOperatorModel.getInputFieldModels().get(0),
                transformOperators(renderOperatorModel.getRenderProductModels()), defaultId);
    }

    private RenderProductModel findOperatorById(String operatorId,
                                                List<RenderProductModel> renderProductModels) {
        for (int i = 0, operatorsSize = renderProductModels.size(); i < operatorsSize; i++) {
            RenderProductModel renderProductModel = renderProductModels.get(i);
            if (String.valueOf(renderProductModel.getOperator().getOperatorId())
                    .equalsIgnoreCase(operatorId)) {
                return renderProductModel;
            }
        }
        return null;
    }

    private List<Operator> transformOperators(List<RenderProductModel> renderProductModels) {
        List<Operator> operators = new ArrayList<>();
        for (RenderProductModel renderProductModel : renderProductModels) {
            operators.add(renderProductModel.getOperator());
        }
        return operators;
    }

}