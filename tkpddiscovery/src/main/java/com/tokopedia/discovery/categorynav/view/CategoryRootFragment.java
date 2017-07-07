package com.tokopedia.discovery.categorynav.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.discovery.R;
import com.tokopedia.core.network.entity.categories.Category;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryRootAdapter;

import java.util.ArrayList;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryRootFragment extends Fragment implements CategoryRootAdapter.OnItemClickListener  {

    public static final String ROOT_CATEGORIES = "ROOT_CATEGORIES";

    RecyclerView categoryRootRecyclerView;
    LinearLayoutManager linearLayoutManager;
    CategoryRootAdapter categoryRootAdapter;
    CategoryRootAdapter.OnItemClickListener onItemClickListener;

    public static Fragment newInstance(ArrayList<Category> title, CategoryRootAdapter.OnItemClickListener onItemClickListener) {
        Bundle argument = new Bundle();
        argument.putParcelableArrayList(CategoryRootFragment.ROOT_CATEGORIES, title);
        CategoryRootFragment categoryRootFragment = new CategoryRootFragment();
        categoryRootFragment.setArguments(argument);

        return categoryRootFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_root_category, container, false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryRootAdapter = new CategoryRootAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categoryRootRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRootRecyclerView.setAdapter(categoryRootAdapter);
        ArrayList<Category> categories = getActivity().getIntent().getParcelableArrayListExtra(ROOT_CATEGORIES);
        categoryRootAdapter.setDataList(categories);
        categoryRootAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Category category) {

    }
}
