package com.tokopedia.discovery.dynamicfilter.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.DynamicObject;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.discovery.dynamicfilter.adapter.DynamicCategoryAdapter;
import com.tokopedia.discovery.dynamicfilter.presenter.CategoryPresenter;
import com.tokopedia.discovery.dynamicfilter.presenter.CategoryPresenterImpl;
import com.tokopedia.discovery.dynamicfilter.presenter.CategoryView;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by noiz354 on 7/12/16.
 */
public class DynamicFilterCategoryFragment extends BaseFragment<CategoryPresenter> implements CategoryView {

    private static final String GROUPS_KEY = "groups_key";

    @BindView(R2.id.dynamic_filter_category_recyclerview)
    RecyclerView dynamicFilterCategory;

    @BindView(R2.id.dynamic_filter_category_finish)
    Button dynamicFilterCategoryFinish;

    private DynamicCategoryAdapter dynamicCategoryAdapter;
    private TkpdProgressDialog dialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

    private BroadcastReceiver resetFilterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dynamicCategoryAdapter.reset();
        }
    };


    public static DynamicFilterCategoryFragment newInstance(List<Breadcrumb> breadCrumb, List<DynamicFilterModel.Filter> filterList,
                                                            String currentCategory) {

        DynamicFilterCategoryFragment dynamicFilterCategoryFragment = new DynamicFilterCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DynamicFilterPresenter.EXTRA_PRODUCT_BREADCRUMB_LIST, Parcels.wrap(breadCrumb));
        bundle.putParcelable(DynamicFilterPresenter.EXTRA_FILTER_CATEGORY_LIST, Parcels.wrap(filterList));
        bundle.putString(DynamicFilterPresenter.EXTRA_CURRENT_CATEGORY, currentCategory);
        dynamicFilterCategoryFragment.setArguments(bundle);
        return dynamicFilterCategoryFragment;

    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(resetFilterReceiver, new IntentFilter(DynamicFilterActivity.ACTION_RESET_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(resetFilterReceiver);
    }

    @Override
    protected void initPresenter() {
        presenter = new CategoryPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_filter_category_layout;
    }

    @Override
    public int getFragmentId() {
        return FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        if(data[0] instanceof String) {
            Toast.makeText(getActivity(), (String) data[0], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setupAdapter(List<DynamicObject> dynamicParentObject) {
        dynamicCategoryAdapter = new DynamicCategoryAdapter(
                (DynamicFilterView) getActivity(), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = dynamicFilterCategory.getChildAdapterPosition(v);
                dynamicCategoryAdapter.toggleGroup(position);
            }
        });
        dynamicCategoryAdapter.addAll(dynamicParentObject);
        dynamicCategoryAdapter.expandCheckedCategory();
    }

    @Override
    public void setupRecyclerView() {
        // Set the layout manager to a LinearLayout manager for vertical list
        dynamicFilterCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
//        dynamicFilterCategory.addItemDecoration(new DividerItemDecoration(getActivity()));
        dynamicFilterCategory.setHasFixedSize(true);
        dynamicFilterCategory.setAdapter(dynamicCategoryAdapter);
    }

    @Override
    public void showLoading(boolean bool) {
        if(bool){
            dialog.showDialog();
        } else {
            dialog.dismiss();
        }
    }

    @OnClick(R2.id.dynamic_filter_category_finish)
    public void finishTo(){
        if(getActivity() != null && getActivity() instanceof DynamicFilterView){
            ((DynamicFilterView) getActivity()).finishThis();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(GROUPS_KEY, dynamicCategoryAdapter.saveGroups());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && dynamicCategoryAdapter != null) {
            List<Integer> groups = savedInstanceState.getIntegerArrayList(GROUPS_KEY);
            dynamicCategoryAdapter.restoreGroups(groups);
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
