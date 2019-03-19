package com.tokopedia.digital_deals.view.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.ProductItem;

import org.w3c.dom.Text;

import java.util.List;

public class CuratedDealsView extends LinearLayout implements DealsCategoryAdapter.INavigateToActivityRequest {

    private Context context;
    private TextView dealTitle, seeAllCuratedDeals;
    private RecyclerView curatedDealsRecyclerView;
    private CategoryItem categoryItem;
    private DealsCategoryAdapter categoryAdapter;
    private final boolean IS_SHORT_LAYOUT = false;
    DealsCategoryAdapter.INavigateToActivityRequest listener;
    DealsHomeFragment.OpenTrendingDeals openTrendingDeals;

    public CuratedDealsView(Context context) {
        super(context);
        initView();
    }

    public CuratedDealsView(Context context, CategoryItem categoryItem, DealsHomeFragment.OpenTrendingDeals openTrendingDeals, String addView) {
        super(context);
        this.context = context;
        this.categoryItem = categoryItem;
        this.openTrendingDeals = openTrendingDeals;
        initView();
    }

    public CuratedDealsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CuratedDealsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.curated_deals, this, true);
        dealTitle = view.findViewById(R.id.tv_popular);
        seeAllCuratedDeals = view.findViewById(R.id.tv_see_all_curated_deals);
        curatedDealsRecyclerView = view.findViewById(R.id.rv_curated_deals);
        seeAllCuratedDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrendingDeals.replaceFragment(categoryItem.getItems(), 0);
            }
        });

        dealTitle.setText(categoryItem.getTitle() + " Deals");
        curatedDealsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new DealsCategoryAdapter(categoryItem.getItems(), DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
        categoryAdapter.setDealsHomeLayout(true);
        curatedDealsRecyclerView.setAdapter(categoryAdapter);

    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {

    }
}
