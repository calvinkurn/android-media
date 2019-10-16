package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.filter.common.data.Category;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.R;
import com.tokopedia.filter.newdynamicfilter.adapter.CategoryChildAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.CategoryParentAdapter;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;

import org.parceler.Parcels;

import java.util.List;

import static com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity.EXTRA_TRACKING_DATA;
import static com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity.EXTRA_IS_USING_TRACKING;


/**
 * Created by henrypriyono on 8/24/17.
 */

public class DynamicFilterCategoryActivity extends AppCompatActivity
        implements CategoryParentAdapter.OnItemClickListener, CategoryChildAdapter.OnItemClickListener {



    public static final int REQUEST_CODE = 221;
    public static final String EXTRA_SELECTED_CATEGORY_ID = "EXTRA_SELECTED_CATEGORY_ID";
    public static final String EXTRA_SELECTED_CATEGORY_ROOT_ID = "EXTRA_SELECTED_CATEGORY_ROOT_ID";
    public static final String EXTRA_SELECTED_CATEGORY_NAME = "EXTRA_SELECTED_CATEGORY_NAME";

    private static final String EXTRA_DEFAULT_CATEGORY_ID = "EXTRA_DEFAULT_CATEGORY_ID";
    private static final String EXTRA_DEFAULT_CATEGORY_ROOT_ID = "EXTRA_DEFAULT_CATEGORY_ROOT_ID";
    private static final String EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST";
    private static final int DEFAULT_OFFSET = 170;

    List<Category> categoryList;
    private RecyclerView rootRecyclerView;
    private RecyclerView childRecyclerView;
    private View buttonClose;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryChildAdapter categoryChildAdapter;
    private String defaultCategoryId;
    private String defaultCategoryRootId;
    private boolean isUsingTracking;
    private FilterTrackingData trackingData;

    public static void moveTo(AppCompatActivity activity,
                              List<Option> optionList,
                              String defaultCategoryRootId,
                              String defaultCategoryId,
                              boolean isUsingTracking,
                              FilterTrackingData trackingData
                              ) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterCategoryActivity.class);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
            intent.putExtra(EXTRA_DEFAULT_CATEGORY_ROOT_ID, defaultCategoryRootId);
            intent.putExtra(EXTRA_DEFAULT_CATEGORY_ID, defaultCategoryId);
            intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking);
            intent.putExtra(EXTRA_TRACKING_DATA, trackingData);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_filter_category);
        fetchDataFromIntent();
        bindView();
        loadFilterItems();
    }

    private void fetchDataFromIntent() {
        isUsingTracking = getIntent().getBooleanExtra(EXTRA_IS_USING_TRACKING, false);
        trackingData = getIntent().getParcelableExtra(EXTRA_TRACKING_DATA);
        defaultCategoryId
                = getIntent().getStringExtra(DynamicFilterCategoryActivity.EXTRA_DEFAULT_CATEGORY_ID);
        defaultCategoryRootId
                = getIntent().getStringExtra(DynamicFilterCategoryActivity.EXTRA_DEFAULT_CATEGORY_ROOT_ID);

         List<Option> optionList = Parcels.unwrap(
                getIntent().getParcelableExtra(EXTRA_OPTION_LIST));

        categoryList = OptionHelper.convertToCategoryList(optionList);
    }

    private void bindView() {
        rootRecyclerView = (RecyclerView) findViewById(R.id.category_root_recyclerview);
        childRecyclerView = (RecyclerView) findViewById(R.id.category_child_recyclerview);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadFilterItems() {
        if (TextUtils.isEmpty(defaultCategoryRootId)) {
            defaultCategoryRootId = categoryList.get(0).getId();
        }

        categoryParentAdapter = new CategoryParentAdapter(this, defaultCategoryRootId);
        categoryParentAdapter.setDataList(categoryList);

        LinearLayoutManager rootLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rootRecyclerView.setLayoutManager(rootLayoutManager);

        rootRecyclerView.setAdapter(categoryParentAdapter);
        int defaultRootPosition = categoryParentAdapter.getPositionById(defaultCategoryRootId);
        rootLayoutManager.scrollToPositionWithOffset(defaultRootPosition, DEFAULT_OFFSET);

        categoryChildAdapter = new CategoryChildAdapter(this);
        categoryChildAdapter.setLastSelectedCategoryId(defaultCategoryId);
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(categoryList.get(defaultRootPosition).getChildren());

        LinearLayoutManager childLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        childRecyclerView.setLayoutManager(childLayoutManager);

        childRecyclerView.setAdapter(categoryChildAdapter);

        if (!TextUtils.isEmpty(defaultCategoryId)) {
            categoryChildAdapter.toggleSelectedChildbyId(defaultCategoryId);
            childLayoutManager.scrollToPositionWithOffset(categoryChildAdapter.getActivePosition(), DEFAULT_OFFSET);
        }
    }

    @Override
    public void onItemClicked(Category category, int position) {
        categoryParentAdapter.setActiveId(category.getId());
        categoryParentAdapter.notifyDataSetChanged();
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(category.getChildren());
    }

    @Override
    public void onChildClicked(Category category) {
        if (category.getHasChild()) {
            categoryChildAdapter.toggleSelectedChildbyId(category.getId());
        } else {
            if (isUsingTracking) {
                FilterTracking.eventFilterJourney(
                        trackingData,
                        getResources().getString(R.string.title_category),
                        category.getName(), true, true, category.isAnnotation());
            }
            applyFilter(category);
        }
    }

    private void applyFilter(Category category) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ID, category.getId());
        intent.putExtra(EXTRA_SELECTED_CATEGORY_NAME, category.getName());
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ROOT_ID, categoryParentAdapter.getActiveId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
