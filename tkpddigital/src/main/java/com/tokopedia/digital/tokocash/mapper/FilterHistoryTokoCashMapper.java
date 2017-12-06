package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.model.HeaderHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class FilterHistoryTokoCashMapper {

    private int[] colorBorder;

    public FilterHistoryTokoCashMapper() {

    }

    public List<QuickFilterItem> transform(List<HeaderHistory> headerHistoryList) {
        if (headerHistoryList != null && headerHistoryList.size() > 0) {
            List<QuickFilterItem> quickFilterItemList = new ArrayList<>();
            addColorBorderFilter();

            int j = 0;
            int i = 0;
            while (j < colorBorder.length && i < headerHistoryList.size()) {
                HeaderHistory headerHistory = headerHistoryList.get(i);
                QuickFilterItem quickFilterItem = new QuickFilterItem();
                quickFilterItem.setName(headerHistory.getName());
                quickFilterItem.setType(headerHistory.getType());
                quickFilterItem.setSelected(headerHistory.isSelected());
                quickFilterItem.setColorBorder(colorBorder[j]);

                quickFilterItemList.add(quickFilterItem);

                i++;
                j++;
                if (j == colorBorder.length) j = 0;
            }

            return quickFilterItemList;
        }
        return new ArrayList<>();
    }

    private int[] addColorBorderFilter() {
        colorBorder = new int[4];
        colorBorder[0] = R.color.filter_inside_blue;
        colorBorder[1] = R.color.filter_inside_green;
        colorBorder[2] = R.color.filter_inside_orange;
        colorBorder[3] = R.color.filter_inside_green_medium;
        return colorBorder;
    }
}