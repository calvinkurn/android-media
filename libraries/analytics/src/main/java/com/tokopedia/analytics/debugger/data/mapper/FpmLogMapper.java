package com.tokopedia.analytics.debugger.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.database.FpmLogDB;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.analytics.debugger.ui.model.FpmDebuggerViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.analytics.debugger.helper.FormatterHelperKt.formatDataExcerpt;

/**
 * @author okasurya on 5/16/18.
 */
public class FpmLogMapper implements Func1<FpmLogDB, Observable<Visitable>> {
    private SimpleDateFormat dateFormat;

    @Inject
    FpmLogMapper() {
       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public Observable<Visitable> call(FpmLogDB fpmLogDB) {
        FpmDebuggerViewModel viewModel = new FpmDebuggerViewModel();
        viewModel.setId(fpmLogDB.getId());
        viewModel.setName(fpmLogDB.getTraceName());
        viewModel.setDuration(fpmLogDB.getDuration());
        viewModel.setMetrics(fpmLogDB.getMetrics());
        viewModel.setAttributes(fpmLogDB.getAttributes());
        viewModel.setTimestamp(dateFormat.format(new Date(fpmLogDB.getTimestamp())));

        return Observable.just((Visitable) viewModel);
    }
}
