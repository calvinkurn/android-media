package com.tokopedia.affiliate.common.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.tokopedia.design.text.SearchInputView;

/**
 * @author by yfsx on 06/12/18.
 */
public class ExploreSearchView extends SearchInputView {
    private TextWatcher searchTextWatcher;
    public ExploreSearchView(Context context) {
        super(context);
        searchTextWatcher = getSearchTextWatcher();
    }

    public ExploreSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        searchTextWatcher = getSearchTextWatcher();
    }

    public ExploreSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        searchTextWatcher = getSearchTextWatcher();
    }


    public void addTextWatcherToSearch() {
        getSearchTextView().addTextChangedListener(searchTextWatcher);
    }

    public void removeSearchTextWatcher() {
        getSearchTextView().removeTextChangedListener(searchTextWatcher);
    }
}
