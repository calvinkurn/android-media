package com.tokopedia.core.myproduct.customview.wholesale;

import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.List;

/**
 * Created by sebastianuskh on 12/5/16.
 */

public interface WholesaleAdapter {

    void onUpdateData(int type, int position, String value);

    void removeWholesaleItem(int position);

    boolean checkIfErrorExist();

    List<WholesaleModel> getDatas();

    void setData(List<WholesaleModel> datas);

    void removeAllWholesaleItem();
}
