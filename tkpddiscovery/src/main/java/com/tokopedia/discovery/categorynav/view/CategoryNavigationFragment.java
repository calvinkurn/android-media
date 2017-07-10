package com.tokopedia.discovery.categorynav.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.di.CategoryNavigationInjector;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryParentAdapter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;


/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationFragment extends BaseDaggerFragment implements CategoryNavigationContract.View, CategoryParentAdapter.OnItemClickListener{

    public static final String TAG = "CATEGORY_NAVIGATION_FRAGMENT";
    private String departmentId = "";
    private CategoryNavigationContract.Presenter presenter;

    RecyclerView categoryRootRecyclerView;

    LinearLayoutManager linearLayoutManager;
    CategoryParentAdapter categoryParentAdapter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        presenter = CategoryNavigationInjector.getPresenter(getActivity());
    }

    public static CategoryNavigationFragment createInstance(String departmentId) {
        CategoryNavigationFragment categoryNavigationFragment = new CategoryNavigationFragment();
        categoryNavigationFragment.departmentId = departmentId;
        return categoryNavigationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_category_navigation, container, false);

        categoryRootRecyclerView = (RecyclerView) parentView.findViewById(R.id.category_root_recyclerview);

        presenter.attachView(this);
        presenter.getRootCategory(departmentId);

        return parentView;
    }

    @Override
    public void showLoading() {
        if (isAdded() && ((IntermediaryActivity) getActivity()).getProgressBar() !=null) {
            ((IntermediaryActivity) getActivity()).getProgressBar().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (isAdded() && ((CategoryNavigationActivity) getActivity()).getProgressBar() !=null) {
            ((CategoryNavigationActivity) getActivity()).getProgressBar().setVisibility(View.GONE);
        }
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    @Override
    public void renderRootCategory(CategoryNavDomainModel categoryNavDomainModel) {
        categoryParentAdapter = new CategoryParentAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categoryRootRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRootRecyclerView.setAdapter(categoryParentAdapter);
        categoryParentAdapter.setDataList(categoryNavDomainModel.getCategories());
        categoryParentAdapter.notifyDataSetChanged();
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(),  ((IntermediaryActivity) getActivity())
                        .getFrameLayout(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getRootCategory(departmentId);
                    }
                });
    }


    @Override
    public void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category category) {

    }
}
