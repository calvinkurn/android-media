package com.tokopedia.tkpd.beranda.domain.model.saldo;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsData;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class HomeSaldoModel {
    private TokoCashData tokoCashData;
    private TopPointsData topPointsData;

    public TokoCashData getTokoCashData() {
        return tokoCashData;
    }

    public void setTokoCashData(TokoCashData tokoCashData) {
        this.tokoCashData = tokoCashData;
    }

    public TopPointsData getTopPointsData() {
        return topPointsData;
    }

    public void setTopPointsData(TopPointsData topPointsData) {
        this.topPointsData = topPointsData;
    }

    public boolean hasTokoCash() {
        return tokoCashData != null;
    }

    public boolean hasTopPoint() {
        return topPointsData != null;
    }
}
