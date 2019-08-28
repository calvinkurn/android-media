package com.tokopedia.filter.newdynamicfilter.adapter.typefactory;

import android.view.View;

import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;

/**
 * Created by henrypriyono on 8/11/17.
 */

public interface DynamicFilterTypeFactory {
    int type(Filter filter);

    DynamicFilterViewHolder createViewHolder(View view, int viewType);
}
