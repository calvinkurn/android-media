package viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

public class TradeInVMFactory extends ViewModelProvider.NewInstanceFactory {

    private static TradeInVMFactory sInstance;
    private WeakReference<Activity> activityWeakReference;

    public static TradeInVMFactory getInstance(Activity activity) {
        if (sInstance == null) {
            sInstance = new TradeInVMFactory(activity);
        }
        return sInstance;
    }

    private TradeInVMFactory(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection TryWithIdenticalCatches
        if (TradeInTextViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(Activity.class).newInstance(activityWeakReference.get());
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
