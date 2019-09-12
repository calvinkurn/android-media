package com.tokopedia.tradein_common.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tokopedia.tradein_common.repository.BaseRepository;

public abstract class BaseViewModel extends ViewModel {
    protected MutableLiveData<Boolean> progBarVisibility = new MutableLiveData<>();
    protected MutableLiveData<String> warningMessage = new MutableLiveData<>();

    protected BaseRepository repository;

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
