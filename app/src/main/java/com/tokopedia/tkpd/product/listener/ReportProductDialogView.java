package com.tokopedia.tkpd.product.listener;

import com.tokopedia.tkpd.session.model.network.ReportType;

import java.util.List;

/**
 * Created by stevenfredian on 7/20/16.
 */
public interface ReportProductDialogView {
    void downloadReportType();

    void setReportAdapterFromCache(String data);

    void showError(String errorString);

    void setReportAdapterFromNetwork(List<ReportType> reportTypeList);

    void saveToCache(String s);
}
