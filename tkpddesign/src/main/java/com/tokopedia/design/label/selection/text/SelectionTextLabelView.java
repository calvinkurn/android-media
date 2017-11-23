package com.tokopedia.design.label.selection.text;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionLabelView;
import com.tokopedia.design.label.selection.SelectionListAdapter;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class SelectionTextLabelView extends SelectionLabelView<SelectionItem<String>> {

    @Override
    protected SelectionListAdapter getSelectionListAdapter() {
        return new SelectionTextListAdapter();
    }

    public SelectionTextLabelView(@NonNull Context context) {
        super(context);
    }

    public SelectionTextLabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectionTextLabelView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}