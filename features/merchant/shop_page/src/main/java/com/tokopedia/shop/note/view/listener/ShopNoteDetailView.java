package com.tokopedia.shop.note.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;

/**
 * Created by normansyahputa on 2/8/18.
 */

public interface ShopNoteDetailView extends CustomerView {

    void onErrorGetShopNoteList(Throwable e);

    void onSuccessGetShopNoteList(ShopNoteDetail shopNoteDetail);
}
