package viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
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
