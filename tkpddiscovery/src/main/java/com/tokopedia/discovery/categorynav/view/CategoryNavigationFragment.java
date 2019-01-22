package com.tokopedia.discovery.categorynav.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.di.CategoryNavigationInjector;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryChildAdapter;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryParentAdapter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.util.MoEngageEventTracking;

import java.util.List;


/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationFragment extends BaseDaggerFragment implements CategoryNavigationContract.View,
        CategoryParentAdapter.OnItemClickListener, CategoryChildAdapter.OnItemClickListener {

    public static final String TAG = "CATEGORY_NAVIGATION_FRAGMENT";
    private static final int DEFAULT_OFFSET = 170;
    private static final String PARAM_CATEGORY_ID = "param_category_id";
    private String categoryId = "";
    private String rootCategoryId = "";
    private CategoryNavDomainModel categoryNavDomainModel;
    private CategoryNavigationContract.Presenter presenter;

    private RecyclerView categoryRootRecyclerView;
    private RecyclerView categoryChildRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryChildAdapter categoryChildAdapter = new CategoryChildAdapter(this);

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
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_CATEGORY_ID, departmentId);
        categoryNavigationFragment.setArguments(bundle);
        return categoryNavigationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            categoryId = getArguments().getString(PARAM_CATEGORY_ID, "0");
        } else {
            categoryId = savedInstanceState.getString(PARAM_CATEGORY_ID, "0");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PARAM_CATEGORY_ID, categoryId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_category_navigation, container, false);

        categoryRootRecyclerView = (RecyclerView) parentView.findViewById(R.id.category_root_recyclerview);
        categoryChildRecyclerView = (RecyclerView) parentView.findViewById(R.id.category_child_recyclerview);

        presenter.attachView(this);
        presenter.getRootCategory(categoryId);

        return parentView;
    }

    @Override
    public void showLoading() {
        if (isAdded() && ((CategoryNavigationActivity) getActivity()).getProgressBar() != null) {
            ((CategoryNavigationActivity) getActivity()).getProgressBar().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (isAdded() && ((CategoryNavigationActivity) getActivity()).getProgressBar() != null) {
            ((CategoryNavigationActivity) getActivity()).getProgressBar().setVisibility(View.GONE);
        }
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    @Override
    public void renderRootCategory(CategoryNavDomainModel domainModel) {
        if (domainModel.getCategories() != null && domainModel.getCategories().size() > 0) {
            categoryNavDomainModel = domainModel;
            Category rootCategory = new Category();
            for (Category category : categoryNavDomainModel.getCategories()) {
                if (category.getChildren() != null && category.getChildren().size() > 0) {
                    rootCategoryId = category.getId();
                    rootCategory = category;
                    break;
                }
            }
            if (TextUtils.isEmpty(rootCategoryId)) {
                rootCategoryId = categoryNavDomainModel.getCategories().get(0).getId();
                rootCategory = categoryNavDomainModel.getCategories().get(0);
            }
            categoryParentAdapter = new CategoryParentAdapter(this, rootCategoryId);
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            categoryRootRecyclerView.setLayoutManager(linearLayoutManager);
            categoryRootRecyclerView.setAdapter(categoryParentAdapter);
            categoryParentAdapter.setDataList(categoryNavDomainModel.getCategories());


            LinearLayoutManager linearLayoutManagerChild = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            categoryChildRecyclerView.setLayoutManager(linearLayoutManagerChild);
            categoryChildRecyclerView.setAdapter(categoryChildAdapter);
            if (rootCategory != null && rootCategory.getChildren().size() > 0) {
                categoryChildAdapter.clear();
                categoryChildAdapter.addAll(rootCategory.getChildren());
                categoryParentAdapter.notifyDataSetChanged();
                if (!rootCategoryId.equals(categoryId))
                    categoryChildAdapter.toggleSelectedChildbyId(categoryId);
                linearLayoutManager.scrollToPositionWithOffset(categoryParentAdapter.getPositionById(rootCategoryId), DEFAULT_OFFSET);
            } else {
                presenter.getChildren(2, rootCategory.getId());
            }
        } else {
            showErrorEmptyState();
        }

    }

    @Override
    public void renderCategoryLevel2(String parentCategoryId, List<Category> children) {
        Category parentCategory = categoryNavDomainModel.getCategoryIndexById(parentCategoryId);

        parentCategory.addChildren(children, 2);
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(parentCategory.getChildren());
        categoryParentAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderCategoryLevel3(String categoryId, List<Category> children) {
        for (Category category : categoryNavDomainModel.getCategories()) {
            if (category.getId().equals(rootCategoryId)) {
                for (Category categoryLevel2 : (List<Category>) category.getChildren()) {
                    if (categoryLevel2.getId().equals(categoryId)) {
                        categoryLevel2.addChildren(children, 3);
                        categoryChildAdapter.clear();
                        categoryChildAdapter.addAll(category.getChildren());
                        categoryChildAdapter.toggleSelectedChildbyId(categoryLevel2.getId());
                        break;
                    }
                }
                break;
            }
        }
        categoryParentAdapter.notifyDataSetChanged();
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), ((CategoryNavigationActivity) getActivity())
                        .getFrameLayout(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getRootCategory(!TextUtils.isEmpty(rootCategoryId) ? rootCategoryId : "0");
                    }
                });
    }


    @Override
    public void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category parent, int position) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(getActivity(), parent.getName());
        MoEngageEventTracking.sendCategory(parent.getId(), parent.getName());
        rootCategoryId = parent.getId();
        categoryParentAdapter.setActiveId(parent.getId());
        for (Category category : categoryNavDomainModel.getCategories()) {
            if (category.getId() == rootCategoryId) {
                if (category.getChildren() != null && category.getChildren().size() > 0) {
                    categoryChildAdapter.clear();
                    categoryChildAdapter.addAll(category.getChildren());
                    categoryParentAdapter.notifyDataSetChanged();
                } else {
                    presenter.getChildren(2, rootCategoryId);
                }
                break;
            }
        }
    }

    @Override
    public void onChildClicked(Category category) {
        if (category.getHasChild()) {
            if (category.getChildren() != null && category.getChildren().size() > 0) {
                renderCategoryLevel3(category.getId(), (List<Category>) category.getChildren());
            } else {
                presenter.getChildren(3, category.getId());
            }
        } else {
            IntermediaryActivity.moveTo(
                    getActivity(),
                    category.getId(),
                    category.getName(),
                    true
            );
            MoEngageEventTracking.sendProductCategory(category.getId(), category.getName());
            getActivity().setResult(CategoryNavigationActivity.DESTROY_BROWSE_PARENT);
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        presenter.setOnDestroyView();
        super.onDestroyView();
    }

}
