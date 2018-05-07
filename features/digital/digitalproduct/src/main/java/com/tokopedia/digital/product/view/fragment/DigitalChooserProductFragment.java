package com.tokopedia.digital.product.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;
import com.tokopedia.digital.product.view.adapter.ProductChooserAdapter;
import com.tokopedia.digital.product.view.listener.IProductChooserView;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.presenter.IProductChooserPresenter;
import com.tokopedia.digital.product.view.presenter.ProductChooserPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserProductFragment extends BasePresenterFragment<IProductChooserPresenter>
        implements ProductChooserAdapter.ActionListener, IProductChooserView {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";
    private static final String ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW =
            "ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW";

    private static final String EXTRA_STATE_PRODUCT_STYLE_VIEW =
            "EXTRA_STATE_PRODUCT_STYLE_VIEW";

    @BindView(R2.id.rv_list_chooser)
    RecyclerView rvProductList;
    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;

    private CompositeSubscription compositeSubscription;

    private String categoryId;
    private String operatorId;
    private List<Product> productListData = new ArrayList<>();
    private String productStyleView;

    private ProductChooserAdapter productChooserAdapter;

    private ActionListener actionListener;

    public interface ActionListener {
        void onProductItemSelected(Product product);
    }

    public static Fragment newInstance(String categoryId, String operatorId, String productStyleView) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW, productStyleView);
        Fragment fragment = new DigitalChooserProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.getProductsByCategoryIdAndOperatorId(categoryId, operatorId);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_STATE_PRODUCT_STYLE_VIEW, productStyleView);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        productStyleView = savedState.getString(EXTRA_STATE_PRODUCT_STYLE_VIEW);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();

        CategoryDetailDataSource categoryDetailDataSource = new CategoryDetailDataSource(
                digitalEndpointService, new GlobalCacheManager(), new ProductDigitalMapper()
        );

        DigitalCategoryRepository digitalCategoryRepository = new DigitalCategoryRepository(
                categoryDetailDataSource, null
        );

        GetCategoryByIdUseCase getCategoryByIdUseCase = new GetCategoryByIdUseCase(
                getActivity(), digitalCategoryRepository
        );

        GetProductsByOperatorIdUseCase getProductsByOperatorIdUseCase = new GetProductsByOperatorIdUseCase(
                getCategoryByIdUseCase
        );

        presenter = new ProductChooserPresenter(this, getProductsByOperatorIdUseCase);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
        operatorId = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_ID);
        productStyleView = arguments.getString(ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chooser_operator_digital_module;
    }

    @Override
    protected void initView(View view) {
        rvProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        productChooserAdapter = new ProductChooserAdapter(
                this, productListData, this
        );

        rvProductList.setAdapter(productChooserAdapter);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onProductItemSelected(Product product) {
        actionListener.onProductItemSelected(product);
    }

    @Override
    public void showProducts(List<Product> products) {
        this.productListData.clear();
        this.productListData.addAll(products);
        productChooserAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInitialProgressLoading() {
        pbMainLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();

        super.onDestroy();
    }

}
