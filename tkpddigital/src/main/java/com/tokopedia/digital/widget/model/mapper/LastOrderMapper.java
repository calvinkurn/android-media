package com.tokopedia.digital.widget.model.mapper;

import com.tokopedia.digital.widget.data.entity.lastorder.LastOrderEntity;
import com.tokopedia.digital.widget.model.lastorder.Attributes;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 */

public class LastOrderMapper implements Func1<LastOrderEntity, LastOrder> {

    @Override
    public LastOrder call(LastOrderEntity lastOrderEntity) {
        LastOrder lastOrder = new LastOrder();
        lastOrder.setType(lastOrderEntity.getType());
        lastOrder.setId(lastOrderEntity.getId());

        Attributes attributes = new Attributes();
        if (lastOrderEntity.getAttributes() != null) {
            attributes.setCategoryId(lastOrderEntity.getAttributes().getCategoryId());
            attributes.setClientNumber(lastOrderEntity.getAttributes().getClientNumber());
            attributes.setOperatorId(lastOrderEntity.getAttributes().getOperatorId());
            attributes.setProductId(lastOrderEntity.getAttributes().getProductId());
        }
        lastOrder.setAttributes(attributes);

        return lastOrder;
    }
}
