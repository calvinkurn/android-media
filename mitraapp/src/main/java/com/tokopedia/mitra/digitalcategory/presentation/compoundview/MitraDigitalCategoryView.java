package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
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

        void removeBuyView();

        void onClickDropdownTest(InputFieldModel inputFieldModel, String operatorId, int id);

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

    public void renderWidgetViews(RenderOperatorModel renderOperatorModel, String renderOperatorId) {
        if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
            containerDigitalOperatorWidgetView.removeAllViews();
        }

        final String[] oldOperatorId = {null};

        DigitalWidgetView operatorWidgetView =
                new DigitalWidgetView(getContext());
        operatorWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
            @Override
            public void onItemSelected(BaseWidgetItem operatorItem) {
                if (operatorItem instanceof Operator) {
                    // re-render products for selected operator if only a new operator is actually selected
                    String selectedOperatorId = ((Operator) operatorItem).getOperatorId();
                    if (!selectedOperatorId.equals(oldOperatorId[0])) {
                        oldOperatorId[0] = selectedOperatorId;
                        renderProductsByOperatorId(renderOperatorModel, selectedOperatorId, "");
                    }
                }
            }

            @Override
            public void onOperatorByPrefixNotFound() {
                if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
                    oldOperatorId[0] = "0";
                    containerDigitalProductWidgetView.removeAllViews();
                    actionListener.removeBuyView();
                }
            }

            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickDropdown(inputFieldModel, selectedItemId);
            }

            @Override
            public void onClickInquiryButton() {

            }
        });
        containerDigitalOperatorWidgetView.addView(operatorWidgetView);
        List<BaseWidgetItem> baseWidgetItems =
                new ArrayList<>(transformOperators(renderOperatorModel.getRenderProductModels()));
        operatorWidgetView.renderWidget(renderOperatorModel.getInputFieldModels().get(0),
                baseWidgetItems, renderOperatorId);
    }

//    public void renderProductsByOpId(RenderOperatorModel renderOperatorModel, String operatorId) {
//        // remove rendered products view
//        if (containerDigitalProductWidgetView.getChildCount() > 0) {
//            RenderProductModel renderProductModel = findOperatorById(operatorId,
//                    renderOperatorModel.getRenderProductModels());
//            Operator operator = renderProductModel.getOperator();
//
//            for (int i=0; i<renderProductModel.getInputFieldModels().size(); i++ ) {
//                if (renderProductModel.getInputFieldModels().get(i).)
//            }
//
//            containerDigitalProductWidgetView.removeAllViews();
//            actionListener.removeBuyView();
//        }
//    }

    public void renderProductsByOperatorId(RenderOperatorModel renderOperatorModel, String operatorId,
                                           String renderProductId) {
        // remove rendered products view
        if (containerDigitalProductWidgetView.getChildCount() > 0) {
            containerDigitalProductWidgetView.removeAllViews();
            actionListener.removeBuyView();
        }

        RenderProductModel renderProductModel = findOperatorById(operatorId,
                renderOperatorModel.getRenderProductModels());
        Operator operator = renderProductModel.getOperator();

        DigitalWidgetView productInquiryWidgetView =
                new DigitalWidgetView(getContext());
        productInquiryWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
            @Override
            public void onItemSelected(BaseWidgetItem item) {
                if (item instanceof Product) {
                    actionListener.onProductSelected((Product) item);
                }
            }

            @Override
            public void onOperatorByPrefixNotFound() {

            }

            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
//                actionListener.onClickDropdown(inputFieldModel, operator.getOperatorId());
                actionListener.onClickDropdownTest(inputFieldModel, operator.getOperatorId(),
                        productInquiryWidgetView.getId());
            }

            @Override
            public void onClickInquiryButton() {

            }
        });

        boolean showInputField = true;

        for (int i=0; i<renderProductModel.getInputFieldModels().size(); i++ ) {
            DigitalWidgetView productWidgetView =
                    new DigitalWidgetView(getContext());
            productWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
                @Override
                public void onItemSelected(BaseWidgetItem item) {
                    if (item instanceof Product) {
                        actionListener.onProductSelected((Product) item);
                    }
                }

                @Override
                public void onOperatorByPrefixNotFound() {

                }

                @Override
                public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                    actionListener.onClickDropdown(inputFieldModel,
                            operator.getOperatorId());
                }

                @Override
                public void onClickInquiryButton() {
                    containerDigitalProductWidgetView.addView(productInquiryWidgetView);
                }
            });

            List<BaseWidgetItem> baseWidgetItems = new ArrayList<>(operator.getProductList());
            String productId;
            if (!TextUtils.isEmpty(renderProductId)) {
                productId = renderProductId;
            } else {
                productId = String.valueOf(operator.getDefaultProductId());
            }
            productWidgetView.renderWidget(renderProductModel.getInputFieldModels().get(i),
                    baseWidgetItems,
                    productId);
            if (showInputField) {
                containerDigitalProductWidgetView.addView(productWidgetView);
            } else {
                productInquiryWidgetView.renderWidget(renderProductModel.getInputFieldModels().get(i),
                        baseWidgetItems,
                        productId);
            }
            if (renderProductModel.getInputFieldModels().get(i).getAdditionalButton() != null &&
                    renderProductModel.getInputFieldModels().get(i).getAdditionalButton().getType()
                            .equals("inquiry")) {
                showInputField = false;
            }
        }
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