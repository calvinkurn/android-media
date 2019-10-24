package com.tokopedia.promotionstarget;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.tokopedia.promotionstarget.usecase.AutoApplyUseCase;
import com.tokopedia.promotionstarget.usecase.ClaimPopGratificationUseCase;
import com.tokopedia.promotionstarget.ui.viewmodel.TargetPromotionsDialogVM;

import kotlinx.coroutines.Dispatchers;

//todo Rahul check exsistence of this class
public class MyViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Object[] mParams;

    public MyViewModelFactory(Application application, Object... params) {
        mApplication = application;
        mParams = params;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == TargetPromotionsDialogVM.class) {
            return (T) new TargetPromotionsDialogVM(Dispatchers.getMain(), new AutoApplyUseCase(""));
        } else {
            return super.create(modelClass);
        }
    }
}