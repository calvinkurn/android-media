package com.tokopedia.filter.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.filter.R;

/**
 * Created by henrypriyono on 8/25/17.
 */

public class EmptySearchResultView extends BaseCustomView {

    private View rootView;
    private TextView titleView;
    private TextView contentView;

    public EmptySearchResultView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public EmptySearchResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptySearchResultView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.empty_search_result, this);
        titleView = rootView.findViewById(R.id.empty_search_result_title);
        contentView = rootView.findViewById(R.id.empty_search_result_content);
    }

    public void setSearchCategory(String searchCategory) {
        if (titleView != null) {
            String titleTemplate
                    = titleView.getContext().getResources().getString(com.tokopedia.design.R.string.empty_search_result_title_template);

            titleView.setText(titleTemplate.replace("$1", searchCategory.toLowerCase()));
        }
    }

    public void setSearchQuery(String searchQuery) {
        if (contentView != null) {
            String contentTemplate
                    = contentView.getContext().getResources().getString(com.tokopedia.design.R.string.empty_search_result_content_template);

            contentView.setText(contentTemplate.replace("$1", searchQuery.toLowerCase()));
        }
    }
}
