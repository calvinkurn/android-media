package com.tokopedia.digital.widget.view.model.mapper;

import com.tokopedia.digital.widget.data.entity.lastorder.LastOrderEntity;
import com.tokopedia.digital.widget.view.model.lastorder.Attributes;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;

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

        if (lastOrderEntity.getAttributes() != null) {
            Attributes attributes = new Attributes();
            attributes.setCategoryId(lastOrderEntity.getAttributes().getCategoryId());
            attributes.setClientNumber(lastOrderEntity.getAttributes().getClientNumber());
            attributes.setOperatorId(lastOrderEntity.getAttributes().getOperatorId());
            attributes.setProductId(lastOrderEntity.getAttributes().getProductId());

            lastOrder.setAttributes(attributes);
        }

        return lastOrder;
    }
}
