package com.tokopedia.digital.tokocash.newpackage;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.FavoriteListDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.tokocash.compoundview.TopUpTokoCashView;

import java.util.List;

/**
 * Created by nabillasabbaha on 2/28/18.
 */

public class TopupTokoCashFragment extends BaseDaggerFragment implements TopupTokoCashContract.View {

    private TopUpTokoCashView topUpTokoCashView;
    private TopupTokoCashPresenter presenter;

    private String categoryId;
    private String operatorId;

    public static TopupTokoCashFragment newInstance() {
        TopupTokoCashFragment fragment = new TopupTokoCashFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topup_tokocash, container, false);
        topUpTokoCashView = view.findViewById(R.id.topup_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        CategoryDetailDataSource categoryDetailDataSource = new CategoryDetailDataSource(
                digitalEndpointService, new GlobalCacheManager(), new ProductDigitalMapper()
        );
        FavoriteListDataSource favoriteListDataSource = new FavoriteListDataSource(
                digitalEndpointService, new FavoriteNumberListDataMapper()
        );
        IDigitalCategoryRepository digitalCategoryRepository = new DigitalCategoryRepository(
                categoryDetailDataSource, favoriteListDataSource
        );

        GetCategoryByIdUseCase getCategoryByIdUseCase = new GetCategoryByIdUseCase(
                getActivity(), digitalCategoryRepository
        );

        presenter = new TopupTokoCashPresenter(getCategoryByIdUseCase);
        presenter.processGetDataProductTokoCash();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void renderProductTokoCash(CategoryData categoryData) {
        Operator operatorSelected = null;
        for (Operator operator : categoryData.getOperatorList()) {
            if (operator.getOperatorId().equalsIgnoreCase(categoryData.getDefaultOperatorId())) {
                operatorSelected = operator;
            }
        }
        categoryId = categoryData.getCategoryId();
        operatorId = operatorSelected.getOperatorId();
        topUpTokoCashView.renderDataTopUp(categoryData, operatorSelected);
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


    @Override
    public void showErrorLoadProductTokoCash(Throwable throwable) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    topUpTokoCashView.renderUpdateDataSelected(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA));
                break;
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
