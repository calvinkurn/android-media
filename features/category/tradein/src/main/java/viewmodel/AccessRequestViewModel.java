package viewmodel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AccessRequestViewModel extends ViewModel implements IAccessRequestListener, LifecycleObserver {
    // TODO: Implement the ViewModel
    private MutableLiveData<Boolean> permissionLiveData;

    public MutableLiveData<Boolean> getPermissionLiveData() {
        return permissionLiveData;
    }

    private void setPermissionLiveData(Boolean isPermissionGiven) {
        this.permissionLiveData.setValue(isPermissionGiven);
    }

    @Override
    public void clickAccept() {
        setPermissionLiveData(true);
    }

    @Override
    public void clickDeny() {
        setPermissionLiveData(false);
    }


}
