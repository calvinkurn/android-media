package com.tokopedia.mitra.digitalcategory.presentation.fragment;

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
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.presentation.adapter.MitraDigitalOperatorChooserAdapter;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalProductChooserContract;

import java.util.List;

/**
 * Created by Rizky on 06/09/18.
 */
public class MitraDigitalProductChooserFragment extends BaseDaggerFragment
        implements MitraDigitalProductChooserContract.View {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";

    private RecyclerView rvListChooser;
    private ProgressBar pbMainLoading;

    private String categoryId;
    private String operatorId;

    public static Fragment newInstance(String categoryId, String operatorId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, operatorId);
        Fragment fragment = new MitraDigitalProductChooserFragment();
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

        productChooserAdapter = new MitraDigitalOperatorChooserAdapter(this, operatorList);

        rvListChooser.setAdapter(operatorChooserAdapter);


        return rootview;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void renderProducts(List<Product> productList) {

    }

}
