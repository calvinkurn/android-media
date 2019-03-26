package viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

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
            return (T) new TradeInTextViewModel(activityWeakReference.get());
        } else if (TradeInHomeViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new TradeInHomeViewModel(activityWeakReference.get());
        } else if (FinalPriceViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new FinalPriceViewModel(activityWeakReference.get());
        }
        return super.create(modelClass);
    }
}
