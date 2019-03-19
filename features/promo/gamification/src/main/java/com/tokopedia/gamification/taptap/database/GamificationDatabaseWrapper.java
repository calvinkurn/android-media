package com.tokopedia.gamification.taptap.database;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.gamification.data.entity.CrackResultEntity;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class GamificationDatabaseWrapper {

    private final Context context;

    @Inject
    public GamificationDatabaseWrapper(@ApplicationContext Context context) {
        this.context = context;

    }

    public void insert(long campaignId, CrackResultEntity crackResultEntity) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                crackResultEntity.setCampaignId(campaignId);
                GamificationRoomDb.getDatabase(context).gamificationDao().insert(crackResultEntity);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete() {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                GamificationRoomDb.getDatabase(context).gamificationDao().deleteAll();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void getAllEntries(GamificationDbCallback gamificationDbCallback) {
        Observable.fromCallable(new Callable<List<CrackResultEntity>>() {
            @Override
            public List<CrackResultEntity> call() throws Exception {
                return GamificationRoomDb.getDatabase(context).gamificationDao().getAllRewards();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<CrackResultEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                gamificationDbCallback.onErrorGetFromDb();
            }

            @Override
            public void onNext(List<CrackResultEntity> crackResultEntities) {
                if(crackResultEntities!=null && crackResultEntities.size()!=0) {
                    gamificationDbCallback.onSuccessGetFromDb(crackResultEntities);
                }else{
                    gamificationDbCallback.onErrorGetFromDb();
                }
            }
        });
    }

    public void getAllEntriesForCampaignId(GamificationDbCallback gamificationDbCallback, long campaignId) {
        Observable.fromCallable(new Callable<List<CrackResultEntity>>() {
            @Override
            public List<CrackResultEntity> call() throws Exception {
                return GamificationRoomDb.getDatabase(context).gamificationDao().getRewardsNotFor(campaignId);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<CrackResultEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<CrackResultEntity> crackResultEntities) {
                if(crackResultEntities!=null && crackResultEntities.size()!=0) {
                    gamificationDbCallback.onSuccessGetFromDbForCampaign(crackResultEntities);
                }
            }
        });
    }
}
