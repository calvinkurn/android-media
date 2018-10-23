package com.tokopedia.core.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
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
    private View CustomView;
    public boolean hasLoading;
    public boolean hasRetry;
    public boolean hasNoRes;
    public boolean hasLoadMore;
    public boolean hasCustomView;

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

    private void initVariable() {
        LoadingView = View.inflate(getContext(), R.layout.footer_list_view, null);
        RetryView = View.inflate(getContext(), R.layout.design_retry_footer, null); // R.layout.footer_retry_network.
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

    /*
        Author's note: Tidak berguna, hanya sebagai referensi untuk melakukan lazy load pada onScrollListener
        public boolean isLazyLoad(int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if( firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0){
            return true;
        }
        else
            return false;
    }*/

    public void addRetry() {
        if (!hasFooterHeader()) {
            addFooterView(RetryView);
            hasRetry = true;
        }
    }

    public void setRetryClickListener(OnClickListener listener) {
        RetryView.setOnClickListener(listener);
    }

    public void removeRetry() {
        if (hasRetry) {
            removeFooterView(RetryView);
            hasRetry = false;
        }
    }

    public void addLoadMoreHeader() {
        if (!hasFooterHeader()) {
            addHeaderView(LoadMore);
            hasLoadMore = true;
        }
    }

    public void setLoadListener(OnClickListener listener) {
        LoadMore.setOnClickListener(listener);
    }

    public void removeLoadMore() {
        if (hasLoadMore) {
            removeHeaderView(LoadMore);
            hasLoadMore = false;
        }
    }

    public void addLoadingHeader() {
        CheckAdapter();
        if (!hasFooterHeader()) {
            addHeaderView(LoadingView, null, false);
            hasLoading = true;
        }
    }

    public void addLoadingFooter() {
        CheckAdapter();
        if (!hasFooterHeader()) {
            addFooterView(LoadingView, null, false);
            hasLoading = true;
        }
    }

    public void removeLoading() {
        if (hasLoading) {
            removeFooterView(LoadingView);
            removeHeaderView(LoadingView);
            hasLoading = false;
        }
    }

    public void addNoResult() {
        if (!hasFooterHeader()) {
            if ((((ViewGroup) SimpleListView.this.getParent()).getMeasuredWidth()) > ((ViewGroup) SimpleListView.this.getParent()).getMeasuredHeight()) {
                NoResultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            } else {
                NoResultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ((ViewGroup) SimpleListView.this.getParent()).getMeasuredHeight()));
            }
            NoResultView.requestLayout();
            addFooterView(NoResultView, null, false);
            hasNoRes = true;
        }
    }

    public void addCustomNoResult(String customText) {
        CommonUtils.dumper("SHOPTXSTATUS do add custom no result " + customText);
        if (!hasFooterHeader()) {
            CommonUtils.dumper("SHOPTXSTATUScustom no result footer");
            NoResultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ((ViewGroup) SimpleListView.this.getParent()).getMeasuredHeight()));
            NoResultView.requestLayout();
            TextView textNoResult = (TextView) NoResultView.findViewById(R.id.text_no_result);
            textNoResult.setText(customText);
            addFooterView(NoResultView, null, false);
            hasNoRes = true;
        }
    }


    public void addCustomView(int resID) {
        if (!hasFooterHeader()) {
            CustomView = View.inflate(getContext(), resID, null);
            CustomView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ((ViewGroup) SimpleListView.this.getParent()).getMeasuredHeight()));
            CustomView.requestLayout();
            addFooterView(CustomView, null, false);
            hasCustomView = true;
        }
    }

    public void addCustomView(View view) {
        if (!hasFooterHeader()) {
            CustomView = view;
            addFooterView(CustomView, null, false);
            hasCustomView = true;
        }
    }

    public void removeCustomView() {
        if (hasCustomView) {
            removeFooterView(CustomView);
            hasCustomView = false;
        }
    }

    public void removeNoResult() {
        if (hasNoRes) {
            removeFooterView(NoResultView);
            hasNoRes = false;
        }
    }

    private boolean hasFooterHeader() {
        if (hasLoading || hasRetry || hasNoRes || hasLoadMore || hasCustomView)
            return true;
        else
            return false;
    }

    private void CheckAdapter() {
        if (getAdapter() == null) {
            throw new RuntimeException("Forgot to set the adapter");
        }
    }

}
