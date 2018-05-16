package com.tokopedia.topads.dashboard.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Summary;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TabLayoutViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 15/05/18.
 */

public class TopAdsTabAdapter extends RecyclerView.Adapter<TabLayoutViewHolder>{
    @LayoutRes private int itemLayout = R.layout.item_tab_layout;
    private int selectedPos = 0;
    private OnRecyclerTabItemClick listener;

    private List<TabMenu> tabMenus = new ArrayList<>();

    public void setSummary(Summary summary, String[] subtitles) {
        if (summary == null){
            tabMenus.clear();
            for (String sub : subtitles){
                tabMenus.add(new TabMenu("-", sub));
            }
        } else {
            tabMenus.clear();
            tabMenus.add(new TabMenu(summary.getImpressionSumFmt(), subtitles[0]));
            tabMenus.add(new TabMenu(summary.getClickSumFmt(), subtitles[1]));
            tabMenus.add(new TabMenu(summary.getCtrPercentageFmt(), subtitles[2]));
            tabMenus.add(new TabMenu(summary.getConversionSumFmt(), subtitles[3]));
            tabMenus.add(new TabMenu(summary.getCostAvgFmt(), subtitles[4]));
            tabMenus.add(new TabMenu(summary.getCostSumFmt(), subtitles[5]));
        }
        notifyDataSetChanged();
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
                notifyDataSetChanged();
                if (listener != null){
                    listener.onTabItemClick(position);
                }
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
