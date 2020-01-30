package com.tokopedia.analytics.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.database.IrisSaveLogDB;
import com.tokopedia.analytics.database.IrisSendLogDB;
import com.tokopedia.analytics.debugger.data.source.IrisSaveLogDBSource;
import com.tokopedia.analytics.debugger.data.source.IrisSendLogDBSource;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.usecase.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class IrisSendLogLocalRepository {
    private IrisSendLogDBSource dbSource;
    private DateFormat dateFormat;

    @Inject
    IrisSendLogLocalRepository(IrisSendLogDBSource source) {
        this.dbSource = source;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public Observable<Boolean> removeAll() {
        return dbSource.deleteAll();
    }

    public Observable<List<Visitable>> get(RequestParams parameters) {
        return dbSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<IrisSendLogDB>, Iterable<IrisSendLogDB>>() {
                    @Override
                    public Iterable<IrisSendLogDB> call(List<IrisSendLogDB> logDB) {
                        return logDB;
                    }
                }).flatMap(new Func1<IrisSendLogDB, Observable<Visitable>>() {
                    @Override
                    public Observable<Visitable> call(IrisSendLogDB logDB) {
                        AnalyticsDebuggerViewModel viewModel = new AnalyticsDebuggerViewModel();
                        viewModel.setId(logDB.getTimestamp());
                        viewModel.setName("");
                        viewModel.setCategory("");
                        viewModel.setData(logDB.getData());
                        if(logDB.getData().length() > 100) {
                            viewModel.setDataExcerpt(logDB.getData().replaceAll("\\s+", " ").substring(0,100) + "...");
                        } else {
                            viewModel.setDataExcerpt(logDB.getData().replaceAll("\\s+", " "));
                        }

                        viewModel.setTimestamp(dateFormat.format(new Date(logDB.getTimestamp())));

                        return Observable.just(viewModel);
                    }
                }).toList();
    }
}
