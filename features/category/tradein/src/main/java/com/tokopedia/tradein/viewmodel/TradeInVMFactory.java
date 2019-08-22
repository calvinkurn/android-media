package com.tokopedia.tradein.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

public class TradeInVMFactory extends ViewModelProvider.NewInstanceFactory {

    private WeakReference<FragmentActivity> activityWeakReference;

    public static TradeInVMFactory getInstance(FragmentActivity activity) {
        return new TradeInVMFactory(activity);
    }

    private TradeInVMFactory(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection TryWithIdenticalCatches
        if (TradeInTextViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(FragmentActivity.class).newInstance(activityWeakReference.get());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        } else if (TradeInHomeViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(FragmentActivity.class).newInstance(activityWeakReference.get());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        } else if (FinalPriceViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(FragmentActivity.class).newInstance(activityWeakReference.get());
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
