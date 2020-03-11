package com.tokopedia.analyticsdebugger.debugger.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.ApplinkLogDB;
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.analyticsdebugger.debugger.helper.FormatterHelperKt.formatDataExcerpt;

public class ApplinkLogMapper implements Func1<ApplinkLogDB, Observable<Visitable>> {
    private SimpleDateFormat dateFormat;

    @Inject
    ApplinkLogMapper() {
       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public Observable<Visitable> call(ApplinkLogDB applinkLogDB) {
        ApplinkDebuggerViewModel viewModel = new ApplinkDebuggerViewModel();
        viewModel.setId(applinkLogDB.getId());
        viewModel.setApplink(applinkLogDB.getApplink());
        viewModel.setTrace(applinkLogDB.getTraces());
        viewModel.setPreviewTrace(formatDataExcerpt(applinkLogDB.getTraces()));
        viewModel.setTimestamp(dateFormat.format(new Date(applinkLogDB.getTimestamp())));

        return Observable.just((Visitable) viewModel);
    }
}
