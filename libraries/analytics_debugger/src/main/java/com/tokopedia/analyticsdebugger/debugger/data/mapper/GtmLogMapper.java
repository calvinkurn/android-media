package com.tokopedia.analyticsdebugger.debugger.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.GtmLogDB;
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.analyticsdebugger.debugger.helper.FormatterHelperKt.formatDataExcerpt;

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
        viewModel.setDataExcerpt(formatDataExcerpt(gtmLogDB.getData()));
        viewModel.setTimestamp(dateFormat.format(new Date(gtmLogDB.getTimestamp())));

        return Observable.just((Visitable) viewModel);
    }
}
