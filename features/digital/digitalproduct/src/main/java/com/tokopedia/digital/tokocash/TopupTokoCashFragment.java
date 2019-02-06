package com.tokopedia.digital.tokocash;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.model.CategoryData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/28/18.
 */

public class TopupTokoCashFragment extends BaseDaggerFragment implements TopupTokoCashContract.View {

    private static final String SAVED_CATEGORY_DATA = "saved_category_data";
    private static final String SAVED_PRODUCT_DATA = "saved_product_data";

    private TopUpTokoCashView topUpTokoCashView;
    private ProgressBar progressBar;

    private String categoryId;
    private String operatorId;
    private CategoryData categoryData;
    private Product selectedProduct;

    @Inject
    TopupTokoCashPresenter presenter;

    public static TopupTokoCashFragment newInstance() {
        TopupTokoCashFragment fragment = new TopupTokoCashFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topup_tokocash, container, false);
        topUpTokoCashView = view.findViewById(R.id.topup_layout);
        progressBar = view.findViewById(R.id.progress_bar_topup);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        if (savedInstanceState == null) {
            showLoading();
            presenter.processGetDataProductTokoCash();
        } else {
            this.categoryData = savedInstanceState.getParcelable(SAVED_CATEGORY_DATA);
            this.selectedProduct = savedInstanceState.getParcelable(SAVED_PRODUCT_DATA);
            renderProductTokoCash(categoryData);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);
    }

    @Override
    public void renderProductTokoCash(CategoryData categoryData) {
        hideLoading();
        this.categoryData = categoryData;
        Operator operatorSelected = null;
        for (Operator operator : categoryData.getOperatorList()) {
            if (operator.getOperatorId().equalsIgnoreCase(categoryData.getDefaultOperatorId())) {
                operatorSelected = operator;
            }
        }
        categoryId = categoryData.getCategoryId();
        operatorId = operatorSelected.getOperatorId();
        topUpTokoCashView.renderDataTopUp(categoryData, operatorSelected, selectedProduct);
        topUpTokoCashView.setListener(getActionListenerTopUpView());
    }

    private TopUpTokoCashView.ActionListener getActionListenerTopUpView() {
        return new TopUpTokoCashView.ActionListener() {
            @Override
            public void onDigitalChooserClicked(List<Product> productList, String productText) {
                startActivityForResult(
                        DigitalChooserActivity.newInstanceProductChooser(
                                getActivity(), categoryId, operatorId, productText
                        ),
                        IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
                );
            }

            @Override
            public void onProcessAddToCart(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
                presenter.processAddToCartProduct(preCheckoutProduct);
            }
        };
    }

    private void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            topUpTokoCashView.setVisibility(View.GONE);
        }
    }

    private void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            topUpTokoCashView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorLoadProductTokoCash(Throwable throwable) {
        hideLoading();
    }

    @Override
    public Application getMainApplication() {
        return getActivity().getApplication();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public String getVersionInfoApplication() {
        return VersionInfo.getVersionInfo(getActivity());
    }

    @Override
    public String getUserLoginId() {
        return SessionHandler.getLoginID(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_CATEGORY_DATA, categoryData);
        outState.putParcelable(SAVED_PRODUCT_DATA, selectedProduct);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.categoryData = savedInstanceState.getParcelable(SAVED_CATEGORY_DATA);
            this.selectedProduct = savedInstanceState.getParcelable(SAVED_PRODUCT_DATA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedProduct = data.getParcelableExtra(
                            DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA);
                    renderProductTokoCash(categoryData);
                    topUpTokoCashView.renderUpdateDataSelected(
                            selectedProduct);
                }
                break;
            case DigitalRouter.REQUEST_CODE_CART_DIGITAL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }

}
