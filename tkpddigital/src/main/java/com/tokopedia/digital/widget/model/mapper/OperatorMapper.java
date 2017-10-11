package com.tokopedia.digital.widget.model.mapper;

import com.tokopedia.digital.widget.data.entity.operator.OperatorEntity;
import com.tokopedia.digital.widget.model.operator.Attributes;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.operator.Rule;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 */

public class OperatorMapper implements Func1<List<OperatorEntity>, List<Operator>> {

    @Override
    public List<Operator> call(List<OperatorEntity> operatorEntities) {
        List<Operator> operatorList = new ArrayList<>();
        for (OperatorEntity operatorEntity : operatorEntities) {
            Attributes attributes = new Attributes();
            if (operatorEntity.getAttributes() != null) {
                attributes.setImage(operatorEntity.getAttributes().getImage());
                attributes.setMaximumLength(operatorEntity.getAttributes().getMaximumLength());
                attributes.setMinimumLength(operatorEntity.getAttributes().getMinimumLength());
                attributes.setName(operatorEntity.getAttributes().getName());
                attributes.setSlug(operatorEntity.getAttributes().getSlug());
                attributes.setStatus(operatorEntity.getAttributes().getStatus());
                attributes.setWeight(operatorEntity.getAttributes().getWeight());
                attributes.setDefaultProductId(operatorEntity.getAttributes().getDefaultProductId());
                attributes.setPrefix(operatorEntity.getAttributes().getPrefix());

                Rule rule = new Rule();
                if (operatorEntity.getAttributes().getRule() != null) {
                    rule.setProductText(operatorEntity.getAttributes().getRule().getProductText());
                    rule.setProductsViewStyle(operatorEntity.getAttributes().getRule().getProductsViewStyle());
                    rule.setShowPrice(operatorEntity.getAttributes().getRule().isShowPrice());
                    rule.setShowProduct(operatorEntity.getAttributes().getRule().isShowProduct());
                    rule.setShowProductListPage(operatorEntity.getAttributes().getRule().isShowProductListPage());
                    rule.setAllowAphanumericNumber(operatorEntity.getAttributes().getRule().isAllowAphanumericNumber());
                    rule.setButtonLabel(operatorEntity.getAttributes().getRule().getButtonLabel());
                }
                attributes.setRule(rule);
            }

            Operator operator = new Operator();
            operator.setAttributes(attributes);
            operator.setId(operatorEntity.getId());
            operator.setType(operatorEntity.getType());

            operatorList.add(operator);
        }
        return operatorList;
    }
}
