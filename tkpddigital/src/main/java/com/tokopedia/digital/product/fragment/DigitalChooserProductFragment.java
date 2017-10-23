package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalWebActivity;
import com.tokopedia.digital.product.adapter.ProductChooserAdapter;
import com.tokopedia.digital.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserProductFragment extends BasePresenterFragment
        implements ProductChooserAdapter.ActionListener {

    private static final String ARG_PARAM_EXTRA_PRODUCT_LIST_DATA =
            "ARG_PARAM_EXTRA_PRODUCT_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW =
            "ARG_PARAM_EXTRA_PRODUCT_STYLE_VIEW";

    private static final String EXTRA_STATE_PRODUCT_LIST_DATA =
            "EXTRA_STATE_PRODUCT_LIST_DATA";
    private static final String EXTRA_STATE_PRODUCT_STYLE_VIEW =
            "EXTRA_STATE_PRODUCT_STYLE_VIEW";


    @BindView(R2.id.rv_list_chooser)
    RecyclerView rvProductList;

    private List<Product> productListData;
    private String productStyleView;

    private ProductChooserAdapter productChooserAdapter;

    private ActionListener actionListener;

    public static Fragment newInstance(List<Product> operatorListData, String productStyleView) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_PRODUCT_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorListData);
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

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelableArrayList(EXTRA_STATE_PRODUCT_LIST_DATA,
                (ArrayList<? extends Parcelable>) productListData);
        state.putString(EXTRA_STATE_PRODUCT_STYLE_VIEW, productStyleView);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        productListData = savedState.getParcelableArrayList(EXTRA_STATE_PRODUCT_LIST_DATA);
        productStyleView = savedState.getString(EXTRA_STATE_PRODUCT_STYLE_VIEW);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productListData = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_PRODUCT_LIST_DATA);
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
        rvProductList.setAdapter(productChooserAdapter);
    }

    @Override
    protected void initialVar() {
        productChooserAdapter = new ProductChooserAdapter(
                this, productListData, this
        );
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onProductItemSelected(Product product) {
        actionListener.onProductItemSelected(product);
    }

    public interface ActionListener {
        void onProductItemSelected(Product product);
    }

}
