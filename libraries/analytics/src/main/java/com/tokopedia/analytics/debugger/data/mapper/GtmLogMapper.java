package com.tokopedia.analytics.debugger.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogMapper implements Func1<GtmLogDB, Observable<Visitable>> {
    private SimpleDateFormat dateFormat;

    @Inject
    GtmLogMapper() {
       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public Observable<Visitable> call(GtmLogDB gtmLogDB) {
        AnalyticsDebuggerViewModel viewModel = new AnalyticsDebuggerViewModel();
        viewModel.setId(gtmLogDB.getId());
        viewModel.setName(gtmLogDB.getName());
        viewModel.setCategory(gtmLogDB.getCategory());
        viewModel.setData(gtmLogDB.getData());
        String dataExcerpt = gtmLogDB.getData().replaceAll("\\s+", " ");
        if(dataExcerpt.length() > 100) {
            viewModel.setDataExcerpt(dataExcerpt.substring(0,100) + "...");
        } else {
            viewModel.setDataExcerpt(dataExcerpt);
        }

        viewModel.setTimestamp(dateFormat.format(new Date(gtmLogDB.getTimestamp())));

        return Observable.just((Visitable) viewModel);
    }
}
