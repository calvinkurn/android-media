package com.tokopedia.digital.widget.model.mapper;

import android.support.v4.util.Pair;

import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.model.status.Attributes;
import com.tokopedia.digital.widget.model.status.Status;
import com.tokopedia.digital.widget.model.status.Version;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 * Modified by rizkyfadillah on 11/9/17
 */

public class StatusMapper implements Func1<Pair<StatusEntity, Boolean>, Status> {

    @Override
    public Status call(Pair<StatusEntity, Boolean> pair) {
        Status status = new Status();
        status.setType(pair.first.getType());

        if (pair.first.getAttributes() != null) {
            if (pair.first.getAttributes().getVersion() != null) {
                status.setMinimunAndroidBuild(Integer.valueOf(pair.first.getAttributes().getVersion()
                        .getMinimumAndroidBuild()));
            }
            status.setMaintenance(pair.first.getAttributes().isMaintenance());
            status.setUseCache(pair.second);
        }

        return status;
    }
}
