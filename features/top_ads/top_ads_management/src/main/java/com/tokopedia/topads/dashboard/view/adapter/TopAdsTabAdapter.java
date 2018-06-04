package com.tokopedia.topads.dashboard.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.Summary;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TabLayoutViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 15/05/18.
 */

public class TopAdsTabAdapter extends RecyclerView.Adapter<TabLayoutViewHolder>{
    public static final int INDEX_CONVERSION = 5;

    @LayoutRes private int itemLayout = R.layout.item_tab_layout;
    private int selectedPos = 0;
    private OnRecyclerTabItemClick listener;
    private @TopAdsStatisticsType int selectedStatisticType = TopAdsStatisticsType.ALL_ADS;

    private List<TabMenu> tabMenus;
    private Context context;

    public TopAdsTabAdapter(Context context) {
        this.context = context;
        tabMenus = new ArrayList<>();
    }

    public void setSummary(Summary summary, String[] subtitles) {
        if (summary == null){
            tabMenus.clear();
            for (int i = 0; i < subtitles.length; ++i){
                tabMenus.add(new TabMenu("-", i == INDEX_CONVERSION?
                        getStringConversion(selectedStatisticType) : subtitles[i]));
            }
        } else {
            tabMenus.clear();
            tabMenus.add(new TabMenu(summary.getImpressionSumFmt(), subtitles[0]));
            tabMenus.add(new TabMenu(summary.getClickSumFmt(), subtitles[1]));
            tabMenus.add(new TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.getCostSumFmt()), subtitles[2]));
            tabMenus.add(new TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.getGrossProfitFmt()), subtitles[3]));
            tabMenus.add(new TabMenu(summary.getCtrPercentageFmt(), subtitles[4]));
            tabMenus.add(new TabMenu(summary.getConversionSumFmt(), getStringConversion(selectedStatisticType)));
            tabMenus.add(new TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.getCostAvgFmt()), subtitles[6]));
            tabMenus.add(new TabMenu(summary.getSoldSumFmt(), subtitles[7]));
        }
        notifyDataSetChanged();
    }

    private String getStringConversion(int selectedStatisticType) {
        String title = "";
        switch (selectedStatisticType){
            case TopAdsStatisticsType.SHOP_ADS:
                title =  context.getString(R.string.label_top_ads_conversion_store);
                break;
            case TopAdsStatisticsType.PRODUCT_ADS:
                title = context.getString(R.string.label_top_ads_conversion_product);
                break;
            case TopAdsStatisticsType.ALL_ADS:
            case TopAdsStatisticsType.HEADLINE_ADS:
            default:
                title = context.getString(R.string.label_top_ads_conversion);
                break;
        }
        return title;
    }

    public void setItemLayout(@LayoutRes int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public void setListener(OnRecyclerTabItemClick listener) {
        this.listener = listener;
    }

    @Override
    public TabLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabLayoutViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(TabLayoutViewHolder holder, final int position) {
        holder.bind(tabMenus.get(position).mainTitle, tabMenus.get(position).getSubTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = position;
                if (listener != null){
                    listener.onTabItemClick(position);
                }
                notifyDataSetChanged();
            }
        });
        holder.toggleActivate(position == selectedPos);

    }

    @Override
    public int getItemCount() {
        return tabMenus.size();
    }

    public int getSelectedTabPosition() {
        return selectedPos;
    }

    public void selected(int position) {
        selectedPos = position;
        notifyDataSetChanged();
    }

    public void setStatisticsType(@TopAdsStatisticsType int selectedStatisticType) {
        this.selectedStatisticType = selectedStatisticType;
        tabMenus.get(INDEX_CONVERSION).setSubTitle(getStringConversion(selectedStatisticType));
        notifyDataSetChanged();
    }

    public interface OnRecyclerTabItemClick{
        void onTabItemClick(int position);
    }

    class TabMenu{
        private String mainTitle;
        private String subTitle;

        public TabMenu(String mainTitle, String subTitle) {
            this.mainTitle = mainTitle;
            this.subTitle = subTitle;
        }

        public String getMainTitle() {
            return mainTitle;
        }

        public void setMainTitle(String mainTitle) {
            this.mainTitle = mainTitle;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
    }
}
