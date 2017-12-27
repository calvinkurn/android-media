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

public class StatusMapper implements Func1<StatusEntity, Status> {

    @Override
    public Status call(StatusEntity statusEntity) {
        Status status = new Status();
        status.setType(statusEntity.getType());

        if (statusEntity.getAttributes() != null) {
            if (statusEntity.getAttributes().getVersion() != null) {
                status.setMinimunAndroidBuild(Integer.valueOf(statusEntity.getAttributes().getVersion()
                        .getMinimumAndroidBuild()));
            }
            status.setMaintenance(statusEntity.getAttributes().isMaintenance());
        }

        return status;
    }
}
