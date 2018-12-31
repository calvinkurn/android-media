package com.tkpd.library.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

/**
 * Created by Tkpd_Eka on 12/22/2014.
 */
public class SimpleListView extends ListView {

    private View LoadingView;
    private View RetryView;
    private View NoResultView;
    private View LoadMore;
    public boolean hasLoading;
    public boolean hasRetry;
    public boolean hasNoRes;
    public boolean hasLoadMore;

    public SimpleListView(Context context) {
        super(context);
        initVariable();
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable();
    }

    public SimpleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariable();
    }

    public SimpleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    @Override
    public void setAdapter(ListAdapter adapter) {
        addFooterView(LoadingView, null, false);
        super.setAdapter(adapter);
        removeFooterView(LoadingView);
    }

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
        CheckAdapter();
        if(!hasFooterHeader()) {
            addHeaderView(LoadingView, null, false);
            hasLoading = true;
        }
    }

    public void addLoadingFooter(){
        CheckAdapter();
        if(!hasFooterHeader()) {
            addFooterView(LoadingView, null, false);
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
            ViewGroup parent = (ViewGroup) this.getParent();
            if(parent.getMeasuredHeight() < parent.getMeasuredWidth()){
                NoResultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, parent.getMeasuredWidth()));
            }else{
                NoResultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, parent.getMeasuredHeight()));
            }
            NoResultView.requestLayout();
            addFooterView(NoResultView, null, false);
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

    private void CheckAdapter(){
        if(getAdapter() == null){
            throw new RuntimeException("Forgot to set the adapter");
        }
    }

}
