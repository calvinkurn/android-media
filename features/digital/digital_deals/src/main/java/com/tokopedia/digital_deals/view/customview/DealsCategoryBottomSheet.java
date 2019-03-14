package com.tokopedia.digital_deals.view.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class DealsCategoryBottomSheet extends BottomSheets implements DealsCategoryItemAdapter.CategorySelected {

    private RecyclerView recyclerView;
    List<CategoryItem> categoryItemList;
    OpenCategoryDetail openCategoryDetail;

    @Override
    public int getLayoutResourceId() {
        return R.layout.deals_category_bottomsheet_layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OpenCategoryDetail) {
            openCategoryDetail = (OpenCategoryDetail) getParentFragment();
        }
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_category_items);
        DealsCategoryItemAdapter adapter = new DealsCategoryItemAdapter(categoryItemList, this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    protected String title() {
        return "Pilih Kategori Kupon";
    }

    public void setDealsCategoryList(List<CategoryItem> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

    @Override
    public void openCategoryDetail(CategoriesModel categoriesModel) {
        openCategoryDetail.categorySelected(categoriesModel);
    }

    public interface OpenCategoryDetail {
        void categorySelected(CategoriesModel categoriesModel);
    }
}
