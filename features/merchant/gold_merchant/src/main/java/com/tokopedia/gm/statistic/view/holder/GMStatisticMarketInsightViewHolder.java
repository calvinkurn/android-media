package com.tokopedia.gm.statistic.view.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.view.adapter.GMMarketInsightAdapter;
import com.tokopedia.gm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class GMStatisticMarketInsightViewHolder implements GMStatisticViewHolder {

    public interface Listener {
        void onViewNotGmClicked();
    }

    private static final String DEFAULT_CATEGORY = "kaos";

    private TextView tvMarketInsightFooter;
    private TitleCardView titleCardView;
    private GMMarketInsightAdapter GMMarketInsightAdapter;
    private View notGMView;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public GMStatisticMarketInsightViewHolder(View view) {
        titleCardView = (TitleCardView) view.findViewById(R.id.market_insight_card_view);
        notGMView = titleCardView.getContentView().findViewById(R.id.vg_market_insight_not_gm);

        notGMView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickGMStatBuyGMDetailTransaction(v.getContext());
                if (listener != null) {
                    listener.onViewNotGmClicked();
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvMarketInsightFooter = (TextView) view.findViewById(R.id.market_insight_footer);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                titleCardView.getContext(), LinearLayoutManager.VERTICAL, false));
        GMMarketInsightAdapter = new GMMarketInsightAdapter(new ArrayList<GetKeyword.SearchKeyword>());
        recyclerView.setAdapter(GMMarketInsightAdapter);

        TextView titleUpgradeGM = notGMView.findViewById(R.id.market_insight_gmsubscribe_text);
        titleUpgradeGM.setText(view.getContext().getString(R.string.gm_statistic_upgrade_to_gold_merchant,
                view.getContext().getString(GMConstant.getGMTitleResource(view.getContext()))));
    }

    /**
     * set category for footer
     */
    public void bindCategory(String categoryName) {
        if (TextUtils.isEmpty(categoryName)) {
            tvMarketInsightFooter.setVisibility(View.INVISIBLE);
        } else {
            tvMarketInsightFooter.setText(MethodChecker.fromHtml(
                    tvMarketInsightFooter.getContext().getString(
                            R.string.gm_statistic_these_keywords_are_based_on_category_x, categoryName)));
            tvMarketInsightFooter.setVisibility(View.VISIBLE);
        }
    }

    public void bindData(List<GetKeyword> getKeywords, boolean isGoldMerchant) {
        if (!isGoldMerchant) {
            displayNonGoldMerchant();
            return;
        }

        if (getKeywords == null || getKeywords.size() <= 0) {
            displayEmptyState();
            return;
        }

        notGMView.setVisibility(View.GONE);
        //[START] check whether all is empty
        int isNotEmpty = 0;
        for (GetKeyword getKeyword : getKeywords) {
            if (getKeyword.getSearchKeyword() == null || getKeyword.getSearchKeyword().isEmpty())
                isNotEmpty++;
        }

        // if all keyword empty
        if (isNotEmpty == getKeywords.size()) {
            displayEmptyState();
            return;
        }

        // remove null or empty
        for (int i = 0; i < getKeywords.size(); i++) {
            if (getKeywords.get(i) == null
                    || getKeywords.get(i).getSearchKeyword() == null
                    || getKeywords.get(i).getSearchKeyword().isEmpty())
                getKeywords.remove(i);
        }

        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();
        GMMarketInsightAdapter.setSearchKeywords(searchKeyword);
        GMMarketInsightAdapter.notifyDataSetChanged();
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void bindNoShopCategory(boolean goldMerchant) {
        if (goldMerchant)
            displayEmptyState();
        else {
            displayNonGoldMerchant();
        }
    }

    private void displayNonGoldMerchant() {
        titleCardView.setViewState(LoadingStateView.VIEW_CONTENT);
        notGMView.setVisibility(View.VISIBLE);
        displayDummyContentKeyword();
    }

    private void displayDummyContentKeyword() {
        // create dummy data as replacement for non gold merchant user.
        List<GetKeyword.SearchKeyword> searchKeyword = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            GetKeyword.SearchKeyword searchKeyword1 = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(1000);
            searchKeyword1.setKeyword(
                    String.format(
                            titleCardView.getContext().getString(R.string.market_insight_item_non_gm_text),
                            Integer.toString(i)
                    )
            );
            searchKeyword.add(searchKeyword1);
        }

        bindCategory(DEFAULT_CATEGORY);

        GMMarketInsightAdapter.setSearchKeywords(searchKeyword);
        GMMarketInsightAdapter.notifyDataSetChanged();
    }

    private void displayEmptyState() {
        titleCardView.setEmptyViewRes(R.layout.partial_gm_statistic_market_insight_empty_state);
        titleCardView.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    @Override
    public void setViewState(int viewState) {
        titleCardView.setViewState(viewState);
    }
}