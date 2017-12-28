package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.adapter.ProductChooserAdapter;
import com.tokopedia.digital.product.listener.IProductChooserView;
import com.tokopedia.digital.product.model.Promo;
import com.tokopedia.digital.product.presenter.IProductChooserPresenter;
import com.tokopedia.digital.product.presenter.ProductChooserPresenter;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.model.product.Product;

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
        void onProductItemSelected(com.tokopedia.digital.product.model.Product product);
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

        DigitalWidgetInteractor digitalWidgetInteractor = new DigitalWidgetInteractor(
                compositeSubscription,
                new DigitalWidgetRepository(new DigitalEndpointService(), new FavoriteNumberListDataMapper()),
                new ProductMapper(),
                new OperatorMapper(),
                new JobExecutor(),
                new UIThread());

        presenter = new ProductChooserPresenter(this, digitalWidgetInteractor);
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
    public void onProductItemSelected(Product productWidget) {
        com.tokopedia.digital.product.model.Product product = map(productWidget);
        actionListener.onProductItemSelected(product);
    }

    private com.tokopedia.digital.product.model.Product map(Product productWidget) {
        com.tokopedia.digital.product.model.Product product = new com.tokopedia.digital.product.model.Product();
        product.setDesc(productWidget.getAttributes().getDesc());
        product.setDetail(productWidget.getAttributes().getDetail());
        product.setDetailUrl(productWidget.getAttributes().getDetailUrl());
        product.setDetailUrlText(productWidget.getAttributes().getDetailUrlText());
        product.setInfo(productWidget.getAttributes().getInfo());
        product.setPrice(productWidget.getAttributes().getPrice());
        product.setPricePlain(productWidget.getAttributes().getPricePlain());
        product.setProductId(productWidget.getId()); // changed from int to String
        product.setProductType(productWidget.getType());
        if (productWidget.getAttributes().getPromo() != null) {
            Promo promo = new Promo();
            promo.setBonusText(productWidget.getAttributes().getPromo().getBonusText());
//        promo.setId(productWidget.getAttributes().getPromo().getId());
            promo.setNewPrice(productWidget.getAttributes().getPromo().getNewPrice());
            promo.setNewPricePlain(productWidget.getAttributes().getPromo().getNewPricePlain()); // changed from int to long
            promo.setTag(productWidget.getAttributes().getPromo().getTag());
            promo.setTerms(productWidget.getAttributes().getPromo().getTerms());
            promo.setValueText(productWidget.getAttributes().getPromo().getValueText());
            product.setPromo(promo);
        }
        product.setStatus(productWidget.getAttributes().getStatus());

        return product;
    }

    @Override
    public void showProducts(List<com.tokopedia.digital.widget.model.product.Product> products) {
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
