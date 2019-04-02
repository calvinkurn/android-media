package com.tokopedia.abstraction.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.design.text.SearchInputView;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author normansyahputa on 5/17/17.
 */

public abstract class BaseSearchListFragment<T extends Visitable, F extends AdapterTypeFactory> extends BaseListFragment<T, F> implements SearchInputView.Listener {

    private static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);

    protected SearchInputView searchInputView;

    protected long getDelayTextChanged() {
        return DEFAULT_DELAY_TEXT_CHANGED;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchInputView = getSearchInputView(view);
        searchInputView.setDelayTextChanged(getDelayTextChanged());
        searchInputView.setListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_search_list, container, false);
    }

    @NonNull
    protected SearchInputView getSearchInputView(View view) {
        return (SearchInputView) view.findViewById(R.id.search_input_view);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void renderList(@NonNull List<T> list) {
        getAdapter().clearAllElements();
        this.renderList(list, false);
    }

    @Override
    public void showGetListError(Throwable throwable) {
        super.showGetListError(throwable);
        showSearchViewWithDataSizeCheck();
    }

    protected void showSearchViewWithDataSizeCheck() {
        if (getAdapter().getDataSize() > 0 ||
                !TextUtils.isEmpty(searchInputView.getSearchText())) {
            showSearchView(true);
        } else {
            showSearchView(false);
        }
    }

    private void showSearchView(boolean isVisible) {
        if (isVisible) {
            showSearchInputView();
        } else {
            hideSearchInputView();
        }
    }

    protected void hideSearchInputView(){
        searchInputView.setVisibility(View.GONE);
    }

    protected void showSearchInputView(){
        searchInputView.setVisibility(View.VISIBLE);
    }
}