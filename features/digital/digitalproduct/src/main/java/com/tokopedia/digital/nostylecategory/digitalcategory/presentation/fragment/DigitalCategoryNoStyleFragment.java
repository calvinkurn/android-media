package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent;
import com.tokopedia.common_digital.common.di.DigitalCommonComponent;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;
import com.tokopedia.digital.nostylecategory.digitalcategory.di.DaggerDigitalCategoryNoStyleComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.di.DigitalCategoryNoStyleComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.activity.DigitalChooserNoStyleActivity;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview.DigitalBuyNoStyleView;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview.DigitalCategoryNoStyleView;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter.DigitalCategoryNoStyleContract;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter.DigitalCategoryNoStylePresenter;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/08/18.
 */
public class DigitalCategoryNoStyleFragment extends BaseDaggerFragment implements DigitalCategoryNoStyleContract.View {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    private DigitalCategoryNoStyleView digitalCategoryNoStyleView;
    private DigitalBuyNoStyleView digitalBuyNoStyleView;

    private DigitalCategoryModel digitalCategoryModel;

    private int categoryId;

    private String selectedOperatorId;
    private InputFieldModel tempProductInputFieldModel;

    private ActionListener actionListener;

    @Inject
    DigitalCategoryNoStylePresenter presenter;

    public interface ActionListener {

        void updateToolbarTitle(String toolbarTitle);

    }

    public static Fragment newInstance(int categoryId) {
        Fragment fragment = new DigitalCategoryNoStyleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_agent_digital_category, container, false);

        digitalCategoryNoStyleView = rootview.findViewById(R.id.view_category);
        digitalBuyNoStyleView = rootview.findViewById(R.id.view_buy);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        presenter.getCategory(categoryId);
    }

    @Override
    protected void initInjector() {
        DigitalCommonComponent digitalCommonComponent =
                DaggerDigitalCommonComponent.builder().baseAppComponent((
                        (BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                        .build();
        DigitalCategoryNoStyleComponent digitalCategoryNoStyleComponent =
                DaggerDigitalCategoryNoStyleComponent.builder().digitalCommonComponent(digitalCommonComponent)
                        .build();
        digitalCategoryNoStyleComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderCategory(DigitalCategoryModel digitalCategoryModel) {
        this.digitalCategoryModel = digitalCategoryModel;

        actionListener.updateToolbarTitle(digitalCategoryModel.getName());

        digitalCategoryNoStyleView.setActionListener(new DigitalCategoryNoStyleView.ActionListener() {
            @Override
            public void onClickOperatorDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                if (inputFieldModel.getName().equals(InputFieldModel.Companion.getNAME_OPERATOR_ID())) {
                    String titleChooser = inputFieldModel.getText() + " " + digitalCategoryModel.getOperatorLabel();
                    Intent intent = DigitalChooserNoStyleActivity.newInstanceOperatorChooser(getActivity(),
                            digitalCategoryModel.getId(), titleChooser, digitalCategoryModel.getOperatorLabel(),
                            digitalCategoryModel.getName());
                    startActivityForResult(intent, 1001);
                }
            }

            @Override
            public void onProductSelected(Product product) {
                digitalBuyNoStyleView.setVisibility(View.VISIBLE);
                digitalBuyNoStyleView.renderBuyView(product);
            }

            @Override
            public void removeBuyView() {
                digitalBuyNoStyleView.setVisibility(View.GONE);
            }

            @Override
            public void onClickProductDropdown(InputFieldModel inputFieldModel, String operatorId, int position) {
                if (inputFieldModel.getName().equals(InputFieldModel.Companion.getNAME_PRODUCT_ID())) {
                    selectedOperatorId = operatorId;
                    tempProductInputFieldModel = inputFieldModel;
                    String titleChooser = inputFieldModel.getText();
                    Intent intent = DigitalChooserNoStyleActivity.newInstanceProductChooser2(getActivity(),
                            digitalCategoryModel.getId(), operatorId, titleChooser, position);
                    startActivityForResult(intent, 1002);
                }
            }
        });

        digitalCategoryNoStyleView.renderCategory(digitalCategoryModel.getRenderOperatorModel(),
                digitalCategoryModel.getDefaultOperatorId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1001:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallbackOperatorChooser(
                            data.getParcelableExtra(
                                    DigitalChooserNoStyleActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                break;
            case 1002:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallbackProductChooser2(
                            data.getParcelableExtra(
                                    DigitalChooserNoStyleActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            ),
                            data.getIntExtra(DigitalChooserNoStyleActivity.EXTRA_CALLBACK_POSITION, 0)
                    );
                break;
        }
    }

    private void handleCallbackOperatorChooser(Operator operator) {
        digitalCategoryNoStyleView.renderCategory(digitalCategoryModel.getRenderOperatorModel(),
                operator.getOperatorId());
    }

//    private void handleCallBackProductChooser(Product product) {
//        mitraDigitalCategoryView.renderProductsByOperatorId(digitalCategoryModel.getRenderOperatorModel(),
//                selectedOperatorId, product.getProductId());
////        mitraDigitalCategoryView.updateProduct(selectedOperatorId, product.getProductId());
//    }

    private void handleCallbackProductChooser2(Product product, int position) {
        digitalCategoryNoStyleView.updateProductDropdownView(digitalCategoryModel.getRenderOperatorModel(),
                tempProductInputFieldModel, selectedOperatorId, product.getProductId(), position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.actionListener = (ActionListener) activity;
    }

}