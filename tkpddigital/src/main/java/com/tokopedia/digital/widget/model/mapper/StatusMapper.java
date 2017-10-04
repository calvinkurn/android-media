package com.tokopedia.digital.widget.model.mapper;

import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.model.status.Attributes;
import com.tokopedia.digital.widget.model.status.Status;
import com.tokopedia.digital.widget.model.status.Version;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 */

public class StatusMapper implements Func1<StatusEntity, Status> {

    @Override
    public Status call(StatusEntity statusEntity) {
        Status status = new Status();
        status.setType(statusEntity.getType());

        Attributes attributes = new Attributes();
        attributes.setMaintenance(statusEntity.getAttributes().isMaintenance());

        Version version = new Version();
        version.setCategory(statusEntity.getAttributes().getVersion().getCategory());
        version.setMinimumAndroidBuild(statusEntity.getAttributes().getVersion().getMinimumAndroidBuild());
        version.setOperator(statusEntity.getAttributes().getVersion().getOperator());
        version.setProduct(statusEntity.getAttributes().getVersion().getProduct());
        attributes.setVersion(version);

        status.setAttributes(attributes);

        return status;
    }
}
