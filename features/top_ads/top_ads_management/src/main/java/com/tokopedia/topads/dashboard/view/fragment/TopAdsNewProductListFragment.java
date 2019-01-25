package com.tokopedia.topads.dashboard.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.common.widget.DividerItemDecoration;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsNewProductListAdapter;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyProductListDataBinder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsNewProductListViewHolder;
import com.tokopedia.topads.dashboard.view.listener.TopAdsGetProductDetailView;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductListStepperModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public abstract class TopAdsNewProductListFragment<T extends TopAdsProductListStepperModel, V extends TopAdsGetProductDetailPresenter>
        extends BasePresenterFragment implements TopAdsNewProductListViewHolder.DeleteListener, TopAdsGetProductDetailView {

    public static final int ADD_PRODUCT_REQUEST_CODE = 1;

    @Inject
    V daggerPresenter;

    private TopAdsNewProductListAdapter adapter;
    protected T stepperModel;
    protected StepperListener stepperListener;

    private TextView counterProduct;
    private TextView addProduct;
    protected Button buttonNext;
    private RecyclerView recyclerView;
    protected ProgressDialog progressDialog;

    protected abstract void initiateStepperModel();

    protected abstract void goToNextPage();

    @Override
    protected void initView(View view) {
        super.initView(view);
        counterProduct = (TextView) view.findViewById(R.id.counter_product);
        addProduct = (TextView) view.findViewById(R.id.add_product);
        buttonNext = (Button) view.findViewById(R.id.button_submit);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        if (isShouldLoadItemIdToAdd()) {
            showLoading();
            daggerPresenter.getProductDetail(stepperModel.getIdToAdd());
        }
    }

    private boolean isShouldLoadItemIdToAdd() {
        return stepperModel != null &&
                stepperModel.getIdToAdd() != null &&
                !stepperModel.getIdToAdd().equals("");
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        adapter = new TopAdsNewProductListAdapter(this);
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        updateEmptyView();
        if (stepperModel != null) {
            populateView(stepperModel.getTopAdsProductViewModels());
        }
        updateSelectedProductCount();
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddProduct();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                if (stepperModel == null) {
                    initiateStepperModel();
                }
                stepperModel.setTopAdsProductViewModels(adapter.getData());
                goToNextPage();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    protected void goToAddProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, isHideExistingGroup());
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, true);
        intent.putExtra(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, new ArrayList<Parcelable>(adapter.getData()));
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_product_list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_PRODUCT_REQUEST_CODE && intent != null && intent.hasExtra(TopAdsExtraConstant.EXTRA_SELECTIONS)) {
            populateView(intent.getParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS));
            updateSelectedProductCount();
        }
    }

    private void updateSelectedProductCount() {
        if (adapter.getDataSize() > 0) {
            if(adapter.getDataSize() > 9) {
                counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product, adapter.getDataSize())));
            }else{
                counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product_zero, adapter.getDataSize())));
            }
            addProduct.setText(R.string.label_edit);
            buttonNext.setEnabled(true);
        } else {
            counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product_zero, adapter.getDataSize())));
            addProduct.setText(R.string.top_ads_keyword_add);
            buttonNext.setEnabled(false);
        }
        updateEmptyView();
    }

    protected void populateView(List topAdsProductViewModels) {
        adapter.clearData();
        adapter.addData(topAdsProductViewModels);
        updateEmptyView();
    }

    private void updateEmptyView() {
        if(adapter.getData().size() >0 ){
            adapter.showEmptyFull(false);
        }else{
            adapter.showEmptyFull(true);
        }
    }

    @Override
    public void onDelete(int position) {
        removeProduct(position);
    }

    protected void removeProduct(int position) {
        if (adapter.getData().size() > position) {
            adapter.getData().remove(position);
            adapter.notifyItemRemoved(position);
        }
        updateSelectedProductCount();
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }


    protected TopAdsEmptyProductListDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyProductListDataBinder topAdsEmptyProductListDataBinder = new TopAdsEmptyProductListDataBinder(adapter);
        topAdsEmptyProductListDataBinder.setDrawableAsset(R.drawable.ic_empty_product_list);
        topAdsEmptyProductListDataBinder.setEmptyContentText(getString(R.string.top_ads_label_choose_product_desc_empty));
        return topAdsEmptyProductListDataBinder;
    }


    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }


    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        hideLoading();
        List<TopAdsProductViewModel> topAdsProductViewModels = new ArrayList<>();
        topAdsProductViewModels.add(topAdsProductViewModel);
        populateView(topAdsProductViewModels);
        updateSelectedProductCount();
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        hideLoading();
        Toast.makeText(getActivity(), getString(R.string.msg_network_error), Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        daggerPresenter.detachView();
        super.onDestroy();
    }

    protected boolean isHideExistingGroup() {
        return true;
    }
}
