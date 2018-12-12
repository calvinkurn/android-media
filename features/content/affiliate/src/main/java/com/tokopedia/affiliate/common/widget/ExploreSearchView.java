package com.tokopedia.affiliate.common.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.tokopedia.design.text.SearchInputView;

/**
 * @author by yfsx on 06/12/18.
 */
public class ExploreSearchView extends SearchInputView {

    private TextWatcher mTextWatcher;
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
        mTextWatcher = getSearchTextWatcher();
        getSearchTextView().addTextChangedListener(mTextWatcher);
    }

    public void removeSearchTextWatcher() {
        getSearchTextView().removeTextChangedListener(mTextWatcher);
    }
}
