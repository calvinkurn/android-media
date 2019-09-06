package com.tokopedia.filter.newdynamicfilter.adapter.typefactory;

import android.view.View;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.BottomSheetExpandableItemViewHolder;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterItemPriceViewHolder;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterItemToggleViewHolder;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterNoViewHolder;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView;

public class BottomSheetDynamicFilterTypeFactoryImpl implements DynamicFilterTypeFactory {

    private BottomSheetDynamicFilterView filterView;

    public BottomSheetDynamicFilterTypeFactoryImpl(BottomSheetDynamicFilterView filterView) {
        this.filterView = filterView;
    }

    @Override
    public int type(Filter filter) {
        if (filter.isSeparator()) {
            return R.layout.dynamic_filter_item_separator;
        }
        else if (filter.isPriceFilter()) {
            return R.layout.dynamic_filter_item_price;
        } else if (filter.isExpandableFilter()) {
            return R.layout.dynamic_filter_expandable_item;
        } else {
            return R.layout.dynamic_filter_item_toggle;
        }
    }

    @Override
    public DynamicFilterViewHolder createViewHolder(View view, int viewType) {
        if (viewType == R.layout.dynamic_filter_item_price) {
            return new DynamicFilterItemPriceViewHolder(view, filterView);
        } else if (viewType == R.layout.dynamic_filter_item_toggle) {
            return new DynamicFilterItemToggleViewHolder(view, filterView);
        } else if (viewType == R.layout.dynamic_filter_expandable_item) {
            return new BottomSheetExpandableItemViewHolder(view, filterView);
        } else {
            return new DynamicFilterNoViewHolder(view);
        }
    }
}
