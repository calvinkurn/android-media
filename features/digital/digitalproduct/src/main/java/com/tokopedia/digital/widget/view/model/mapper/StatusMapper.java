package com.tokopedia.digital.widget.view.model.mapper;

import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.view.model.status.Status;

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
