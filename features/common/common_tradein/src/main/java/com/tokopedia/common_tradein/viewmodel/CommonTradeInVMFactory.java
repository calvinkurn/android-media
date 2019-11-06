package com.tokopedia.common_tradein.viewmodel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class CommonTradeInVMFactory extends ViewModelProvider.AndroidViewModelFactory {
    private static CommonTradeInVMFactory sInstance;
    private Application application;
    private Intent intent;

    private CommonTradeInVMFactory(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    @NotNull
    public static CommonTradeInVMFactory getInstance(@NotNull Application application, Intent intent) {
        if (sInstance == null) {
            sInstance = new CommonTradeInVMFactory(application);
        }
        sInstance.intent = intent;
        return sInstance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection TryWithIdenticalCatches
        if (TradeInTextViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(Application.class).newInstance(application);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
        return super.create(modelClass);
    }
}
