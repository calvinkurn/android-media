package com.tokopedia.gm.statistic.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;

import java.util.List;

import static com.tokopedia.gm.statistic.constant.GMStatConstant.NUMBER_TIMES_FORMAT;

/**
 * Created by Nathan on 7/25/2017.
 */
public class GMMarketViewHolder extends RecyclerView.ViewHolder {

    final String TAG = "MarketInsight";
    TextView marketInsightKeyword;
    TextView marketInsightNumber;
    ImageView zoomIcon;
    RoundCornerProgressBar marketInsightProgress;

    public GMMarketViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    void initView(View itemView) {
        marketInsightKeyword = (TextView) itemView.findViewById(R.id.market_insight_keyword);
        marketInsightNumber = (TextView) itemView.findViewById(R.id.market_insight_number);
        zoomIcon = (ImageView) itemView.findViewById(R.id.zoom_icon);
        marketInsightProgress = (RoundCornerProgressBar) itemView.findViewById(R.id.market_insight_progress);
    }

    public void bindData(GetKeyword.SearchKeyword searchKeyword, List<GetKeyword.SearchKeyword> list) {
        double total = list.get(0).getFrequency();
        double v = searchKeyword.getFrequency() / total;
        double percentage = Math.floor((v * 100) + 0.5);
        marketInsightProgress.setProgress((float) percentage);
        marketInsightNumber.setText(String.format(NUMBER_TIMES_FORMAT, String.valueOf(searchKeyword.getFrequency())));
        marketInsightKeyword.setText(searchKeyword.getKeyword());
    }
}
