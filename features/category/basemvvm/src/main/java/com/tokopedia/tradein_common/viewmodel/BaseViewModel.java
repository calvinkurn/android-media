package com.tokopedia.tradein_common.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.tokopedia.tradein_common.repository.BaseRepository;

public abstract class BaseViewModel extends AndroidViewModel {
    protected MutableLiveData<Boolean> progBarVisibility = new MutableLiveData<>();
    protected MutableLiveData<String> warningMessage = new MutableLiveData<>();
    protected MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected Application applicationInstance;

    protected BaseRepository repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        applicationInstance = application;
    }

    protected void doOnStart() {

    }

    protected void doOnCreate() {
        createRepository();
    }

    protected void doOnPause() {

    }

    protected void doOnResume() {


    }

    protected void doOnStop() {

    }

    protected void doOnDestroy() {

    }

    public MutableLiveData<Boolean> getProgBarVisibility() {
        return progBarVisibility;
    }

    public MutableLiveData<String> getWarningMessage() {
        return warningMessage;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void createRepository() {
        if (getRepository() == null)
            repository = BaseRepository.Companion.getRepositoryInstance();
        else
            repository = getRepository();

    }

    protected <BR extends BaseRepository> BR getRepository() {
        return null;
    }
}
