package com.tokopedia.mitra.digitalcategory.presentation.fragment;

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
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.di.MitraDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.di.DaggerMitraDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.presentation.activity.MitraDigitalChooserActivity;
import com.tokopedia.mitra.digitalcategory.presentation.compoundview.MitraDigitalBuyView;
import com.tokopedia.mitra.digitalcategory.presentation.compoundview.MitraDigitalCategoryView;
import com.tokopedia.mitra.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalCategoryContract;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalCategoryPresenter;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryFragment extends BaseDaggerFragment implements MitraDigitalCategoryContract.View {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    private MitraDigitalCategoryView mitraDigitalCategoryView;
    private MitraDigitalBuyView mitraDigitalBuyView;

    private DigitalCategoryModel digitalCategoryModel;

    private int categoryId;

    private String selectedOperatorId;
    private InputFieldModel tempProductInputFieldModel;

    private ActionListener actionListener;

    @Inject
    MitraDigitalCategoryPresenter presenter;

    public interface ActionListener {

        void updateToolbarTitle(String toolbarTitle);

    }

    public static Fragment newInstance(int categoryId) {
        Fragment fragment = new MitraDigitalCategoryFragment();
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

        mitraDigitalCategoryView = rootview.findViewById(R.id.view_category);
        mitraDigitalBuyView = rootview.findViewById(R.id.view_buy);

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
        DigitalComponent digitalComponent =
                DaggerDigitalComponent.builder().baseAppComponent((
                        (BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                        .build();
        MitraDigitalCategoryComponent mitraDigitalCategoryComponent =
                DaggerMitraDigitalCategoryComponent.builder().digitalComponent(digitalComponent)
                        .build();
        mitraDigitalCategoryComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderCategory(DigitalCategoryModel digitalCategoryModel) {
        this.digitalCategoryModel = digitalCategoryModel;

        actionListener.updateToolbarTitle(digitalCategoryModel.getName());

        mitraDigitalCategoryView.setActionListener(new MitraDigitalCategoryView.ActionListener() {
            @Override
            public void onClickOperatorDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                if (inputFieldModel.getName().equals(InputFieldModel.NAME_OPERATOR_ID)) {
                    String titleChooser = inputFieldModel.getText() + " " + digitalCategoryModel.getOperatorLabel();
                    Intent intent = MitraDigitalChooserActivity.newInstanceOperatorChooser(getActivity(),
                            digitalCategoryModel.getId(), titleChooser, digitalCategoryModel.getOperatorLabel(),
                            digitalCategoryModel.getName());
                    startActivityForResult(intent, 1001);
                }
            }

            @Override
            public void onProductSelected(Product product) {
                mitraDigitalBuyView.setVisibility(View.VISIBLE);
                mitraDigitalBuyView.renderBuyView(product);
            }

            @Override
            public void removeBuyView() {
                mitraDigitalBuyView.setVisibility(View.GONE);
            }

            @Override
            public void onClickProductDropdown(InputFieldModel inputFieldModel, String operatorId, int position) {
                if (inputFieldModel.getName().equals(InputFieldModel.NAME_PRODUCT_ID)) {
                    selectedOperatorId = operatorId;
                    tempProductInputFieldModel = inputFieldModel;
                    String titleChooser = inputFieldModel.getText();
                    Intent intent = MitraDigitalChooserActivity.newInstanceProductChooser2(getActivity(),
                            digitalCategoryModel.getId(), operatorId, titleChooser, position);
                    startActivityForResult(intent, 1002);
                }
            }
        });

        mitraDigitalCategoryView.renderCategory(digitalCategoryModel.getRenderOperatorModel(),
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
                                    MitraDigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                break;
            case 1002:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallbackProductChooser2(
                            data.getParcelableExtra(
                                    MitraDigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            ),
                            data.getIntExtra(MitraDigitalChooserActivity.EXTRA_CALLBACK_POSITION, 0)
                    );
                break;
        }
    }

    private void handleCallbackOperatorChooser(Operator operator) {
        mitraDigitalCategoryView.renderCategory(digitalCategoryModel.getRenderOperatorModel(),
                operator.getOperatorId());
    }

//    private void handleCallBackProductChooser(Product product) {
//        mitraDigitalCategoryView.renderProductsByOperatorId(digitalCategoryModel.getRenderOperatorModel(),
//                selectedOperatorId, product.getProductId());
////        mitraDigitalCategoryView.updateProduct(selectedOperatorId, product.getProductId());
//    }

    private void handleCallbackProductChooser2(Product product, int position) {
        mitraDigitalCategoryView.updateProductDropdownView(digitalCategoryModel.getRenderOperatorModel(),
                tempProductInputFieldModel, selectedOperatorId, product.getProductId(), position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.actionListener = (ActionListener) activity;
    }

}