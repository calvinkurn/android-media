package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;
import com.tokopedia.sellerapp.gmstat.models.GetShopCategory;
import com.tokopedia.sellerapp.gmsubscribe.GMSubscribeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.sellerapp.gmstat.views.DataTransactionViewHelper.dpToPx;

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

    @BindView(R.id.market_insight_gold_merchant)
    LinearLayout marketInsightGoldMerchant;

    @BindView(R.id.market_insight_non_gold_merchant)
    LinearLayout marketInsightNonGoldMerchant;

    @BindView(R.id.market_insight_empty_state)
    LinearLayout marketInsightEmptyState;

    @BindView(R.id.market_insight_container_top)
    FrameLayout marketInsightContainerTop;

    @BindColor(R.color.empty_background)
    int emptyColorBackground;

    @BindColor(android.R.color.transparent)
    int transparantColor;

//    @BindView(R.id.market_insight_container_upper)
//    RelativeLayout marketInsightContainerUpper;

    @OnClick(R.id.market_insight_empty_state)
    public void addProductMarketInsight(){
        ProductActivity.moveToAddProduct(view.getContext());
    }

    @OnClick({R.id.move_to_gmsubscribe_market_insight,
            R.id.market_insight_container_top,
            R.id.market_insight_non_gold_merchant,
            R.id.market_insight_gmsubscribe_text
    })
    public void moveToGMSubscribe(){
        if(!isGoldMerchant)
            view.getContext().startActivity(new Intent(view.getContext(), GMSubscribeActivity.class));
    }

    private View view;
    private boolean isGoldMerchant;
    private MarketInsightAdapter marketInsightAdapter;

    public MarketInsightViewHelper(View view, boolean isGoldMerchant){
        this.view = view;
        this.isGoldMerchant = isGoldMerchant;
        ButterKnife.bind(this, view);

        view.findViewById(R.id.move_to_gmsubscribe_market_insight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToGMSubscribe();
            }
        });

//        if(isGoldMerchant){
//
//            marketInsightContainerUpper.setPadding(0, 0, 0, (int) dpToPx(view.getContext(), 16));
//        }
    }

    /**
     * set category for footer
     * @param hadesV1Models non null and non empty dataset
     */
    public void bindDataCategory(List<HadesV1Model> hadesV1Models){
        if(hadesV1Models==null||hadesV1Models.size()<=0)
            return;

        HadesV1Model.Category category = hadesV1Models.get(0).getData().getCategories().get(0);

        String categoryBold = String.format("\"<i><b>%s</b></i>\"", category.getName());
//        String footerText = String.format("Kata kunci ini berdasarkan kategori dari \"%s\"", category.getName());
//        marketInsightFooter_.setText(footerText);
        marketInsightFooter_.setText(MethodChecker.fromHtml("Kata kunci ini berdasarkan kategori dari "+categoryBold+" "));
    }

    public void bindData(List<GetKeyword> getKeywords){
        if(isGoldMerchant){
            marketInsightGoldMerchant.setVisibility(View.VISIBLE);
            marketInsightNonGoldMerchant.setVisibility(View.GONE);
            marketInsightEmptyState.setVisibility(View.GONE);
        }else{
            displayNonGoldMerchant();
            return;
        }

        if(getKeywords==null||getKeywords.size()<=0) {
            displayEmptyState();
            return;
        }

        //[START] check whether all is empty
        int isNotEmpty = 0;
        for (GetKeyword getKeyword : getKeywords) {
            if(getKeyword.getSearchKeyword() == null || getKeyword.getSearchKeyword().isEmpty())
                isNotEmpty++;
        }

        // if all keyword empty
        if(isNotEmpty == getKeywords.size()) {
            displayEmptyState();
            return;
        }

        // remove null or empty
        for(int i=0;i<getKeywords.size();i++){
            if(getKeywords.get(i) == null
                    || getKeywords.get(i).getSearchKeyword() == null
                    || getKeywords.get(i).getSearchKeyword().isEmpty())
                getKeywords.remove(i);
        }


        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(searchKeyword);
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);
        marketInsightContainerTop.setBackgroundColor(transparantColor);
    }

    public void displayNonGoldMerchant() {
        marketInsightGoldMerchant.setVisibility(View.VISIBLE);
        marketInsightNonGoldMerchant.setVisibility(View.VISIBLE);
        marketInsightEmptyState.setVisibility(View.GONE);


        // init dummy data
        List<GetKeyword.SearchKeyword> searchKeyword = new ArrayList<>();
        for(int i=0;i<3;i++){
            GetKeyword.SearchKeyword searchKeyword1 = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(1000);
            searchKeyword1.setKeyword("kaos");
            searchKeyword.add(searchKeyword1);
        }

        String categoryBold = String.format("\"<i><b>%s</b></i>\"", "kaos");
        marketInsightFooter_.setText(MethodChecker.fromHtml("Kata kunci ini berdasarkan kategori dari "+categoryBold+" "));

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(searchKeyword);
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);
    }

    public void displayEmptyState() {
        marketInsightContainerTop.setBackgroundColor(emptyColorBackground);
        marketInsightGoldMerchant.setVisibility(View.GONE);
        marketInsightNonGoldMerchant.setVisibility(View.GONE);
        marketInsightEmptyState.setVisibility(View.VISIBLE);
    }

    public void bindData(GetShopCategory getShopCategory) {
        if(getShopCategory == null || getShopCategory.getShopCategory() == null || getShopCategory.getShopCategory().isEmpty()){
            if(isGoldMerchant)
                displayEmptyState();
            else{
                displayNonGoldMerchant();
            }
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
                    R.layout.market_insight_item_layout, parent, false);
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

            marketInsightNumber.setText(String.valueOf(searchKeyword.getFrequency()));

            marketInsightKeyword.setText(searchKeyword.getKeyword());
        }
    }
}
