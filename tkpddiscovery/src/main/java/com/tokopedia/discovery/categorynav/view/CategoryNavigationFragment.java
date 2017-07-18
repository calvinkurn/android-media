package com.tokopedia.discovery.categorynav.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.di.CategoryNavigationInjector;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryChildAdapter;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryParentAdapter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;

import java.util.List;


/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationFragment extends BaseDaggerFragment implements CategoryNavigationContract.View,
        CategoryParentAdapter.OnItemClickListener{

    public static final String TAG = "CATEGORY_NAVIGATION_FRAGMENT";
    private String departmentId = "";
    private CategoryNavDomainModel categoryNavDomainModel;
    private CategoryNavigationContract.Presenter presenter;

    private RecyclerView categoryRootRecyclerView;
    private RecyclerView categoryChildRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryChildAdapter categoryChildAdapter;

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
        categoryChildRecyclerView = (RecyclerView) parentView.findViewById(R.id.category_child_recyclerview);

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
    public void renderRootCategory(CategoryNavDomainModel domainModel) {
        categoryNavDomainModel = domainModel;
        categoryParentAdapter = new CategoryParentAdapter(this,departmentId);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categoryRootRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRootRecyclerView.setAdapter(categoryParentAdapter);
        categoryParentAdapter.setDataList(categoryNavDomainModel.getCategories());
        categoryParentAdapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(departmentId)) {
            LinearLayoutManager linearLayoutManagerChild = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            categoryChildRecyclerView.setLayoutManager(linearLayoutManagerChild);
            categoryChildAdapter = new CategoryChildAdapter(this);
            categoryChildRecyclerView.setAdapter(categoryChildAdapter);
            Category parentCategory = categoryNavDomainModel.getCategoryIndexById(departmentId);
            if (parentCategory!=null) {
                categoryChildAdapter.setNewDataList(parentCategory.getChildren());
            }
        }
    }

    @Override
    public void renderCategoryChildren(List<ChildCategory> children) {
        for (Category category: categoryNavDomainModel.getCategories()) {
            if (category.getId() == departmentId) {
                category.setChildren(children);
                break;
            }
        }
        categoryChildAdapter.setNewDataList(children);
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
    public void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category parent) {
        departmentId = parent.getId();
        categoryParentAdapter.setActiveId(parent.getId());
        for (Category category: categoryNavDomainModel.getCategories()) {
            if (category.getId() == departmentId) {
                if (category.getChildren()!=null && category.getChildren().size()>0) {
                    renderCategoryChildren(category.getChildren());
                } else {
                    presenter.getChildren(departmentId);
                }
                break;
            }
        }
    }
}
