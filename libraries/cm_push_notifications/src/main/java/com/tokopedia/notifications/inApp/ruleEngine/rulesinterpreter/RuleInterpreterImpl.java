package com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter;

import com.tokopedia.notifications.inApp.ruleEngine.RulesUtil;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.InterfaceRuleInterpreter;
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RuleInterpreterImpl implements InterfaceRuleInterpreter {

    private List<CMInApp> inAppList;
    private ElapsedTime elapsedTimeObj;

    @Override
    public void checkForValidity(String entity, int state, long currentTime,
                                 DataProvider dataProvider) {
        makeRequestForData(entity, currentTime, dataProvider);
    }

    private void makeRequestForData(
            final String entity,
            final long currentTime,
            final DataProvider dataProvider
    ){
        Observable.fromCallable(new Callable<ElapsedTime>() {
            @Override
            public ElapsedTime call() throws Exception {
                return RepositoryManager.getInstance()
                        .getStorageProvider()
                        .getElapsedTimeFromStore();
            }
        }).map(new Func1<ElapsedTime, List<CMInApp>>() {
            @Override
            public List<CMInApp> call(ElapsedTime elapsedTime) {
                if (elapsedTime != null) {
                    elapsedTimeObj = elapsedTime;
                } else {
                    createAndSetElapsedTime();
                }
                return RepositoryManager.getInstance()
                        .getStorageProvider()
                        .getDataFromStore(entity);
            }
        }).map(new Func1<List<CMInApp>, List<CMInApp>>() {
            @Override
            public List<CMInApp> call(List<CMInApp> cmInApps) {
                dataProvider.sendEventInAppPrepared(cmInApps);
                return cmInApps;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CMInApp>>() {
                    @Override public void onNext(List<CMInApp> inAppDataList) {
                        if (inAppDataList != null) {
                            inAppList = inAppDataList;
                            Iterator<CMInApp> inApp = inAppList.iterator();
                            while (inApp.hasNext()) {
                                CMInApp inAppData = inApp.next();
                                if (!(checkIfActiveInTimeFrame(inAppData, System.currentTimeMillis()) &&
                                        checkIfFrequencyIsValid(inAppData, System.currentTimeMillis()) &&
                                        checkIfBehaviourRulesAreValid(inAppData))) {
                                    inApp.remove();
                                    if (performDeletion(inAppData)) {
                                        RepositoryManager.getInstance()
                                                .getStorageProvider()
                                                .deleteRecord(inAppData.id);
                                    }
                                }
                            }
                        }
                        dataProvider.notificationsDataResult(inAppList);
                    }

                    @Override public void onError(Throwable e) {
                        dataProvider.notificationsDataResult(null);
                    }

                    @Override public void onCompleted() {}
                });
    }

    private boolean checkIfActiveInTimeFrame(CMInApp inAppData, long currentTime){
        if(checkIfTimeNotAssigned(inAppData)){
            return true;
        }
        else {
            //check how to get current time stamp
            return checkForTimeIntervalValidity(inAppData, currentTime);
        }
    }

    private boolean checkForTimeIntervalValidity(CMInApp inAppData, long currentTime){
        return  (RulesUtil.isValidTimeFrame(inAppData.startTime,
                inAppData.endTime, currentTime, elapsedTimeObj));
    }


    private boolean checkIfTimeNotAssigned(CMInApp inAppData){
        if(inAppData.startTime == 0l && inAppData.endTime == 0l){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkIfFrequencyIsValid(CMInApp inAppData, long currentTime){
        if(inAppData.freq == RulesUtil.Constants.DEFAULT_FREQ && !inAppData.isShown){
            return true;
        }
        else if(inAppData.freq > 0 && !inAppData.isShown){
            return true;
        }
        else {
            return checkIfInvaildFreq(inAppData, currentTime);
        }
    }

    private boolean checkIfInvaildFreq(CMInApp inAppData, long currentTime){
        if(inAppData.freq < RulesUtil.Constants.DEFAULT_FREQ){
            return checkIfLessThanDefaultFreq(inAppData, currentTime);
        }else {
            return false;
        }
    }

    private boolean checkIfLessThanDefaultFreq(CMInApp inAppData, long currentTime){
        if(checkIfTimeNotAssigned(inAppData)){
            return false;
        }else if(inAppData.startTime > 0 && inAppData.endTime > 0){
            return checkForTimeIntervalValidity(inAppData, currentTime);
        }
        else {
            return false;
        }
    }

    private boolean performDeletion(CMInApp inAppData){
        if((inAppData.startTime != 0L && inAppData.endTime != 0L) || (!inAppData.isShown && (inAppData.freq == 0 || inAppData.freq < RulesUtil.Constants.DEFAULT_FREQ))){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkIfBehaviourRulesAreValid(CMInApp inAppData){
        //check for behaviour base expiry
        //if no behaviour data is present then return true
        //need to figure out which format to use to send behaviour data
        return true;
    }

    private void createAndSetElapsedTime(){
        elapsedTimeObj = new ElapsedTime();
        elapsedTimeObj.elapsedTime = android.os.SystemClock.elapsedRealtime();
    }

}
