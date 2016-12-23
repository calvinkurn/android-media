package com.tokopedia.sellerapp.gmstat.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class MarketInsightViewHelper {

    @BindView(R.id.market_insight_header)
    LinearLayout marketInsightHeader;

    @BindView(R.id.market_insight_header_)
    TextView marketInsightHeader_;

    @BindView(R.id.market_insight_recyclerview)
    RecyclerView marketInsightRecyclerView;

    @BindView(R.id.market_insight_footer_)
    TextView marketInsightFooter_;

    @BindView(R.id.market_insight_non_gold_merchant)
    LinearLayout marketInsightNonGoldMerchant;

    private View view;
    private boolean isGoldMerchant;
    private MarketInsightAdapter marketInsightAdapter;

    public MarketInsightViewHelper(View view, boolean isGoldMerchant){
        this.view = view;
        this.isGoldMerchant = isGoldMerchant;
        ButterKnife.bind(this, view);
    }

    /**
     * set category for footer
     * @param hadesV1Models non null and non empty dataset
     */
    public void bindDataCategory(List<HadesV1Model> hadesV1Models){
        if(hadesV1Models==null||hadesV1Models.size()<=0)
            return;

        HadesV1Model.Category category = hadesV1Models.get(0).getData().getCategories().get(0);

        String categoryBold = String.format("\"<b>%s</b>\"", category.getName());
//        String footerText = String.format("Kata kunci ini berdasarkan kategori dari \"%s\"", category.getName());
//        marketInsightFooter_.setText(footerText);
        marketInsightFooter_.setText(Html.fromHtml("Kata kunci ini berdasarkan kategori dari "+categoryBold+" "));
    }

    public void bindData(List<GetKeyword> getKeywords){
        if(getKeywords==null||getKeywords.size()<=0)
            return;

        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(searchKeyword);
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);

        // hit hades for detail category

        if(isGoldMerchant){
            marketInsightNonGoldMerchant.setVisibility(View.GONE);
        }
    }

    public static class MarketInsightAdapter extends RecyclerView.Adapter<MarketInsightViewHolder>{

        private double total;
        List<GetKeyword.SearchKeyword> searchKeywords;

        public MarketInsightAdapter(List<GetKeyword.SearchKeyword> searchKeywords){
            this.searchKeywords = searchKeywords;

            total = 0;
            for (GetKeyword.SearchKeyword searchKeyword : searchKeywords) {
                total += searchKeyword.getFrequency();
            }

        }

        @Override
        public MarketInsightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.market_insight_item_layout, parent, false
            );
            return new MarketInsightViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(MarketInsightViewHolder holder, int position) {
            holder.bindData(searchKeywords.get(position), searchKeywords);
        }

        @Override
        public int getItemCount() {
            return searchKeywords.size()>=3?3:searchKeywords.size();
        }
    }

    public static class MarketInsightViewHolder extends RecyclerView.ViewHolder{

        final String TAG = "MarketInsight";

        @BindView(R.id.market_insight_keyword)
        TextView marketInsightKeyword;

        @BindView(R.id.market_insight_number)
        TextView marketInsightNumber;

        @BindView(R.id.zoom_icon)
        ImageView zoomIcon;

        @BindView(R.id.market_insight_progress)
        RoundCornerProgressBar marketInsightProgress;

        public MarketInsightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(GetKeyword.SearchKeyword searchKeyword, List<GetKeyword.SearchKeyword> list){

//            double total = 0;
//            for (GetKeyword.SearchKeyword sk : list) {
//                total += sk.getFrequency();
//            }
            double total = list.get(0).getFrequency();

            double v = searchKeyword.getFrequency() / total;
            double percentage = Math.floor((v*100)+0.5);
            Log.d(TAG, "total "+total+" percentage "+percentage+" frequency "+searchKeyword.getFrequency());

            marketInsightProgress.setProgress((float) percentage);

            marketInsightNumber.setText(searchKeyword.getFrequency()+"");

            marketInsightKeyword.setText(searchKeyword.getKeyword());
        }
    }
}
