package com.tokopedia.analyticsdebugger.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.GtmErrorLogDB;
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmErrorLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.usecase.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.analyticsdebugger.debugger.helper.FormatterHelperKt.formatDataExcerpt;

public class GtmErrorLogLocalRepository{
    private GtmErrorLogDBSource dbSource;
    private DateFormat dateFormat;

    @Inject
    GtmErrorLogLocalRepository(GtmErrorLogDBSource source) {
        this.dbSource = source;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public Observable<Boolean> removeAll() {
        return dbSource.deleteAll();
    }

    public Observable<List<Visitable>> get(RequestParams parameters) {
        return dbSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<GtmErrorLogDB>, Iterable<GtmErrorLogDB>>() {
                    @Override
                    public Iterable<GtmErrorLogDB> call(List<GtmErrorLogDB> gtmLogDBS) {
                        return gtmLogDBS;
                    }
                }).flatMap(new Func1<GtmErrorLogDB, Observable<Visitable>>() {
                    @Override
                    public Observable<Visitable> call(GtmErrorLogDB gtmErrorLogDB) {
                        AnalyticsDebuggerViewModel viewModel = new AnalyticsDebuggerViewModel();
                        viewModel.setId(gtmErrorLogDB.getTimestamp());
                        viewModel.setName("");
                        viewModel.setCategory("");
                        viewModel.setData(gtmErrorLogDB.getData());
                        viewModel.setDataExcerpt(formatDataExcerpt(gtmErrorLogDB.getData()));
                        viewModel.setTimestamp(dateFormat.format(new Date(gtmErrorLogDB.getTimestamp())));

                        return Observable.just(viewModel);
                    }
                }).toList();
    }
}
