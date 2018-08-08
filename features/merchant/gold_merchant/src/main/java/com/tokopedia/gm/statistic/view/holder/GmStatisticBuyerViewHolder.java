package com.tokopedia.gm.statistic.view.holder;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.gm.statistic.view.widget.circleprogress.DonutProgress;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.view.widget.ArrowPercentageView;

import java.util.Locale;

import static com.tokopedia.gm.statistic.constant.GMStatConstant.LOWER_BUYER_FORMAT;
import static com.tokopedia.gm.statistic.constant.GMStatConstant.UPPER_BUYER_FORMAT;

/**
 * Created by normansyahputa on 11/11/16.
 */

public class GmStatisticBuyerViewHolder implements GMStatisticViewHolder {

    public static final String DEFAULT_PERCENT_PIE = "0%";
    private DonutProgress buyerDataPieChart;
    private TextView tvBuyerAmount;
    private TextView tvMainLegendPie;
    private TextView tvSecondLegendPie;

    private String[] genderArray;
    private TextView tvMainLegendPieDesc;

    private TitleCardView buyerDataCardView;
    private ArrowPercentageView arrowPercentageView;
    private int redColor;
    private int greyColor;

    public GmStatisticBuyerViewHolder(View view) {
        buyerDataCardView = (TitleCardView) view.findViewById(R.id.buyer_data_card_view);
        buyerDataPieChart = (DonutProgress) buyerDataCardView.findViewById(R.id.buyer_data_pie_chart);
        tvBuyerAmount = (TextView) buyerDataCardView.findViewById(R.id.tv_buyer_amount);

        arrowPercentageView = (ArrowPercentageView) buyerDataCardView.findViewById(R.id.arrow_buyer_data_percentage);

        tvMainLegendPie = (TextView) buyerDataCardView.findViewById(R.id.tv_main_legend_pie);
        tvSecondLegendPie = (TextView) buyerDataCardView.findViewById(R.id.tv_second_legend_pie);
        tvMainLegendPieDesc = (TextView) buyerDataCardView.findViewById(R.id.tv_main_legend_pie_desc);

        genderArray = buyerDataCardView.getResources().getStringArray(R.array.gm_stat_gender);
        redColor = ResourcesCompat.getColor(buyerDataCardView.getResources(), R.color.tkpd_main_orange, null);
        greyColor = ResourcesCompat.getColor(buyerDataCardView.getResources(), R.color.grey_400, null);
    }

    public void bindData(GetBuyerGraph getBuyerGraph) {
        long totalBuyer = getBuyerGraph.getTotalBuyer();
        long maleBuyerCount = getBuyerGraph.getMaleBuyer();
        long femaleBuyerCount = getBuyerGraph.getFemaleBuyer();
        double diffTotalPercent = getBuyerGraph.getDiffTotal();

        /* this is empty state */
        if (totalBuyer == 0 && (maleBuyerCount == 0 || femaleBuyerCount == 0)) {
            tvBuyerAmount.setText(KMNumbers.getSummaryString(totalBuyer));
            buyerDataPieChart.setProgress(0f);
            arrowPercentageView.setPercentage(diffTotalPercent);
            tvMainLegendPie.setTextColor(greyColor);
            tvMainLegendPie.setText(DEFAULT_PERCENT_PIE);
            tvMainLegendPieDesc.setVisibility(View.GONE);
            tvSecondLegendPie.setVisibility(View.GONE);
            setViewState(LoadingStateView.VIEW_CONTENT);
            return;
        }
        tvMainLegendPie.setTextColor(redColor);

        tvMainLegendPieDesc.setVisibility(View.VISIBLE);
        tvSecondLegendPie.setVisibility(View.VISIBLE);
        tvMainLegendPie.setVisibility(View.VISIBLE);

        double malePercentage = (double) maleBuyerCount / (double) totalBuyer;
        double malePercent = Math.floor((malePercentage * 100) + 0.5);

        double femalePercentage = (double) femaleBuyerCount / (double) totalBuyer;
        double femalePercent = Math.floor((femalePercentage * 100) + 0.5);

        if (malePercent >= femalePercent) { // the male will be bigger
            tvMainLegendPieDesc.setText(genderArray[0]);
            if (femalePercent <= 0) {
                tvSecondLegendPie.setVisibility(View.GONE);
            } else
                tvSecondLegendPie.setText(String.format(Locale.US, UPPER_BUYER_FORMAT, (int) femalePercent, genderArray[1]));
            tvMainLegendPie.setText(String.format(Locale.US, LOWER_BUYER_FORMAT, (int) malePercent));
            buyerDataPieChart.setProgress((float) malePercent);
        } else { // the female will be bigger
            tvMainLegendPieDesc.setText(genderArray[1]);
            if (malePercent <= 0) {
                tvSecondLegendPie.setVisibility(View.GONE);
            } else
                tvSecondLegendPie.setText(String.format(Locale.US, UPPER_BUYER_FORMAT, (int) malePercent, genderArray[0]));
            tvMainLegendPie.setText(String.format(Locale.US, LOWER_BUYER_FORMAT, (int) femalePercent));
            buyerDataPieChart.setProgress((float) femalePercent);
        }

        tvBuyerAmount.setText(KMNumbers.getSummaryString(totalBuyer));

        arrowPercentageView.setPercentage(diffTotalPercent);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void setViewState(int viewState) {
        buyerDataCardView.setViewState(viewState);
    }

}
