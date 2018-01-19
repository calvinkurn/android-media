package com.tokopedia.digital.widget.view.model.mapper;

import com.tokopedia.digital.widget.data.entity.product.ProductEntity;
import com.tokopedia.digital.widget.view.model.product.Attributes;
import com.tokopedia.digital.widget.view.model.product.Category;
import com.tokopedia.digital.widget.view.model.product.Data;
import com.tokopedia.digital.widget.view.model.product.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;
import com.tokopedia.digital.widget.view.model.product.Promo;
import com.tokopedia.digital.widget.view.model.product.Relationship;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 */

public class ProductMapper implements Func1<List<ProductEntity>, List<Product>> {

    private final String BPJS_CATEGORY_ID = "4";

    @Override
    public List<Product> call(List<ProductEntity> productEntities) {
        List<Product> productList = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            Product product = new Product();
            product.setId(productEntity.getId());
            product.setType(productEntity.getType());

            if (productEntity.getAttributes() != null) {
                Attributes attributes = new Attributes();
                attributes.setDesc(productEntity.getAttributes().getDesc());
                attributes.setDetail(productEntity.getAttributes().getDetail());
                attributes.setDetailUrl(productEntity.getAttributes().getDetailUrl());
                attributes.setDetailUrlText(productEntity.getAttributes().getDetailUrlText());
                attributes.setInfo(productEntity.getAttributes().getInfo());
                if (productEntity.getRelationships() != null) {
                    if (productEntity.getRelationships().getCategory().getData().getId().equals(BPJS_CATEGORY_ID)) {
                        attributes.setPrice("");
                    } else {
                        attributes.setPrice(productEntity.getAttributes().getPrice());
                    }
                }
                attributes.setPricePlain(productEntity.getAttributes().getPricePlain());
                attributes.setStatus(productEntity.getAttributes().getStatus());
                attributes.setWeight(productEntity.getAttributes().getWeight());

                if (productEntity.getAttributes().getPromo() != null) {
                    Promo promo = new Promo();
                    promo.setBonusText(productEntity.getAttributes().getPromo().getBonusText());
                    promo.setNewPrice(productEntity.getAttributes().getPromo().getNewPrice());
                    promo.setNewPricePlain(productEntity.getAttributes().getPromo().getNewPricePlain());
                    promo.setTag(productEntity.getAttributes().getPromo().getTag());
                    promo.setTerms(productEntity.getAttributes().getPromo().getTerms());
                    promo.setValueText(productEntity.getAttributes().getPromo().getValueText());
                    attributes.setPromo(promo);
                }
                product.setAttributes(attributes);
            }

            if (productEntity.getRelationships() != null) {
                Relationship relationship = new Relationship();
                Category category = new Category();
                Data dataCategory = new Data();
                dataCategory.setId(Integer.valueOf(productEntity.getRelationships().getCategory().getData().getId()));
                dataCategory.setType(productEntity.getRelationships().getCategory().getData().getType());
                category.setData(dataCategory);
                relationship.setCategory(category);

                Operator operator = new Operator();
                Data dataOperator = new Data();
                dataOperator.setId(Integer.valueOf(productEntity.getRelationships().getOperator().getData().getId()));
                dataOperator.setType(productEntity.getRelationships().getOperator().getData().getType());
                operator.setData(dataOperator);
                relationship.setOperator(operator);

                product.setRelationships(relationship);
            }

            productList.add(product);
        }
        return productList;
    }
}
