package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment;

import android.app.Activity;
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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.common_digital.product.presentation.adapter.ProductChooserAdapter;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;
import com.tokopedia.digital.nostylecategory.digitalcategory.di.DaggerDigitalCategoryNoStyleComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.di.DigitalCategoryNoStyleComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter.DigitalProductChooserNoStyleContract;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter.DigitalProductChooserNoStylePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rizky on 06/09/18.
 */
public class DigitalProductChooserNoStyleFragment extends BaseDaggerFragment
        implements DigitalProductChooserNoStyleContract.View, ProductChooserAdapter.ActionListener {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";

    private RecyclerView rvListChooser;
    private ProgressBar pbMainLoading;

    private String categoryId;
    private String operatorId;

    private ProductChooserAdapter productChooserAdapter;

    private List<Product> products = new ArrayList<>();

    private ActionListener actionListener;

    @Inject
    DigitalProductChooserNoStylePresenter digitalProductChooserNoStylePresenter;

    public interface ActionListener {

        void onProductItemSelected(Product product);

    }

    public static Fragment newInstance(String categoryId, String operatorId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, operatorId);
        Fragment fragment = new DigitalProductChooserNoStyleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString(ARG_PARAM_EXTRA_CATEGORY_ID);
            operatorId = getArguments().getString(ARG_PARAM_EXTRA_OPERATOR_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mitra_digital_product_chooser, container,
                false);

        rvListChooser = rootview.findViewById(R.id.rv_list_chooser);
        pbMainLoading = rootview.findViewById(R.id.pb_main_loading);

        rvListChooser.setLayoutManager(new LinearLayoutManager(getActivity()));

        productChooserAdapter = new ProductChooserAdapter(this, products, this);

        rvListChooser.setAdapter(productChooserAdapter);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        digitalProductChooserNoStylePresenter.attachView(this);

        digitalProductChooserNoStylePresenter.getProducts(Integer.valueOf(categoryId), operatorId);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DigitalComponent digitalComponent =
                DaggerDigitalComponent.builder().baseAppComponent((
                        (BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                        .build();
        DigitalCategoryNoStyleComponent digitalCategoryNoStyleComponent =
                DaggerDigitalCategoryNoStyleComponent.builder().digitalComponent(digitalComponent)
                        .build();
        digitalCategoryNoStyleComponent.inject(this);
    }

    @Override
    public void renderProducts(List<Product> productList) {
        products.addAll(productList);
        productChooserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductItemSelected(Product product) {
        actionListener.onProductItemSelected(product);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.actionListener = (ActionListener) activity;
    }

}
