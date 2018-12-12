package com.tokopedia.affiliate.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.design.text.SearchInputView;

/**
 * @author by yfsx on 06/12/18.
 */
public class ExploreSearchView extends SearchInputView {
    public ExploreSearchView(Context context) {
        super(context);
    }

    public ExploreSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExploreSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addTextWatcherToSearch() {
        getSearchTextView().addTextChangedListener(getSearchTextWatcher());
    }

    public void removeSearchTextWatcher() {
        getSearchTextView().removeTextChangedListener(getSearchTextWatcher());
    }
}
