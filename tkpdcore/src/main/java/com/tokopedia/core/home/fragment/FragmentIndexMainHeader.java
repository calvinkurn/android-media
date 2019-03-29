package com.tokopedia.core.home.fragment;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.home.adapter.HistoryProductRecyclerViewAdapter;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

/**
 * Created by m.normansyah on 22/10/2015.
 * 1. builder pattern
 * 2. step checking
 * modified by m.normansyah on 06/01/2015 - set item id to distinct items
 */
public class FragmentIndexMainHeader {
    // view
    private View parentView;
    private HistoryProductRecyclerViewAdapter adapter;
    TextView title;
    TextView seeAll;
    TextView findNow;
    RecyclerView recyclerView;
    RelativeLayout emptyLayout;
    RelativeLayout emptyHistory;
    TextView findFavoriteShop;
    View officialStoreLinkContainer;

    // data
    private Data data;

    // checking itself
    int step;
    private static final int STEP_1 = 1, STEP_2 = 2, STEP_3 = 3, STEP_4 = 4;

    public FragmentIndexMainHeader(){
        data = new Data();
        step = 0;
    }

    /**
     * step one
     * @param inflater inflater for view
     * @return
     */
    @Deprecated
    public FragmentIndexMainHeader addParentView(@NonNull LayoutInflater inflater){
        if(++step!=STEP_1)
            throw new RuntimeException("new FragmentIndexMainHeader()");

        this.parentView = inflater.inflate(R.layout.child_main_history_product, null);
        return this;
    }

    public FragmentIndexMainHeader addParentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parentView){
        if(++step!=STEP_1)
            throw new RuntimeException("new FragmentIndexMainHeader()");

        this.parentView = inflater.inflate(R.layout.child_main_history_product, parentView, false);
        return this;
    }

    /**
     * step 2
     * @param layoutId valid layout id
     * @return
     */
    public FragmentIndexMainHeader initView(@NonNull @IdRes int layoutId){
        if(++step!=STEP_2)
            throw new RuntimeException("addParentView(LayoutInflater inflater)");

        // get static view
        seeAll = (TextView) parentView.findViewById(R.id.textview_see_all);
        emptyLayout = (RelativeLayout) parentView.findViewById(R.id.empty_layout);
        emptyHistory = (RelativeLayout) parentView.findViewById(R.id.empty_history);
        findNow = (TextView) parentView.findViewById(R.id.find_now);
        findFavoriteShop = (TextView) parentView.findViewById(R.id.find_favorite_shop);
        officialStoreLinkContainer = parentView.findViewById(R.id.official_store_link_container);
        title = (TextView) parentView.findViewById(R.id.title);

        // get recycler view
        recyclerView = (RecyclerView) parentView.findViewById(layoutId);

        // create horizontal layout manager and set up its properties
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(parentView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        return this;
    }

    public FragmentIndexMainHeader addData(List<ProductItem> listProduct) {
        if(++step!=STEP_3)
            throw new RuntimeException("initView(int layoutId)");
        this.data.setListProduct(listProduct);
        return this;
    }

    public FragmentIndexMainHeader createAdapter(HistoryProductRecyclerViewAdapter historyProductRecyclerViewAdapter){
        if(++step!=STEP_4 || historyProductRecyclerViewAdapter == null)
            throw new RuntimeException("addData(List<ProductItem> listProduct)");
        this.adapter = historyProductRecyclerViewAdapter;
        this.recyclerView.setAdapter(this.adapter);
        return this;
    }

    public FragmentIndexMainHeader createAdapter(){
        if(++step!=STEP_4)
            throw new RuntimeException("addData(List<ProductItem> listProduct)");
        this.adapter = new HistoryProductRecyclerViewAdapter(parentView.getContext(), data.getListProduct());
        this.recyclerView.setAdapter(this.adapter);
        return this;
    }


    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public HistoryProductRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(HistoryProductRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getSeeAll() {
        return seeAll;
    }

    public void setSeeAll(TextView seeAll) {
        this.seeAll = seeAll;
    }

    public TextView getFindNow() {
        return findNow;
    }

    public void setFindNow(TextView findNow) {
        this.findNow = findNow;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RelativeLayout getEmptyLayout() {
        return emptyLayout;
    }

    public void setEmptyLayout(RelativeLayout emptyLayout) {
        this.emptyLayout = emptyLayout;
    }

    public RelativeLayout getEmptyHistory() {
        return emptyHistory;
    }

    public void setEmptyHistory(RelativeLayout emptyHistory) {
        this.emptyHistory = emptyHistory;
    }

    private class Data{
        private List<ProductItem> listProduct;

        public List<ProductItem> getListProduct() {
            return listProduct;
        }

        public void setListProduct(List<ProductItem> listProduct) {
            this.listProduct = listProduct;
        }
    }

    public TextView getFindFavoriteShop() {
        return findFavoriteShop;
    }

    public void setFindFavoriteShop(TextView findFavoriteShop) {
        this.findFavoriteShop = findFavoriteShop;
    }

    public View getOfficialStoreLinkContainer() {
        return officialStoreLinkContainer;
    }
}
