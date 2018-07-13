package com.tokopedia.home.beranda.listener;

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;

/**
 * Created by errysuprayogi on 3/23/18.
 */
public interface GridItemClickListener {
    void onGridItemClick(int pos, DynamicHomeChannel.Grid grid);
}
