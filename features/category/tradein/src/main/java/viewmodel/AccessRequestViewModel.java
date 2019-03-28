package viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AccessRequestViewModel extends ViewModel {
    private MutableLiveData<Boolean> permissionLiveData;

    public AccessRequestViewModel() {
        permissionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getPermissionLiveData() {
        return permissionLiveData;
    }

    private void setPermissionLiveData(Boolean isPermissionGiven) {
        this.permissionLiveData.setValue(isPermissionGiven);
    }


}
