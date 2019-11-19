package com.tokopedia.tradein.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
