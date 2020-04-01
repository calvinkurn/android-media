package com.tokopedia.digital_deals.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;

public class CuratedDealsView extends LinearLayout implements DealsCategoryAdapter.INavigateToActivityRequest {

    private Context context;
    private TextView dealTitle, seeAllCuratedDeals;
    private RecyclerView curatedDealsRecyclerView;
    private CategoryItem categoryItem;
    private DealsCategoryAdapter categoryAdapter;
    private final boolean IS_SHORT_LAYOUT = false;
    DealsCategoryAdapter.INavigateToActivityRequest listener;
    DealsHomeFragment.OpenTrendingDeals openTrendingDeals;
    DealsHomePresenter mPresenter;
    int homePosition;

    public CuratedDealsView(Context context) {
        super(context);
        initView();
    }

    public CuratedDealsView(Context context, CategoryItem categoryItem, DealsCategoryAdapter.INavigateToActivityRequest listener, DealsHomeFragment.OpenTrendingDeals openTrendingDeals, String addView, DealsHomePresenter mPresenter, int homePosition) {
        super(context);
        this.context = context;
        this.categoryItem = categoryItem;
        this.listener = listener;
        this.openTrendingDeals = openTrendingDeals;
        this.mPresenter = mPresenter;
        this.homePosition = homePosition;
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
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.curated_deals, this, true);
        dealTitle = view.findViewById(com.tokopedia.digital_deals.R.id.tv_popular);
        seeAllCuratedDeals = view.findViewById(com.tokopedia.digital_deals.R.id.tv_see_all_curated_deals);
        curatedDealsRecyclerView = view.findViewById(com.tokopedia.digital_deals.R.id.rv_curated_deals);


        if (categoryItem.getItems() != null && categoryItem.getItems().size() > 0) {
            dealTitle.setText(categoryItem.getTitle());
            curatedDealsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            categoryAdapter = new DealsCategoryAdapter(categoryItem.getItems(), DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
            categoryAdapter.setDealsHomeLayout(true);
            categoryAdapter.setDealType(DealsAnalytics.CURATED_DEALS);
            categoryAdapter.setHomePosition(homePosition);
            curatedDealsRecyclerView.setAdapter(categoryAdapter);
            if (categoryItem.getItems().size() >= 9) {
                seeAllCuratedDeals.setVisibility(VISIBLE);
                seeAllCuratedDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(categoryItem.getCategoryUrl())) {
                            openTrendingDeals.replaceFragment(categoryItem.getCategoryUrl(), dealTitle.getText().toString(), homePosition);
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
