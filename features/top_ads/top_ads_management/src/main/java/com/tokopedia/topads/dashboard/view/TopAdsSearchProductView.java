package com.tokopedia.topads.dashboard.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.product.edit.common.util.ItemType;

import java.util.List;

/**
 * Created by normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductView extends CustomerView {
    void loadData(List<ItemType> datas);

    void loadMore(List<ItemType> datas);

    void dismissSnackbar();

    boolean isExistingGroup();

    void setLoadMoreFlag(boolean eof);

    void resetEmptyViewHolder();

    void showBottom();
}
