package com.tokopedia.gamification.taptap.database;

import com.tokopedia.gamification.data.entity.CrackResultEntity;

import java.util.List;

public interface GamificationDbCallback {

    void onSuccessGetFromDb(List<CrackResultEntity> crackResultEntities);

    void onErrorGetFromDb();

    void onSuccessGetFromDbForCampaign(List<CrackResultEntity> crackResultEntities);
}
