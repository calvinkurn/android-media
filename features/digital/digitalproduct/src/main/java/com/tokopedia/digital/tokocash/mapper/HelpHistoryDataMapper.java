package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public class HelpHistoryDataMapper implements Func1<List<HelpHistoryTokoCashEntity>, List<HelpHistoryTokoCash>> {

    @Override
    public List<HelpHistoryTokoCash> call(List<HelpHistoryTokoCashEntity> helpHistoryTokoCashEntities) {
        List<HelpHistoryTokoCash> helpHistoryTokoCashList = new ArrayList<>();

        if (helpHistoryTokoCashEntities != null && helpHistoryTokoCashEntities.size() > 0) {

            for (HelpHistoryTokoCashEntity helpHistory : helpHistoryTokoCashEntities) {
                HelpHistoryTokoCash helpHistoryTokoCash = new HelpHistoryTokoCash();
                helpHistoryTokoCash.setId(helpHistory.getId());
                helpHistoryTokoCash.setTranslation(helpHistory.getTranslation());
                helpHistoryTokoCashList.add(helpHistoryTokoCash);
            }
        }

        return helpHistoryTokoCashList;
    }
}
