package com.tokopedia.digital.widget.domain.interactor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 22/01/18.
 */

public class DigitalWidgetUseCase extends UseCase<List<Category>> {

    private static final String TAG = DigitalWidgetUseCase.class.getSimpleName();

    private Context context;
    private DigitalWidgetRepository digitalWidgetRepository;

    public DigitalWidgetUseCase(Context context,
                                DigitalWidgetRepository digitalWidgetRepository) {
        this.context = context;
        this.digitalWidgetRepository = digitalWidgetRepository;
    }

    @Override
    public Observable<List<Category>> createObservable(RequestParams requestParams) {
        return digitalWidgetRepository.getObservableStatus()
                .flatMap(new Func1<Status, Observable<List<Category>>>() {
                    @Override
                    public Observable<List<Category>> call(Status status) {
                        if (status.isMaintenance() || !isVersionMatch(status)) {
                            // failed
                            List<Category> categories = new ArrayList<>();
                            return Observable.just(categories);
                        } else {
                            return digitalWidgetRepository.getObservableCategoryList();
                        }
                    }
                });
    }

    private boolean isVersionMatch(Status status) {
        try {
            int minApiSupport = status.getMinimunAndroidBuild();
            Log.d(TAG, "version code : " + getVersionCode());
            return getVersionCode() >= minApiSupport;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getVersionCode() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionCode;
    }

}
