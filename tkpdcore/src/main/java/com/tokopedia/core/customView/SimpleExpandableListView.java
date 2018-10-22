package com.tokopedia.core.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

/**
 * Created by hangnadi on 17/02/2015.
 */
public class SimpleExpandableListView extends ExpandableListView {

    private View LoadingView;
    private View RetryView;
    private View NoResultView;
    private View LoadMore;
    public boolean hasLoading;
    public boolean hasRetry;
    public boolean hasNoRes;
    public boolean hasLoadMore;

    public SimpleExpandableListView(Context context) {
        super(context);
        initVariable();
    }

    public SimpleExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable();
    }

    public SimpleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariable();
    }

    public SimpleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVariable();
    }

    private void initVariable(){
        LoadingView = View.inflate(getContext(), R.layout.footer_list_view, null);
        RetryView = View.inflate(getContext(), R.layout.footer_retry_network, null);
        NoResultView = View.inflate(getContext(), R.layout.view_no_result, null);
        ImageHandler.loadImageWithId(((ImageView) NoResultView.findViewById(R.id.no_result_image)), R.drawable.status_no_result);
        LoadMore = View.inflate(getContext(), R.layout.header_refresh_button, null);
    }

    public void setAdapter(BaseExpandableListAdapter adapter) {
        addLoadingFooter();
        super.setAdapter(adapter);
        removeLoading();
    }

    /*
        Author's note: Tidak berguna, hanya sebagai referensi untuk melakukan lazy load pada onScrollListener
        public boolean isLazyLoad(int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if( firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0){
            return true;
        }
        else
            return false;
    }*/

    public void addRetry(){
        if(!hasFooterHeader()){
            addFooterView(RetryView);
            hasRetry = true;
        }
    }

    public void removeRetry(){
        if(hasRetry){
            removeFooterView(RetryView);
            hasRetry = false;
        }
    }

    public void addLoadMoreHeader(){
        if(!hasFooterHeader()){
            addHeaderView(LoadMore);
            hasLoadMore = true;
        }
    }

    public void setLoadListener(OnClickListener listener){
        LoadMore.setOnClickListener(listener);
    }

    public void removeLoadMore(){
        if(hasLoadMore){
            removeHeaderView(LoadMore);
            hasLoadMore = false;
        }
    }

    public void addLoadingHeader(){
        if(!hasFooterHeader()) {
            addHeaderView(LoadingView);
            hasLoading = true;
        }
    }

    public void addLoadingFooter(){
        if(!hasFooterHeader()) {
            addFooterView(LoadingView);
            hasLoading = true;
        }
    }

    public void removeLoading(){
        if(hasLoading){
            removeFooterView(LoadingView);
            removeHeaderView(LoadingView);
            hasLoading = false;
        }
    }

    public void addNoResult(){
        if(!hasFooterHeader()){
            addFooterView(NoResultView);
            hasNoRes = true;
        }
    }

    public void removeNoResult(){
        if(hasNoRes){
            removeFooterView(NoResultView);
            hasNoRes = false;
        }
    }

    private boolean hasFooterHeader(){
        if(hasLoading || hasRetry || hasNoRes || hasLoadMore)
            return true;
        else
            return false;
    }
}
