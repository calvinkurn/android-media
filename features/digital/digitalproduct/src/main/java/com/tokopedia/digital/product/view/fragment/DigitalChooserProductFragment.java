package com.tokopedia.digital.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.view.adapter.ProductChooserAdapter;
import com.tokopedia.digital.product.view.presenter.ProductChooserContract;
import com.tokopedia.digital.product.view.presenter.ProductChooserPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserProductFragment extends BaseDaggerFragment
        implements ProductChooserAdapter.ActionListener, ProductChooserContract.View {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";
    private static final String ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW =
            "ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW";

    private static final String EXTRA_STATE_PRODUCT_STYLE_VIEW =
            "EXTRA_STATE_PRODUCT_STYLE_VIEW";

    private RecyclerView rvProductList;
    private ProgressBar pbMainLoading;

    private CompositeSubscription compositeSubscription;

    private String categoryId;
    private String operatorId;
    private List<Product> productListData = new ArrayList<>();
    private String productStyleView;

    private ProductChooserAdapter productChooserAdapter;

    private ActionListener actionListener;

    @Inject
    ProductChooserPresenter presenter;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_STATE_PRODUCT_STYLE_VIEW, productStyleView);
    }

    @Override
    protected void initInjector() {
        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chooser_operator_digital_module, container, false);
        initView(view);
        initialVar();
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        actionListener = (ActionListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
        operatorId = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_ID);
        productStyleView = arguments.getString(ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW);
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        if (savedInstanceState != null){
            productStyleView = savedInstanceState.getString(EXTRA_STATE_PRODUCT_STYLE_VIEW);
        }
        presenter.attachView(this);
        presenter.getProductsByCategoryIdAndOperatorId(categoryId, operatorId);
    }

    protected void initView(View view) {
        rvProductList = view.findViewById(R.id.rv_list_chooser);
        pbMainLoading = view.findViewById(R.id.pb_main_loading);

        rvProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    private void initialVar() {
        productChooserAdapter = new ProductChooserAdapter(
                this, productListData, this
        );

        rvProductList.setAdapter(productChooserAdapter);
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
