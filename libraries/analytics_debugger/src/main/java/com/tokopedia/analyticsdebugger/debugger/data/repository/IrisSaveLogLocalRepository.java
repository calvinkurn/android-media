package com.tokopedia.analyticsdebugger.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB;
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSaveLogDBSource;
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

public class IrisSaveLogLocalRepository {
    private IrisSaveLogDBSource dbSource;
    private DateFormat dateFormat;

    @Inject
    IrisSaveLogLocalRepository(IrisSaveLogDBSource source) {
        this.dbSource = source;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public Observable<Boolean> removeAll() {
        return dbSource.deleteAll();
    }

    public Observable<List<Visitable>> get(RequestParams parameters) {
        return dbSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<IrisSaveLogDB>, Iterable<IrisSaveLogDB>>() {
                    @Override
                    public Iterable<IrisSaveLogDB> call(List<IrisSaveLogDB> logDB) {
                        return logDB;
                    }
                }).flatMap(new Func1<IrisSaveLogDB, Observable<Visitable>>() {
                    @Override
                    public Observable<Visitable> call(IrisSaveLogDB logDB) {
                        String data = logDB.getData();
                        AnalyticsDebuggerViewModel viewModel = new AnalyticsDebuggerViewModel();
                        viewModel.setId(logDB.getTimestamp());
                        viewModel.setName("");
                        viewModel.setCategory("");

                        viewModel.setData(data);
                        viewModel.setDataExcerpt(formatDataExcerpt(data));
                        viewModel.setTimestamp(dateFormat.format(new Date(logDB.getTimestamp())));

                        return Observable.just(viewModel);
                    }
                }).toList();
    }
}
