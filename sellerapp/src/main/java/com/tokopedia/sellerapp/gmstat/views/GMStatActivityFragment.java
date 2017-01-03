package com.tokopedia.sellerapp.gmstat.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.chart.renderer.StringFormatRenderer;
import com.db.chart.renderer.XRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.view.LineChartView;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.library.LoaderImageView;
import com.tokopedia.sellerapp.gmstat.library.LoaderTextView;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;
import com.tokopedia.sellerapp.gmstat.models.GetPopularProduct;
import com.tokopedia.sellerapp.gmstat.models.GetProductGraph;
import com.tokopedia.sellerapp.gmstat.models.GetShopCategory;
import com.tokopedia.sellerapp.gmstat.models.GetTransactionGraph;
import com.tokopedia.sellerapp.gmstat.presenters.GMFragmentPresenterImpl;
import com.tokopedia.sellerapp.gmstat.presenters.GMFragmentView;
import com.tokopedia.sellerapp.gmstat.presenters.GMStat;
import com.tokopedia.sellerapp.gmstat.utils.GridDividerItemDecoration;
import com.tokopedia.sellerapp.gmstat.utils.KMNumbers;
import com.tokopedia.sellerapp.gmstat.utils.GrossGraphChartConfig;
import com.tokopedia.sellerapp.home.utils.ShopNetworkController;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.sellerapp.gmstat.views.DataTransactionViewHelper.dpToPx;
import static com.tokopedia.sellerapp.gmstat.views.GMStatHeaderViewHelper.getDates;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatActivityFragment extends Fragment implements GMFragmentView {

    public static final double NoDataAvailable = -2147483600;
    private static final String TAG = "GMStatActivityFragment";


    @BindArray(R.array.month_names_abrev)
    String[] monthNamesAbrev;

//    @BindArray(R.array.month_names)
//    String[] monthNames;

    @BindView(R.id.gross_income_graph2)
    LineChartView grossIncomeGraph2;

    @BindView(R.id.gmstat_recyclerview)
    RecyclerView gmStatRecyclerView;
    GMStatWidgetAdapter gmStatWidgetAdapter;

    @BindView(R.id.gross_income_graph2_loading)
    LoaderImageView grossIncomeGraph2Loading;

    @BindView(R.id.popular_product)
    View popularProduct;

    @BindView(R.id.transaction_data)
    View transactionData;

    @BindView(R.id.buyer_data)
    View marketInsight;

    @BindView(R.id.market_insight)
    View marketInsightReal;

    @BindView(R.id.parent_fragment_gmstat)
    LinearLayout parentFragmentGmStat;

    @BindView(R.id.gross_income_graph_container)
    HorizontalScrollView grossIncomeGraphContainer;

    @BindView(R.id.gross_income_graph_container2)
    LinearLayout grossIncomeGraphContainer2;

    @BindDrawable(R.drawable.oval_2_copy_6)
    Drawable oval2Copy6;

    private GridLayoutManager gridLayoutManager;
    private MarketInsightViewHelper marketInsightViewHelper;
    private PopularProductLoading popularProductLoading;
    private TransactionDataLoading transactionDataLoading;
    private BuyerDataLoading buyerDataLoading;
    private MarketInsightLoading marketInsightLoading;
    PopularProductViewHelper popularProductViewHelper;
    private View rootView;

    private GMFragmentPresenterImpl gmFragmentPresenter;

    @OnClick(R.id.header_gmstat)
    public void onClickHeaderGMStat(){
        if(gmstatHeaderViewHelper!=null){
            gmstatHeaderViewHelper.onClick(this);
        }
    }
    private GMStat gmstat;

    private Unbinder unbind;
    private DataTransactionViewHelper dataTransactionViewHelper;
    private BuyerDataViewHelper buyerDataViewHelper;
    private GMStatHeaderViewHelper gmstatHeaderViewHelper;
    private GrossGraphChartConfig grossGraphChartConfig;
    private SnackBar snackBar;

    private List<NExcel> joinDateAndGrossGraph(List<Integer> dateGraph, List<Integer> grossGraph){
        List<NExcel> nExcels = new ArrayList<>();
        if(dateGraph == null || grossGraph == null || dateGraph.isEmpty() || grossGraph.isEmpty())
            return null;

        int lowerSize;
        if(dateGraph.size()>grossGraph.size()){
            lowerSize = grossGraph.size();
        }else{
            lowerSize = dateGraph.size();
        }

        for(int i=0;i<lowerSize;i++){
            Integer date = dateGraph.get(i);
            Integer gross = grossGraph.get(i);

            nExcels.add(new NExcel(gross, getDate(date)));
        }

        return nExcels;
    }

    public static String getDateWithYear(int date, String[] monthNames){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month)-1];

        String day = dateRaw.get(0);
        Log.d(TAG, "bulan "+month+" tanggal "+day+" rawDate "+date);

        return day + " "+ month+" "+year;
    }

    public static String getDateWithYear(String date, String[] monthNames){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month)-1];

        String day = dateRaw.get(0);
        Log.d(TAG, "bulan "+month+" tanggal "+day+" rawDate "+date);

        return day + " "+ month+" "+year;
    }

    private static String getDate(Integer date){
        List<String> dateRaw = getDateRaw(date);
        String month = dateRaw.get(1);
        String day = dateRaw.get(0);
        Log.d(TAG, "bulan "+month+" tanggal "+day);

        return day + " "+ month;
    }

    public static String getDateRaw(String label , String[] monthNames){
        String[] split = label.split(" ");
        return split[0]+" "+monthNames[Integer.parseInt(split[1])-1];
    }

    private static List<String> getDateRaw(String s){
        List<String> result = new ArrayList<>();
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        Log.d(TAG, "getDateRaw : "+s+ " day "+day+" int "+s);
        result.add(day);result.add(month);result.add(year);
        return result;
    }

    private static List<String> getDateRaw(int date){
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        Log.d(TAG, "getDateRaw : "+s+ " day "+day+" int "+date);
        result.add(day);result.add(month);result.add(year);
        return result;
    }

    protected void initAdapter() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        GridDividerItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(getContext());
        gmStatRecyclerView.addItemDecoration(gridDividerItemDecoration);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)){
                    case SuccessfulTransaction.TYPE:
                    case ProdSeen.TYPE:
                    case ProdSold.TYPE:
                    case ConvRate.TYPE:
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    protected void initAdapter(final GMStatWidgetAdapter gmStatWidgetAdapter) {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)){
                    case SuccessfulTransaction.TYPE:
                    case ProdSeen.TYPE:
                    case ProdSold.TYPE:
                    case ConvRate.TYPE:
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    @Override
    public void resetToLoading(){
        resetEmptyAdapter();
        gmstatHeaderViewHelper.resetToLoading();
        initChartLoading();
        initPopularLoading();
        initTransactionDataLoading();
        initMarketInsightLoading();
        initMarketInsightLoading2();
    }

    @Override
    public void bindHeader(long sDate, long eDate) {
        gmstatHeaderViewHelper.bindDate(sDate, eDate);
    }

    /**
     * reset 4 box to loading state
     */
    private void resetEmptyAdapter() {
        gmStatWidgetAdapter.clear();

        List<BaseGMModel> loadingBases = new ArrayList<>();
        for(int i=0;i<4;i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());
        gmStatWidgetAdapter.addAll(loadingBases);
        gmStatWidgetAdapter.notifyDataSetChanged();
    }

    protected void initEmptyAdapter(){
        List<BaseGMModel> loadingBases = new ArrayList<>();
        for(int i=0;i<4;i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());

        gmStatWidgetAdapter = new GMStatWidgetAdapter(loadingBases, gmstat);
        initAdapter();
    }

//    private final long shopId = 560900;
    private long shopId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null && context instanceof GMStat){
            this.gmstat = (GMStat) context;

            // get shop id
            try {
                shopId = Long.parseLong(gmstat.getShopId());
            }catch (NumberFormatException nfe){
                throw new RuntimeException(nfe.getMessage()+"\n [need valid shop id]");
            }

            gmFragmentPresenter = new GMFragmentPresenterImpl(this, gmstat, shopId);
        }
    }

    public GMStatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gmFragmentPresenter.setFirstTime(false);
        rootView = inflater.inflate(R.layout.fragment_gmstat, container, false);
        this.unbind = ButterKnife.bind(this, rootView);
        initNumberFormatter();
        initEmptyAdapter();
        initChartLoading();
        popularProductViewHelper = new PopularProductViewHelper(rootView);
        dataTransactionViewHelper = new DataTransactionViewHelper(rootView, gmstat.getImageHandler(), gmstat.isGoldMerchant());
        buyerDataViewHelper = new BuyerDataViewHelper(rootView);
        gmstatHeaderViewHelper = new GMStatHeaderViewHelper(rootView, gmstat.isGoldMerchant());
        marketInsightViewHelper = new MarketInsightViewHelper(rootView, gmstat.isGoldMerchant());
        popularProductLoading = new PopularProductLoading(rootView);
        transactionDataLoading = new TransactionDataLoading(rootView);
        buyerDataLoading = new BuyerDataLoading(rootView);
        marketInsightLoading = new MarketInsightLoading(rootView);
        initPopularLoading();
        initTransactionDataLoading();
        initMarketInsightLoading();
        initMarketInsightLoading2();
        gmFragmentPresenter.initInstance();
        grossGraphChartConfig = new GrossGraphChartConfig(
                gmFragmentPresenter.getmLabels(), gmFragmentPresenter.getmValues());
        return rootView;
    }

    private void initNumberFormatter() {
        KMNumbers.overrideSuffixes(1000000L, "jt");
    }

    private void initMarketInsightLoading2() {
        marketInsightLoading.displayLoading();
        marketInsightReal.setVisibility(View.GONE);
    }

    private void initMarketInsightLoading() {
        buyerDataLoading.displayLoading();
        marketInsight.setVisibility(View.GONE);
    }

    private void initTransactionDataLoading() {
        transactionDataLoading.displayLoading();
        transactionData.setVisibility(View.GONE);
    }

    private void initPopularLoading() {
        popularProductLoading.displayLoading();
        popularProduct.setVisibility(View.GONE);
    }

    private void initChartLoading() {
        grossIncomeGraphContainer.setVisibility(View.GONE);
        grossIncomeGraph2Loading.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.resetLoader();
    }

    private void displayChart(){
        grossIncomeGraphContainer.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.setVisibility(View.GONE);
    }

    /**
     * limitation of william chart ( for big width it cannot draw, effectively for size of 15 )
     * https://github.com/diogobernardino/WilliamChart/issues/152
     * @param numChart
     */
    private void resizeChart(int numChart){
        Log.d(TAG, "resizeChart "+numChart);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) dpToPx(getActivity(), 360); //displaymetrics.widthPixels;
        /*
            set only 8 values in  Window width rest are on sroll or dynamically change the width of linechart
            is  window width/8 * total values returns you the total width of linechart with scrolling and set it in
            layout Params of linechart .
        */
        double newSizeRatio = ((double) numChart) / 7;
        if(newSizeRatio > 1){
            grossIncomeGraph2.setLayoutParams(new LinearLayout.LayoutParams((int) dpToPx(getActivity(), 680),grossIncomeGraph2.getLayoutParams().height));//(int) (newSizeRatio * width / 2)
        } else {
            grossIncomeGraph2.setLayoutParams(new LinearLayout.LayoutParams(width, grossIncomeGraph2.getLayoutParams().height));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gmFragmentPresenter.onResume();
        fetchData();
    }

    public void displayDefaultValue(){
        gmFragmentPresenter.displayDefaultValue(getActivity().getAssets());
    }

    @Override
    public void fetchData() {
        gmFragmentPresenter.fetchData();
    }

    @Override
    public void fetchData(long sDate, long eDate){
        gmFragmentPresenter.fetchData(sDate, eDate);
    }

    @Override
    public void onPause() {
        super.onPause();
        snackBar = null;
        gmFragmentPresenter.onPause();
        if(grossIncomeGraph2 != null)
            grossIncomeGraph2.dismissAllTooltips();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
        marketInsightReal.setVisibility(View.VISIBLE);
        marketInsightLoading.hideLoading();
        marketInsightViewHelper.bindData(getShopCategory);
    }

    @Override
    public void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph, long sDate, long eDate) {
        GrossIncome grossIncome = new GrossIncome(getTransactionGraph.getGrossRevenue());
        List<BaseGMModel> baseGMModels = new ArrayList<>();
        baseGMModels.add(grossIncome);

        List<Integer> dateGraph = getTransactionGraph.getDateGraph();
        List<String> dates = getDates(dateGraph, GMStatActivityFragment.this.monthNamesAbrev);
        if(dates != null) {
            grossIncome.textDescription = dates.get(0)+" - "+dates.get(1);
        }
        List<Integer> grossGraph = getTransactionGraph.getGrossGraph();
        List<NExcel> nExcels = joinDateAndGrossGraph(dateGraph, grossGraph);

        if(nExcels != null){
            //[]START] try used willam chart
            displayChart();
            resizeChart(nExcels.size());
            int i = 0;
            String[] mLabels = new String[nExcels.size()];
            final float[] mValues = new float[nExcels.size()];
            for (NExcel nExcel : nExcels) {
                mLabels[i] = getDateRaw(nExcel.getXmsg(), monthNamesAbrev);
                mValues[i] = nExcel.getUpper();
                i++;
            }

            final List<Integer> indexToDisplay = new ArrayList<>();
            int divide = mValues.length/10;
            for(int j=1;j<=divide-1;j++){
                indexToDisplay.add((j*10)-1);
            }
            grossGraphChartConfig
                    .setmLabels(mLabels)
                    .setmValues(mValues, new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0L) int i) {
                            if(i==0 || mValues.length-1 == i)
                                return true;

                            if(mValues.length <= 15){
                                return true;
                            }

                            return indexToDisplay.contains(i);

                        }
                    })
                    .setDotDrawable(oval2Copy6)
                    .setTooltip(new Tooltip(getContext(),
                            R.layout.gm_stat_tooltip,
                            R.id.gm_stat_tooltip_textview,
                            new StringFormatRenderer() {
                                @Override
                                public String formatString(String s) {
                                    return KMNumbers.formatNumbers(Float.valueOf(s));
                                }
                            }))
                    .buildChart(grossGraphChartConfig.buildLineChart(grossIncomeGraph2));
            //[END] try used willam chart
        }


        gmStatWidgetAdapter.addAll(baseGMModels);
        gmStatWidgetAdapter.notifyDataSetChanged();

        dataTransactionViewHelper.bindData(getTransactionGraph);
        transactionDataLoading.hideLoading();
        transactionData.setVisibility(View.VISIBLE);

        if(sDate == -1 && eDate == -1)
            gmstatHeaderViewHelper.bindData(dateGraph);
        else {
            gmstatHeaderViewHelper.bindDate(sDate, eDate);
            gmstatHeaderViewHelper.stopLoading();
        }
    }

    @Override
    public void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime) {
        List<BaseGMModel> baseGMModels = new ArrayList<>();
        SuccessfulTransaction successfulTransaction
                = new SuccessfulTransaction(getProductGraph.getSuccessTrans());
        successfulTransaction.percentage = getProductGraph.getDiffTrans()*100;

        ProdSeen prodSeen = new ProdSeen(getProductGraph.getProductView());
        prodSeen.percentage = getProductGraph.getDiffView()*100;

        ProdSold prodSold = new ProdSold(getProductGraph.getProductSold());
        prodSold.percentage = getProductGraph.getDiffSold()*100;

        ConvRate convRate = new ConvRate(getProductGraph.getConversionRate()*100);
        convRate.percentage = getProductGraph.getDiffConv()*100;

        baseGMModels.add(successfulTransaction);
        baseGMModels.add(prodSeen);
        baseGMModels.add(prodSold);
        baseGMModels.add(convRate);
        gmStatWidgetAdapter.clear();
        gmStatWidgetAdapter.addAll(baseGMModels);

        if(!isFirstTime) {
            initAdapter(gmStatWidgetAdapter);
            gmFragmentPresenter.setFirstTime(true);
        }
    }

    @Override
    public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
        popularProductViewHelper.bindData(getPopularProduct, gmstat.getImageHandler());
        popularProductLoading.hideLoading();
        popularProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessBuyerData(GetBuyerData getBuyerData) {
        buyerDataViewHelper.bindData(getBuyerData);
        marketInsight.setVisibility(View.VISIBLE);
        buyerDataLoading.hideLoading();
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        marketInsightViewHelper.bindData(getKeywords);
        marketInsightReal.setVisibility(View.VISIBLE);
        marketInsightLoading.hideLoading();
    }

    @Override
    public void onSuccessGetCategory(List<HadesV1Model> hadesV1Models) {
        marketInsightViewHelper.bindDataCategory(hadesV1Models);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        displayDefaultValue();

        snackBar = new SnackBar();
        String textMessage ="Kesalahan tidak diketahui";
        if(e instanceof UnknownHostException){
            textMessage = "Tidak ada koneksi. \nSilahkan coba kembali";
        }else if(e instanceof ShopNetworkController.MessageErrorException){
            textMessage = "Terjadi kesalahan koneksi. \nSilahkan coba kembali";
        }
        snackBar.view(rootView)
                .text(textMessage, "COBA KEMBALI")
                .textColors(Color.WHITE,Color.GREEN)
                .backgroundColor(Color.BLACK)
                .duration(SnackBar.SnackBarDuration.INDEFINITE)
                .setOnClickListener(true, new OnActionClickListener() {
                    @Override
                    public void onClick(View view) {
                        gmFragmentPresenter.setFetchData(true);
                        gmFragmentPresenter.fetchData();
                    }
                })
                .show();
    }

    @Override
    public void onFailure() {

    }

    private static class GMStatWidgetAdapter extends RecyclerView.Adapter{

        List<BaseGMModel> baseGMModels;
        GMStat gmStat;

        public GMStatWidgetAdapter() {
            baseGMModels = new ArrayList<>();
        }

        public GMStatWidgetAdapter(List<BaseGMModel> baseGMModels, GMStat gmStat){
            this.baseGMModels = baseGMModels;
            this.gmStat = gmStat;
        }

        public void clear(){
            this.baseGMModels.clear();
        }

        public void addAll(List<BaseGMModel> baseGMModels){
            if(baseGMModels == null)
                return;
            this.baseGMModels.addAll(baseGMModels);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case SuccessfulTransaction.TYPE:
                case ProdSeen.TYPE:
                case ProdSold.TYPE:
                case ConvRate.TYPE:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gmstat, parent, false);
                    return new CommonGMVH(view);
                case GrossIncome.TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn, parent, false);
                    return new GrossEarnVH(view);
                case LoadingGMModel.TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gmstat_loading, parent, false);
                    return new LoadingGM(view);
                case LoadingGMTwoModel.TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn_loading, parent, false);
                    return new LoadingGMGrossIncome(view);
            }
            return new EmptyVH(new ImageView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (baseGMModels.get(position).type) {
                case SuccessfulTransaction.TYPE:
                case ProdSeen.TYPE:
                case ProdSold.TYPE:
                case ConvRate.TYPE:
                    CommonGMVH commonGMVH = ((CommonGMVH)holder);
                    commonGMVH.gmStat = gmStat;
                    commonGMVH.bind((CommomGMModel) baseGMModels.get(position));
                    break;
                case GrossIncome.TYPE:
                    ((GrossEarnVH)holder).bind((GrossIncome) baseGMModels.get(position));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return baseGMModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            switch (baseGMModels.get(position).type){
                case SuccessfulTransaction.TYPE:
                case ProdSeen.TYPE:
                case ProdSold.TYPE:
                case GrossIncome.TYPE:
                case ConvRate.TYPE:
                case LoadingGMModel.TYPE:
                case LoadingGMTwoModel.TYPE:
                    return baseGMModels.get(position).type;
                default:
                    return super.getItemViewType(position);
            }
        }
    }

    public static class EmptyVH extends RecyclerView.ViewHolder{

        public EmptyVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class CommonGMVH extends RecyclerView.ViewHolder{

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.textDescription)
        TextView textDescription;

        @BindView(R.id.percentage)
        TextView percentage;

        @BindView(R.id.arrow_icon)
        ImageView arrowIcon;

        @BindColor(R.color.arrow_down)
        int arrowDown;

        @BindColor(R.color.arrow_up)
        int arrowUp;

        @BindColor(R.color.grey_400)
        int gredyColor;

//        @BindDrawable(R.drawable.ic_rectangle_down)
        Drawable icRectagleDown;

//        @BindDrawable(R.drawable.ic_rectangle_up)
        Drawable icRectagleUp;

        public GMStat gmStat;

        public CommonGMVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                    R.drawable.ic_rectangle_down);
            icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                    R.drawable.ic_rectangle_up);
        }

        public void bind(CommomGMModel commomGMModel){
            text.setText(commomGMModel.text);
            textDescription.setText(commomGMModel.textDescription);


            // image for arrow is here
            boolean isDefault;
            if(commomGMModel.percentage == 0){
                arrowIcon.setVisibility(View.GONE);
                percentage.setTextColor(arrowUp);
                isDefault = true;
            }else if(commomGMModel.percentage < 0){// down here
                if(commomGMModel.percentage == NoDataAvailable*100){
                    arrowIcon.setVisibility(View.GONE);
                    percentage.setTextColor(gredyColor);
                    isDefault = false;
                }else{
                    arrowIcon.setVisibility(View.VISIBLE);
                    arrowIcon.setImageDrawable(icRectagleDown);
//                gmStat.getImageHandler().loadImage(arrowIcon, R.mipmap.arrow_down_percentage);
                    percentage.setTextColor(arrowDown);
                    isDefault = true;
                }
            }else{// up here
                arrowIcon.setVisibility(View.VISIBLE);
                arrowIcon.setImageDrawable(icRectagleUp);
//                gmStat.getImageHandler().loadImage(arrowIcon, R.mipmap.arrow_up_percentage);
                percentage.setTextColor(arrowUp);
                isDefault = true;
            }

            if(isDefault) {
                DecimalFormat formatter = new DecimalFormat("#0.00");
                double d = commomGMModel.percentage;
                String text;
                System.out.println(text = formatter.format(d));
                percentage.setText(String.format("%s%%", text.replace("-", "")));
            }else{
                percentage.setText(R.string.no_data);
            }
        }
    }

    public static class GrossEarnVH extends RecyclerView.ViewHolder{

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.textDescription)
        TextView textDescription;

        @BindView(R.id.dot)
        ImageView dot;

        public GrossEarnVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GrossIncome grossIncome){
            text.setText(grossIncome.text);
            textDescription.setText(grossIncome.textDescription);
        }
    }

    public static class LoadingGMGrossIncome extends RecyclerView.ViewHolder{
        @BindView(R.id.grossIncomeHeader)
        LoaderTextView grossIncomeHeader;
        @BindView(R.id.text)
        LoaderTextView text;
        @BindView(R.id.dot)
        LoaderImageView dot;
        @BindView(R.id.textDescription)
        LoaderTextView textDescription;

        public LoadingGMGrossIncome(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            grossIncomeHeader.resetLoader();
            text.resetLoader();
            dot.resetLoader();
            textDescription.resetLoader();
        }
    }

    public static class LoadingGM extends RecyclerView.ViewHolder{

        @BindView(R.id.textDescription)
        LoaderTextView textDescription;

        @BindView(R.id.text)
        LoaderTextView text;

        @BindView(R.id.no_data_text)
        LoaderTextView noDataText;

        public LoadingGM(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            textDescription.resetLoader();
            text.resetLoader();
            noDataText.resetLoader();
        }
    }

    private static class BaseGMModel{
        protected int type;
    }

    private static class CommomGMModel extends BaseGMModel{
        String text;
        String textDescription;
        double percentage;
    }

    private static class LoadingGMModel extends CommomGMModel{
        private static final int TYPE = 12812412;

        public LoadingGMModel(){
            type = TYPE;
        }
    }

    private static class LoadingGMTwoModel extends CommomGMModel{
        private static final int TYPE = 12891922;

        public LoadingGMTwoModel(){
            type = TYPE;
        }
    }

    private static class SuccessfulTransaction extends CommomGMModel{
        private static final int TYPE = 1282912;
        private long successTrans;

        /**
         * convert long to K format ( not suitable for million )
         * @param successTrans
         */
        public SuccessfulTransaction(long successTrans) {
            type = TYPE;
            this.successTrans = successTrans;
            if(successTrans < 1_000_000){
                Locale locale = new Locale("in", "ID");
                NumberFormat currencyFormatter = NumberFormat.getNumberInstance(locale);
                System.out.println(text = (currencyFormatter.format(successTrans)));
//                text = successTrans+"";
            }else if(successTrans >= 1_000_000){
                text = KMNumbers.formatNumbers(successTrans);
            }
            //[START] This is obsolete
//            double l = successTrans / 1000D;
//            text = Double.toString(l)+"K";
            //[END] This is obsolete
            textDescription = "Transaksi Berhasil";
        }
    }

    public static String toKFormat(long input){
        double l = input / 1000D;
        return Double.toString(l)+"K";
    }

    private static class ProdSeen extends SuccessfulTransaction{
        private static final int TYPE = 121231;

        public ProdSeen(long successTrans) {
            super(successTrans);
            type = TYPE;
            textDescription = "Produk Dilihat";
        }
    }

    private static class ProdSold extends SuccessfulTransaction{
        private static final int TYPE = 1219231;

        public ProdSold(long successTrans) {
            super(successTrans);
            type = TYPE;
            textDescription = "Produk Terjual";
        }
    }

    private static class GrossIncome extends SuccessfulTransaction{
        private static final int TYPE = 1219281;

        public GrossIncome(long successTrans) {
            super(successTrans);
            type = TYPE;
            text = "Rp "+text;
            textDescription="Pendapatan Kotor";
        }
    }

    private static class ConvRate extends SuccessfulTransaction{

        private static final int TYPE = 121121291;
        private double convRate;

        /**
         * @param convRate multiple with 100 to get the percentage
         */
        public ConvRate(double convRate){
            super(0);
            type = TYPE;
            this.convRate = convRate*100;
            NumberFormat formatter = new DecimalFormat("#0.00");
            text = formatter.format(convRate)+"%";
            //[START] obsolete things
//            text = convRate+"%";
            //[END] obsolete things
            textDescription = "Tingkat Konversi";
        }

        private ConvRate(long successTrans) {
            super(successTrans);
        }
    }


    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static class PopularProductLoading {

        @BindView(R.id.popular_product_description_loading)
        LoaderTextView popularProductDescription;

        @BindView(R.id.text_popular_product_loading)
        LoaderTextView textPopularProduct;

        @BindView(R.id.image_popular_product_loading)
        LoaderImageView imagePopularProduct;

        @BindView(R.id.data_product_title_loading)
        LoaderTextView dataProductTitle;

        @BindView(R.id.data_product_icon_loading)
        LoaderImageView dataProductIcon;

        View parentView;

        public PopularProductLoading(View itemView){
            ButterKnife.bind(this, itemView);

            parentView = itemView.findViewById(R.id.popular_product_loading);

            popularProductDescription.resetLoader();
            textPopularProduct.resetLoader();
            imagePopularProduct.resetLoader();
            dataProductTitle.resetLoader();
            dataProductIcon.resetLoader();
        }

        public void displayLoading() {
            parentView.setVisibility(View.VISIBLE);
        }

        public void hideLoading(){
            parentView.setVisibility(View.GONE);
        }
    }

    public static class TransactionDataLoading{
        private final View parentView;

        @BindView(R.id.data_transaction_header_loading_ic)
        LoaderImageView dataTransactionHeaderLoadingIc;

        @BindView(R.id.data_transaction_header_loading_text)
        LoaderTextView dataTransactionHeaderLoadingText;

        @BindView(R.id.buyer_number_header_loading)
        LoaderTextView buyerNumberHeaderLoading;

        @BindView(R.id.transaction_count_loading)
        LoaderTextView transactionCountLoading;

        @BindView(R.id.transaction_count_icon_loading)
        LoaderImageView transactionCountIconLoading;

        @BindView(R.id.transaction_chart_loading)
        LoaderImageView trasactionChartLoading;

        public TransactionDataLoading(View itemView){
            ButterKnife.bind(this, itemView);

            parentView = itemView.findViewById(R.id.transaction_data_loading);

            dataTransactionHeaderLoadingIc.resetLoader();
            dataTransactionHeaderLoadingText.resetLoader();
            buyerNumberHeaderLoading.resetLoader();
            transactionCountLoading.resetLoader();
            transactionCountIconLoading.resetLoader();
            trasactionChartLoading.resetLoader();
        }

        public void displayLoading() {
            parentView.setVisibility(View.VISIBLE);
        }

        public void hideLoading(){
            parentView.setVisibility(View.GONE);
        }
    }

    public static class BuyerDataLoading {
        private final View parentView;

        @BindView(R.id.buyer_data_header_ic)
        LoaderImageView buyerdataHeaderIc;

        @BindView(R.id.buyer_data_header_text)
        LoaderTextView buyerDataHeaderText;

        @BindView(R.id.buyer_number_header_loading)
        LoaderTextView buyerNumberHeaderLoading;

        @BindView(R.id.buyer_count_loading)
        LoaderTextView buyerCountLoading;

        @BindView(R.id.buyer_count_icon_loading)
        LoaderImageView buyerCountIconLoading;

        @BindView(R.id.data_buyer_loading)
        LoaderImageView dataBuyerLoading;


        public BuyerDataLoading(View itemView){
            ButterKnife.bind(this, itemView);

            parentView = itemView.findViewById(R.id.buyer_data_loading);

            buyerdataHeaderIc.resetLoader();
            buyerDataHeaderText.resetLoader();
            buyerNumberHeaderLoading.resetLoader();
            buyerCountLoading.resetLoader();
            buyerCountIconLoading.resetLoader();
            dataBuyerLoading.resetLoader();
        }

        public void displayLoading() {
            parentView.setVisibility(View.VISIBLE);
        }

        public void hideLoading(){
            parentView.setVisibility(View.GONE);
        }
    }

    public static class MarketInsightLoading {
        @BindView(R.id.market_insight_header_ic)
        LoaderImageView marketInsightHeaderIc;

        @BindView(R.id.market_insight_header_text)
        LoaderTextView marketInsightHeaderText;

        @BindView(R.id.market_insight_header_loading)
        LoaderTextView marketInsightHeaderLoading;

        @BindView(R.id.market_insight_loading_recyclerview)
        RecyclerView marketInsightLoadingRec;

        private final View parentView;

        public MarketInsightLoading(View itemView){
            ButterKnife.bind(this, itemView);

            parentView = itemView.findViewById(R.id.market_insight_loading);

            marketInsightHeaderIc.resetLoader();
            marketInsightHeaderText.resetLoader();
            marketInsightHeaderLoading.resetLoader();

            marketInsightLoadingRec.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            marketInsightLoadingRec.setAdapter(new MarketInsightLoadingAdapter());
        }

        public void displayLoading() {
            parentView.setVisibility(View.VISIBLE);
        }

        public void hideLoading(){
            parentView.setVisibility(View.GONE);
        }
    }

    public static class MarketInsightLoadingAdapter extends RecyclerView.Adapter{

        public MarketInsightLoadingAdapter(){
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.market_insight_item_layout_loading, parent, false);
            return new ViewHolder3(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // do nothing
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ViewHolder3 extends RecyclerView.ViewHolder{

            @BindView(R.id.market_insight_keyword_loading)
            LoaderTextView marketInsightKeywordLoading;

            @BindView(R.id.market_insight_number_loading)
            LoaderTextView marketInsightNumberLoading;

            public ViewHolder3(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                marketInsightKeywordLoading.resetLoader();
                marketInsightNumberLoading.resetLoader();
            }
        }
    }

}
