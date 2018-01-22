package com.tokopedia.digital.widget.domain.interactor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.digital.widget.view.model.status.Status;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 22/01/18.
 */

public class DigitalWidgetUseCase extends UseCase<List<Category>> {

    private static final String TAG = DigitalWidgetUseCase.class.getSimpleName();

    private Context context;
    private DigitalWidgetRepository digitalWidgetRepository;
    private StatusMapper statusMapper;
    private CategoryMapper categoryMapper;

    public DigitalWidgetUseCase(Context context,
                                DigitalWidgetRepository digitalWidgetRepository,
                                StatusMapper statusMapper,
                                CategoryMapper categoryMapper) {
        this.context = context;
        this.digitalWidgetRepository = digitalWidgetRepository;
        this.statusMapper = statusMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Observable<List<Category>> createObservable(RequestParams requestParams) {
        return digitalWidgetRepository.getObservableStatus()
                .map(statusMapper)
                .flatMap(new Func1<Status, Observable<List<Category>>>() {
                    @Override
                    public Observable<List<Category>> call(Status status) {
                        if (status.isMaintenance() || !isVersionMatch(status)) {
                            // failed
                        } else {
                            return digitalWidgetRepository.getObservableCategoryData()
                                    .map(categoryMapper);
                        }
                        return null;
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
